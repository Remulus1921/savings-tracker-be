package org.bekierz.savingstrackerbe.asset.service.datasource.currency;

import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.dto.AssetMonthValueDto;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.asset.model.response.api.currency.CurrencyRatesResponse;
import org.bekierz.savingstrackerbe.asset.service.datasource.AssetHandler;
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
    public List<AssetMonthValueDto> handle(Asset asset, LocalDate startDate, LocalDate endDate) {
        String beginDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String finishDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String currencyUrl = assetConfigProps.api().currencyUrl() + "rates/c/" + asset.getCode() + "/"
                + beginDate + "/" + finishDate + "/";
        ResponseEntity<CurrencyRatesResponse> response = restTemplate
                .getForEntity(currencyUrl, CurrencyRatesResponse.class);

        CurrencyRatesResponse assetResponse = response.getBody();

        assert assetResponse != null;
        return assetResponse.rates().stream()
                .map(rate -> AssetMonthValueDto.builder()
                        .date(rate.effectiveDate())
                        .price(rate.ask())
                        .build())
                .toList();
    }
}
