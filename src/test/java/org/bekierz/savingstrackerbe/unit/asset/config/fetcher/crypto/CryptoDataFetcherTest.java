package org.bekierz.savingstrackerbe.unit.asset.config.fetcher.crypto;

import org.bekierz.savingstrackerbe.asset.config.fetcher.crypto.CryptoDataFetcher;
import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.datasource.model.api.response.crypto.CryptoResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoDataFetcherTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private AssetRepository assetRepository;
    @Mock
    private AssetTypeRepository assetTypeRepository;
    @Mock
    private AssetConfigProps assetConfigProps;
    @InjectMocks
    private CryptoDataFetcher cryptoDataFetcher;

    @Test
    public void should_fetch_crypto_data() {
        // given
        CryptoResponse cryptoResponse = new CryptoResponse(
                List.of(
                        new CryptoResponse.Data("bitcoin", "BTC", "Bitcoin", "50000"),
                        new CryptoResponse.Data("ethereum", "ETH", "Ethereum", "3000")
                )
        );

        AssetConfigProps.Urls api = new AssetConfigProps.Urls(
                "http://mocked-currency-url/",
                "http://mocked-crypto-url/"
        );

        // when
        when(assetConfigProps.api()).thenReturn(api);
        when(restTemplate.getForEntity(
                assetConfigProps.api().cryptoUrl() + "?limit=20",
                CryptoResponse.class
        )).thenReturn(new ResponseEntity<>(cryptoResponse, HttpStatus.OK));

        cryptoDataFetcher.fetchAssets();

        // then
        ArgumentCaptor<List<Asset>> captor = ArgumentCaptor.captor();
        verify(assetRepository).saveAll(captor.capture());
        verify(assetTypeRepository, atLeast(2)).findByName("cryptocurrency");

        List<Asset> assets = captor.getValue();

        assertNotNull(assets);
        assertEquals(2, assets.size());
        assertEquals("Bitcoin", assets.getFirst().getName());
        assertEquals("BTC", assets.getFirst().getCode());
        assertEquals("Ethereum", assets.getLast().getName());
        assertEquals("ETH", assets.getLast().getCode());
    }
}