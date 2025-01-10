package org.bekierz.savingstrackerbe.asset.service.datasource.crypto;

import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.dto.AssetMonthValueDto;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.asset.model.response.api.crypto.CryptoMonthResponse;
import org.bekierz.savingstrackerbe.asset.model.response.api.currency.CurrencyRatesResponse;
import org.bekierz.savingstrackerbe.asset.model.response.api.currency.CurrencyResponse;
import org.bekierz.savingstrackerbe.asset.service.datasource.AssetHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class CryptocurrencyAssetHandler implements AssetHandler {

    private final RestTemplate restTemplate;
    private final AssetConfigProps assetConfigProps;

    public CryptocurrencyAssetHandler(RestTemplate restTemplate, AssetConfigProps assetConfigProps) {
        this.restTemplate = restTemplate;
        this.assetConfigProps = assetConfigProps;
    }

    @Override
    public List<AssetMonthValueDto> handle(Asset asset, LocalDate startDate, LocalDate endDate) {
        long end = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        String cryptocurrencyApiUrl = assetConfigProps.api().cryptoUrl() + asset.getName()
                + "/history?interval=d1&start=" + start + "&end=" + end;

        ResponseEntity<CryptoMonthResponse> response = restTemplate
                .getForEntity(cryptocurrencyApiUrl, CryptoMonthResponse.class);

        CryptoMonthResponse assetResponse = response.getBody();

        String udsUrl = assetConfigProps.api().currencyUrl() + "rates/c/usd/last/1/?format=json";
        CurrencyRatesResponse usdResponse = restTemplate
                .getForEntity(udsUrl, CurrencyRatesResponse.class).getBody();

        assert usdResponse != null;
        double usdValue = usdResponse.rates().getFirst().ask();

        assert assetResponse != null;
        return assetResponse.data().stream()
                .map(data -> AssetMonthValueDto.builder()
                        .date(Instant
                                .ofEpochMilli(data.time())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .price(Double.parseDouble(data.priceUsd()) * usdValue)
                        .build())
                .toList();
    }
}
