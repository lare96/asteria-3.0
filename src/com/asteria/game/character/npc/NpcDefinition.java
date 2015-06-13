package com.asteria.game.character.npc;

/**
 * The container that represents an NPC definition.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDefinition {

    /**
     * The array that contains all of the NPC definitions.
     */
    public static final NpcDefinition[] DEFINITIONS = new NpcDefinition[6102];

    /**
     * The identification for this NPC.
     */
    private final int id;

    /**
     * The name of this NPC.
     */
    private final String name;

    /**
     * The description of this NPC.
     */
    private final String description;

    /**
     * The combat level of this NPC.
     */
    private final int combatLevel;

    /**
     * The size of this NPC.
     */
    private final int size;

    /**
     * Determines if this NPC can be attacked.
     */
    private final boolean attackable;

    /**
     * Determines if this NPC is aggressive.
     */
    private final boolean aggressive;

    /**
     * Determines if this NPC retreats.
     */
    private final boolean retreats;

    /**
     * Determines if this NPC is poisonous.
     */
    private final boolean poisonous;

    /**
     * The time it takes for this NPC to respawn.
     */
    private final int respawnTime;

    /**
     * The max hit of this NPC.
     */
    private final int maxHit;

    /**
     * The maximum amount of hitpoints this NPC has.
     */
    private final int hitpoints;

    /**
     * The attack speed of this NPC.
     */
    private final int attackSpeed;

    /**
     * The attack animation of this NPC.
     */
    private final int attackAnimation;

    /**
     * The defence animation of this NPC.
     */
    private final int defenceAnimation;

    /**
     * The death animation of this NPC.
     */
    private final int deathAnimation;

    /**
     * The attack bonus of this NPC.
     */
    private final int attackBonus;

    /**
     * The melee defence bonus of this NPC.
     */
    private final int meleeDefence;

    /**
     * The ranged defence of this NPC.
     */
    private final int rangedDefence;

    /**
     * The magic defence of this NPC.
     */
    private final int magicDefence;

    /**
     * Creates a new {@link NpcDefintion}.
     *
     * @param id
     *            the identification for this NPC.
     * @param name
     *            the name of this NPC.
     * @param description
     *            the description of this NPC.
     * @param combatLevel
     *            the combat level of this NPC.
     * @param size
     *            the size of this NPC.
     * @param attackable
     *            determines if this NPC can be attacked.
     * @param aggressive
     *            determines if this NPC is aggressive.
     * @param retreats
     *            determines if this NPC retreats.
     * @param poisonous
     *            determines if this NPC is poisonous.
     * @param respawnTime
     *            the time it takes for this NPC to respawn.
     * @param maxHit
     *            the max hit of this NPC.
     * @param hitpoints
     *            the maximum amount of hitpoints this NPC has.
     * @param attackSpeed
     *            the attack speed of this NPC.
     * @param attackAnimation
     *            the attack animation of this NPC.
     * @param defenceAnimation
     *            the defence animation of this NPC.
     * @param deathAnimation
     *            the death animation of this NPC.
     * @param attackBonus
     *            the attack bonus of this NPC.
     * @param meleeDefence
     *            the melee defence bonus of this NPC.
     * @param rangedDefence
     *            the ranged defence of this NPC.
     * @param magicDefence
     *            the magic defence of this NPC.
     */
    public NpcDefinition(int id, String name, String description, int combatLevel, int size, boolean attackable, boolean aggressive,
        boolean retreats, boolean poisonous, int respawnTime, int maxHit, int hitpoints, int attackSpeed, int attackAnimation,
        int defenceAnimation, int deathAnimation, int attackBonus, int meleeDefence, int rangedDefence, int magicDefence) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.combatLevel = combatLevel;
        this.size = size;
        this.attackable = attackable;
        this.aggressive = aggressive;
        this.retreats = retreats;
        this.poisonous = poisonous;
        this.respawnTime = respawnTime;
        this.maxHit = maxHit;
        this.hitpoints = hitpoints;
        this.attackSpeed = attackSpeed;
        this.attackAnimation = attackAnimation;
        this.defenceAnimation = defenceAnimation;
        this.deathAnimation = deathAnimation;
        this.attackBonus = attackBonus;
        this.meleeDefence = meleeDefence;
        this.rangedDefence = rangedDefence;
        this.magicDefence = magicDefence;
    }

    /**
     * Gets the identification for this NPC.
     *
     * @return the identification.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of this NPC.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of this NPC.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the combat level of this npc.
     *
     * @return the combat level
     */
    public int getCombatLevel() {
        return combatLevel;
    }

    /**
     * Gets the size of this NPC.
     *
     * @return the size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Determines if this NPC can be attacked.
     *
     * @return {@code true} if this NPC can be attacked, {@code false}
     *         otherwise.
     */
    public boolean isAttackable() {
        return attackable;
    }

    /**
     * Determines if this NPC is aggressive.
     *
     * @return {@code true} if this NPC is aggressive, {@code false} otherwise.
     */
    public boolean isAggressive() {
        return aggressive;
    }

    /**
     * Determines if this NPC retreats.
     *
     * @return {@code true} if this NPC can retreat, {@code false} otherwise.
     */
    public boolean isRetreats() {
        return retreats;
    }

    /**
     * Determines if this NPC is poisonous.
     *
     * @return {@code true} if this NPC is poisonous, {@code false} otherwise.
     */
    public boolean isPoisonous() {
        return poisonous;
    }

    /**
     * Gets the time it takes for this NPC to respawn.
     *
     * @return the respawn time.
     */
    public int getRespawnTime() {
        return ((respawnTime - 1) <= 0 ? 1 : (respawnTime - 1));
    }

    /**
     * Gets the max hit of this NPC.
     *
     * @return the max hit.
     */
    public int getMaxHit() {
        return maxHit;
    }

    /**
     * Gets the maximum amount of hitpoints this NPC has.
     *
     * @return the maximum amount of hitpoints.
     */
    public int getHitpoints() {
        return hitpoints;
    }

    /**
     * Gets the maximum amount of hitpoints this NPC has.
     *
     * @return the attack speed.
     */
    public int getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * Gets the attack animation of this NPC.
     *
     * @return the attack animation.
     */
    public int getAttackAnimation() {
        return attackAnimation;
    }

    /**
     * Gets the defence animation of this NPC.
     *
     * @return the defence animation.
     */
    public int getDefenceAnimation() {
        return defenceAnimation;
    }

    /**
     * Gets the death animation of this NPC.
     *
     * @return the death animation.
     */
    public int getDeathAnimation() {
        return deathAnimation;
    }

    /**
     * Gets the attack bonus of this NPC.
     *
     * @return the attack bonus.
     */
    public int getAttackBonus() {
        return attackBonus;
    }

    /**
     * Gets the melee defence bonus of this NPC.
     *
     * @return the melee defence bonus.
     */
    public int getMeleeDefence() {
        return meleeDefence;
    }

    /**
     * Gets the ranged defence of this NPC.
     *
     * @return the ranged defence bonus.
     */
    public int getRangedDefence() {
        return rangedDefence;
    }

    /**
     * Gets the magic defence of this NPC.
     *
     * @return the magic defence bonus.
     */
    public int getMagicDefence() {
        return magicDefence;
    }
}
