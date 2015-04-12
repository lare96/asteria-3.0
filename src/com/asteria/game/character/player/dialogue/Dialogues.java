package com.asteria.game.character.player.dialogue;

import java.util.Arrays;
import java.util.Objects;

import com.asteria.game.character.npc.NpcDefinition;
import com.asteria.game.character.player.Player;

/**
 * The static utility class that contains functions for sending dialogues.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Dialogues {

    /**
     * The maximum length of a single line of dialogue.
     */
    private static final int MAXIMUM_LENGTH = ("A string representing maximum " + "dialogue text length!!").length();

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private Dialogues() {
        throw new UnsupportedOperationException("This class cannot be " + "instantiated!");
    }

    /**
     * The method that sends an NPC dialogue to {@code player}.
     *
     * @param player
     *            the player to send the dialogue to.
     * @param expression
     *            the expression the NPC will make.
     * @param npc
     *            the NPC that is sending the dialogue.
     * @param text
     *            the text contained within the dialogue.
     * @throws IllegalArgumentException
     *             if the text array is greater than {@code 4} or less than
     *             {@code 1}.
     */
    public static void npcDialogue(Player player, Expression expression, int npc, String... text) {
        validateLength(text);
        switch (text.length) {
        case 1:
            player.getEncoder().sendInterfaceAnimation(4883, expression.getExpression());
            player.getEncoder().sendString(NpcDefinition.DEFINITIONS[npc].getName(), 4884);
            player.getEncoder().sendString(text[0], 4885);
            player.getEncoder().sendNpcModelOnInterface(4883, npc);
            player.getEncoder().sendChatInterface(4882);
            break;
        case 2:
            player.getEncoder().sendInterfaceAnimation(4888, expression.getExpression());
            player.getEncoder().sendString(NpcDefinition.DEFINITIONS[npc].getName(), 4889);
            player.getEncoder().sendString(text[0], 4890);
            player.getEncoder().sendString(text[1], 4891);
            player.getEncoder().sendNpcModelOnInterface(4888, npc);
            player.getEncoder().sendChatInterface(4887);
            break;
        case 3:
            player.getEncoder().sendInterfaceAnimation(4894, expression.getExpression());
            player.getEncoder().sendString(NpcDefinition.DEFINITIONS[npc].getName(), 4895);
            player.getEncoder().sendString(text[0], 4896);
            player.getEncoder().sendString(text[1], 4897);
            player.getEncoder().sendString(text[2], 4898);
            player.getEncoder().sendNpcModelOnInterface(4894, npc);
            player.getEncoder().sendChatInterface(4893);
            break;
        case 4:
            player.getEncoder().sendInterfaceAnimation(4901, expression.getExpression());
            player.getEncoder().sendString(NpcDefinition.DEFINITIONS[npc].getName(), 4902);
            player.getEncoder().sendString(text[0], 4903);
            player.getEncoder().sendString(text[1], 4904);
            player.getEncoder().sendString(text[2], 4905);
            player.getEncoder().sendString(text[3], 4906);
            player.getEncoder().sendNpcModelOnInterface(4901, npc);
            player.getEncoder().sendChatInterface(4900);
            break;
        default:
            throw new IllegalArgumentException("Illegal npc dialogue " + "length: " + text.length);
        }
    }

    /**
     * The method that sends a player dialogue to {@code player}.
     *
     * @param player
     *            the player to send the dialogue to.
     * @param expression
     *            the expression the player will make.
     * @param text
     *            the text contained within the dialogue.
     * @throws IllegalArgumentException
     *             if the text array is greater than {@code 4} or less than
     *             {@code 1}.
     */
    public static void playerDialogue(Player player, Expression expression, String... text) {
        validateLength(text);
        switch (text.length) {
        case 1:
            player.getEncoder().sendInterfaceAnimation(969, expression.getExpression());
            player.getEncoder().sendString(player.getFormatUsername(), 970);
            player.getEncoder().sendString(text[0], 971);
            player.getEncoder().sendPlayerModelOnInterface(969);
            player.getEncoder().sendChatInterface(968);
            break;
        case 2:
            player.getEncoder().sendInterfaceAnimation(974, expression.getExpression());
            player.getEncoder().sendString(player.getFormatUsername(), 975);
            player.getEncoder().sendString(text[0], 976);
            player.getEncoder().sendString(text[1], 977);
            player.getEncoder().sendPlayerModelOnInterface(974);
            player.getEncoder().sendChatInterface(973);
            break;
        case 3:
            player.getEncoder().sendInterfaceAnimation(980, expression.getExpression());
            player.getEncoder().sendString(player.getFormatUsername(), 981);
            player.getEncoder().sendString(text[0], 982);
            player.getEncoder().sendString(text[1], 983);
            player.getEncoder().sendString(text[2], 984);
            player.getEncoder().sendPlayerModelOnInterface(980);
            player.getEncoder().sendChatInterface(979);
            break;
        case 4:
            player.getEncoder().sendInterfaceAnimation(987, expression.getExpression());
            player.getEncoder().sendString(player.getFormatUsername(), 988);
            player.getEncoder().sendString(text[0], 989);
            player.getEncoder().sendString(text[1], 990);
            player.getEncoder().sendString(text[2], 991);
            player.getEncoder().sendString(text[3], 992);
            player.getEncoder().sendPlayerModelOnInterface(987);
            player.getEncoder().sendChatInterface(986);
            break;
        default:
            throw new IllegalArgumentException("Illegal player dialogue " + "length: " + text.length);
        }
    }

    /**
     * The method that sends an option dialogue to {@code player}.
     *
     * @param player
     *            the player to send the dialogue to.
     * @param text
     *            the text contained within the options.
     * @throws IllegalArgumentException
     *             if the text array is greater than {@code 5} or less than
     *             {@code 2}.
     */
    public static void optionDialogue(Player player, String... text) {
        validateLength(text);
        switch (text.length) {
        case 2:
            player.getEncoder().sendString(text[0], 14445);
            player.getEncoder().sendString(text[1], 14446);
            player.getEncoder().sendChatInterface(14443);
            break;
        case 3:
            player.getEncoder().sendString(text[0], 2471);
            player.getEncoder().sendString(text[1], 2472);
            player.getEncoder().sendString(text[2], 2473);
            player.getEncoder().sendChatInterface(2469);
            break;
        case 4:
            player.getEncoder().sendString(text[0], 8209);
            player.getEncoder().sendString(text[1], 8210);
            player.getEncoder().sendString(text[2], 8211);
            player.getEncoder().sendString(text[3], 8212);
            player.getEncoder().sendChatInterface(8207);
            break;
        case 5:
            player.getEncoder().sendString(text[0], 8221);
            player.getEncoder().sendString(text[1], 8222);
            player.getEncoder().sendString(text[2], 8223);
            player.getEncoder().sendString(text[3], 8224);
            player.getEncoder().sendString(text[4], 8225);
            player.getEncoder().sendChatInterface(8219);
            break;
        default:
            throw new IllegalArgumentException("Illegal dialogue option " + "length: " + text.length);
        }
    }

    /**
     * The method that validates the length of {@code text}.
     *
     * @param text
     *            the text that will be validated.
     * @throws IllegalStateException
     *             if any lines of the text exceed a certain length.
     */
    private static void validateLength(String... text) {
        if (Arrays.stream(text).filter(Objects::nonNull).anyMatch(s -> s.length() > MAXIMUM_LENGTH))
            throw new IllegalStateException("Dialogue length too long, maximum length is: " + MAXIMUM_LENGTH);
    }
}
