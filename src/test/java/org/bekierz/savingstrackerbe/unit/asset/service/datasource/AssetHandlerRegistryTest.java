package org.bekierz.savingstrackerbe.unit.asset.service.datasource;

import org.bekierz.savingstrackerbe.asset.service.datasource.AssetHandler;
import org.bekierz.savingstrackerbe.asset.service.datasource.AssetHandlerRegistry;
import org.bekierz.savingstrackerbe.asset.service.datasource.crypto.CryptocurrencyAssetHandler;
import org.bekierz.savingstrackerbe.asset.service.datasource.currency.CurrencyAssetHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AssetHandlerRegistryTest {
    private AssetHandlerRegistry assetHandlerRegistry;
    private final AssetHandler testHandler = mock(CryptocurrencyAssetHandler.class);
    private final AssetHandler testHandler2 = mock(CurrencyAssetHandler.class);

    @BeforeEach
    public void setUp() {
        assetHandlerRegistry = new AssetHandlerRegistry(List.of(testHandler, testHandler2));
    }

    @Test
    public void should_return_handler_based_on_provided_type() {
        // given
        String assetType = "cryptocurrency";

        // when
        AssetHandler result = assetHandlerRegistry.getHandler(assetType);

        // then
        assertNotNull(result);
        assertEquals(testHandler, result);
    }

    @Test
    public void should_throw_exception_when_no_handler_found() {
        // given
        String assetType = "stock";

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> assetHandlerRegistry.getHandler(assetType));

        // then
        assertEquals("No handler found for asset type: stock", exception.getMessage());
    }
}
