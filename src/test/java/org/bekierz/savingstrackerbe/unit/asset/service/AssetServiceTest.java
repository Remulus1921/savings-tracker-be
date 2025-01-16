package org.bekierz.savingstrackerbe.unit.asset.service;

import org.bekierz.savingstrackerbe.asset.model.dto.AssetDto;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.asset.model.entity.AssetType;
import org.bekierz.savingstrackerbe.asset.repository.AssetRepository;
import org.bekierz.savingstrackerbe.asset.service.AssetService;
import org.bekierz.savingstrackerbe.datasource.service.AssetHandler;
import org.bekierz.savingstrackerbe.datasource.service.AssetHandlerRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private AssetHandlerRegistry handlerRegistry;
    @Mock
    private AssetHandler handler;
    @InjectMocks
    private AssetService assetService;

    @Test
    void should_return_all_assets_of_given_type() {
        // given
        String assetType = "currency";
        List<Asset> assets = List.of(
            Asset.builder()
                    .id(1L)
                    .name("Euro")
                    .assetType(AssetType.builder()
                            .id(1L)
                            .name("currency")
                            .build()
                    )
                    .code("EUR")
                    .build(),
            Asset.builder()
                    .id(1L)
                    .name("Dolar")
                    .assetType(AssetType.builder()
                            .id(2L)
                            .name("currency")
                            .build()
                    )
                    .code("USD")
                    .build(),
            Asset.builder()
                    .id(3L)
                    .name("Bitcoin")
                    .assetType(AssetType.builder()
                            .id(3L)
                            .name("crypto")
                            .build()
                    )
                    .code("BTC")
                    .build()
        );

        // when
        when(assetRepository.findAll()).thenReturn(assets);
        List<AssetDto> result = assetService.getAssets(assetType);

        // then
        verify(assetRepository, times(1)).findAll();
        verifyNoMoreInteractions(assetRepository);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Euro", result.getFirst().name());
        assertEquals("EUR", result.getFirst().code());
        assertEquals("Dolar", result.get(1).name());
        assertEquals("USD", result.get(1).code());

    }

    @Test
    void should_invoke_correct_handler() {
        // given
        String assetCode = "EUR";
        Asset asset = Asset.builder()
                .id(1L)
                .name("Euro")
                .assetType(AssetType.builder()
                        .id(1L)
                        .name("currency")
                        .build()
                )
                .code("EUR")
                .build();

        // when
        when(assetRepository.findByCode(assetCode)).thenReturn(Optional.of(asset));
        when(handlerRegistry.getHandler(asset.getAssetType().getName())).thenReturn(handler);
        when(handler.getMontValue(any(), any(), any())).thenReturn(Collections.emptyList());
        assetService.getMonthValue(assetCode);

        // then
        verify(assetRepository, times(1)).findByCode(assetCode);
        verify(handlerRegistry, times(1)).getHandler(asset.getAssetType().getName());
        verify(handlerRegistry).getHandler("currency");
        verify(handlerRegistry.getHandler("currency"),
                times(1)).getMontValue(
                        asset,
                        LocalDate.now().minusDays(30),
                        LocalDate.now()
        );

    }
}
