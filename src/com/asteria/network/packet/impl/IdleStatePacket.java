package com.asteria.network.packet.impl;

import com.asteria.game.character.player.Player;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when an {@link Player} enters an idle state.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class IdleStatePacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {

    }
}
