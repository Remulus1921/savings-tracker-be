package org.bekierz.savingstrackerbe.datasource.service.crypto;

import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.dto.AssetValueDto;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.datasource.model.api.response.crypto.CryptoMonthResponse;
import org.bekierz.savingstrackerbe.datasource.model.api.response.crypto.CryptoResponse;
import org.bekierz.savingstrackerbe.datasource.service.AssetHandler;
import org.bekierz.savingstrackerbe.datasource.service.currency.CurrencyAssetHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CryptocurrencyAssetHandler implements AssetHandler {

    private final RestTemplate restTemplate;
    private final AssetConfigProps assetConfigProps;
    private final CurrencyAssetHandler currencyAssetHandler;

    public CryptocurrencyAssetHandler(RestTemplate restTemplate,
                                      AssetConfigProps assetConfigProps,
                                      CurrencyAssetHandler currencyAssetHandler) {
        this.restTemplate = restTemplate;
        this.assetConfigProps = assetConfigProps;
        this.currencyAssetHandler = currencyAssetHandler;
    }

    @Override
    public List<AssetValueDto> getMontValue(Asset asset, LocalDate startDate, LocalDate endDate) {
        long end = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        String cryptocurrencyApiUrl = assetConfigProps.api().cryptoUrl() + asset.getName().toLowerCase()
                + "/history?interval=d1&start=" + start + "&end=" + end;

        ResponseEntity<CryptoMonthResponse> response = restTemplate
                .getForEntity(cryptocurrencyApiUrl, CryptoMonthResponse.class);

        CryptoMonthResponse assetResponse = response.getBody();

        double usdValue = currencyAssetHandler.getAssetValue("usd").price();

        assert assetResponse != null;
        return assetResponse.data().stream()
                .map(data -> AssetValueDto.builder()
                        .date(Instant
                                .ofEpochMilli(data.time())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .price(Double.parseDouble(data.priceUsd()) * usdValue)
                        .build())
                .toList();
    }

    @Override
    public AssetValueDto getAssetValue(String code) {
        String cryptocurrencyApiUrl = assetConfigProps.api().cryptoUrl() + code;
        ResponseEntity<CryptoResponse> response = restTemplate
                .getForEntity(cryptocurrencyApiUrl, CryptoResponse.class);

        double usdValue = currencyAssetHandler.getAssetValue("usd").price();

        if (response.getBody() == null) {
            throw new RuntimeException("Error while fetching data from external API");
        }
        return AssetValueDto.builder()
                .date(response.getBody().data().getFirst().symbol())
                .price(Double.parseDouble(response.getBody().data().getFirst().priceUsd()) * usdValue)
                .build();
    }
}
