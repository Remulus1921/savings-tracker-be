package org.bekierz.savingstrackerbe.saving.service;

import org.bekierz.savingstrackerbe.asset.model.dto.AssetValueDto;
import org.bekierz.savingstrackerbe.datasource.service.AssetHandlerRegistry;
import org.bekierz.savingstrackerbe.saving.model.dto.SavingDto;
import org.bekierz.savingstrackerbe.saving.model.entity.Saving;
import org.bekierz.savingstrackerbe.saving.repository.SavingRepository;
import org.bekierz.savingstrackerbe.utils.config.jwt.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavingService {

    private final SavingRepository savingRepository;
    private final JwtService jwtService;
    private final AssetHandlerRegistry handlerRegistry;

    public SavingService(SavingRepository savingRepository,
                         JwtService jwtService,
                         AssetHandlerRegistry handlerRegistry
    ) {
        this.savingRepository = savingRepository;
        this.jwtService = jwtService;
        this.handlerRegistry = handlerRegistry;
    }

    public List<SavingDto> getUserSavings() {
        String email = this.extractEmail();

        return savingRepository.findByUserEmail(email).stream().map(
                saving -> SavingDto.builder()
                            .amount(saving.getAmount())
                            .assetName(saving.getAsset().getName())
                            .assetCode(saving.getAsset().getCode())
                            .value(handlerRegistry.getHandler(saving
                                            .getAsset()
                                            .getAssetType()
                                            .getName())
                                    .getAssetValue(saving
                                            .getAsset()
                                            .getCode()
                                    )
                                    .price() * saving.getAmount())
                            .build()
        ).toList();
    }

    public Optional<SavingDto> getSavingValue(String assetCode) {
        String email = this.extractEmail();
        Saving saving = savingRepository.findSavingByUserEmailAndAssetCode(email, assetCode);

        if(saving == null) {
            return Optional.empty();
        }

        AssetValueDto assetValue = handlerRegistry.getHandler(saving
                        .getAsset()
                        .getAssetType()
                        .getName())
                .getAssetValue(saving
                        .getAsset()
                        .getCode()
                );

        return Optional.of(SavingDto.builder()
                .amount(saving.getAmount())
                .assetName(saving.getAsset().getName())
                .assetCode(saving.getAsset().getCode())
                .value(saving.getAmount() * assetValue.price())
                .exchangeRate(assetValue.price())
                .build());
    }

    private String extractEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof UsernamePasswordAuthenticationToken) {
            return jwtService.extractEmail((String) authentication.getCredentials());
        }
        throw new IllegalStateException("User not authenticated");
    }
}
