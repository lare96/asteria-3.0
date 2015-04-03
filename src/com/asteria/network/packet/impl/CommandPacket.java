package com.asteria.network.packet.impl;

import com.asteria.game.character.player.Player;
import com.asteria.game.plugin.PluginHandler;
import com.asteria.game.plugin.context.CommandPlugin;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet that is sent from the client when the player chats anything
 * beginning with '::'.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CommandPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.isDisabled())
            return;

        String[] text = buf.getString().toLowerCase().split(" ");
        PluginHandler.execute(player, CommandPlugin.class, new CommandPlugin(text));
    }
}
