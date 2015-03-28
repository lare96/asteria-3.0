package com.asteria.utility;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * The {@link Random} implementation that provides additional functionality for
 * generating pseudo-random numbers.
 *
 * @author lare96 <http://github.com/lare96>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class RandomGen extends Random {

    /**
     * The generated serial version UID for serialization.
     */
    private static final long serialVersionUID = 2653463867405580121L;

    /**
     * Creates a new {@link RandomGen} with {@code seed}.
     *
     * @param seed
     *         the seed to create this random number generator with.
     */
    public RandomGen(long seed) {
        super(seed);
    }

    /**
     * Creates a new {@link RandomGen} with a unique {@code seed}.
     */
    public RandomGen() {
        super();
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive {@code min}
     * and exclusive {@code max}.
     * <p>
     * <p>
     * <p>
     * This method is thread-safe. </br>
     *
     * @param min
     *         The minimum inclusive number.
     * @param max
     *         The maximum exclusive number.
     * @return The pseudo-random {@code int}.
     * @throws IllegalArgumentException
     *         If the specified range is less {@code 0}
     */
    public int exclusive(int min, int max) {
        if (max <= min) {
            max = min + 1;
        }
        return nextInt((max - min)) + min;
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive {@code 0}
     * and
     * exclusive {@code range}.
     * <p>
     * <p>
     * <p>
     * This method is thread-safe. </br>
     *
     * @param range
     *         the exclusive range.
     * @return the pseudo-random {@code int}.
     * @throws IllegalArgumentException
     *         if the specified range is less {@code 0}
     */
    public int exclusive(int range) {
        return exclusive(0, range);
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive {@code min}
     * and inclusive {@code max}.
     *
     * @param min
     *         the minimum inclusive number.
     * @param max
     *         the maximum inclusive number.
     * @return the pseudo-random {@code int}.
     * @throws IllegalArgumentException
     *         if {@code max - min + 1} is less than {@code 0}.
     * @see {@link #exclusive(int)}.
     */
    public int inclusive(int min, int max) {
        if (max < min) {
            max = min + 1;
        }
        return exclusive((max - min) + 1) + min;
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive {@code 0}
     * and
     * inclusive {@code range}.
     *
     * @param range
     *         the maximum inclusive number.
     * @return the pseudo-random {@code int}.
     * @throws IllegalArgumentException
     *         if {@code max - min + 1} is less than {@code 0}.
     * @see {@link #exclusive(int)}.
     */
    public int inclusive(int range) {
        return inclusive(0, range);
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive {@code min}
     * and inclusive {@code max} excluding the specified numbers within the
     * {@code excludes} array.
     *
     * @param min
     *         the minimum inclusive number.
     * @param max
     *         the maximum inclusive number.
     * @return the pseudo-random {@code int}.
     * @throws IllegalArgumentException
     *         if {@code max - min + 1} is less than {@code 0}.
     * @see {@link #inclusive(int, int)}.
     */
    public int inclusiveExcludes(int min, int max, int... exclude) {
        Arrays.sort(exclude);

        int result = inclusive(min, max);
        while (Arrays.binarySearch(exclude, result) >= 0) {
            result = inclusive(min, max);
        }

        return result;
    }

    /**
     * Returns a pseudo-random {@code float} between inclusive {@code 0} and
     * exclusive {@code range}.
     *
     * @param range
     *         The exclusive range.
     * @return The pseudo-random {@code float}.
     * @throws IllegalArgumentException
     *         If the specified range is less than {@code 0}.
     */
    public float floatRandom(float range) {
        if (range < 0F)
            throw new IllegalArgumentException("range <= 0");
        return nextFloat() * range;
    }

    /**
     * Pseudo-randomly retrieves a element from {@code array}.
     *
     * @param array
     *         the array to retrieve an element from.
     * @return the element retrieved from the array.
     */
    public <T> T random(T[] array) {
        return array[(int) (nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code int} from this {@code array}.
     *
     * @param array
     *         the array to retrieve an {@code int} from.
     * @return the {@code int} retrieved from the array.
     */
    public int random(int[] array) {
        return array[(int) (nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code long} from this {@code array}.
     *
     * @param array
     *         the array to retrieve an {@code long} from.
     * @return the {@code long} retrieved from the array.
     */
    public long random(long[] array) {
        return array[(int) (nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code double} from this {@code array}.
     *
     * @param array
     *         the array to retrieve an {@code double} from.
     * @return the {@code double} retrieved from the array.
     */
    public double random(double[] array) {
        return array[(int) (nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code short} from this {@code array}.
     *
     * @param array
     *         the array to retrieve an {@code short} from.
     * @return the {@code short} retrieved from the array.
     */
    public short random(short[] array) {
        return array[(int) (nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code byte} from this {@code array}.
     *
     * @param array
     *         the array to retrieve an {@code byte} from.
     * @return the {@code byte} retrieved from the array.
     */
    public byte random(byte[] array) {
        return array[(int) (nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code float} from this {@code array}.
     *
     * @param array
     *         the array to retrieve an {@code float} from.
     * @return the {@code float} retrieved from the array.
     */
    public float random(float[] array) {
        return array[(int) (nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code boolean} from this {@code array}.
     *
     * @param array
     *         the array to retrieve an {@code boolean} from.
     * @return the {@code boolean} retrieved from the array.
     */
    public boolean random(boolean[] array) {
        return array[(int) (nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves an {@code char} from this {@code array}.
     *
     * @param array
     *         the array to retrieve an {@code char} from.
     * @return the {@code char} retrieved from the array.
     */
    public char random(char[] array) {
        return array[(int) (nextDouble() * array.length)];
    }

    /**
     * Pseudo-randomly retrieves a element from {@code list}.
     *
     * @param list
     *         the list to retrieve an element from.
     * @return the element retrieved from the list.
     */
    public <T> T random(List<T> list) {
        return list.get((int) (nextDouble() * list.size()));
    }

    /**
     * Rounds and returns the {@code counter} to the {@code place}.
     *
     * @param counter
     *         the number that will be rounded.
     * @param place
     *         the decimal place to round to.
     * @return the rounded number.
     */
    public double round(double counter, double place) {
        return Math.round(counter * place) / place;
    }

    /**
     * Generates a pseudo-random {@code double} to be rounded and rolled
     * against
     * the {@code bet}.
     *
     * @param bet
     *         the bet that the number will be rolled against.
     * @param round
     *         the {@code double} that will be used to round the bet and
     *         pseudo-randomly generated number.
     * @return {@code true} if the roll was successful, {@code false} otherwise.
     */
    public boolean roll(double bet, double round) {
        double betRound = round(bet, round);
        double genRound = round(nextDouble(), round);
        return genRound <= betRound;
    }

    /**
     * Generates a pseudo-random {@code double} to be rounded to {@code 100.0}
     * and rolled against the {@code bet}.
     *
     * @param bet
     *         the bet that the number will be rolled against.
     * @return {@code true} if the roll was successful, {@code false} otherwise.
     */
    public boolean roll(double bet) {
        return roll(bet, 100.0);
    }
}
