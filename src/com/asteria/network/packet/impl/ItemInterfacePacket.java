package com.asteria.network.packet.impl;

import java.util.Arrays;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemDefinition;
import com.asteria.game.shop.Shop;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when a player operates an item on an
 * interface.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class ItemInterfacePacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        switch (opcode) {
        case 145:
            firstSlot(player, buf);
            break;
        case 117:
            secondSlot(player, buf);
            break;
        case 43:
            thirdSlot(player, buf);
            break;
        case 129:
            fourthSlot(player, buf);
            break;
        case 41:
            equipItem(player, buf);
            break;
        case 214:
            swapSlots(player, buf);
            break;
        }
    }

    /**
     * Handles the first item slot click on an interface.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer for reading the sent data.
     */
    private void firstSlot(Player player, DataBuffer buf) {
        int interfaceId = buf.getShort(com.asteria.network.ValueType.A);
        int slot = buf.getShort(com.asteria.network.ValueType.A);
        int itemId = buf.getShort(com.asteria.network.ValueType.A);
        Arrays.fill(player.getSkillEvent(), false);

        if (interfaceId < 0 || slot < 0 || itemId < 0)
            return;

        switch (interfaceId) {
        case 1688:
            Arrays.fill(player.getSkillEvent(), false);
            player.getEquipment().unequipItem(slot, true);
            player.getCombatBuilder().cooldown(true);
            break;
        case 5064:
            player.getBank().depositFromInventory(slot, 1);
            break;
        case 5382:
            player.getBank().withdraw(slot, 1, true);
            break;
        case 3900:
            Shop.SHOPS.get(player.getOpenShop()).sendPurchasePrice(player, new Item(itemId));
            break;
        case 3823:
            Shop.SHOPS.get(player.getOpenShop()).sendSellingPrice(player, new Item(itemId));
            break;
        case 3322:
            player.getTradeSession().add(new Item(itemId, 1), slot);
            break;
        case 3415:
            player.getTradeSession().remove(new Item(itemId, 1));
            break;
        }
    }

    /**
     * Handles the second item slot click on an interface.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer for reading the sent data.
     */
    private void secondSlot(Player player, DataBuffer buf) {
        int interfaceId = buf.getShort(true, com.asteria.network.ValueType.A, com.asteria.network.ByteOrder.LITTLE);
        int itemId = buf.getShort(true, com.asteria.network.ValueType.A, com.asteria.network.ByteOrder.LITTLE);
        int slot = buf.getShort(true, com.asteria.network.ByteOrder.LITTLE);
        if (interfaceId < 0 || slot < 0 || itemId < 0)
            return;
        switch (interfaceId) {

        case 5064:
            player.getBank().depositFromInventory(slot, 5);
            break;
        case 5382:
            player.getBank().withdraw(slot, 5, true);
            break;
        case 3900:
            Shop.SHOPS.get(player.getOpenShop()).purchase(player, new Item(itemId, 1));
            break;
        case 3823:
            Shop.SHOPS.get(player.getOpenShop()).sell(player, new Item(itemId, 1), slot);
            break;
        case 3322:
            player.getTradeSession().add(new Item(itemId, 5), slot);
            break;
        case 3415:
            player.getTradeSession().remove(new Item(itemId, 5));
            break;
        }
    }

    /**
     * Handles the third item slot click on an interface.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer for reading the sent data.
     */
    private void thirdSlot(Player player, DataBuffer buf) {
        int interfaceId = buf.getShort(com.asteria.network.ByteOrder.LITTLE);
        int itemId = buf.getShort(com.asteria.network.ValueType.A);
        int slot = buf.getShort(com.asteria.network.ValueType.A);
        if (interfaceId < 0 || slot < 0 || itemId < 0)
            return;
        switch (interfaceId) {

        case 5064:
            player.getBank().depositFromInventory(slot, 10);
            break;

        case 5382:
            player.getBank().withdraw(slot, 10, true);
            break;
        case 3900:
            Shop.SHOPS.get(player.getOpenShop()).purchase(player, new Item(itemId, 5));
            break;
        case 3823:
            Shop.SHOPS.get(player.getOpenShop()).sell(player, new Item(itemId, 5), slot);
            break;
        case 3322:
            player.getTradeSession().add(new Item(itemId, 10), slot);
            break;
        case 3415:
            player.getTradeSession().remove(new Item(itemId, 10));
            break;

        }
    }

    /**
     * Handles the fourth item slot click on an interface.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer for reading the sent data.
     */
    private void fourthSlot(Player player, DataBuffer buf) {
        int slot = buf.getShort(com.asteria.network.ValueType.A);
        int interfaceId = buf.getShort();
        int itemId = buf.getShort(com.asteria.network.ValueType.A);
        if (interfaceId < 0 || slot < 0 || itemId < 0)
            return;

        switch (interfaceId) {

        case 5064:
            player.getBank().depositFromInventory(slot, player.getInventory().amount(player.getInventory().getId(slot)));
            break;

        case 5382:
            int amount = 0;
            if (player.isWithdrawAsNote()) {
                amount = player.getBank().amount(itemId);
            } else {
                Item itemWithdrew = new Item(itemId, 1);
                amount = ItemDefinition.DEFINITIONS[itemWithdrew.getId()].isStackable() ? player.getBank().amount(itemId)
                    : 28;
            }

            player.getBank().withdraw(slot, amount, true);
            break;
        case 3900:
            Shop.SHOPS.get(player.getOpenShop()).purchase(player, new Item(itemId, 10));
            break;

        case 3823:
            Shop.SHOPS.get(player.getOpenShop()).sell(player, new Item(itemId, 10), slot);
            break;
        case 3322:
            player.getTradeSession().add(new Item(itemId, player.getInventory().amount(itemId)), slot);
            break;
        case 3415:
            player.getTradeSession().remove(new Item(itemId, player.getTradeSession().getContainer().amount(itemId)));
            break;
        }
    }

    /**
     * Handles the equipping of an item for {@code player}.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer for reading the sent data.
     */
    private void equipItem(Player player, DataBuffer buf) {
        int itemId = buf.getShort(false);
        int slot = buf.getShort(false, com.asteria.network.ValueType.A);
        int interfaceId = buf.getShort(false, com.asteria.network.ValueType.A);
        if (interfaceId < 0 || slot < 0 || itemId < 0)
            return;
        switch (itemId) {

        }
        player.getEquipment().equipItem(slot);
        Arrays.fill(player.getSkillEvent(), false);
        player.getCombatBuilder().cooldown(true);
    }

    /**
     * Handles the swapping of items on an interface for {@code player}.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer for reading the sent data.
     */
    private void swapSlots(Player player, DataBuffer buf) {
        int interfaceId = buf.getShort(com.asteria.network.ValueType.A, com.asteria.network.ByteOrder.LITTLE);
        buf.get(com.asteria.network.ValueType.C);
        int fromSlot = buf.getShort(com.asteria.network.ValueType.A, com.asteria.network.ByteOrder.LITTLE);
        int toSlot = buf.getShort(com.asteria.network.ByteOrder.LITTLE);
        if (interfaceId < 0 || fromSlot < 0 || toSlot < 0)
            return;

        switch (interfaceId) {
        case 3214:
            player.getInventory().swap(fromSlot, toSlot);
            player.getInventory().refresh();
            break;
        case 5382:
            if (player.isInsertItem()) {
                player.getBank().swap(fromSlot, toSlot);
            } else {
                player.getBank().transfer(fromSlot, toSlot);
            }
            player.getBank().refresh();
            break;
        }
    }
}
