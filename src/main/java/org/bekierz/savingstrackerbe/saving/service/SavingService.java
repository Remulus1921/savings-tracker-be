package org.bekierz.savingstrackerbe.saving.service;

import org.bekierz.savingstrackerbe.asset.model.dto.AssetValueDto;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.asset.service.AssetService;
import org.bekierz.savingstrackerbe.datasource.service.AssetHandlerRegistry;
import org.bekierz.savingstrackerbe.saving.model.dto.SavingDto;
import org.bekierz.savingstrackerbe.saving.model.dto.SavingPostDto;
import org.bekierz.savingstrackerbe.saving.model.dto.SavingUpdateDto;
import org.bekierz.savingstrackerbe.saving.model.entity.Saving;
import org.bekierz.savingstrackerbe.saving.repository.SavingRepository;
import org.bekierz.savingstrackerbe.user.model.CustomUserDetails;
import org.bekierz.savingstrackerbe.user.model.entity.User;
import org.bekierz.savingstrackerbe.user.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavingService {

    private final SavingRepository savingRepository;
    private final AssetHandlerRegistry handlerRegistry;
    private final AssetService assetService;
    private final UserRepository userRepository;

    public SavingService(SavingRepository savingRepository,
                         AssetHandlerRegistry handlerRegistry,
                         AssetService assetService,
                         UserRepository userRepository) {
        this.savingRepository = savingRepository;
        this.handlerRegistry = handlerRegistry;
        this.assetService = assetService;
        this.userRepository = userRepository;
    }

    public List<SavingDto> getUserSavings() {
        String email = this.extractEmail();

        return savingRepository.findByUserEmail(email).stream().map(
                this::buildDto
        ).toList();
    }

    public Optional<SavingDto> getSavingValue(String assetCode) {
        String email = this.extractEmail();
        Saving saving = savingRepository.findSavingByUserEmailAndAssetCode(email, assetCode.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Saving not found"));

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
                .assetCode(saving.getAsset().getCode())
                .value(saving.getAmount() * assetValue.price())
                .exchangeRate(assetValue.price())
                .build());
    }

    public void addNewSaving(SavingPostDto savingDto) {
        String email = this.extractEmail();

        Optional<Saving> saving = savingRepository.findSavingByUserEmailAndAssetCode(email, savingDto.assetCode().toUpperCase());
        if (saving.isPresent()) {
            saving.get().setAmount(saving.get().getAmount() + savingDto.amount());
        }
        else {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Asset asset = assetService.getAsset(savingDto.assetCode().toUpperCase());

            saving = Optional.of(Saving.builder()
                    .amount(savingDto.amount())
                    .asset(asset)
                    .user(user)
                    .build()
            );
        }

        savingRepository.save(saving.get());
    }

    public void deleteSaving(String assetCode) {
        String email = this.extractEmail();

        savingRepository.deleteSavingByUserEmailAndAssetCode(email, assetCode.toUpperCase());
    }

    public SavingDto updateSaving(SavingUpdateDto updateDto, String assetCode) {
        String email = this.extractEmail();

        Saving saving = savingRepository.findSavingByUserEmailAndAssetCode(email, assetCode.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Saving not found"));

        saving.setAmount(updateDto.amount());
        savingRepository.save(saving);

        return this.buildDto(saving);
    }

    public Double getTotalSavings() {
        String email = this.extractEmail();

        return savingRepository.findByUserEmail(email).stream()
                .map(saving -> saving.getAmount() * handlerRegistry.getHandler(saving
                        .getAsset()
                        .getAssetType()
                        .getName())
                        .getAssetValue(saving
                                .getAsset()
                                .getCode()
                        )
                        .price())
                .reduce(0.00, Double::sum);
    }

    private String extractEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof UsernamePasswordAuthenticationToken) {
            CustomUserDetails token = (CustomUserDetails) authentication.getPrincipal();
            return token.getUsername();
        }
        throw new IllegalStateException("User not authenticated");
    }

    private SavingDto buildDto(Saving saving) {
        return SavingDto.builder()
                .amount(saving.getAmount())
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
                .build();
    }
}
