package com.asteria.utility;

/**
 * The static-utility class that contains text utility functions.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class TextUtils {

    /**
     * The array of characters used for unpacking text.
     */
    public static final char CHARACTER_TABLE[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y',
            'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',',
            ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']' };

    /**
     * The array of valid characters.
     */
    public static final char VALID_CHARACTERS[] = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%',
            '^', '&', '*', '(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',', '"', '[', ']', '|', '?', '/', '`' };

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private TextUtils() {
        throw new UnsupportedOperationException("This class cannot be " + "instantiated!");
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
     * Formats {@code price} into K, million, or its default value.
     *
     * @param price
     *            the price to format.
     * @return the newly formatted price.
     */
    public static String formatPrice(int price) {
        if (price >= 1000 && price < 1000000) {
            return "(" + (price / 1000) + "K)";
        } else if (price >= 1000000) {
            return "(" + (price / 1000000) + " million)";
        }
        return Integer.toString(price);
    }
}
