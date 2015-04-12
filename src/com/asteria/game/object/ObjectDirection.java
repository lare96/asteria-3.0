package com.asteria.game.object;

/**
 * The enumerated type whose elements represent the directions for objects.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum ObjectDirection {
    NORTH(1),
    SOUTH(3),
    EAST(2),
    WEST(0);

    /**
     * The identification of this direction.
     */
    private final int id;

    /**
     * Creates a new {@link ObjectDirection}.
     *
     * @param id
     *            the identification of this direction.
     */
    private ObjectDirection(int id) {
        this.id = id;
    }

    /**
     * Gets the identification of this direction.
     *
     * @return the identification of this direction.
     */
    public final int getId() {
        return id;
    }
}