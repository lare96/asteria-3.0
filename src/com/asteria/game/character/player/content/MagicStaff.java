package com.asteria.game.character.player.content;

import java.util.Arrays;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.item.container.Equipment;

/**
 * The enumerated type whose elements represent the different types of staves.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum MagicStaff {
    AIR(new int[] { 1381, 1397, 1405 }, new int[] { 556 }),
    WATER(new int[] { 1383, 1395, 1403 }, new int[] { 555 }),
    EARTH(new int[] { 1385, 1399, 1407 }, new int[] { 557 }),
    FIRE(new int[] { 1387, 1393, 1401 }, new int[] { 554 }),
    MUD(new int[] { 6562, 6563 }, new int[] { 555, 557 }),
    LAVA(new int[] { 3053, 3054 }, new int[] { 554, 557 });

    /**
     * The current identifiers for this staff type.
     */
    private final int[] ids;

    /**
     * The runes that this staff type can replace.
     */
    private final int[] runes;

    /**
     * Creates a new {@link MagicStaff}.
     *
     * @param ids
     *            the current identifiers for this staff type.
     * @param runes
     *            the runes that this staff type can replace.
     */
    private MagicStaff(int[] ids, int[] runes) {
        this.ids = ids;
        this.runes = runes;
    }

    /**
     * Suppresses items in {@code required} if any of the items match the runes
     * that are represented by the staff {@code player} is wielding.
     *
     * @param player
     *            the player to suppress runes for.
     * @param required
     *            the array of runes to suppress.
     * @return the new array of items with suppressed runes removed.
     */
    public static Item[] suppressRunes(Player player, Item[] required) {
        int weapon = player.getInventory().getId(Equipment.WEAPON_SLOT);
        if (player.getWeapon() != WeaponInterface.STAFF || weapon == -1)
            return required;
        Arrays.stream(MagicStaff.values()).forEach(m -> {
            if (Arrays.stream(m.ids).anyMatch(i -> i == weapon)) {
                for (int id : m.runes) {
                    for (int i = 0; i < required.length; i++) {
                        if (required[i] == null)
                            continue;
                        if (required[i].getId() == id) {
                            required[i] = null;
                        }
                    }
                }
            }
        });
        return required;
    }

    /**
     * Gets the current identifiers for this staff type.
     *
     * @return the identifiers for this staff.
     */
    public final int[] getIds() {
        return ids;
    }

    /**
     * Gets the runes that this staff type can replace.
     *
     * @return the runes this staff replaces.
     */
    public final int[] getRunes() {
        return runes;
    }
}
