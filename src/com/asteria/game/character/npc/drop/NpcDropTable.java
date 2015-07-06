package com.asteria.game.character.npc.drop;

import static com.asteria.utility.Chance.RARE;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.asteria.game.GameConstants;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.item.container.Equipment;
import com.asteria.utility.ArrayIterator;
import com.asteria.utility.Chance;
import com.asteria.utility.RandomGen;

/**
 * A container that holds the unique and common drop tables.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class NpcDropTable {

    /**
     * The unique drop table that consists of both dynamic and rare drops.
     */
    private final NpcDrop[] unique;

    /**
     * The common drop table that is shared with other tables.
     */
    private final NpcDropCache[] common;

    /**
     * Creates a new {@link NpcDropTable}.
     *
     * @param unique
     *            the unique drop table.
     * @param common
     *            the common drop table.
     */
    public NpcDropTable(NpcDrop[] unique, NpcDropCache[] common) {
        this.unique = unique;
        this.common = common;
    }

    /**
     * Performs the necessary calculations on all of the tables in this
     * container to determine an array of items to drop. Please note that this
     * is not a static implementation meaning that calling this multiple times
     * will return a different array of items.
     *
     * @param player
     *            the player that these calculations are being performed for.
     * @return the array of items that were calculated.
     */
    public List<Item> toItems(Player player) {

        // Instantiate the random generator, the list of items to drop, the list
        // for the rare items, the common table, and a list that contains a
        // shuffled copy of the unique table.
        RandomGen random = new RandomGen();
        List<Item> items = new LinkedList<>();
        NpcDropCache cache = random.random(common);
        Iterator<NpcDrop> $it = new ArrayIterator<>(random.shuffle(unique.clone()));

        // Determines if the rare, common, and dynamic tables should be rolled.
        // The breakdown of each of the formulas are touched upon later on.
        boolean rollRare = random.get().nextInt(5) == 0; // 20% chance.
        boolean rollCommon = player != null && player.getEquipment().getId(Equipment.RING_SLOT) == 2572 ? random.get().nextInt(4) == 0
            : random.get().nextInt(8) == 0; // 12.5%-25% chance.
        boolean rollDynamic = random.get().nextBoolean(); // 50% chance.

        // Iterate through the unique table, drop ALWAYS items, roll a RARE+
        // item if possible, and roll dynamic items if possible.
        int amount = 0;
        while ($it.hasNext()) {
            NpcDrop next = $it.next();
            Chance chance = next.getChance();

            if (chance.getTier() == 0) {

                // 100% Chance to drop an item from the always table, the lowest
                // Chance tier.
                items.add(next.toItem(random));
            } else if (chance.getTier() >= RARE.getTier() && rollRare) {

                // 20% Chance to roll an item from the rare table, pick one drop
                // from the table and roll it.
                if (chance.successful(random))
                    items.add(next.toItem(random));
                rollRare = false;
            } else if (rollDynamic && chance.getTier() < RARE.getTier()) {

                // 50% Chance to roll an item from the dynamic table, pick one
                // drop from the table and roll it.
                if (amount++ == GameConstants.DROP_THRESHOLD)
                    rollDynamic = false;
                if (next.getChance().successful(random))
                    items.add(next.toItem(random));
            }

            if (!$it.hasNext() && rollCommon) {

                // n (n = 12.5% chance, 25% if wearing Ring of Wealth)
                // Chance to roll an item from the common table, pick one drop
                // from the table and roll it.
                next = random.random(NpcDropManager.COMMON.get(cache));
                if (next.getChance().successful(random))
                    items.add(next.toItem(random));
                rollCommon = false;
            }
        }
        return items;
    }
}
