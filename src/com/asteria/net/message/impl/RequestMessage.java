package com.asteria.net.message.impl;

import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.net.ByteOrder;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when a player sends some sort of request to
 * another player.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class RequestMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        switch (opcode) {
        case 139:
            tradeRequest(player, payload);
            break;
        }
    }

    /**
     * Handles a trade request for {@code player}.
     *
     * @param player
     *            the player to handle this for.
     * @param payload
     *            the payloadfer for reading the sent data.
     */
    private void tradeRequest(Player player, MessageBuilder payload) {
        int index = payload.getShort(true, ByteOrder.LITTLE);
        Player other = World.getPlayers().get(index);
        if (other == null || !validate(player, other))
            return;
        player.setSkillAction(false);
        if (!MinigameHandler.execute(player, true, m -> m.canTrade(player, other)))
            return;
        player.getTradeSession().request(other);
    }

    /**
     * Determines if {@code player} can be a valid request to {@code other}.
     *
     * @param player
     *            the player making the request.
     * @param other
     *            the player being requested.
     * @return {@code true} if the player can make a request, {@code false}
     *         otherwise.
     */
    private boolean validate(Player player, Player other) {
        if (!other.getPosition().isViewableFrom(player.getPosition()) || other.equals(player))
            return false;
        return true;
    }
}
