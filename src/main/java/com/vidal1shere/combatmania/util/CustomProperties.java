package com.vidal1shere.combatmania.util;

import java.util.HashMap;
import java.util.Map;

public class CustomProperties {

    private final Map<Key<?>, Object> properties = new HashMap<>();
    private final boolean immutable;

    private CustomProperties(boolean immutable, Set<?>... properties) {
        this.immutable = immutable;
        for (Set<?> set : properties) {
            this.properties.put(set.key(), set.value());
        }
    }
    
    public static CustomProperties of(Set<?>... properties) {
        return new CustomProperties(false, properties);
    }

    public static CustomProperties ofImmutable(Set<?>... properties) {
        return new CustomProperties(true, properties);
    }

    public static CustomProperties empty() {
        return new CustomProperties(true);
    }
    
    public <T> void set(Key<T> key, T value) {
        if (immutable) return;
        properties.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Key<T> key) {
        return (T) properties.getOrDefault(key, key.defaultValue());
    }

    public boolean contains(Key<?> key) {
        return properties.containsKey(key);
    }

    public void remove(Key<?> key) {
        if (immutable) return;
        properties.remove(key);
    }

    public void clear() {
        if (immutable) return;
        properties.clear();
    }
    
    public void copy(CustomProperties properties) {
        this.properties.putAll(properties.properties);
    }

    public record Key<T>(T defaultValue) {}
    public record Set<T>(Key<T> key, T value) {}
}
