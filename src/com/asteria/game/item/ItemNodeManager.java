package com.asteria.game.item;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.game.location.Position;
import com.asteria.task.Task;

/**
 * The node manager that manages all registered item nodes.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemNodeManager extends Task {

    /**
     * The amount of ticks to execute the sequence listener.
     */
    private static final int SEQUENCE_TICKS = 100;

    /**
     * The linked collection of registered items.
     */
    public static final List<ItemNode> ITEMS = new LinkedList<>();

    /**
     * Creates a new {@link ItemNodeManager}.
     */
    public ItemNodeManager() {
        super(10, true);
    }

    @Override
    public void execute() {
        Iterator<ItemNode> it = ITEMS.iterator();
        while (it.hasNext()) {
            ItemNode item = it.next();
            if (item.getCounter().incrementAndGet(10) >= ItemNodeManager.SEQUENCE_TICKS) {
                item.onSequence();
                item.getCounter().set(0);
            }
            if (!item.isRegistered()) {
                item.dispose();
                it.remove();
            }
        }
    }

    @Override
    public void onCancel() {
        World.submit(new ItemNodeManager());
    }

    @Override
    public void onThrowable(Throwable t) {
        ITEMS.stream().forEach(i -> i.dispose());
        ITEMS.clear();
    }

    /**
     * The method that attempts to register {@code item}.
     *
     * @param item
     *            the item to attempt to register.
     * @param stack
     *            if the item should stack upon registration.
     * @return {@code true} if the item was registered, {@code false} otherwise.
     */
    public static boolean register(ItemNode item, boolean stack) {
        if (item.isRegistered())
            return false;
        if (stack) {
            Iterator<ItemNode> it = ITEMS.iterator();
            while (it.hasNext()) {
                ItemNode next = it.next();
                if (next.getPlayer() == null || next.getPosition() == null || next.getItem() == null)
                    continue;
                if (next.getItem().getId() == item.getItem().getId() && next.getPosition().equals(item.getPosition()) && next.getPlayer()
                    .equals(item.getPlayer())) {
                    next.getItem().incrementAmountBy(item.getItem().getAmount());
                    if (next.getItem().getAmount() <= 5) {
                        next.dispose();
                        next.create();
                    }
                    return true;
                }
            }
            ITEMS.add(item);
            item.create();
            item.setRegistered(true);
            return true;
        }
        if (item.getItem().getDefinition().isStackable()) {
            ITEMS.add(item);
            item.create();
            item.setRegistered(true);
            return true;
        }
        int amount = item.getItem().getAmount();
        item.getItem().setAmount(1);
        for (int i = 0; i < amount; i++) {
            ITEMS.add(item);
            item.create();
            item.setRegistered(true);
        }
        return true;
    }

    /**
     * The method that attempts to register {@code item} and does not stack by
     * default.
     *
     * @param item
     *            the item to attempt to register.
     * @return {@code true} if the item was registered, {@code false} otherwise.
     */
    public static boolean register(ItemNode item) {
        return register(item, false);
    }

    /**
     * The method that attempts to unregister {@code item}.
     *
     * @param item
     *            the item to attempt to unregister.
     * @return {@code true} if the item was unregistered, {@code false}
     *         otherwise.
     */
    public static boolean unregister(ItemNode item) {
        if (!item.isRegistered())
            return false;
        if (ITEMS.remove(item)) {
            item.dispose();
            item.setRegistered(true);
            return true;
        }
        return false;
    }

    /**
     * The method that retrieves the item with {@code id} on {@code position}.
     *
     * @param id
     *            the identifier to retrieve the item with.
     * @param position
     *            the position to retrieve the item on.
     * @return the item instance wrapped in an optional, or an empty optional if
     *         no item is found.
     */
    public static Optional<ItemNode> getItem(int id, Position position) {
        return ITEMS.stream().filter(
            i -> i.getState() != ItemState.HIDDEN && i.isRegistered() && i.getItem().getId() == id && i.getPosition().equals(position))
            .findFirst();
    }

    /**
     * The method that updates all items in the region for {@code player}.
     *
     * @param player
     *            the player to update items for.
     */
    public static void updateRegion(Player player) {
        for (ItemNode item : ITEMS) {
            if (item.getState() == ItemState.HIDDEN || !item.isRegistered())
                continue;
            player.getMessages().sendRemoveGroundItem(item);
            if (item.getPosition().withinDistance(player.getPosition(), 60)) {
                if (item.getPlayer() == null && item.getState() == ItemState.SEEN_BY_EVERYONE) {
                    player.getMessages().sendGroundItem(item);
                    continue;
                }
                if (item.getPlayer().equals(player) && item.getState() == ItemState.SEEN_BY_OWNER) {
                    player.getMessages().sendGroundItem(item);
                    continue;
                }
            }
        }
    }
}
