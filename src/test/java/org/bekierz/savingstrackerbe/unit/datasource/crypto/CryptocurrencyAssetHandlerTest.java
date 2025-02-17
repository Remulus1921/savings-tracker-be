package org.bekierz.savingstrackerbe.unit.datasource.crypto;

import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.dto.AssetValueDto;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.asset.model.entity.AssetType;
import org.bekierz.savingstrackerbe.asset.repository.AssetRepository;
import org.bekierz.savingstrackerbe.datasource.model.api.response.crypto.CryptoMonthResponse;
import org.bekierz.savingstrackerbe.datasource.model.api.response.crypto.CryptoResponse;
import org.bekierz.savingstrackerbe.datasource.model.api.response.crypto.SingleCryptoResponse;
import org.bekierz.savingstrackerbe.datasource.service.crypto.CryptocurrencyAssetHandler;
import org.bekierz.savingstrackerbe.datasource.service.currency.CurrencyAssetHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptocurrencyAssetHandlerTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private AssetConfigProps assetConfigProps;
    @Mock
    private CurrencyAssetHandler currencyAssetHandler;
    @Mock
    private AssetRepository assetRepository;
    @InjectMocks
    private CryptocurrencyAssetHandler cryptocurrencyAssetHandler;

    @Test
    public void should_return_list_of_asset_month_value_dto() {
        // given
        Asset asset = Asset.builder()
                .name("Bitcoin")
                .code("BTC")
                .assetType(AssetType.builder()
                        .name("crypto")
                        .build()
                )
                .build();

        AssetConfigProps.Urls api = new AssetConfigProps.Urls(
                "http://mocked-currency-url/",
                "http://mocked-crypto-url/"
        );

        LocalDate endDate = LocalDate.of(2025, 1, 31);
        LocalDate startDate = endDate.minusDays(30);

        CryptoMonthResponse data = new CryptoMonthResponse(
                List.of(
                        new CryptoMonthResponse.Data("50000", 1735686000000L, new Date()),
                        new CryptoMonthResponse.Data("60000", 1735686000000L, new Date())
                ),
                123456789L
        );
        AssetValueDto usdValue = new AssetValueDto("2025-01-10", 4.1795);

        // when
        when(assetConfigProps.api()).thenReturn(api);
        when(restTemplate.getForEntity("http://mocked-crypto-url/bitcoin/history?interval=d1&start=1735686000000&end=1738278000000", CryptoMonthResponse.class))
                .thenReturn(new ResponseEntity<>(data, null, 200));
        when(currencyAssetHandler.getAssetValue("usd")).thenReturn(usdValue);
        var result = cryptocurrencyAssetHandler.getMontValue(asset, startDate, endDate);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("2025-01-01", result.getFirst().date());
        assertEquals(208975.0, result.getFirst().price());
        assertEquals("2025-01-01", result.get(1).date());
        assertEquals(250770.0, result.get(1).price());
    }

    @Test
    public void should_return_crypto_value() {
        // given
        Asset asset = Asset.builder()
                .name("Bitcoin")
                .code("BTC")
                .assetType(AssetType.builder()
                        .name("crypto")
                        .build()
                )
                .build();

        AssetConfigProps.Urls api = new AssetConfigProps.Urls(
                "http://mocked-currency-url/",
                "http://mocked-crypto-url/"
        );

        SingleCryptoResponse data = new SingleCryptoResponse(
                new SingleCryptoResponse.Data("bitcoin", "BTC", "Bitcoin", "50000")
        );

        AssetValueDto usdValue = new AssetValueDto("2025-01-10", 4.1795);

        // when
        when(assetConfigProps.api()).thenReturn(api);
        when(restTemplate.getForEntity("http://mocked-crypto-url/bitcoin", SingleCryptoResponse.class))
                .thenReturn(new ResponseEntity<>(data, null, 200));
        when(currencyAssetHandler.getAssetValue("usd")).thenReturn(usdValue);
        when(assetRepository.findByCode("BTC")).thenReturn(Optional.of(asset));
        var result = cryptocurrencyAssetHandler.getAssetValue("BTC");

        // then
        assertNotNull(result);
        assertEquals(208975, result.price());
    }
}
