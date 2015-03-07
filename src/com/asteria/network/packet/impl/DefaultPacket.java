package com.asteria.network.packet.impl;

import com.asteria.game.character.player.Player;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;

/**
 * The decoder used to handle useless packets sent from the client.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class DefaultPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {

    }
}
