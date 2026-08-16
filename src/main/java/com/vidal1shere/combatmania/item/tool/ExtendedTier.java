package com.vidal1shere.combatmania.item.tool;

import com.vidal1shere.combatmania.api.IExtendedTier;
import com.vidal1shere.combatmania.util.CustomProperties;

import java.util.Objects;

public class ExtendedTier implements IExtendedTier {
    private final ToolProperties toolProperties;
    private final CustomProperties customProperties;

    public ExtendedTier(ToolProperties toolProperties, CustomProperties.PropertySet<?>... customProperties) {
        Objects.requireNonNull(toolProperties);
        this.toolProperties = toolProperties;
        this.customProperties = CustomProperties.ofImmutable(customProperties);
    }

    @Override
    public CustomProperties getCustomProperties() {
        return customProperties;
    }

    @Override
    public ToolProperties getToolProperties() {
        return toolProperties;
    }
}
