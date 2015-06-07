package com.asteria.utility;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * The static-utility class that contains collection utility functions.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CollectionUtils {
    
    public static <K, V> ImmutableMap<K, V> build(ImmutableMapBuilder<K, V> builder) {
        return ImmutableMap.copyOf(builder.create());
    }
    
    @FunctionalInterface
    public static interface ImmutableMapBuilder<K, V> {
        
        public Map<K, V> build(Map<K, V> map);

        default Map<K, V> create() {
            return build(Maps.newHashMap());
        }
    }
}
