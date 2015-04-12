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
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     *
     * @return the copy of this instance that does not hold any references.
     */
    public BitMask copy() {
        return new BitMask(flags);
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
     * Checks if the state of {@code mask} is active in this bit mask.
     *
     * @param mask
     *            the mask to has the state of.
     * @return {@code true} if the mask is activated, {@code false} otherwise.
     */
    public boolean has(int mask) {
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
     * The flag that determines if this bit mask is empty, or in other words if
     * this bit mask does not have any active or inactivate flags.
     *
     * @return {@code true} if this bit mask is empty, {@code false} otherwise.
     */
    public boolean empty() {
        return flags == 0;
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
