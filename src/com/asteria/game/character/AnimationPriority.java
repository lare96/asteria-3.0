package com.asteria.game.character;

/**
 * The enumerated type whose elements represent the levels an animation can be
 * prioritized with.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum AnimationPriority {
    LOW(0),
    NORMAL(1),
    HIGH(2);

    /**
     * The value of this priority. A higher value indicates a greater priority.
     */
    private final int value;

    /**
     * Creates a new {@link AnimationPriority}.
     *
     * @param value
     *            the value of this priority.
     */
    private AnimationPriority(int value) {
        this.value = value;
    }

    /**
     * Gets the value of this priority.
     *
     * @return the value of this priority.
     */
    public final int getValue() {
        return value;
    }
}
