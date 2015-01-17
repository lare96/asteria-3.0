package com.asteria.utility;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.asteria.game.location.Position;

/**
 * A collection of miscellaneous utility methods and constants.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class Utility {

    /**
     * The array of characters used for unpacking text.
     */
    public static final char CHARACTER_TABLE[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w',
            'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ',
            '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[',
            ']' };

    /**
     * The buffer used for unpacking text.
     */
    public static final char DECODE_BUFFER[] = new char[4096];

    /**
     * The array of valid characters.
     */
    public static final char VALID_CHARACTERS[] = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!',
            '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',', '"', '[', ']', '|', '?',
            '/', '`' };

    /**
     * The default constructor.
     * 
     * @throws InstantiationException
     *             if this class is instantiated.
     */
    private Utility() {
        throw new InstantiationError("This class cannot be instantiated!");
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
        if(from.length != to.length)
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
     * Creates a new logger instance using {@link Class#getSimpleName()} of
     * {@code clazz}.
     * 
     * @param clazz
     *            the class to create the new logger with.
     * @return the logger instance.
     */
    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getSimpleName());
    }

    /**
     * Gets the current time in {@link TimeUnit#MILLISECONDS}. This method is
     * more accurate than {@link System#currentTimeMillis()} and does not rely
     * on the underlying OS.
     * 
     * @return the current time in milliseconds.
     */
    public static long currentTime() {
        return TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    /**
     * Determines the indefinite article of {@code thing}.
     * 
     * @param thing
     *            the thing to determine for.
     * @return the indefinite article.
     */
    public static String determineIndefiniteArticle(String thing) {
        char first = thing.toLowerCase().charAt(0);
        boolean vowel = first == 'a' || first == 'e' || first == 'i' || first == 'o' || first == 'u';
        return vowel ? "an" : "a";
    }

    /**
     * Appends the determined indefinite article to {@code thing}.
     * 
     * @param thing
     *            the thing to append.
     * @return the {@code thing} after the indefinite article has been appended.
     */
    public static String appendIndefiniteArticle(String thing) {
        return determineIndefiniteArticle(thing).concat(" " + thing);
    }

    /**
     * Formats {@code price} into K, million, or its default value.
     * 
     * @param price
     *            the price to format.
     * @return the newly formatted price.
     */
    public static String formatPrice(int price) {
        if (price >= 1000 && price < 1000000) {
            return " (" + (price / 1000) + "K)";
        } else if (price >= 1000000) {
            return " (" + (price / 1000000) + " million)";
        }
        return Integer.toString(price);
    }

    /**
     * Converts a {@code long} hash into a string value.
     * 
     * @param l
     *            the long to convert.
     * @return the converted string.
     */
    public static String hashToName(long l) {
        int i = 0;
        char ac[] = new char[12];
        while (l != 0L) {
            long l1 = l;
            l /= 37L;
            ac[11 - i++] = VALID_CHARACTERS[(int) (l1 - l * 37L)];
        }
        return new String(ac, 12 - i, i);
    }

    /**
     * Converts a string to a {@code long} hash value.
     * 
     * @param s
     *            the string to convert.
     * @return the long hash value.
     */
    public static long nameToHash(String s) {
        long l = 0L;
        for (int i = 0; i < s.length() && i < 12; i++) {
            char c = s.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z')
                l += (1 + c) - 65;
            else if (c >= 'a' && c <= 'z')
                l += (1 + c) - 97;
            else if (c >= '0' && c <= '9')
                l += (27 + c) - 48;
        }
        while (l % 37L == 0L && l != 0L)
            l /= 37L;
        return l;
    }

    /**
     * Converts an array of bytes to an {@code int}.
     * 
     * @param data
     *            the array of bytes.
     * @return the newly constructed {@code int}.
     */
    public static int hexToInt(byte[] data) {
        int value = 0;
        int n = 1000;
        for (int i = 0; i < data.length; i++) {
            int num = (data[i] & 0xFF) * n;
            value += num;
            if (n > 1) {
                n = n / 1000;
            }
        }
        return value;
    }

    /**
     * Capitalizes the first character of {@code str}. Any leading or trailing
     * whitespace in the string should be trimmed before using this method.
     * 
     * @param str
     *            the string to capitalize.
     * @return the capitalized string.
     */
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase().concat(str.substring(1, str.length()));
    }

    /**
     * Returns the delta coordinates. Note that the returned position is not an
     * actual position, instead it's values represent the delta values between
     * the two arguments.
     * 
     * @param a
     *            the first position.
     * @param b
     *            the second position.
     * @return the delta coordinates contained within a position.
     */
    public static Position delta(Position a, Position b) {
        return new Position(b.getX() - a.getX(), b.getY() - a.getY());
    }

    /**
     * Calculates the direction between the two coordinates.
     * 
     * @param dx
     *            the first coordinate.
     * @param dy
     *            the second coordinate.
     * @return the direction.
     */
    public static int direction(int dx, int dy) {
        if (dx < 0) {
            if (dy < 0) {
                return 5;
            } else if (dy > 0) {
                return 0;
            } else {
                return 3;
            }
        } else if (dx > 0) {
            if (dy < 0) {
                return 7;
            } else if (dy > 0) {
                return 2;
            } else {
                return 4;
            }
        } else {
            if (dy < 0) {
                return 6;
            } else if (dy > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
