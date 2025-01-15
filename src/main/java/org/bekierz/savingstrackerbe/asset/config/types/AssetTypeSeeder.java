package org.bekierz.savingstrackerbe.asset.config.types;

import org.bekierz.savingstrackerbe.asset.config.properties.AssetConfigProps;
import org.bekierz.savingstrackerbe.asset.model.entity.AssetType;
import org.bekierz.savingstrackerbe.asset.repository.AssetTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@Order(1)
public class AssetTypeSeeder {
    private final AssetTypeRepository assetTypeRepository;
    private final AssetConfigProps assetConfigProps;

    public AssetTypeSeeder(AssetTypeRepository assetTypeRepository, AssetConfigProps assetConfigProps) {
        this.assetTypeRepository = assetTypeRepository;
        this.assetConfigProps = assetConfigProps;
    }
    @Bean
    public CommandLineRunner seedTypes() {
        List<AssetType> typeList = assetConfigProps.types().stream()
                .map(type -> AssetType.builder()
                        .name(type)
                        .build()
                )
                .toList();
        return args -> assetTypeRepository.saveAll(typeList);
    }
}
