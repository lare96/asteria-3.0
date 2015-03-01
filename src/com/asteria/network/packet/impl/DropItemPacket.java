package com.asteria.network.packet.impl;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemDefinition;
import com.asteria.game.item.ItemNode;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.location.Position;
import com.asteria.network.DataBuffer;
import com.asteria.network.ValueType;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when the player drops an item.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class DropItemPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.getViewingOrb() != null)
            return;

        int id = buf.getShort(false, ValueType.A);
        buf.get(false);
        buf.get(false);
        int slot = buf.getShort(false, ValueType.A);
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
