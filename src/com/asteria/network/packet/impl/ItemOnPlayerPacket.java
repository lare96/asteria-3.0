package com.asteria.network.packet.impl;

import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.plugin.PluginHandler;
import com.asteria.game.plugin.context.ItemOnPlayerPlugin;
import com.asteria.network.ByteOrder;
import com.asteria.network.DataBuffer;
import com.asteria.network.ValueType;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when a player uses an item on another player.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class ItemOnPlayerPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.getViewingOrb() != null)
            return;

        int container = buf.getShort(ValueType.A, ByteOrder.BIG);
        int index = buf.getShort();
        int itemUsed = buf.getShort();
        int itemSlot = buf.getShort(false, ValueType.A, ByteOrder.LITTLE);
        Item item = player.getInventory().get(itemSlot);
        Player usedOn = World.getPlayers().get(index);

        if (container < 0 || item == null || usedOn == null || itemUsed < 0)
            return;

        player.getMovementListener().append(() -> {
            if (player.getPosition().withinDistance(usedOn.getPosition(), 1)) {
                PluginHandler.execute(player, ItemOnPlayerPlugin.class, new ItemOnPlayerPlugin(player, item));
            }
        });
    }
}
