package com.asteria.game.character.npc.drop;

import java.util.HashMap;
import java.util.Map;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.utility.RandomGen;

/**
 * The container class that contains the drop tables for a set of NPCs along
 * with functions to manage these tables.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDropTable {

    /**
     * The hash collection of drop tables for all NPCs.
     */
    public static final Map<Integer, NpcDropTable> DROPS = new HashMap<>();

    /**
     * The random generator instance that will generate random numbers.
     */
    private static RandomGen random = new RandomGen();

    /**
     * The NPC identifiers that these drop tables are for.
     */
    private final int[] npcs;

    /**
     * The dynamic drop table where all items will be accounted for and given
     * the chance to be selected for a drop.
     */
    private final NpcDrop[] dynamic;

    /**
     * The rare drop table where only one item will be accounted for and given
     * the chance to be selected for a drop.
     */
    private final NpcDrop[] rare;

    /**
     * Creates a new {@link NpcDropTable}.
     *
     * @param npcs
     *         the NPC identifiers that these drop tables are for.
     * @param dynamic
     *         the dynamic drop table.
     * @param rare
     *         the rare drop table.
     */
    public NpcDropTable(int[] npcs, NpcDrop[] dynamic, NpcDrop[] rare) {
        this.npcs = npcs;
        this.dynamic = dynamic;
        this.rare = rare;
    }

    /**
     * Performs the necessary calculations on all of the tables in this
     * container to determine an array of items to drop. Please note that this
     * is not a static implementation meaning that calling this multiple times
     * will return a different array of items.
     *
     * @param player
     *         the player that these calculations are being performed for.
     * @return the array of items that were calculated.
     */
    public Item[] toItems(Player player) {
        int slot = 0;
        Item[] items = new Item[(dynamic.length + 1)];
        for (NpcDrop drop : dynamic)
            if (drop.getChance().successful(random))
                items[slot++] = drop.toItem(random);
        if (rare.length == 0)
            return items;
        NpcDrop drop = random.random(rare);
        if (drop.getChance().successful(random))
            items[slot++] = drop.toItem(random);
        return items;
    }

    /**
     * Gets the NPC identifiers that these drop tables are for.
     *
     * @return the NPC identifiers for these tables.
     */
    public int[] getNpcs() {
        return npcs;
    }

    /**
     * Gets the dynamic drop table.
     *
     * @return the dynamic table.
     */
    public NpcDrop[] getDynamic() {
        return dynamic;
    }

    /**
     * Gets the rare drop table.
     *
     * @return the rare table.
     */
    public NpcDrop[] getRare() {
        return rare;
    }
}
