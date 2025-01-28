package org.bekierz.savingstrackerbe.asset.config.fetcher.type;

import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.entity.AssetType;
import org.bekierz.savingstrackerbe.asset.repository.AssetTypeRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssetTypeFetcher {
    private final AssetTypeRepository assetTypeRepository;
    private final AssetConfigProps assetConfigProps;

    public AssetTypeFetcher(AssetTypeRepository assetTypeRepository, AssetConfigProps assetConfigProps) {
        this.assetTypeRepository = assetTypeRepository;
        this.assetConfigProps = assetConfigProps;
    }

    public void fetchTypes() {
        List<AssetType> typeList = assetConfigProps.types().stream()
                .map(type -> AssetType.builder()
                        .name(type)
                        .build()
                )
                .toList();
        assetTypeRepository.saveAll(typeList);
    }
}
