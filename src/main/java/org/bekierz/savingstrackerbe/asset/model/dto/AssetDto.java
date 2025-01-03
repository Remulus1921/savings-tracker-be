package org.bekierz.savingstrackerbe.asset.model.dto;

import lombok.Builder;

@Builder
public record AssetDto(
        String name,
        String code
) {
}
