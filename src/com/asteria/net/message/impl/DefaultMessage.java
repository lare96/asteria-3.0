package com.asteria.net.message.impl;

import com.asteria.game.character.player.Player;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The decoder used to handle useless messages sent from the client.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class DefaultMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {

    }
}
