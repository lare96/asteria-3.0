package com.asteria.network.packet.impl;

import java.util.Arrays;

import plugin.skills.prayer.Bone;
import plugin.skills.prayer.PrayerBoneBury;

import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.content.FoodConsumable;
import com.asteria.game.character.player.content.PotionConsumable;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemDefinition;
import com.asteria.game.plugin.PluginHandler;
import com.asteria.game.plugin.context.ItemFirstClickPlugin;
import com.asteria.network.ByteOrder;
import com.asteria.network.DataBuffer;
import com.asteria.network.ValueType;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when the player clicks an item.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class ItemActionPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.getViewingOrb() != null)
            return;

        switch (opcode) {
        case 122:
            firstClick(player, buf);
            break;
        }
    }

    /**
     * Handles the first slot of an item action.
     * 
     * @param player
     *            the player to handle this for.
     * @param buf
     *            the buffer for reading the sent data.
     */
    private void firstClick(Player player, DataBuffer buf) {
        int container = buf.getShort(true, ValueType.A, ByteOrder.LITTLE);
        int slot = buf.getShort(false, ValueType.A);
        int id = buf.getShort(false, ByteOrder.LITTLE);

        if (slot < 0 || container < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length)
            return;

        Arrays.fill(player.getSkillEvent(), false);
        player.getCombatBuilder().cooldown(true);

        if (container == 3214) {
            Item item = player.getInventory().get(slot);

            if (item == null || item.getId() != id) {
                return;
            }
            if (FoodConsumable.consume(player, item, slot)) {
                return;
            }
            if (PotionConsumable.consume(player, item, slot)) {
                return;
            }
            Bone bone = Bone.getBone(id);
            if (bone != null) {
                PrayerBoneBury buryAction = new PrayerBoneBury(player, bone);
                buryAction.start();
                return;
            }
            PluginHandler.execute(player, ItemFirstClickPlugin.class, new ItemFirstClickPlugin(slot, item));
        }
    }
}
