package com.asteria.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

/**
 * The static-utility class that contains collection utility functions.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CollectionUtils {

    /**
     * Returns an {@link Iterable} constructed using the given {@code iterator}.
     * 
     * @param iterator
     *            the iterator to construct with.
     * @return the newly constructed iterable.
     */
    public static <E> Iterable<E> newIterable(Iterator<E> iterator) {
        return new Iterable<E>() {
            @Override
            public Iterator<E> iterator() {
                return iterator;
            }
        };
    }

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
     * Builds an {@link ImmutableMultimap} using the {@code builder}.
     * 
     * @param builder
     *            the builder to build the map with.
     * @return the immutable multimap.
     */
    public static <K, V> ImmutableMultimap<K, V> build(ImmutableMultimapBuilder<K, V> builder) {
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
         * Provides logic space for the user to build and return a map, to be
         * later transformed into an {@link ImmutableMap}.
         * 
         * @return the map to be transformed.
         */
        public Map<K, V> build();

        /**
         * Transforms the map from {@code build} into an {@link ImmutableMap}.
         * 
         * @return the built map.
         */
        default ImmutableMap<K, V> create() {
            return ImmutableMap.copyOf(build());
        }
    }

    /**
     * The functional interface that serves the purpose of building
     * {@link ImmutableMultimap}s from regular {@link Multimap}s.
     * 
     * @author lare96 <http://github.org/lare96>
     * @param <K>
     *            the key for the immutable multimap builder.
     * @param <V>
     *            the value for the immutable multimap builder.
     */
    @FunctionalInterface
    public static interface ImmutableMultimapBuilder<K, V> {

        /**
         * Provides logic space for the user to build and return a multimap, to
         * be later transformed into an {@link ImmutableMultimap}.
         * 
         * @return the map to be transformed.
         */
        public Multimap<K, V> build();

        /**
         * Transforms the map from {@code build} into an {@link ImmutableMap}.
         * 
         * @return the built map.
         */
        default ImmutableMultimap<K, V> create() {
            return ImmutableMultimap.copyOf(build());
        }
    }
}
