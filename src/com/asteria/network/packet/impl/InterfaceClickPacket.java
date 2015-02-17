package com.asteria.network.packet.impl;

import com.asteria.game.character.player.Player;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when a player clicks certain options on an
 * interface.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class InterfaceClickPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.getViewingOrb() != null)
            return;

        if (player.getTradeSession().inTradeSession())
            player.getTradeSession().reset(true);
        player.getEncoder().sendCloseWindows();
    }
}
