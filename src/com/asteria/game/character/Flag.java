package com.asteria.game.character;

/**
 * The enumerated type whose elements represent the update flags used for the
 * update blocks.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public enum Flag {
    APPEARANCE(0),
    CHAT(1),
    GRAPHICS(2),
    ANIMATION(3),
    FORCED_CHAT(4),
    FACE_CHARACTER(5),
    FACE_COORDINATE(6),
    HIT(7),
    HIT_2(8),
    TRANSFORM(9),
    FORCED_MOVEMENT(10);

    /**
     * The identification for this update flag.
     */
    private final int id;

    /**
     * Creates a new {@link Flag}.
     * 
     * @param id
     *            the identification for this update flag.
     */
    private Flag(int id) {
        this.id = id;
    }

    /**
     * Gets the identification for this update flag.
     * 
     * @return the identification for this update flag.
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the size of this enumerated type.
     * 
     * @return the size.
     */
    public static int size() {
        return Flag.values().length;
    }
}