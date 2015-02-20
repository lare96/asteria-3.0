package com.asteria.network.packet.impl;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.plugin.PluginHandler;
import com.asteria.game.plugin.context.ItemOnItemPlugin;
import com.asteria.network.DataBuffer;
import com.asteria.network.ValueType;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when a player uses an item on another item.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class ItemOnItemPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.getViewingOrb() != null)
            return;
        int secondSlot = buf.getShort();
        int firstSlot = buf.getShort(ValueType.A);
        buf.getShort();
        buf.getShort();
        Item itemUsed = player.getInventory().get(firstSlot);
        Item itemOn = player.getInventory().get(secondSlot);

        if (secondSlot < 0 || firstSlot < 0 || itemUsed == null || itemOn == null)
            return;

        PluginHandler.execute(player, ItemOnItemPlugin.class, new ItemOnItemPlugin(itemUsed, itemOn));
    }
}
