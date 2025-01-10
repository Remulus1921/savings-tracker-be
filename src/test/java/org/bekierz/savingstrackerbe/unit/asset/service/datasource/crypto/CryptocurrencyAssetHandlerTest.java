package org.bekierz.savingstrackerbe.unit.asset.service.datasource.crypto;

import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.asset.model.entity.AssetType;
import org.bekierz.savingstrackerbe.asset.model.response.api.crypto.CryptoMonthResponse;
import org.bekierz.savingstrackerbe.asset.model.response.api.currency.CurrencyRatesResponse;
import org.bekierz.savingstrackerbe.asset.service.datasource.crypto.CryptocurrencyAssetHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptocurrencyAssetHandlerTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private AssetConfigProps assetConfigProps;
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
                        new CryptoMonthResponse.Data("50000", 1735686000000L),
                        new CryptoMonthResponse.Data("60000", 1735686000000L)
                )
        );

        CurrencyRatesResponse currencyResponse = new CurrencyRatesResponse(
                "C",
                "dolar amerykański",
                "USD",
                List.of(
                        new CurrencyRatesResponse.Rate(
                                "006/C/NBP/2025",
                                "2025-01-10",
                                4.0967,
                                4.1795
                        )
                )
        );
        // when
        when(assetConfigProps.api()).thenReturn(api);
        when(restTemplate.getForEntity("http://mocked-crypto-url/Bitcoin/history?interval=d1&start=1735686000000&end=1738278000000", CryptoMonthResponse.class))
                .thenReturn(new ResponseEntity<>(data, null, 200));
        when(restTemplate.getForEntity("http://mocked-currency-url/rates/c/usd/last/1/?format=json", CurrencyRatesResponse.class))
                .thenReturn(new ResponseEntity<>(currencyResponse, null, 200));
        var result = cryptocurrencyAssetHandler.handle(asset, startDate, endDate);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("2025-01-01", result.getFirst().date());
        assertEquals(208975.0, result.getFirst().price());
        assertEquals("2025-01-01", result.get(1).date());
        assertEquals(250770.0, result.get(1).price());
    }
}
