package com.asteria.game.character.player.skill;

import com.asteria.utility.TextUtils;

/**
 * The enumerated type whose elements represent data for the skills.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public enum SkillData {
    ATTACK(Skills.ATTACK, 6248, 6249, 6247),
    DEFENCE(Skills.DEFENCE, 6254, 6255, 6253),
    STRENGTH(Skills.STRENGTH, 6207, 6208, 6206),
    HITPOINTS(Skills.HITPOINTS, 6217, 6218, 6216),
    RANGED(Skills.RANGED, 5453, 6114, 4443),
    PRAYER(Skills.PRAYER, 6243, 6244, 6242, 0),
    MAGIC(Skills.MAGIC, 6212, 6213, 6211),
    COOKING(Skills.COOKING, 6227, 6228, 6226, 1),
    WOODCUTTING(Skills.WOODCUTTING, 4273, 4274, 4272, 2),
    FLETCHING(Skills.FLETCHING, 6232, 6233, 6231, 3),
    FISHING(Skills.FISHING, 6259, 6260, 6258, 4),
    FIREMAKING(Skills.FIREMAKING, 4283, 4284, 4282, 5),
    CRAFTING(Skills.CRAFTING, 6264, 6265, 6263, 6),
    SMITHING(Skills.SMITHING, 6222, 6223, 6221, 7),
    MINING(Skills.MINING, 4417, 4438, 4416, 8),
    HERBLORE(Skills.HERBLORE, 6238, 6239, 6237, 9),
    AGILITY(Skills.AGILITY, 4278, 4279, 4277, 10),
    THIEVING(Skills.THIEVING, 4263, 4264, 4261, 11),
    SLAYER(Skills.SLAYER, 12123, 12124, 12122, 12),
    FARMING(Skills.FARMING, 4889, 4890, 4887, 13),
    RUNECRAFTING(Skills.RUNECRAFTING, 4268, 4269, 4267, 14);

    /**
     * The identification for this skill in the skills array.
     */
    private final int id;

    /**
     * The first line that level up text will be printed on.
     */
    private final int firstLine;

    /**
     * The second line that level up text will be printed on.
     */
    private final int secondLine;

    /**
     * The chatbox interface displayed on level up.
     */
    private final int chatbox;

    /**
     * The index in the skill event array.
     */
    private final int index;

    /**
     * Creates a new {@link SkillData}.
     * 
     * @param id
     *            the identification for this skill in the skills array.
     * @param firstLine
     *            the first line that level up text will be printed on.
     * @param secondLine
     *            the second line that level up text will be printed on.
     * @param chatbox
     *            the chatbox interface displayed on level up.
     * @param index
     *            the index in the skill event array.
     */
    private SkillData(int id, int firstLine, int secondLine, int chatbox, int index) {
        this.id = id;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.chatbox = chatbox;
        this.index = index;
    }

    @Override
    public final String toString() {
        return TextUtils.capitalize(name().toLowerCase().replaceAll("_", " "));
    }

    /**
     * Creates a new {@link SkillData} with no skill event index.
     * 
     * @param firstLine
     *            the first line that level up text will be printed on.
     * @param secondLine
     *            the second line that level up text will be printed on.
     * @param chatbox
     *            the chatbox interface displayed on level up.
     */
    private SkillData(int id, int firstLine, int secondLine, int chatbox) {
        this(id, firstLine, secondLine, chatbox, -1);
    }

    /**
     * Gets the identification for this skill in the skills array.
     * 
     * @return the identification for this skill.
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the first line that level up text will be printed on.
     * 
     * @return the first line.
     */
    public final int getFirstLine() {
        return firstLine;
    }

    /**
     * Gets the second line that level up text will be printed on.
     * 
     * @return the second line.
     */
    public final int getSecondLine() {
        return secondLine;
    }

    /**
     * Gets the chatbox interface displayed on level up.
     * 
     * @return the chatbox interface.
     */
    public final int getChatbox() {
        return chatbox;
    }

    /**
     * Gets the index in the skill event array.
     * 
     * @return the index.
     */
    public final int getIndex() {
        return index;
    }
}