package com.asteria.utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * The static-utility class that contains array utility functions.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ArrayUtils {

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private ArrayUtils() {
        throw new UnsupportedOperationException("This class cannot be " + "instantiated!");
    }

    /**
     * Pseudo-randomly retrieves a element from {@code array}.
     *
     * @param array
     *            the array to retrieve an element from.
     * @return the element retrieved from the array.
     */
    public static <T> T random(T[] array, Random random) {
        return array[(int) (random.nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code int} from this {@code array}.
     *
     * @param array
     *            the array to retrieve an {@code int} from.
     * @return the {@code int} retrieved from the array.
     */
    public static int random(int[] array, Random random) {
        return array[(int) (random.nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code long} from this {@code array}.
     *
     * @param array
     *            the array to retrieve an {@code long} from.
     * @return the {@code long} retrieved from the array.
     */
    public static long random(long[] array, Random random) {
        return array[(int) (random.nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code double} from this {@code array}.
     *
     * @param array
     *            the array to retrieve an {@code double} from.
     * @return the {@code double} retrieved from the array.
     */
    public static double random(double[] array, Random random) {
        return array[(int) (random.nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code float} from this {@code array}.
     *
     * @param array
     *            the array to retrieve an {@code float} from.
     * @return the {@code float} retrieved from the array.
     */
    public static float random(float[] array, Random random) {
        return array[(int) (random.nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code short} from this {@code array}.
     *
     * @param array
     *            the array to retrieve an {@code short} from.
     * @return the {@code short} retrieved from the array.
     */
    public static short random(short[] array, Random random) {
        return array[(int) (random.nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code byte} from this {@code array}.
     *
     * @param array
     *            the array to retrieve an {@code byte} from.
     * @return the {@code byte} retrieved from the array.
     */
    public static byte random(byte[] array, Random random) {
        return array[(int) (random.nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code boolean} from this {@code array}.
     *
     * @param array
     *            the array to retrieve an {@code boolean} from.
     * @return the {@code boolean} retrieved from the array.
     */
    public static boolean random(boolean[] array, Random random) {
        return array[(int) (random.nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code char} from this {@code array}.
     *
     * @param array
     *            the array to retrieve an {@code char} from.
     * @return the {@code char} retrieved from the array.
     */
    public static char random(char[] array, Random random) {
        return array[(int) (random.nextDouble() * array.length)];
    }

    /**
     * Replaces all the data in {@code to} with {@code from} without making
     * modifications to the array being dumped. References are held to all of
     * the dumped Objects.
     *
     * @param from
     *            the data array to dump from.
     * @param to
     *            the data array to dump to.
     * @throws IllegalArgumentException
     *             if the arrays are not equal in length.
     */
    public static <T> void dump(T[] from, T[] to) {
        if (from.length != to.length)
            throw new IllegalArgumentException("Arrays must be equal in size!");
        for (int i = 0; i < from.length; i++)
            to[i] = from[i];
    }

    /**
     * Replaces all the data in {@code to} with {@code from} without making
     * modifications to the array being dumped. No references are held to the
     * dumped data since {@code int} is a primitive type.
     *
     * @param from
     *            the data array to dump from.
     * @param to
     *            the data array to dump to.
     * @throws IllegalArgumentException
     *             if the arrays are not equal in length.
     */
    public static void dump(int[] from, int[] to) {
        if (from.length != to.length)
            throw new IllegalArgumentException("Arrays must be equal in size!");
        for (int i = 0; i < from.length; i++)
            to[i] = from[i];
    }

    /**
     * An {@link Iterator} implementation that will iterate over the elements in
     * an array without the overhead of the {@link ArrayList}s {@code remove}
     * operation.
     *
     * @param <E>
     *            the type of array being iterated over.
     * @author lare96 <http://github.com/lare96>
     */
    public static final class ArrayIterator<E> implements Iterator<E> {

        /**
         * The array that is storing the elements.
         */
        private final E[] array;

        /**
         * The current index that the iterator is iterating over.
         */
        private int index;

        /**
         * The last index that the iterator iterated over.
         */
        private int lastIndex = -1;

        /**
         * Creates a new {@link CharacterListIterator}.
         *
         * @param list
         *            the array that is storing the elements.
         */
        public ArrayIterator(E[] array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return !(index + 1 > array.length);
        }

        @Override
        public E next() {
            if (index >= array.length)
                throw new ArrayIndexOutOfBoundsException("There are no elements left to iterate over!");
            lastIndex = index;
            index++;
            return array[lastIndex];
        }

        @Override
        public void remove() {
            if (lastIndex == -1)
                throw new IllegalStateException("This method can only be called once after \"next\".");
            array[lastIndex] = null;
            index = lastIndex;
            lastIndex = -1;
        }
    }
}
