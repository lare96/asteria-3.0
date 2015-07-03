package com.asteria.net.message.impl;

import java.util.Optional;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemNode;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.location.Position;
import com.asteria.net.ByteOrder;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when a player attempts to pick up an item.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class PickupItemMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        int itemY = payload.getShort(ByteOrder.LITTLE);
        int itemId = payload.getShort(false);
        int itemX = payload.getShort(ByteOrder.LITTLE);
        if (itemY < 0 || itemId < 0 || itemX < 0)
            return;
        player.setSkillAction(false);
        player.getMovementListener().append(() -> {
            if (player.getPosition().equals(new Position(itemX, itemY, player.getPosition().getZ()))) {
                Position position = new Position(itemX, itemY, player.getPosition().getZ());
                Optional<ItemNode> item = ItemNodeManager.getItem(itemId, position);

                if (item.isPresent()) {
                    if (!player.getInventory().spaceFor(new Item(itemId, item.get().getItem().getAmount()))) {
                        player.getMessages().sendMessage("You don't have " + "enough inventory space to pick this item up.");
                        return;
                    }
                    item.get().onPickup(player);
                }
            }
        });
    }
}
