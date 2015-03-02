package com.asteria.network.packet.impl;

import java.util.Optional;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemNode;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.location.Position;
import com.asteria.network.ByteOrder;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when a player attempts to pick up an item.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class PickupItemPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.isDisabled())
            return;

        int itemY = buf.getShort(ByteOrder.LITTLE);
        int itemId = buf.getShort(false);
        int itemX = buf.getShort(ByteOrder.LITTLE);
        if (itemY < 0 || itemId < 0 || itemX < 0)
            return;
        player.setSkillAction(false);
        player.getMovementListener().append(() -> {
            if (player.getPosition().equals(new Position(itemX, itemY, player.getPosition().getZ()))) {
                Position position = new Position(itemX, itemY, player.getPosition().getZ());
                Optional<ItemNode> item = ItemNodeManager.getItem(itemId, position);

                if (item.isPresent()) {
                    if (!player.getInventory().spaceFor(new Item(itemId, item.get().getItem().getAmount()))) {
                        player.getEncoder().sendMessage("You don't have enough inventory space to pick this item up.");
                        return;
                    }
                    item.get().onPickup(player);
                }
            }
        });
    }
}
