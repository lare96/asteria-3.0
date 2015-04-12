package com.asteria.game.character;

import java.util.BitSet;

/**
 * The container class that contains functions for managing update flags.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class UpdateFlags {

    /**
     * The bit buffer that holds data for the update flags.
     */
    private final BitSet bits = new BitSet(Flag.size());

    /**
     * The default constructor with a {@code protected} access modifier, to
     * restrict the instantiation of this class to the
     * {@code com.asteria.game.character} package.
     */
    protected UpdateFlags() {

    }

    /**
     * Sets an update flag in the bit buffer to {@code value}.
     *
     * @param flag
     *            the flag in the bit buffer to set.
     * @param value
     *            the value to set the flag with.
     */
    public void set(Flag flag, boolean value) {
        bits.set(flag.getId(), value);
    }

    /**
     * Sets an update flag in the bit buffer to {@code true}.
     *
     * @param flag
     *            the flag in the bit buffer to set.
     */
    public void set(Flag flag) {
        set(flag, true);
    }

    /**
     * Retrieves the value of an update flag in the bit buffer.
     *
     * @param flag
     *            the flag in the bit buffer to retrieve the value of.
     * @return the value of the retrieved flag.
     */
    public boolean get(Flag flag) {
        return bits.get(flag.getId());
    }

    /**
     * Determines if at least of flag in the bit buffer has a value of
     * {@code true}.
     *
     * @return {@code true} if at least one flag in the bit buffer is flagged,
     *         {@code false} otherwise.
     */
    public boolean needsUpdate() {
        return !bits.isEmpty();
    }

    /**
     * Resets the backing bit buffer by setting all flags to a value of
     * {@code false}.
     */
    public void reset() {
        bits.clear();
    }
}
