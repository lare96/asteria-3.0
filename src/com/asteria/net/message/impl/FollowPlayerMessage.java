package com.asteria.net.message.impl;

import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.net.ByteOrder;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when a player tries to follow another player.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class FollowPlayerMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        int index = payload.getShort(false, ByteOrder.LITTLE);
        Player follow = World.getPlayers().get(index);

        if (follow == null || !follow.getPosition().isViewableFrom(player.getPosition()) || follow.equals(player))
            return;
        player.setSkillAction(false);
        player.getMovementQueue().follow(follow);
    }
}
