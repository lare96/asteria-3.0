package com.asteria.network.packet.impl;

import com.asteria.game.character.Flag;
import com.asteria.game.character.player.Player;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when the player speaks.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ChatPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.isDisabled())
            return;

        int effects = buf.get(false, com.asteria.network.ValueType.S);
        int color = buf.get(false, com.asteria.network.ValueType.S);
        int chatLength = (size - 2);
        byte[] text = buf.getBytesReverse(chatLength, com.asteria.network.ValueType.A);
        if (effects < 0 || color < 0 || chatLength < 0)
            return;
        player.setChatEffects(effects);
        player.setChatColor(color);
        player.setChatText(text);
        player.getFlags().set(Flag.CHAT);
    }
}
