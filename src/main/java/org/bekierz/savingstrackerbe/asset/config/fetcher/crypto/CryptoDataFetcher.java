package org.bekierz.savingstrackerbe.asset.config.fetcher.crypto;

import org.bekierz.savingstrackerbe.asset.config.fetcher.AssetDataFetcher;
import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.asset.model.response.api.crypto.CryptoResponse;
import org.bekierz.savingstrackerbe.asset.repository.AssetRepository;
import org.bekierz.savingstrackerbe.asset.repository.AssetTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CryptoDataFetcher implements AssetDataFetcher {
    private final RestTemplate restTemplate;
    private final AssetRepository assetRepository;
    private final AssetTypeRepository assetTypeRepository;
    private final AssetConfigProps assetConfigProps;

    public CryptoDataFetcher(RestTemplate restTemplate,
                             AssetRepository assetRepository,
                             AssetTypeRepository assetTypeRepository,
                             AssetConfigProps assetConfigProps) {
        this.restTemplate = restTemplate;
        this.assetRepository = assetRepository;
        this.assetTypeRepository = assetTypeRepository;
        this.assetConfigProps = assetConfigProps;
    }

    @Override
    public void fetchAssets() {
        ResponseEntity<CryptoResponse> response = restTemplate.getForEntity(
                assetConfigProps.api().cryptoUrl() + "?limit=20",
                CryptoResponse.class
        );

        CryptoResponse cryptoResponse = response.getBody();

        if(cryptoResponse == null || cryptoResponse.data() == null) {
            throw new RuntimeException("Failed to fetch crypto data");
        }

        assetRepository.saveAll(cryptoResponse.data().stream()
                .map(data -> Asset.builder()
                        .name(data.name())
                        .code(data.symbol())
                        .assetType(assetTypeRepository.findByName("crypto"))
                        .build())
                .toList()
        );
    }

}
