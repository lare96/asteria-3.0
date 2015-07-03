package com.asteria.net.message.impl;

import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.game.plugin.context.ButtonClickPlugin;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;
import com.asteria.utility.BufferUtils;

/**
 * The message sent from the client when the player clicks some sort of button or
 * module.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ClickButtonMessage implements InputMessageListener {

    // TODO: Convert all buttons to the proper identifications.

    /**
     * The flag that determines if this message should be read properly.
     */
    private static final boolean PROPER_READ = false;

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        int button = PROPER_READ ? payload.getShort() : BufferUtils.hexToInt(payload.getBytes(2));
        World.getPlugins().execute(player, ButtonClickPlugin.class, new ButtonClickPlugin(button));
    }
}
