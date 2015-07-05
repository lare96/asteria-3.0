package com.asteria.game.character.npc.drop;

import com.asteria.game.item.Item;
import com.asteria.utility.Chance;
import com.asteria.utility.RandomGen;

/**
 * The container class that represents one {@code NpcDrop} within a table.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDrop {

    /**
     * The identification of this {@code NpcDrop}.
     */
    private final int id;

    /**
     * The minimum amount that will be dropped.
     */
    private final int minimum;

    /**
     * The maximum amount that will be dropped.
     */
    private final int maximum;

    /**
     * The chance of this item being dropped.
     */
    private final Chance chance;

    /**
     * Creates a new {@link NpcDrop}.
     *
     * @param id
     *            the identification of this {@code NpcDrop}.
     * @param minimum
     *            the minimum amount that will be dropped.
     * @param maximum
     *            the maximum amount that will be dropped.
     * @param chance
     *            the chance of this item being dropped.
     */
    public NpcDrop(int id, int minimum, int maximum, Chance chance) {
        this.id = id;
        this.minimum = minimum;
        this.maximum = maximum;
        this.chance = chance;
    }

    /**
     * Converts this {@code NpcDrop} into an {@link Item} Object.
     *
     * @param random
     *            the random number generator to use.
     * @return the converted drop.
     */
    public Item toItem(RandomGen random) {
        return new Item(id, random.inclusive(minimum, maximum));
    }

    /**
     * Gets the identification of this {@code NpcDrop}.
     *
     * @return the identification.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the minimum amount that will be dropped.
     *
     * @return the minimum amount.
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * Gets the maximum amount that will be dropped.
     *
     * @return the maximum amount.
     */
    public int getMaximum() {
        return maximum;
    }

    /**
     * Gets the chance of this item being dropped.
     *
     * @return the drop chance.
     */
    public Chance getChance() {
        return chance;
    }
}