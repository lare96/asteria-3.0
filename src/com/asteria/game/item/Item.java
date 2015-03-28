package com.asteria.game.item;

/**
 * The container class that represents an item that can be interacted with.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Item {

    /**
     * The identification of this item.
     */
    private int id;

    /**
     * The quantity of this item.
     */
    private int amount;

    /**
     * Creates a new {@link Item}.
     *
     * @param id
     *         the identification of this item.
     * @param amount
     *         the quantity of this item.
     */
    public Item(int id, int amount) {
        if (amount < 0)
            amount = 0;
        this.id = id;
        this.amount = amount;
    }

    /**
     * Creates a new {@link Item} with an quantity of {@code 1}.
     *
     * @param id
     *         the identification of this item.
     */
    public Item(int id) {
        this(id, 1);
    }

    @Override
    public String toString() {
        return "ITEM[id= " + id + ", amount= " + amount + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + amount;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Item))
            return false;
        Item other = (Item) obj;
        if (amount != other.amount)
            return false;
        if (id != other.id)
            return false;
        return true;
    }

    /**
     * Determines if {@code item} is valid. In other words, determines if
     * {@code item} is not {@code null} and the {@link Item#id} and
     * {@link Item#amount} are above {@code 0}.
     *
     * @param item
     *         the item to determine if valid.
     * @return {@code true} if the item is valid, {@code false} otherwise.
     */
    public static boolean valid(Item item) {
        return item != null && item.id > 0 && item.amount > 0;
    }

    /**
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     *
     * @return the copy of this instance that does not hold any references.
     */
    public Item copy() {
        return new Item(id, amount);
    }

    /**
     * Increments the amount by {@code 1}.
     */
    public final void incrementAmount() {
        incrementAmountBy(1);
    }

    /**
     * Decrements the amount by {@code 1}.
     */
    public final void decrementAmount() {
        decrementAmountBy(1);
    }

    /**
     * Increments the amount by {@code amount}.
     *
     * @param amount
     *         the amount to increment by.
     */
    public final void incrementAmountBy(int amount) {
        this.amount += amount;
    }

    /**
     * Decrements the amount by {@code amount}
     *
     * @param amount
     *         the amount to decrement by.
     */
    public final void decrementAmountBy(int amount) {
        if ((this.amount - amount) < 1) {
            this.amount = 0;
        }
        else {
            this.amount -= amount;
        }
    }

    /**
     * Gets the item definition for the item identifier.
     *
     * @return the item definition.
     */
    public final ItemDefinition getDefinition() {
        return ItemDefinition.DEFINITIONS[id];
    }

    /**
     * Gets the identification of this item.
     *
     * @return the identification.
     */
    public final int getId() {
        return id;
    }

    /**
     * Sets the identification of this item.
     *
     * @param id
     *         the new identification of this item.
     */
    public final void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the quantity of this item.
     *
     * @return the quantity.
     */
    public final int getAmount() {
        return amount;
    }

    /**
     * Sets the quantity of this item.
     *
     * @param amount
     *         the new quantity of this item.
     */
    public final void setAmount(int amount) {
        if (amount < 0)
            amount = 0;
        this.amount = amount;
    }
}