package com.asteria.game.character.player.skill;

/**
 * The class that represents one skill that can be trained.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class Skill {

    /**
     * The level of this skill that can be trained.
     */
    private int level = 1;

    /**
     * The experience of this skill that can be trained.
     */
    private int experience = 0;

    /**
     * The real level of this skill that can be trained.
     */
    private int realLevel = 1;

    /**
     * Gets the level based on the experience the player has.
     * 
     * @return the level based on experience.
     */
    public int getLevelForExperience() {
        int points = 0;
        int output = 0;

        if (realLevel >= 99) {
            return 99;
        }

        for (int lvl = 1; lvl <= 99; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);
            if (output >= this.getExperience()) {
                realLevel = lvl;
                return lvl;
            }
        }
        realLevel = 99;
        return 99;
    }

    /**
     * Gets the amount of experience needed for the next level.
     * 
     * @return the amount of experience.
     */
    public int getExperienceForNextLevel() {
        int points = 0;
        int output = 0;

        for (int lvl = 1; lvl <= (this.getLevel() + 1); lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            if (lvl >= (this.getRealLevel() + 1))
                return output;
            output = (int) Math.floor(points / 4);
        }

        return 0;
    }

    /**
     * Determines if your level is greater than or equal to {@code level}.
     * 
     * @param level
     *            the level to compare against this one.
     * @return {@code true} if this level is greater than or equal to the other
     *         one, {@code false} otherwise.
     */
    public boolean reqLevel(int level) {
        return this.level >= level;
    }

    /**
     * Increments this level by {@code amount} to a maximum of {@code 120}.
     * 
     * @param amount
     *            the amount to increase this level by.
     */
    public void increaseLevel(int amount) {
        increaseLevel(amount, 120);
    }

    /**
     * Increments this level by {@code amount} to {@code maximum}.
     * 
     * @param amount
     *            the amount to increase this level by.
     * @param maximum
     *            the maximum level to increase this to.
     */
    public void increaseLevel(int amount, int maximum) {
        if ((level + amount) > maximum) {
            level = maximum;
            return;
        }
        level += amount;
    }

    /**
     * Decrements this level by {@code amount} to a minimum of {@code 0}.
     * 
     * @param amount
     *            the amount to decrease this level by.
     */
    public void decreaseLevel(int amount) {
        decreaseLevel(amount, 0);
    }

    /**
     * Decrements this level by {@code amount} to {@code minimum}.
     * 
     * @param amount
     *            the amount to decrease this level by.
     */
    public void decreaseLevel(int amount, int minimum) {
        if ((level - amount) < minimum) {
            level = minimum;
            return;
        }
        level -= amount;
    }

    /**
     * Gets the experience for this skill.
     * 
     * @return the experience.
     */
    public int getExperience() {
        return experience;
    }

    /**
     * Sets the value for {@link Skill#experience}.
     * 
     * @param experience
     *            the new value to set.
     */
    public void setExperience(int experience) {
        this.experience = experience;

        if (this.experience > 2000000000) {
            this.experience = 2000000000;
        }
    }

    /**
     * Gets the level for this skill.
     * 
     * @return the level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the value for {@link Skill#level}.
     * 
     * @param level
     *            the new value to set.
     * @param restriction
     *            if the level should be modified before being set.
     */
    public void setLevel(int level, boolean restriction) {
        this.level = level;

        if (restriction) {
            if (this.level < 0) {
                this.level = 0;
            } else if (this.level > 120) {
                this.level = 120;
            }
        }
    }

    /**
     * Gets the level based on how much experience you have.
     * 
     * @return the level based on how much experience you have.
     */
    public int getRealLevel() {
        return realLevel;
    }

    /**
     * Sets the value for {@link Skill#realLevel}.
     * 
     * @param realLevel
     *            the new value to set.
     */
    protected void setRealLevel(int realLevel) {
        this.realLevel = realLevel;
    }
}
