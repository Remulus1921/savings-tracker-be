package org.bekierz.savingstrackerbe.asset.service;

import lombok.extern.log4j.Log4j2;
import org.bekierz.savingstrackerbe.asset.model.dto.AssetDto;
import org.bekierz.savingstrackerbe.asset.model.dto.AssetValueDto;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.asset.repository.AssetRepository;
import org.bekierz.savingstrackerbe.datasource.service.AssetHandlerRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetHandlerRegistry handlerRegistry;

    public AssetService(AssetRepository assetRepository,
                        AssetHandlerRegistry handlerRegistry) {
        this.assetRepository = assetRepository;
        this.handlerRegistry = handlerRegistry;
    }

    public List<AssetDto> getAssets(String assetType) {
        return assetRepository.findAll().stream()
                .filter(asset -> asset.getAssetType().getName().equalsIgnoreCase(assetType))
                .map(asset -> AssetDto.builder()
                        .name(asset.getName())
                        .code(asset.getCode())
                        .build())
                .collect(Collectors.toList());
    }

    public List<AssetValueDto> getMonthValue(String assetCode) {
        Asset asset = assetRepository.findByCode(assetCode);

        LocalDate endDate = LocalDate.now();

        LocalDate startDate = endDate.minusDays(30);


        return handlerRegistry.getHandler(asset.getAssetType().getName())
                .getMontValue(asset, startDate, endDate);
    }
}
