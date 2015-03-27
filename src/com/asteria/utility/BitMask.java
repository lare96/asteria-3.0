package com.asteria.utility;

/**
 * The faster and more memory efficient alternative to associating
 * {@code Object}s to boolean values.
 * <p>
 * <p>
 * Please note that a maximum of only {@code 32} masks can be used.
 * Alternatively, a backing {@code long} can be used to ensure that a maximum of
 * {@code 64} masks can be used.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class BitMask {

    /**
     * The flags that this bit mask contains.
     */
    private int flags;

    /**
     * Creates a new {@link BitMask}.
     *
     * @param flags
     *            the initial flags to create this bit mask with.
     */
    public BitMask(int flags) {
        this.flags = flags;
    }

    /**
     * Creates a new {@link BitMask} with no initial flags.
     */
    public BitMask() {
        this(0);
    }

    /**
     * Activates the state for {@code mask} in this bit mask, regardless of its
     * current state.
     * 
     * @param mask
     *            the mask to activate in this bit mask.
     */
    public void set(int mask) {
        flags |= mask;
    }

    /**
     * Deactivates the state for {@code mask} in this bit mask, regardless of
     * its current state.
     * 
     * @param mask
     *            the mask to deactivate in this bit mask.
     */
    public void unset(int mask) {
        flags &= ~mask;
    }

    /**
     * Checks the state of {@code mask} in this bit mask.
     * 
     * @param mask
     *            the mask to check the state of.
     * @return {@code true} if the mask is activated, {@code false} otherwise.
     */
    public boolean check(int mask) {
        return (flags & mask) == mask;
    }

    /**
     * Flips {@code mask} in this bit mask, effectively toggling the state.
     * 
     * @param mask
     *            the mask to toggle the state of.
     */
    public void flip(int mask) {
        flags ^= mask;
    }

    /**
     * Clears this bit mask of all its flags.
     */
    public void clear() {
        flags = 0;
    }

    /**
     * Gets the internal {@code int} representing the flags in this bit mask.
     * 
     * @return the internal {@code int} representing the flags.
     */
    public int get() {
        return flags;
    }
}
