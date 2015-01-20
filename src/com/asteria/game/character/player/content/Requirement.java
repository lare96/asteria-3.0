package com.asteria.game.character.player.content;

import java.util.HashMap;
import java.util.Map;

import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.SkillData;
import com.asteria.game.item.Item;
import com.asteria.utility.TextUtils;

/**
 * The container class that represents one equipment requirement.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class Requirement {

    /**
     * The hash collection of equipment requirements.
     */
    public static final Map<Integer, Requirement[]> REQUIREMENTS = new HashMap<>();

    /**
     * The level of this equipment requirement.
     */
    private final int level;

    /**
     * The skill identifier for this equipment requirement.
     */
    private final int skill;

    /**
     * Creates a new {@link Requirement}.
     * 
     * @param level
     *            the level of this equipment requirement.
     * @param skill
     *            the skill identifier for this equipment requirement.
     */
    public Requirement(int level, int skill) {
        this.level = level;
        this.skill = skill;
    }

    /**
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy is <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     * 
     * @return a reference-free copy of this instance.
     */
    public Requirement copy() {
        return new Requirement(level, skill);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + level;
        result = prime * result + skill;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Requirement))
            return false;
        Requirement other = (Requirement) obj;
        if (level != other.level)
            return false;
        if (skill != other.skill)
            return false;
        return true;
    }

    /**
     * Determines if {@code player} can equip {@code item} based on its
     * equipment requirements.
     * 
     * @param player
     *            the player that is equipping the item.
     * @param item
     *            the item being equipped.
     * @return {@code true} if the player can equip the item, {@code false}
     *         otherwise.
     */
    public static boolean canEquip(Player player, Item item) {
        if (item == null)
            return true;
        Requirement[] req = REQUIREMENTS.get(item.getId());
        if (req == null)
            return true;
        for (Requirement r : req) {
            if (player.getSkills()[r.getSkill()].getRealLevel() < r.getLevel()) {
                String append = TextUtils.appendIndefiniteArticle(SkillData.values()[r.getSkill()].toString());
                player.getEncoder().sendMessage("You need " + append + " level of " + r.getLevel() + " to equip this item.");
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the level of this equipment requirement.
     * 
     * @return the level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the skill identifier for this equipment requirement.
     * 
     * @return the skill identifier.
     */
    public int getSkill() {
        return skill;
    }
}