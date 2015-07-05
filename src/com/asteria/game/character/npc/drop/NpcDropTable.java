package com.asteria.game.character.npc.drop;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.asteria.game.GameConstants;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.item.container.Equipment;
import com.asteria.utility.Chance;
import com.asteria.utility.CollectionUtils;
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
        List<NpcDrop> rareTable = new LinkedList<>();
        NpcDropCache cache = random.random(common);
        LinkedList<NpcDrop> copyItems = CollectionUtils.newLinkedList(unique);
        Collections.shuffle(copyItems);

        // Iterate through the unique table, drop ALWAYS items and add RARE+
        // items to the rare table.
        Iterator<NpcDrop> $it = copyItems.iterator();
        while ($it.hasNext()) {
            NpcDrop next = $it.next();
            if (next.getChance() == Chance.ALWAYS) {
                items.add(next.toItem(random));
                $it.remove();
            } else if (next.getChance().getTier() >= Chance.RARE.getTier()) {
                rareTable.add(next);
                $it.remove();
            }
        }

        // 50% Chance to drop an item from the unique table, pick DROP_THRESHOLD
        // amount of dynamic drops from the table and roll them.
        if (random.get().nextBoolean()) {
            for (int amount = 0; amount < GameConstants.DROP_THRESHOLD; amount++) {
                if (copyItems.size() == 0)
                    break;
                NpcDrop next = copyItems.remove();
                if (next.getChance().successful(random))
                    items.add(next.toItem(random));
            }
        }

        // n (n = specific table chance or 100% if wearing Ring of Wealth)
        // Chance to drop an item from the common table, pick one drop from
        // the table and roll it.
        boolean row = player != null && player.getEquipment().getId(Equipment.RING_SLOT) == 2572;
        int denominator = row ? 4 : 8;
        if (random.get().nextInt(denominator) == 0) {
            NpcDrop next = random.random(NpcDropManager.COMMON.get(cache));
            if (next.getChance().successful(random)) {
                items.add(next.toItem(random));
                if (row)
                    player.getMessages().sendMessage("Your Ring of Wealth illuminates...");
            }
        }

        // 20% Chance to drop an item from the rare table, pick one drop from
        // the table and roll it.
        if (rareTable.size() == 0)
            return items;
        if (random.get().nextInt(5) == 0) {
            NpcDrop next = random.random(rareTable);
            if (next.getChance().successful(random))
                items.add(next.toItem(random));
        }
        return items;
    }
}
