package com.asteria.network.packet.impl;

import com.asteria.game.character.player.Player;
import com.asteria.game.plugin.PluginHandler;
import com.asteria.game.plugin.context.ButtonClickPlugin;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;
import com.asteria.utility.BufferUtils;

/**
 * The packet sent from the client when the player clicks some sort of button
 * or
 * module.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ClickButtonPacket extends PacketDecoder {

    // TODO: Convert all buttons to ones that have been read properly.

    /**
     * The flag that determines if this packet should be read properly.
     */
    private static final boolean PROPER_READ = false;

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        int button = PROPER_READ ? buf.getShort() : BufferUtils.hexToInt(buf.getBytes(2));
        PluginHandler.execute(player, ButtonClickPlugin.class, new ButtonClickPlugin(button));
    }
}
