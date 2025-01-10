package org.bekierz.savingstrackerbe.unit.asset.service;

import org.bekierz.savingstrackerbe.asset.model.dto.AssetTypeDto;
import org.bekierz.savingstrackerbe.asset.model.entity.AssetType;
import org.bekierz.savingstrackerbe.asset.repository.AssetTypeRepository;
import org.bekierz.savingstrackerbe.asset.service.AssetTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetTypeServiceTest {

    @Mock
    private AssetTypeRepository assetTypeRepository;
    @InjectMocks
    private AssetTypeService assetTypeService;

    @Test
    public void should_return_all_asset_types() {
        // given
        List<AssetType> assetTypes = List.of(
                AssetType.builder()
                        .id(1L)
                        .name("currency")
                        .build(),
                AssetType.builder()
                        .id(2L)
                        .name("stock")
                        .build(),
                AssetType.builder()
                        .id(3L)
                        .name("crypto")
                        .build()
        );

        // when
        when(assetTypeRepository.findAll()).thenReturn(assetTypes);
        List<AssetTypeDto> result = assetTypeService.getAssetTypes();

        // then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("currency", result.getFirst().name());
        assertEquals("stock", result.get(1).name());
        assertEquals("crypto", result.getLast().name());
    }
}
