package com.asteria.content.skills.fishing;

import java.util.EnumSet;
import java.util.Optional;

import com.asteria.game.character.player.Player;
import com.asteria.game.location.Location;

/**
 * The enumerated type whose elements represent catchable items.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public enum Catchable {
    SHRIMP(317, 1, 0.85, 10),
    SARDINE(327, 5, 0.8, 20),
    HERRING(345, 10, 0.85, 30),
    ANCHOVY(321, 15, 0.45, 40),
    MACKEREL(353, 16, 0.9, 20),
    CASKET(405, 16, 0.01, 100),
    OYSTER(407, 16, 0.05, 80),
    TROUT(335, 20, 0.95, 50),
    COD(341, 23, 0.9, 45),
    PIKE(349, 25, 0.9, 60),
    SLIMY_EEL(3379, 28, 0.05, 65) {
        @Override
        public boolean catchable(Player player) {
            return Location.inWilderness(player);
        }
    },
    SALMON(331, 30, 0.75, 70),
    TUNA(359, 35, 0.95, 80),
    CAVE_EEL(5001, 38, 0.07, 80) {
        @Override
        public boolean catchable(Player player) {
            return Location.inWilderness(player);
        }
    },
    LOBSTER(377, 40, 0.85, 90),
    BASS(363, 46, 0.5, 100),
    SWORDFISH(371, 50, 0.75, 100),
    LAVA_EEL(2148, 53, 0.85, 60) {
        @Override
        public boolean catchable(Player player) {
            return Location.inWilderness(player);
        }
    },
    SHARK(383, 76, 0.7, 110);

    /**
     * The enum set containing all of the elements in this enumeration.
     */
    private static final EnumSet<Catchable> ELEMENTS = EnumSet.allOf(Catchable.class);

    /**
     * The identification for this catchable item.
     */
    private final int id;

    /**
     * The level needed to be able to catch this item.
     */
    private final int level;

    /**
     * The chance to be able to catch this item.
     */
    private final double chance;

    /**
     * The experience given when catching this item.
     */
    private final int experience;

    /**
     * Creates a new {@link Catchable}.
     * 
     * @param id
     *            the identification for this catchable item.
     * @param level
     *            the level needed to be able to catch this item.
     * @param chance
     *            the chance to be able to catch this item.
     * @param experience
     *            the experience given when catching this item.
     */
    private Catchable(int id, int level, double chance, int experience) {
        this.id = id;
        this.level = level;
        this.chance = chance;
        this.experience = experience;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replaceAll("_", " ");
    }

    /**
     * The method that determines if this catchable item can be caught by
     * {@code player}.
     * 
     * @param player
     *            the player attempting to catch this item.
     * @return {@code true} if the player can catch this item, {@code false}
     *         otherwise.
     */
    public boolean catchable(Player player) {
        return true;
    }

    /**
     * Gets the identification for this catchable item.
     * 
     * @return the identification.
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the level needed to be able to catch this item.
     * 
     * @return the level needed.
     */
    public final int getLevel() {
        return level;
    }

    /**
     * Gets the chance to be able to catch this item.
     * 
     * @return the chance to catch this.
     */
    public final double getChance() {
        return chance;
    }

    /**
     * Gets the experience given when catching this item.
     * 
     * @return the experience given.
     */
    public final int getExperience() {
        return experience;
    }

    /**
     * Retrieves an element by its identification {@code id}.
     * 
     * @param id
     *            the identification of the catchable to retrieve.
     * @return the retrieved element.
     */
    public static Optional<Catchable> getCatchable(int id) {
        return ELEMENTS.stream().filter(c -> c.id == id).findFirst();
    }
}