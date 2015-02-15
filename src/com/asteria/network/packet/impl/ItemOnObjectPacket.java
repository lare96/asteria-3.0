package com.asteria.network.packet.impl;

import plugin.skills.prayer.Bone;
import plugin.skills.prayer.PrayerBoneAltar;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.location.Position;
import com.asteria.game.plugin.PluginHandler;
import com.asteria.game.plugin.context.ItemOnObjectPlugin;
import com.asteria.network.ByteOrder;
import com.asteria.network.DataBuffer;
import com.asteria.network.ValueType;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when a player uses an item on an object.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class ItemOnObjectPacket extends PacketDecoder {

    // TODO: When cache reading is done, check position of objects to
    // see if you're actually near them or not.

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        int container = buf.getShort(false);
        int objectId = buf.getShort(true, ByteOrder.LITTLE);
        int objectY = buf.getShort(true, ValueType.A, ByteOrder.LITTLE);
        int slot = buf.getShort(true, ByteOrder.LITTLE);
        int objectX = buf.getShort(true, ValueType.A, ByteOrder.LITTLE);
        int itemId = buf.getShort(false);
        int objectSize = 1;
        Item item = player.getInventory().get(slot);
        Position position = new Position(objectX, objectY, player.getPosition().getZ());
        if (item == null || container != 3214 || objectId < 0 || objectY < 0 || slot < 0 || objectX < 0 || itemId < 0)
            return;

        player.facePosition(position);
        player.getMovementListener().append(
            () -> {
                if (player.getPosition().withinDistance(position, objectSize)) {
                    Bone bone = Bone.getBone(itemId);
                    if (bone != null) {
                        PrayerBoneAltar altarAction = new PrayerBoneAltar(player, bone, new Position(objectX, objectY));
                        altarAction.start();
                        return;
                    }
                    PluginHandler.execute(player, ItemOnObjectPlugin.class, new ItemOnObjectPlugin(objectId, position,
                        objectSize, item, slot));
                }
            });
    }
}
