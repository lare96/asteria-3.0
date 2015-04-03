package com.asteria.game.item.container;

import com.asteria.game.character.Flag;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.content.Requirement;
import com.asteria.game.character.player.content.WeaponAnimation;
import com.asteria.game.character.player.content.WeaponInterface;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.item.Item;

/**
 * The container that manages the equipment for a player.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Equipment extends ItemContainer {

    /**
     * The head identification equipment slot.
     */
    public static final int HEAD_SLOT = 0;

    /**
     * The cape identification equipment slot.
     */
    public static final int CAPE_SLOT = 1;

    /**
     * The amulet identification equipment slot.
     */
    public static final int AMULET_SLOT = 2;

    /**
     * The weapon identification equipment slot.
     */
    public static final int WEAPON_SLOT = 3;

    /**
     * The chest identification equipment slot.
     */
    public static final int CHEST_SLOT = 4;

    /**
     * The shield identification equipment slot.
     */
    public static final int SHIELD_SLOT = 5;

    /**
     * The legs identification equipment slot.
     */
    public static final int LEGS_SLOT = 7;

    /**
     * The hands identification equipment slot.
     */
    public static final int HANDS_SLOT = 9;

    /**
     * The feet identification equipment slot.
     */
    public static final int FEET_SLOT = 10;

    /**
     * The ring identification equipment slot.
     */
    public static final int RING_SLOT = 12;

    /**
     * The arrows identification equipment slot.
     */
    public static final int ARROWS_SLOT = 13;

    /**
     * The player who's equipment is being managed.
     */
    private final Player player;

    /**
     * Creates a new {@link Equipment}.
     *
     * @param player
     *         the player who's equipment is being managed.
     */
    public Equipment(Player player) {
        super(14, ItemContainerPolicy.NORMAL);
        this.player = player;
    }

    /**
     * Refreshes the contents of this equipment container to the interface.
     */
    public void refresh() {
        refresh(player, 1688);
    }

    /**
     * Equips the item in {@code inventorySlot} to the equipment container.
     *
     * @param inventorySlot
     *         the slot to equip the item on.
     * @return {@code true} if the item was equipped, {@code false} otherwise.
     */
    public boolean equipItem(int inventorySlot) {
        Item item = player.getInventory().get(inventorySlot);
        if (!Item.valid(item))
            return false;
        if (!MinigameHandler.execute(player, true, m -> m.canEquip(player, item, item.getDefinition().getEquipmentSlot())))
            return false;
        if (!Requirement.canEquip(player, item))
            return false;
        if (item.getDefinition().isStackable()) {
            int designatedSlot = item.getDefinition().getEquipmentSlot();
            Item equipItem = get(designatedSlot);
            if (used(designatedSlot)) {
                if (item.getId() == equipItem.getId()) {
                    set(designatedSlot, new Item(item.getId(), item.getAmount() + equipItem.getAmount()));
                } else {
                    player.getInventory().set(inventorySlot, equipItem);
                    player.getInventory().refresh();
                    set(designatedSlot, item);
                }
            } else {
                set(designatedSlot, item);
            }
            player.getInventory().remove(item, inventorySlot);
        } else {
            int designatedSlot = item.getDefinition().getEquipmentSlot();
            if (designatedSlot == Equipment.WEAPON_SLOT && item.getDefinition().isTwoHanded() && used(Equipment.SHIELD_SLOT)) {
                if (!unequipItem(Equipment.SHIELD_SLOT, true))
                    return false;
            }
            if (designatedSlot == Equipment.SHIELD_SLOT && used(Equipment.WEAPON_SLOT)) {
                if (get(Equipment.WEAPON_SLOT).getDefinition().isTwoHanded()) {
                    if (!unequipItem(Equipment.WEAPON_SLOT, true))
                        return false;
                }
            }
            if (used(designatedSlot)) {
                Item equipItem = get(designatedSlot);
                if (!equipItem.getDefinition().isStackable()) {
                    player.getInventory().set(inventorySlot, equipItem);
                } else {
                    player.getInventory().add(equipItem);
                    player.getInventory().set(inventorySlot, null);
                }
                player.getInventory().refresh();
            } else {
                player.getInventory().remove(item, inventorySlot);
            }
            set(designatedSlot, new Item(item.getId(), item.getAmount()));
        }
        if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
            WeaponInterface.execute(player, item);
            WeaponAnimation.execute(player, item);
            player.setCastSpell(null);
            player.setAutocastSpell(null);
            player.setAutocast(false);
            player.getEncoder().sendByteState(108, 0);
            player.getEncoder().sendByteState(301, 0);
            player.setSpecialActivated(false);
        }
        player.sendBonus();
        refresh();
        player.getFlags().set(Flag.APPEARANCE);
        return true;
    }

    /**
     * Unequips the item in {@code equipmentSlot} from the equipment container.
     *
     * @param equipmentSlot
     *         the slot to unequip the item on.
     * @param addItem
     *         if the unequipped item should be added to the inventory.
     * @return {@code true} if the item was unequipped, {@code false} otherwise.
     */
    public boolean unequipItem(int equipmentSlot, boolean addItem) {
        if (free(equipmentSlot))
            return false;
        Item item = get(equipmentSlot);
        if (!MinigameHandler.execute(player, true, m -> m.canUnequip(player, item, item.getDefinition().getEquipmentSlot())))
            return false;
        if (!player.getInventory().spaceFor(item)) {
            player.getEncoder().sendMessage("You do not have enough space in " + "your inventory!");
            return false;
        }
        super.remove(item, equipmentSlot);
        if (addItem)
            player.getInventory().add(new Item(item.getId(), item.getAmount()));
        if (equipmentSlot == Equipment.WEAPON_SLOT) {
            WeaponInterface.execute(player, null);
            player.setCastSpell(null);
            player.setAutocastSpell(null);
            player.setAutocast(false);
            player.getEncoder().sendByteState(108, 0);
            player.setWeaponAnimation(null);
            player.getEncoder().sendByteState(301, 0);
            player.setSpecialActivated(false);
        }
        player.sendBonus();
        refresh();
        player.getInventory().refresh();
        player.getFlags().set(Flag.APPEARANCE);
        return true;
    }

    /**
     * Unequips {@code item} from the equipment container.
     *
     * @param item
     *         the item to unequip from this container.
     * @param addItem
     *         if the unequipped item should be added to the inventory.
     * @return {@code true} if the item was unequipped, {@code false} otherwise.
     */
    public boolean unequipItem(Item item, boolean addItem) {
        int slot = super.searchSlot(item.getId());
        if (slot == -1)
            return false;
        return unequipItem(slot, addItem);
    }

    /**
     * This method is not supported by this container implementation.
     *
     * @throws UnsupportedOperationException
     *         if this method is invoked by default, this method will always
     *         throw an exception.
     */
    @Override
    public boolean add(Item item, int slot) {
        throw new UnsupportedOperationException("This method is not supported" + " by this container implementation!");
    }

    /**
     * This method is not supported by this container implementation.
     *
     * @throws UnsupportedOperationException
     *         if this method is invoked by default, this method will always
     *         throw an exception.
     */
    @Override
    public boolean remove(Item item, int slot) {
        throw new UnsupportedOperationException("This method is not supported by this container implementation!");
    }
}