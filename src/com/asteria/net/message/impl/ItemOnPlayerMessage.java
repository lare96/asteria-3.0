package com.asteria.net.message.impl;

import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.plugin.context.ItemOnPlayerPlugin;
import com.asteria.net.ByteOrder;
import com.asteria.net.ValueType;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when a player uses an item on another player.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnPlayerMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        int container = payload.getShort(ValueType.A, ByteOrder.BIG);
        int index = payload.getShort();
        int itemUsed = payload.getShort();
        int itemSlot = payload.getShort(false, ValueType.A, ByteOrder.LITTLE);
        Item item = player.getInventory().get(itemSlot);
        Player usedOn = World.getPlayers().get(index);

        if (container < 0 || item == null || usedOn == null || itemUsed < 0)
            return;
        if (item.getId() != itemUsed)
            return;

        player.getMovementListener().append(() -> {
            if (player.getPosition().withinDistance(usedOn.getPosition(), 1)) {
                World.getPlugins().execute(player, ItemOnPlayerPlugin.class, new ItemOnPlayerPlugin(player, item));
            }
        });
    }
}
