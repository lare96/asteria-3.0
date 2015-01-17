package com.asteria.content.skills.cooking;

import java.util.EnumSet;
import java.util.Optional;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.ItemDefinition;

/**
 * The enumerated type whose elements represent food that can be cooked.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public enum CookingData {
    SHRIMP(317, 1, 315, 34, 323, 30),
    SARDINE(327, 5, 325, 38, 369, 40),
    HERRING(345, 10, 347, 37, 357, 50),
    ANCHOVIES(321, 15, 319, 34, 323, 30),
    MACKEREL(353, 16, 355, 45, 357, 60),
    TROUT(335, 20, 333, 50, 343, 70),
    COD(341, 23, 339, 39, 343, 75),
    PIKE(349, 25, 351, 52, 343, 80),
    SLIMY_EEL(3379, 28, 3381, 56, 3383, 95),
    SALMON(331, 30, 329, 58, 343, 90),
    TUNA(359, 35, 361, 63, 367, 100),
    CAVE_EEL(5001, 38, 5003, 72, 5002, 115),
    LOBSTER(377, 40, 379, 74, 381, 120),
    BASS(363, 46, 365, 80, 367, 130),
    SWORDFISH(371, 50, 373, 86, 375, 140),
    LAVA_EEL(2148, 53, 2149, 89, 3383, 140),
    SHARK(383, 76, 385, 94, 387, 210);

    /**
     * The enum set containing all of the elements in this enumeration.
     */
    private static final EnumSet<CookingData> ELEMENTS = EnumSet.allOf(CookingData.class);

    /**
     * The identification of the raw food.
     */
    private final int rawId;

    /**
     * The level to cook this food.
     */
    private final int level;

    /**
     * The identification of the cooked food.
     */
    private final int cookedId;

    /**
     * The level that this food stops burning at.
     */
    private final int masterLevel;

    /**
     * The identification of the burnt food.
     */
    private final int burntId;

    /**
     * The experience given when this food is successfully cooked.
     */
    private final int experience;

    /**
     * Creates a new {@link CookingData}.
     * 
     * @param rawId
     *            the identification of the raw food.
     * @param level
     *            the level to cook this food.
     * @param cookedId
     *            the identification of the cooked food.
     * @param masterLevel
     *            the level that this food stops burning at.
     * @param burntId
     *            the identification of the burnt food.
     * @param experience
     *            the experience given when this food is successfully cooked.
     */
    private CookingData(int rawId, int level, int cookedId, int masterLevel, int burntId, int experience) {
        this.rawId = rawId;
        this.level = level;
        this.cookedId = cookedId;
        this.masterLevel = masterLevel;
        this.burntId = burntId;
        this.experience = experience;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replaceAll("_", " ");
    }

    /**
     * Opens the cooking interface for the food trying to be cooked.
     * 
     * @param player
     *            the player to open the interface for.
     */
    public void openInterface(Player player) {
        player.getEncoder().sendChatInterface(1743);
        player.getEncoder().sendItemModelOnInterface(13716, 190, rawId);
        player.getEncoder().sendString("\\n\\n\\n\\n\\n" + ItemDefinition.DEFINITIONS[rawId].getName(), 13717);
    }

    /**
     * Gets the identification of the raw food.
     * 
     * @return the raw identification.
     */
    public final int getRawId() {
        return rawId;
    }

    /**
     * Gets the level to cook this food.
     * 
     * @return the level.
     */
    public final int getLevel() {
        return level;
    }

    /**
     * Gets the identification of the cooked food.
     * 
     * @return the cooked identification.
     */
    public final int getCookedId() {
        return cookedId;
    }

    /**
     * Gets the level that this food stops burning at.
     * 
     * @return the level that burning stops at.
     */
    public final int getMasterLevel() {
        return masterLevel;
    }

    /**
     * Gets the identification of the burnt food.
     * 
     * @return the burnt identification.
     */
    public final int getBurntId() {
        return burntId;
    }

    /**
     * Gets the experience given when this food is successfully cooked.
     * 
     * @return the experience given.
     */
    public final int getExperience() {
        return experience;
    }

    /**
     * Retrieves an element by its identification {@code rawId}.
     * 
     * @param rawId
     *            the identification of the cooking data to retrieve.
     * @return the retrieved element.
     */
    public static Optional<CookingData> getData(int rawId) {
        return ELEMENTS.stream().filter(f -> f.rawId == rawId).findFirst();
    }
}