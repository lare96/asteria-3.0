package com.asteria.net.message.impl;

import com.asteria.game.character.Flag;
import com.asteria.game.character.player.Appearance;
import com.asteria.game.character.player.Player;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when the character clicks "accept" on the
 * character selection interface.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CharacterSelectionMessage implements InputMessageListener {

    /**
     * The valid colors for the character selection message.
     */
    private static final int[][] VALID_COLORS = { { 0, 11 }, // hair color
            { 0, 15 }, // torso color
            { 0, 15 }, // legs color
            { 0, 5 }, // feet color
            { 0, 7 } // skin color
    };

    /**
     * The valid female appearance values for the character selection message.
     */
    private static final int[][] FEMALE_VALUES = { { 45, 54 }, // head
            { -1, -1 }, // jaw
            { 56, 60 }, // torso
            { 61, 65 }, // arms
            { 67, 68 }, // hands
            { 70, 77 }, // legs
            { 79, 80 }, // feet
    };

    /**
     * The valid male appearance values for the character selection message.
     */
    private static final int[][] MALE_VALUES = { { 0, 8 }, // head
            { 10, 17 }, // jaw
            { 18, 25 }, // torso
            { 26, 31 }, // arms
            { 33, 34 }, // hands
            { 36, 40 }, // legs
            { 42, 43 }, // feet
    };

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;
        int cursor = 0;
        int[] values = new int[13];
        int gender = payload.get();
        if (gender != Appearance.GENDER_FEMALE && gender != Appearance.GENDER_MALE)
            return;
        values[cursor++] = gender;
        for (int i = 0; i < 7; i++) {
            int value = payload.get();
            if ((value < (gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0])) || (value > (gender == 0 ? MALE_VALUES[i][1]
                : FEMALE_VALUES[i][1]))) {
                return;
            }
            values[cursor++] = value;
        }
        for (int i = 0; i < VALID_COLORS.length; i++) {
            int value = payload.get();
            if ((value < VALID_COLORS[i][0]) || (value > VALID_COLORS[i][1])) {
                return;
            }
            values[cursor++] = value;
        }
        player.getAppearance().setValues(values);
        player.getFlags().set(Flag.APPEARANCE);
        player.getMessages().sendCloseWindows();
    }
}
