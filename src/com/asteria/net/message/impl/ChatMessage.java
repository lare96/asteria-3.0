package com.asteria.net.message.impl;

import com.asteria.game.character.Flag;
import com.asteria.game.character.player.Player;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when the player speaks.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ChatMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        int effects = payload.get(false, com.asteria.net.ValueType.S);
        int color = payload.get(false, com.asteria.net.ValueType.S);
        int chatLength = (size - 2);
        byte[] text = payload.getBytesReverse(chatLength, com.asteria.net.ValueType.A);
        if (effects < 0 || color < 0 || chatLength < 0)
            return;
        player.setChatEffects(effects);
        player.setChatColor(color);
        player.setChatText(text);
        player.getFlags().set(Flag.CHAT);
    }
}
