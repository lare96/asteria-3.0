package com.asteria.net.message.impl;

import com.asteria.game.character.player.Player;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when an {@link Player} enters an idle state.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class IdleStateMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {

    }
}
