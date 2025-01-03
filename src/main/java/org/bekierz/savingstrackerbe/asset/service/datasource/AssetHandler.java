package org.bekierz.savingstrackerbe.asset.service.datasource;

import org.bekierz.savingstrackerbe.asset.model.dto.AssetMonthValueDto;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;

import java.time.LocalDate;
import java.util.List;

public interface AssetHandler {
    List<AssetMonthValueDto> handle(Asset asset, LocalDate startDate, LocalDate endDate);
}
