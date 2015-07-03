package com.asteria.net.message;

import com.asteria.game.character.player.Player;

/**
 * The listener for {@link InputMessage}s that will handle logic for incoming
 * messages based on {@code opcode}, {@code size}, and {@code payload}.
 * 
 * @author lare96 <http://github.org/lare96>
 */
@FunctionalInterface
public interface InputMessageListener {

    /**
     * Handles the message designated to {@code opcode}.
     * 
     * @param player
     *            the player this message is being handled for.
     * @param opcode
     *            the opcode of this message.
     * @param size
     *            the size of this message.
     * @param payload
     *            the data contained within this message.
     */
    public abstract void handleMessage(Player player, int opcode, int size, MessageBuilder payload);
}
