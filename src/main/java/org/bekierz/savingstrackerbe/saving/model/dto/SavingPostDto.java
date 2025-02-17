package org.bekierz.savingstrackerbe.saving.model.dto;

import lombok.Builder;

@Builder
public record SavingPostDto(
        Double amount,
        String assetCode
) {
}
