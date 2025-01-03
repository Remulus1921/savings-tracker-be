package org.bekierz.savingstrackerbe.asset.service.datasource.currency;

import org.bekierz.savingstrackerbe.asset.model.dto.AssetMonthValueDto;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.asset.model.response.api.currency.CurrencyMonthResponse;
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

    public CurrencyAssetHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<AssetMonthValueDto> handle(Asset asset, LocalDate startDate, LocalDate endDate) {
        String begineDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String finishDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String currencyUrl = "https://api.nbp.pl/api/exchangerates/rates/c/" + asset.getCode() + "/"
                + begineDate + "/" + finishDate + "/";
        ResponseEntity<CurrencyMonthResponse> response = restTemplate
                .getForEntity(currencyUrl, CurrencyMonthResponse.class);

        CurrencyMonthResponse assetResponse = response.getBody();

        assert assetResponse != null;
        return assetResponse.rates().stream()
                .map(rate -> AssetMonthValueDto.builder()
                        .date(rate.effectiveDate())
                        .price(rate.ask())
                        .build())
                .toList();
    }
}
