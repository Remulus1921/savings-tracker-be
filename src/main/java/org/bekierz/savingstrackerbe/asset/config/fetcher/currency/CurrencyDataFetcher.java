package org.bekierz.savingstrackerbe.asset.config.fetcher.currency;

import lombok.extern.log4j.Log4j2;
import org.bekierz.savingstrackerbe.asset.config.fetcher.AssetDataFetcher;
import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.datasource.model.api.response.currency.CurrencyResponseFetcher;
import org.bekierz.savingstrackerbe.asset.repository.AssetRepository;
import org.bekierz.savingstrackerbe.asset.repository.AssetTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Log4j2
public class CurrencyDataFetcher implements AssetDataFetcher {

    private final RestTemplate restTemplate;
    private final AssetTypeRepository assetTypeRepository;
    private final AssetRepository assetRepository;
    private final AssetConfigProps assetConfigProps;

    public CurrencyDataFetcher(RestTemplate restTemplate,
                               AssetTypeRepository assetTypeRepository,
                               AssetRepository assetRepository,
                               AssetConfigProps assetConfigProps
    ) {
        this.restTemplate = restTemplate;
        this.assetTypeRepository = assetTypeRepository;
        this.assetRepository = assetRepository;
        this.assetConfigProps = assetConfigProps;
    }

    @Override
    public void fetchAssets() {
        ResponseEntity<CurrencyResponseFetcher[]> response = restTemplate.getForEntity(
                assetConfigProps.api().currencyUrl() + "tables/a/?format=json",
                CurrencyResponseFetcher[].class
        );

        CurrencyResponseFetcher[] currencyResponse = response.getBody();

        if(currencyResponse == null || currencyResponse[0].rates() == null) {
            throw new RuntimeException("Failed to fetch currency data");
        }

        assetRepository.saveAll(currencyResponse[0].rates().stream()
                .map(rate -> Asset.builder()
                        .name(rate.currency())
                        .code(rate.code())
                        .assetType(assetTypeRepository.findByName("currency"))
                        .build())
                .toList()
        );
    }
}
