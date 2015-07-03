package com.asteria.net.message.impl;

import com.asteria.game.character.player.Player;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when a player clicks certain options on an
 * interface.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class InterfaceClickMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        if (player.getTradeSession().inTradeSession())
            player.getTradeSession().reset(true);
        player.getMessages().sendCloseWindows();
    }
}
