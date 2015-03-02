package com.asteria.network.packet.impl;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.object.ObjectNodeManager;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;
import com.asteria.utility.Settings;

/**
 * The packet sent from the client when a player sends the load map region
 * packet.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class UpdateRegionPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.isUpdateRegion()) {
            ObjectNodeManager.updateRegion(player);
            ItemNodeManager.updateRegion(player);
            player.sendInterfaces();
            player.getTolerance().reset();
            player.setUpdateRegion(false);

            if (Settings.DEBUG)
                player.getEncoder().sendMessage("DEBUG[region= " + player.getPosition().getRegion() + "]");
        }
    }
}
