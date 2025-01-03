package org.bekierz.savingstrackerbe.asset.service.datasource;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AssetHandlerRegistry {
    private final Map<String, AssetHandler> handlers = new HashMap<>();

    public AssetHandlerRegistry(List<AssetHandler> assetHandlers) {
        assetHandlers.forEach(handler -> {
                    String type = handler.getClass()
                            .getSimpleName()
                            .replace("AssetHandler", "")
                            .toLowerCase();
                    handlers.put(type, handler);
                });
    }

    public AssetHandler getHandler(String assetType) {
        AssetHandler handler = handlers.get(assetType.toLowerCase());
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for asset type: " + assetType);
        }

        return handler;
    }
}
