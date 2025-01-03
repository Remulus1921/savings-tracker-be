package org.bekierz.savingstrackerbe.asset.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

@Builder
public record AssetMonthValueDto(
        String date,
        @JsonFormat(pattern = "0.0000")
        Double price
) {
}
