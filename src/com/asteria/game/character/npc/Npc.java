package com.asteria.game.character.npc;

import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.Flag;
import com.asteria.game.character.Hit;
import com.asteria.game.character.PoisonType;
import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.combat.CombatStrategy;
import com.asteria.game.character.combat.CombatType;
import com.asteria.game.character.combat.effect.CombatPoisonEffect;
import com.asteria.game.character.combat.magic.CombatWeaken;
import com.asteria.game.location.Position;

/**
 * The character implementation that represents a node that is operated by the
 * server. This type of node functions solely through the server executing
 * functions.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Npc extends CharacterNode {

    /**
     * The identification for this NPC.
     */
    private final int id;

    /**
     * The maximum health of this NPC.
     */
    private final int maxHealth;

    /**
     * The original position that this NPC was created on.
     */
    private final Position originalPosition;

    /**
     * The movement coordinator for this NPC.
     */
    private final NpcMovementCoordinator movementCoordinator = new NpcMovementCoordinator(this);

    /**
     * The current health of this NPC.
     */
    private int currentHealth;

    /**
     * Determines if this NPC was originally random walking.
     */
    private boolean originalRandomWalk;

    /**
     * Determines if this NPC respawns.
     */
    private boolean respawn;

    /**
     * The spell that this NPC is weakened by.
     */
    private CombatWeaken weakenedBy;
    
    /**
     * The transformation identifier.
     */
    private int transform = -1;

    /**
     * Creates a new {@link Npc}.
     *
     * @param id
     *            the identification for this NPC.
     * @param position
     *            the position of this character in the world.
     */
    public Npc(int id, Position position) {
        super(position, NodeType.NPC);
        this.id = id;
        this.originalPosition = position.copy();
        this.maxHealth = getDefinition().getHitpoints();
        this.currentHealth = maxHealth;
    }

    @Override
    public void create() {

    }

    @Override
    public void dispose() {
        setPosition(new Position(1, 1));
        World.getTaskQueue().cancel(this);
    }

    @Override
    public void sequence() throws Exception {
        movementCoordinator.sequence();
    }

    @Override
    public Hit decrementHealth(Hit hit) {
        if (hit.getDamage() > currentHealth)
            hit = new Hit(currentHealth, hit.getType());
        currentHealth -= hit.getDamage();
        return hit;
    }

    @Override
    public int getAttackSpeed() {
        return this.getDefinition().getAttackSpeed();
    }

    @Override
    public int getCurrentHealth() {
        return currentHealth;
    }

    @Override
    public String toString() {
        return "NPC[slot= " + getSlot() + ", name=" + getDefinition().getName() + "]";
    }

    @Override
    public CombatStrategy determineStrategy() {
        return Combat.determineStrategy(id);
    }

    @Override
    public int getBaseAttack(CombatType type) {
        int value = getDefinition().getAttackBonus();
        if (weakenedBy == CombatWeaken.ATTACK_LOW || weakenedBy == CombatWeaken.ATTACK_HIGH)
            value -= (int) ((weakenedBy.getRate()) * (value));
        return value;
    }

    @Override
    public int getBaseDefence(CombatType type) {
        int value = type == CombatType.MAGIC ? getDefinition().getMagicDefence() : type == CombatType.RANGED ? getDefinition()
            .getRangedDefence() : getDefinition().getMeleeDefence();
        if (weakenedBy == CombatWeaken.DEFENCE_LOW || weakenedBy == CombatWeaken.DEFENCE_HIGH)
            value -= (int) ((weakenedBy.getRate()) * (value));
        return value;
    }

    @Override
    public void onSuccessfulHit(CharacterNode victim, CombatType type) {
        if (getDefinition().isPoisonous())
            victim.poison(CombatPoisonEffect.getPoisonType(id).orElse(PoisonType.DEFAULT_NPC));
    }

    @Override
    public void healCharacter(int damage) {
        if ((currentHealth + damage) > maxHealth) {
            currentHealth = maxHealth;
            return;
        }
        currentHealth += damage;
    }

    @Override
    public boolean weaken(CombatWeaken effect) {
        if (weakenedBy == null) {
            weakenedBy = effect;
            return true;
        }
        return false;
    }

    /**
     * Activates the {@code TRANSFORM} update mask for this non-player
     * character.
     * 
     * @param id
     *            the new npc to transform this npc into.
     */
    public void transform(int id) {
        transform = id;
        getFlags().set(Flag.TRANSFORM);
    }

    /**
     * Gets the movement coordinator for this NPC.
     *
     * @return the movement coordinator.
     */
    public NpcMovementCoordinator getMovementCoordinator() {
        return movementCoordinator;
    }

    /**
     * Determines if this NPC was originally random walking.
     *
     * @return {@code true} if this NPC was originally walking, {@code false}
     *         otherwise.
     */
    public boolean isOriginalRandomWalk() {
        return originalRandomWalk;
    }

    /**
     * Sets the value for {@link Npc#originalRandomWalk}.
     *
     * @param originalRandomWalk
     *            the new value to set.
     */
    public void setOriginalRandomWalk(boolean originalRandomWalk) {
        this.originalRandomWalk = originalRandomWalk;
    }

    /**
     * Determines if this NPC respawns.
     *
     * @return {@code true} if this NPC respawns, {@code false} otherwise.
     */
    public boolean isRespawn() {
        return respawn;
    }

    /**
     * Sets the value for {@link Npc#respawn}.
     *
     * @param respawn
     *            the new value to set.
     */
    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
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
     * Gets the maximum health of this NPC.
     *
     * @return the maximum health.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Gets the original position that this NPC was created on.
     *
     * @return the original position.
     */
    public Position getOriginalPosition() {
        return originalPosition;
    }

    /**
     * Sets the value for {@link Npc#currentHealth}.
     *
     * @param currentHealth
     *            the new value to set.
     */
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    /**
     * Gets the spell that this NPC is weakened by.
     *
     * @return the weakening spell.
     */
    public CombatWeaken getWeakenedBy() {
        return weakenedBy;
    }

    /**
     * Sets the value for {@link Npc#weakenedBy}.
     *
     * @param weakenedBy
     *            the new value to set.
     */
    public void setWeakenedBy(CombatWeaken weakenedBy) {
        this.weakenedBy = weakenedBy;
    }

    /**
     * Gets the definition for this NPC.
     *
     * @return the definition.
     */
    public NpcDefinition getDefinition() {
        return NpcDefinition.DEFINITIONS[id];
    }

    /**
     * Gets the transformation identifier.
     * 
     * @return the transformation id.
     */
    public int getTransform() {
        return transform;
    }
}
