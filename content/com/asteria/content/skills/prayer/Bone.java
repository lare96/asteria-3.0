package com.asteria.content.skills.prayer;

import java.util.Arrays;
import java.util.Optional;

/**
 * The enumerated type whose elements represent bones that can be buried.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public enum Bone {
    BONES(526, 4),
    BAT_BONES(530, 5),
    MONKEY_BONES(3179, 5),
    WOLF_BONES(2859, 4),
    BIG_BONES(532, 15),
    BABYDRAGON_BONES(534, 30),
    DRAGON_BONES(536, 72);

    /**
     * The identifier of this prayer bone.
     */
    private final int id;

    /**
     * The experience given for burying this prayer bone.
     */
    private final int experience;

    /**
     * Creates a new {@link Bone}.
     * 
     * @param id
     *            the identifier of this prayer bone.
     * @param experience
     *            the experience given for burying this prayer bone.
     */
    private Bone(int id, int experience) {
        this.id = id;
        this.experience = experience;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replaceAll("_", " ");
    }

    /**
     * Gets the identifier of this prayer bone.
     * 
     * @return the identifier.
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the experience given for burying this prayer bone.
     * 
     * @return the experience given.
     */
    public final int getExperience() {
        return experience;
    }

    /**
     * Retrieves an element by its {@code id}.
     * 
     * @param id
     *            the identifier of the prayer bone.
     * @return the element wrapped in an optional, or an empty optional if no
     *         element was found.
     */
    public static Optional<Bone> getBone(int id) {
        return Arrays.stream(Bone.values()).filter(l -> l.id == id).findFirst();
    }
}
