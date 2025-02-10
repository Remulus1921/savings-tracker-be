package org.bekierz.savingstrackerbe.unit.datasource.currency;

import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.asset.model.entity.AssetType;
import org.bekierz.savingstrackerbe.datasource.model.api.response.currency.CurrencyResponse;
import org.bekierz.savingstrackerbe.datasource.service.currency.CurrencyAssetHandler;
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
class CurrencyAssetHandlerTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private AssetConfigProps assetConfigProps;
    @InjectMocks
    private CurrencyAssetHandler currencyAssetHandler;

    @Test
    public void should_return_list_of_asset_month_value_dto() {
        // given
        Asset asset = Asset.builder()
                .name("Euro")
                .code("EUR")
                .assetType(AssetType.builder()
                        .name("currency")
                        .build()
                )
                .build();

        AssetConfigProps.Urls api = new AssetConfigProps.Urls(
                "http://mocked-currency-url/",
                "http://mocked-crypto-url/"
        );

        LocalDate endDate = LocalDate.of(2025, 1, 31);
        LocalDate startDate = endDate.minusDays(30);

        CurrencyResponse currencyResponse = new CurrencyResponse(
                "C",
                "euro",
                "EUR",
                List.of(
                        new CurrencyResponse.Rate(
                                "006/C/NBP/2025",
                                "2025-01-10",
                                4.2226
                        ),
                        new CurrencyResponse.Rate(
                                "006/C/NBP/2025",
                                "2025-01-09",
                                4.223
                        )
                )
        );

        // when
        when(assetConfigProps.api()).thenReturn(api);
        when(restTemplate.getForEntity(
                "http://mocked-currency-url/rates/a/EUR/2025-01-01/2025-01-31/",
                CurrencyResponse.class
        )).thenReturn(new ResponseEntity<>(currencyResponse, null, 200));

        var result = currencyAssetHandler.getMontValue(asset, startDate, endDate);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("2025-01-10", result.getFirst().date());
        assertEquals(4.2226, result.getFirst().price());
        assertEquals("2025-01-09", result.getLast().date());
        assertEquals(4.223, result.getLast().price());
    }

    @Test
    public void should_return_currency_value() {
        // given
        Asset asset = Asset.builder()
                .name("euro")
                .code("EUR")
                .assetType(AssetType.builder()
                        .name("currency")
                        .build()
                )
                .build();

        AssetConfigProps.Urls api = new AssetConfigProps.Urls(
                "http://mocked-currency-url/",
                "http://mocked-crypto-url/"
        );

        CurrencyResponse currencyResponse = new CurrencyResponse(
                "C",
                "euro",
                "EUR",
                List.of(
                        new CurrencyResponse.Rate(
                                "006/C/NBP/2025",
                                "2025-01-10",
                                4.2226
                        )
                )
        );

        // when
        when(assetConfigProps.api()).thenReturn(api);
        when(restTemplate.getForEntity(
                "http://mocked-currency-url/rates/a/EUR?format=json",
                CurrencyResponse.class
        )).thenReturn(new ResponseEntity<>(currencyResponse, null, 200));

        var result = currencyAssetHandler.getAssetValue("EUR");

        // then
        assertNotNull(result);
        assertEquals("2025-01-10", result.date());
        assertEquals(4.2226, result.price());
    }
}
