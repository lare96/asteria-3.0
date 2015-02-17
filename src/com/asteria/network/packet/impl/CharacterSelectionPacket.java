package com.asteria.network.packet.impl;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.asteria.game.World;
import com.asteria.game.character.Flag;
import com.asteria.game.character.player.Player;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet sent from the client when the character clicks "accept" on the
 * character selection interface.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class CharacterSelectionPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        if (player.getViewingOrb() != null)
            return;

        IntStream stream = Arrays.stream(new int[13]).map(v -> buf.get());

        if (stream.anyMatch(v -> v < 1)) {
            World.getPlayers().remove(player);
            return;
        }
        int[] values = stream.toArray();
        player.setGender(values[0]);
        player.getAppearance()[Player.APPEARANCE_SLOT_HEAD] = values[1];
        player.getAppearance()[Player.APPEARANCE_SLOT_BEARD] = values[2];
        player.getAppearance()[Player.APPEARANCE_SLOT_CHEST] = values[3];
        player.getAppearance()[Player.APPEARANCE_SLOT_ARMS] = values[4];
        player.getAppearance()[Player.APPEARANCE_SLOT_HANDS] = values[5];
        player.getAppearance()[Player.APPEARANCE_SLOT_LEGS] = values[6];
        player.getAppearance()[Player.APPEARANCE_SLOT_FEET] = values[7];
        player.getColors()[0] = values[8];
        player.getColors()[1] = values[9];
        player.getColors()[2] = values[10];
        player.getColors()[3] = values[11];
        player.getColors()[4] = values[12];
        player.getFlags().set(Flag.APPEARANCE);
        player.getEncoder().sendCloseWindows();
    }
}
