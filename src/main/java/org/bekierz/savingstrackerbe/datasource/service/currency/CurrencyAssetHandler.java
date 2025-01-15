package org.bekierz.savingstrackerbe.datasource.service.currency;

import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.dto.AssetValueDto;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.datasource.model.api.response.currency.CurrencyResponseTableC;
import org.bekierz.savingstrackerbe.datasource.service.AssetHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CurrencyAssetHandler implements AssetHandler {

    private final RestTemplate restTemplate;
    private final AssetConfigProps assetConfigProps;

    public CurrencyAssetHandler(RestTemplate restTemplate, AssetConfigProps assetConfigProps) {
        this.restTemplate = restTemplate;
        this.assetConfigProps = assetConfigProps;
    }

    @Override
    public List<AssetValueDto> getMontValue(Asset asset, LocalDate startDate, LocalDate endDate) {
        String beginDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String finishDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String currencyUrl = assetConfigProps.api().currencyUrl() + "rates/c/" + asset.getCode() + "/"
                + beginDate + "/" + finishDate + "/";
        ResponseEntity<CurrencyResponseTableC> response = restTemplate
                .getForEntity(currencyUrl, CurrencyResponseTableC.class);

        CurrencyResponseTableC assetResponse = response.getBody();

        if (assetResponse == null) {
            throw new RuntimeException("Error while fetching data from external API");
        }

        return assetResponse.rates().stream()
                .map(rate -> AssetValueDto.builder()
                        .date(rate.effectiveDate())
                        .price(rate.ask())
                        .build())
                .toList();
    }

    @Override
    public AssetValueDto getAssetValue(String code) {
        String currencyUrl = assetConfigProps.api().currencyUrl() + "rates/c/" + code + "?format=json";
        ResponseEntity<CurrencyResponseTableC> response = restTemplate
                .getForEntity(currencyUrl, CurrencyResponseTableC.class);
        if (response.getBody() == null) {
            throw new RuntimeException("Error while fetching data from external API");
        }

        return AssetValueDto.builder()
                .date(response.getBody().rates().getFirst().effectiveDate())
                .price(response.getBody().rates().getFirst().ask())
                .build();
    }
}
