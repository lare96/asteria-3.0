package com.asteria.net.message.impl;

import com.asteria.Server;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.object.ObjectNodeManager;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when a player sends the load map region
 * message.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class UpdateRegionMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isUpdateRegion()) {
            ObjectNodeManager.updateRegion(player);
            ItemNodeManager.updateRegion(player);
            player.sendInterfaces();
            player.getTolerance().reset();
            player.setUpdateRegion(false);

            if (Server.DEBUG)
                player.getMessages().sendMessage("DEBUG[region= " + player.getPosition().getRegion() + "]");
        }
    }
}
