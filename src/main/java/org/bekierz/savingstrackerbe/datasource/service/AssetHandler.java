package org.bekierz.savingstrackerbe.datasource.service;

import org.bekierz.savingstrackerbe.asset.model.dto.AssetValueDto;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;

import java.time.LocalDate;
import java.util.List;

public interface AssetHandler {
    List<AssetValueDto> getMontValue(Asset asset, LocalDate startDate, LocalDate endDate);
    AssetValueDto getAssetValue(String code);
}
