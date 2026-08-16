package com.vidal1shere.combatmania.util;

import java.util.HashMap;
import java.util.Map;

public class CustomProperties {

    private final Map<PropertyKey<?>, Object> properties = new HashMap<>();
    private final boolean immutable;

    private CustomProperties(boolean immutable, PropertySet<?>... properties) {
        this.immutable = immutable;
        for (PropertySet<?> set : properties) {
            this.properties.put(set.key(), set.value());
        }
    }
    
    public static CustomProperties of(PropertySet<?>... properties) {
        return new CustomProperties(false, properties);
    }

    public static CustomProperties ofImmutable(PropertySet<?>... properties) {
        return new CustomProperties(true, properties);
    }

    public static CustomProperties empty() {
        return new CustomProperties(true);
    }
    
    public <T> void set(PropertyKey<T> key, T value) {
        if (immutable) return;
        properties.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(PropertyKey<T> key) {
        return (T) properties.getOrDefault(key, key.defaultValue());
    }

    public boolean contains(PropertyKey<?> key) {
        return properties.containsKey(key);
    }

    public void remove(PropertyKey<?> key) {
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

    public record PropertyKey<T>(T defaultValue) {}
    public record PropertySet<T>(PropertyKey<T> key, T value) {}
}
