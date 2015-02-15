package com.asteria.game.item.container;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;

/**
 * The container that manages the bank for a player.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class Bank extends ItemContainer {

    /**
     * The player who's bank is being managed.
     */
    private final Player player;

    /**
     * Creates a new {@link Bank}.
     * 
     * @param player
     *            the player who's bank is being managed.
     */
    public Bank(Player player) {
        super(250, ItemContainerPolicy.STACK_ALWAYS);
        this.player = player;
    }

    /**
     * Opens and refreshes the bank for {@code player}.
     */
    public void open() {
        player.setWithdrawAsNote(false);
        player.getEncoder().sendByteState(115, 0);
        player.getEncoder().sendInventoryInterface(5292, 5063);
        refresh();
        player.getEncoder().sendItemsOnInterface(5064, player.getInventory().container());
    }

    /**
     * Refreshes the contents of this bank container to the interface.
     */
    public void refresh() {
        refresh(player, 5382);
    }

    /**
     * Deposits an item to this bank that currently exists in the player's
     * inventory. This is used for when a player is manually depositing an item
     * using the banking interface.
     * 
     * @param inventorySlot
     *            the slot from the player's inventory.
     * @param amount
     *            the amount of the item being deposited.
     * @return {@code true} if the item was deposited, {@code false} otherwise.
     */
    public boolean depositFromInventory(int inventorySlot, int amount) {
        Item item = new Item(player.getInventory().get(inventorySlot).getId(), amount);
        int count = player.getInventory().amount(item.getId());

        if (item.getAmount() > count) {
            item.setAmount(count);
        }

        if (deposit(item.copy())) {
            player.getInventory().remove(item, inventorySlot);
            refresh();
            player.getEncoder().sendItemsOnInterface(5064, player.getInventory().container());
            return true;
        }
        return false;
    }

    /**
     * Deposits {@code item} directly into this bank.
     * 
     * @param item
     *            the item to deposit into this bank.
     * @return {@code true} if the item was deposited, {@code false} otherwise.
     */
    public boolean deposit(Item item) {
        item.setId(item.getDefinition().isNoted() ? item.getId() - 1 : item.getId());
        int slot = freeSlot();
        boolean contains = contains(item.getId());
        if (slot == -1 && !contains) {
            player.getEncoder().sendMessage("You don't have enough space to deposit this item!");
            return false;
        }
        int itemId = item.getDefinition().isNoted() ? item.getId() - 1 : item.getId();
        if (!contains)
            return super.add(new Item(itemId, item.getAmount()), slot);
        get(searchSlot(itemId)).incrementAmountBy(item.getAmount());
        return true;
    }

    /**
     * Withdraws an item from this bank from the {@code bankSlot} slot. This is
     * used for when a player is manually withdrawing an item using the banking
     * interface.
     * 
     * @param bankSlot
     *            the slot from the player's bank.
     * @param amount
     *            the amount of the item being withdrawn.
     * @param addItem
     *            if the item should be added back into the player's inventory
     *            after being withdrawn.
     * @return {@code true} if the item was withdrawn, {@code false} otherwise.
     */
    public boolean withdraw(int bankSlot, int amount, boolean addItem) {

        Item item = new Item(get(bankSlot).getId(), amount);
        boolean withdrawItemNoted = item.getDefinition().isNoteable();
        int withdrawAmount = amount(item.getId());

        if (free(bankSlot)) {
            return false;
        }

        if (item.getAmount() > withdrawAmount) {
            item.setAmount(withdrawAmount);
        }

        if (item.getAmount() > player.getInventory().remaining() && !item.getDefinition().isStackable() && !player
            .isWithdrawAsNote()) {
            item.setAmount(player.getInventory().remaining());
        }

        if (!item.getDefinition().isStackable() && !item.getDefinition().isNoted() && !player.isWithdrawAsNote()) {
            if (player.getInventory().remaining() < item.getAmount()) {
                player.getEncoder().sendMessage("You do not have enough space in your inventory!");
                return false;
            }
        } else {
            if (player.getInventory().remaining() < 1 && !player.getInventory().contains(
                !player.isWithdrawAsNote() ? item.getId() : item.getId() + 1)) {
                player.getEncoder().sendMessage("You do not have enough space in your inventory!");
                return false;
            }
        }

        if (player.isWithdrawAsNote() && !withdrawItemNoted) {
            player.getEncoder().sendMessage("This item can't be withdrawn as a note.");
            player.setWithdrawAsNote(false);
            player.getEncoder().sendByteState(115, 0);

            if (!item.getDefinition().isStackable()) {
                item.setAmount(player.getInventory().remaining());
            }
        }
        super.remove(item, bankSlot);
        if (player.isWithdrawAsNote()) {
            item.setId(item.getId() + 1);
        }
        if (addItem)
            player.getInventory().add(item);
        shift();
        refresh();
        player.getEncoder().sendItemsOnInterface(5064, player.getInventory().container());
        return true;
    }

    /**
     * Withdraws {@code item} from this bank.
     * 
     * @param item
     *            the item to withdraw.
     * @param addItem
     *            if the item should be added back into the player's inventory
     *            after being withdrawn.
     * @return {@code true} if the item was withdrawn, {@code false} otherwise.
     */
    public boolean withdraw(Item item, boolean addItem) {
        return withdraw(searchSlot(item.getId()), item.getAmount(), addItem);
    }

    /**
     * This method is not supported by this container implementation.
     * 
     * @throws UnsupportedOperationException
     *             if this method is invoked by default, this method will always
     *             throw an exception.
     */
    @Override
    public final boolean add(Item item, int slot) {
        throw new UnsupportedOperationException("This method is not supported by this container implementation!");
    }

    /**
     * This method is not supported by this container implementation.
     * 
     * @throws UnsupportedOperationException
     *             if this method is invoked by default, this method will always
     *             throw an exception.
     */
    @Override
    public final boolean remove(Item item, int slot) {
        throw new UnsupportedOperationException("This method is not supported by this container implementation!");
    }
}