package org.bekierz.savingstrackerbe.unit.asset.config.fetcher.currency;

import org.bekierz.savingstrackerbe.asset.config.fetcher.currency.CurrencyDataFetcher;
import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.datasource.model.api.response.currency.CurrencyResponseTableA;
import org.bekierz.savingstrackerbe.asset.repository.AssetRepository;
import org.bekierz.savingstrackerbe.asset.repository.AssetTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyDataFetcherTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private AssetTypeRepository assetTypeRepository;
    @Mock
    private AssetRepository assetRepository;
    @Mock
    private AssetConfigProps assetConfigProps;
    @InjectMocks
    private CurrencyDataFetcher currencyDataFetcher;

    @Test
    public void should_fetch_currency_data() {
        // given
        CurrencyResponseTableA[] response = new CurrencyResponseTableA[]{
                new CurrencyResponseTableA(
                        "A",
                        "1",
                        "2025-01-09",
                        List.of(
                                new CurrencyResponseTableA.Rate(
                                        "euro",
                                        "EUR",
                                        4.2226
                                ),
                                new CurrencyResponseTableA.Rate(
                                        "dollar",
                                        "USD",
                                        3.2226
                                ),
                                new CurrencyResponseTableA.Rate(
                                        "pound",
                                        "GBP",
                                        5.2226
                                )
                        )
                )
        };

        AssetConfigProps.Urls api = new AssetConfigProps.Urls(
                "http://mocked-currency-url/",
                "http://mocked-crypto-url/"
        );

        // when
        when(assetConfigProps.api()).thenReturn(api);
        when(restTemplate.getForEntity(
                assetConfigProps.api().currencyUrl() + "tables/a/?format=json",
                CurrencyResponseTableA[].class
        )).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        currencyDataFetcher.fetchAssets();

        // then
        ArgumentCaptor<List<Asset>> captor = ArgumentCaptor.captor();
        verify(assetRepository).saveAll(captor.capture());
        verify(assetTypeRepository, atLeast(3)).findByName("currency");

        List<Asset> assets = captor.getValue();

        assertNotNull(assets);
        assertEquals(3, assets.size());
        assertEquals("euro", assets.getFirst().getName());
        assertEquals("EUR", assets.getFirst().getCode());
        assertEquals("dollar", assets.get(1).getName());
        assertEquals("USD", assets.get(1).getCode());
        assertEquals("pound", assets.getLast().getName());
        assertEquals("GBP", assets.getLast().getCode());
    }
}
