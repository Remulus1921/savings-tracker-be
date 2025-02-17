package org.bekierz.savingstrackerbe.asset.service;

import org.bekierz.savingstrackerbe.asset.model.dto.AssetTypeDto;
import org.bekierz.savingstrackerbe.asset.repository.AssetTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetTypeService {
    private final AssetTypeRepository assetTypeRepository;

    public AssetTypeService(AssetTypeRepository assetTypeRepository) {
        this.assetTypeRepository = assetTypeRepository;
    }


    public List<AssetTypeDto> getAssetTypes() {
        return assetTypeRepository.findAll().stream()
                .map(assetType -> AssetTypeDto.builder()
                        .name(assetType.getName())
                        .build())
                .toList();
    }
}
