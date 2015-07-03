package com.asteria.net.message.impl;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemDefinition;
import com.asteria.game.item.ItemNode;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.location.Position;
import com.asteria.net.ValueType;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when the player drops an item.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class DropItemMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        int id = payload.getShort(false, ValueType.A);
        payload.get(false);
        payload.get(false);
        int slot = payload.getShort(false, ValueType.A);
        if (slot < 0 || id < 0)
            return;
        Item item = player.getInventory().get(slot);
        if (item == null || item.getId() != id)
            return;
        player.setSkillAction(false);
        int amount = ItemDefinition.DEFINITIONS[id].isStackable() ? item.getAmount() : 1;
        player.getInventory().remove(new Item(id, amount), slot);
        Position p = new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
        ItemNodeManager.register(new ItemNode(new Item(id, amount), p, player));
    }
}
