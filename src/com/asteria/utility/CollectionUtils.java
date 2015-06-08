package com.asteria.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * The static-utility class that contains collection utility functions.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CollectionUtils {

    /**
     * Returns an {@link ArrayList} with {@code elements} packed into the
     * collection. The list returned will be shallow, in that the list will not
     * hold references to the array itself but the contents within the array.
     * 
     * @param elements
     *            the elements to pack into the list.
     * @return the list with the packed elements.
     */
    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... elements) {
        ArrayList<E> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }

    /**
     * Returns a {@link LinkedList} with {@code elements} packed into the
     * collection. The list returned will be shallow, in that the list will not
     * hold references to the array itself but the contents within the array.
     * 
     * @param elements
     *            the elements to pack into the list.
     * @return the list with the packed elements.
     */
    @SafeVarargs
    public static <E> LinkedList<E> newLinkedList(E... elements) {
        LinkedList<E> list = new LinkedList<>();
        Collections.addAll(list, elements);
        return list;
    }

    /**
     * Builds an {@link ImmutableMap} using the {@code builder}.
     * 
     * @param builder
     *            the builder to build the map with.
     * @return the immutable map.
     */
    public static <K, V> ImmutableMap<K, V> build(ImmutableMapBuilder<K, V> builder) {
        return builder.create();
    }

    /**
     * The functional interface that serves the purpose of building
     * {@link ImmutableMap}s from regular {@link Map}s.
     * 
     * @author lare96 <http://github.org/lare96>
     * @param <K>
     *            the key for the immutable map builder.
     * @param <V>
     *            the value for the immutable map builder.
     */
    @FunctionalInterface
    public static interface ImmutableMapBuilder<K, V> {

        /**
         * Provides an empty map for the user to build and return, to be later
         * transformed into an {@link ImmutableMap}.
         * 
         * @param map
         *            an empty map that can be manipulated.
         * @return
         */
        public Map<K, V> build(Map<K, V> map);

        /**
         * Transforms the map from {@code build} into an {@link ImmutableMap}.
         * 
         * @return the built map.
         */
        default ImmutableMap<K, V> create() {
            return ImmutableMap.copyOf(build(Maps.newHashMap()));
        }
    }
}
