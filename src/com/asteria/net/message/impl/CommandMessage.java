package com.asteria.net.message.impl;

import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.game.plugin.context.CommandPlugin;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message that is sent from the client when the player chats anything
 * beginning with '::'.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CommandMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        String[] text = payload.getString().toLowerCase().split(" ");
        World.getPlugins().execute(player, CommandPlugin.class, new CommandPlugin(text));
    }
}
