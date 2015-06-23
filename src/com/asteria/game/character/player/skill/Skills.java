package com.asteria.game.character.player.skill;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.asteria.game.character.Flag;
import com.asteria.game.character.Graphic;
import com.asteria.game.character.player.Player;
import com.asteria.utility.TextUtils;

/**
 * The class that contains methods to handle the functionality of skills.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Skills {

    /**
     * The attack skill identifier for the skill array.
     */
    public static final int ATTACK = 0;

    /**
     * The defence skill identifier for the skill array.
     */
    public static final int DEFENCE = 1;

    /**
     * The strength skill identifier for the skill array.
     */
    public static final int STRENGTH = 2;

    /**
     * The hitpoints skill identifier for the skill array.
     */
    public static final int HITPOINTS = 3;

    /**
     * The ranged skill identifier for the skill array.
     */
    public static final int RANGED = 4;

    /**
     * The prayer skill identifier for the skill array.
     */
    public static final int PRAYER = 5;

    /**
     * The magic skill identifier for the skill array.
     */
    public static final int MAGIC = 6;

    /**
     * The cooking skill identifier for the skill array.
     */
    public static final int COOKING = 7;

    /**
     * The woodcutting skill identifier for the skill array.
     */
    public static final int WOODCUTTING = 8;

    /**
     * The fletching skill identifier for the skill array.
     */
    public static final int FLETCHING = 9;

    /**
     * The fishing skill identifier for the skill array.
     */
    public static final int FISHING = 10;

    /**
     * The firemaking skill identifier for the skill array.
     */
    public static final int FIREMAKING = 11;

    /**
     * The crafting skill identifier for the skill array.
     */
    public static final int CRAFTING = 12;

    /**
     * The smithing skill identifier for the skill array.
     */
    public static final int SMITHING = 13;

    /**
     * The mining skill identifier for the skill array.
     */
    public static final int MINING = 14;

    /**
     * The herblore skill identifier for the skill array.
     */
    public static final int HERBLORE = 15;

    /**
     * The agility skill identifier for the skill array.
     */
    public static final int AGILITY = 16;

    /**
     * The thieving skill identifier for the skill array.
     */
    public static final int THIEVING = 17;

    /**
     * The slayer skill identifier for the skill array.
     */
    public static final int SLAYER = 18;

    /**
     * The farming skill identifier for the skill array.
     */
    public static final int FARMING = 19;

    /**
     * The runecrafting skill identifier for the skill array.
     */
    public static final int RUNECRAFTING = 20;

    /**
     * The experience multiplier that all experience will be calculated with.
     */
    private static final int EXPERIENCE_MULTIPLIER = 1;

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private Skills() {
        throw new UnsupportedOperationException("This class cannot be " + "instantiated!");
    }

    /**
     * Attempts to add {@code amount} of experience for {@code player}.
     *
     * @param player
     *            the player to add the experience for.
     * @param amount
     *            the amount of experience that will be added.
     * @param skill
     *            the skill to add the experience for.
     */
    public static void experience(Player player, double amount, int skill) {
        if (amount <= 0)
            return;
        int oldLevel = player.getSkills()[skill].getRealLevel();
        amount *= Skills.EXPERIENCE_MULTIPLIER;
        player.getSkills()[skill].increaseExperience(amount);
        if (oldLevel < 99) {
            int newLevel = player.getSkills()[skill].getLevelForExperience();
            if (oldLevel < newLevel) {
                if (skill != 3) {
                    player.getSkills()[skill].setLevel(newLevel, true);
                } else {
                    int old = player.getSkills()[skill].getLevel();
                    player.getSkills()[skill].setLevel(old + 1, true);
                }
                SkillData data = SkillData.values()[skill];
                String append = TextUtils.appendIndefiniteArticle(data.toString());
                player.getEncoder()
                    .sendString("@dre@Congratulations, you've " + "just advanced " + append + " level!", data.getFirstLine());
                player.getEncoder().sendString("Your " + data + " level is " + "now " + newLevel + ".", data.getSecondLine());
                player.getEncoder().sendMessage("Congratulations, you've just" + " advanced " + append + " level!");
                player.getEncoder().sendChatInterface(data.getChatbox());
                player.graphic(new Graphic(199));
                player.getFlags().set(Flag.APPEARANCE);
            }
        }
        Skills.refresh(player, skill);
    }

    /**
     * Sends {@code skill} to the client which will refresh it for
     * {@code player}.
     *
     * @param player
     *            the player to refresh the skill for.
     * @param skill
     *            the skill that will be refreshed.
     */
    public static void refresh(Player player, int skill) {
        Skill s = player.getSkills()[skill];
        if (s == null) {
            s = new Skill();
            if (skill == Skills.HITPOINTS) {
                s.setLevel(10, true);
                s.setExperience(1300);
            }
            player.getSkills()[skill] = s;
        }
        player.getEncoder().sendSkill(skill, s.getLevel(), (int) s.getExperience());
    }

    /**
     * Sends {@code skills} to the client which will refresh it for
     * {@code player}.
     *
     * @param player
     *            the player to refresh the skills for.
     * @param skills
     *            the skills that will be refreshed.
     */
    public static void refresh(Player player, int... skills) {
        Arrays.stream(skills).forEach(it -> refresh(player, it));
    }

    /**
     * Sends skills to the client which will refresh them for {@code player}.
     *
     * @param player
     *            the player to refresh the skill for.
     */
    public static void refreshAll(Player player) {
        for (int i = 0; i < player.getSkills().length; i++)
            refresh(player, i);
    }

    /**
     * Creates an array of skills for {@code player}.
     *
     * @param player
     *            the player to create it for.
     */
    public static void create(Player player) {
        for (int i = 0; i < player.getSkills().length; i++) {
            player.getSkills()[i] = new Skill();
            if (i == Skills.HITPOINTS) {
                player.getSkills()[i].setLevel(10, true);
                player.getSkills()[i].setRealLevel(10);
                player.getSkills()[i].setExperience(1300);
            }
        }
    }

    /**
     * Restores {@code skill} back to its original level for {@code player}.
     *
     * @param player
     *            the player to restore the skill for.
     * @param skill
     *            the skill to restore.
     */
    public static void restore(Player player, int skill) {
        player.getSkills()[skill].setLevel(player.getSkills()[skill].getRealLevel(), true);
        refresh(player, skill);
    }

    /**
     * Restores skills back their its original levels for {@code player}.
     *
     * @param player
     *            the player to restore the skills for.
     */
    public static void restoreAll(Player player) {
        IntStream.range(0, player.getSkills().length).forEach(it -> restore(player, it));
    }
}
