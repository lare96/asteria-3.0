package com.asteria.net.message.impl;

import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.plugin.context.ItemOnItemPlugin;
import com.asteria.net.ValueType;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when a player uses an item on another item.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnItemMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;
        int secondSlot = payload.getShort();
        int firstSlot = payload.getShort(ValueType.A);
        payload.getShort();
        payload.getShort();
        Item itemUsed = player.getInventory().get(firstSlot);
        Item itemOn = player.getInventory().get(secondSlot);

        if (secondSlot < 0 || firstSlot < 0 || itemUsed == null || itemOn == null)
            return;

        World.getPlugins().execute(player, ItemOnItemPlugin.class, new ItemOnItemPlugin(itemUsed, itemOn));
    }
}
