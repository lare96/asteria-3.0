package com.asteria.game.character.combat;

import java.util.concurrent.TimeUnit;

import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.task.EventListener;

/**
 * Controls and gives access to the main parts of the combat process such as
 * starting and ending combat sessions.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatBuilder {

    /**
     * The character in control of this combat builder.
     */
    private final CharacterNode character;

    /**
     * The current character that the controller is attacking.
     */
    private CharacterNode currentVictim;

    /**
     * The last character that attacked the controller.
     */
    private CharacterNode lastAttacker;

    /**
     * The task that handles the entire combat process.
     */
    private CombatSession combatTask;

    /**
     * The task that handles the pre-combat process.
     */
    private CombatDistanceListener distanceTask;

    /**
     * The cache of damage dealt to this controller during combat.
     */
    private final CombatDamage damageCache = new CombatDamage();

    /**
     * The combat strategy this character attacking with.
     */
    private CombatStrategy strategy;

    /**
     * The timer that controls how long this character must wait to attack.
     */
    private int attackTimer;

    /**
     * The cooldown timer used when the character breaks the combat session.
     */
    private int cooldown;

    /**
     * Creates a new {@link CombatBuilder}.
     *
     * @param character
     *            the character in control of this combat builder.
     */
    public CombatBuilder(CharacterNode character) {
        this.character = character;
    }

    /**
     * Prompts the controller to attack {@code target}. If the controller is
     * already attacking the target this method has no effect.
     *
     * @param target
     *            the character that this controller will be prompted to attack.
     */
    public void attack(CharacterNode target) {
        if (character.equals(target))
            return;
        if (target.equals(currentVictim)) {
            determineStrategy();
            if (character.getPosition().withinDistance(currentVictim.getPosition(), strategy.attackDistance(character))) {
                character.getMovementQueue().reset();
            }
        }
        character.getMovementQueue().follow(target);
        if (combatTask != null && combatTask.isRunning()) {
            currentVictim = target;
            if (character.getType() == NodeType.PLAYER) {
                Player player = (Player) character;
                if (player.isAutocast() || player.getCastSpell() == null || attackTimer < 1) {
                    cooldown = 0;
                }
            }
            return;
        }

        if (distanceTask != null && distanceTask.isRunning())
            distanceTask.cancel();
        distanceTask = new CombatDistanceListener(this, target);
        World.submit(distanceTask);
    }

    /**
     * Instantly executes the combat task regardless of its state. Should really
     * only be used for instant special attacks.
     */
    public void instant() {
        combatTask.execute();
    }

    /**
     * Resets this builder by discarding various values associated with the
     * combat process.
     */
    public void reset() {
        if (distanceTask != null)
            distanceTask.cancel();
        if (combatTask != null)
            combatTask.cancel();
        currentVictim = null;
        combatTask = null;
        attackTimer = 0;
        strategy = null;
        cooldown = 0;
        character.faceCharacter(null);
        character.setFollowing(false);
    }

    /**
     * Resets the attack timer to it's original value based on the combat
     * strategy.
     */
    public void resetAttackTimer() {
        if (strategy == null)
            return;
        attackTimer = strategy.attackDelay(character);
    }

    /**
     * Sets the attack timer to a value of {@code 0}.
     */
    public void clearAttackTimer() {
        attackTimer = 0;
    }

    /**
     * Starts the cooldown sequence for this controller.
     *
     * @param resetAttack
     *            if the attack timer should be reset.
     */
    public void cooldown(boolean resetAttack) {
        if (strategy == null)
            return;
        cooldown = 10;
        character.setFollowing(false);
        if (resetAttack) {
            attackTimer = strategy.attackDelay(character);
        }
    }

    /**
     * Calculates and sets the combat strategy.
     */
    public void determineStrategy() {
        this.strategy = character.determineStrategy();
    }

    /**
     * Determines if this character is attacking another character.
     *
     * @return {@code true} if this character is attacking another character,
     *         {@code false} otherwise.
     */
    public boolean isAttacking() {
        return currentVictim != null;
    }

    /**
     * Determines if this character is being attacked by another character.
     *
     * @return {@code true} if this character is being attacked by another
     *         character, {@code false} otherwise.
     */
    public boolean isBeingAttacked() {
        return !character.getLastCombat().elapsed(5, TimeUnit.SECONDS);
    }

    /**
     * Determines if this character is being attacked by or attacking another
     * character.
     *
     * @return {@code true} if this player is in combat, {@code false}
     *         otherwise.
     */
    public boolean inCombat() {
        return isAttacking() || isBeingAttacked();
    }

    /**
     * Determines if this combat builder is in cooldown mode.
     *
     * @return {@code true} if this combat builder is in cooldown mode,
     *         {@code false} otherwise.
     */
    public boolean isCooldown() {
        return cooldown > 0;
    }

    /**
     * Gets the cooldown timer used when the character breaks the combat
     * session.
     *
     * @return the cooldown timer.
     */
    public int getCooldown() {
        return cooldown;
    }

    /**
     * Decrements the cooldown timer used when the character breaks the combat
     * session.
     */
    public void decrementCooldown() {
        cooldown--;
    }

    /**
     * Gets the timer that controls how long this character must wait to attack.
     *
     * @return the timer determines when the controller attacks.
     */
    public int getAttackTimer() {
        return attackTimer;
    }

    /**
     * Decrements the timer that controls how long this character must wait to
     * attack.
     */
    public void decrementAttackTimer() {
        attackTimer--;
    }

    /**
     * Gets the character in control of this combat builder.
     *
     * @return the character in control.
     */
    public CharacterNode getCharacter() {
        return character;
    }

    /**
     * Gets the current character that the controller is attacking.
     *
     * @return the character the controller is attacking
     */
    public CharacterNode getVictim() {
        return currentVictim;
    }

    /**
     * Gets the last character that attacked the controller.
     *
     * @return the last character that attacked.
     */
    public CharacterNode getLastAttacker() {
        return lastAttacker;
    }

    /**
     * Sets the value for {@link CombatBuilder#lastAttacker}.
     *
     * @param lastAttacker
     *            the new value to set.
     */
    public void setLastAttacker(CharacterNode lastAttacker) {
        this.lastAttacker = lastAttacker;
    }

    /**
     * Gets the combat strategy this character attacking with.
     *
     * @return the combat strategy.
     */
    public CombatStrategy getStrategy() {
        return strategy;
    }

    /**
     * Gets the task that handles the entire combat process.
     *
     * @return the task for the combat process.
     */
    public CombatSession getCombatTask() {
        return combatTask;
    }

    /**
     * Gets the cache of damage dealt to this controller during combat.
     *
     * @return the cache of damage.
     */
    public CombatDamage getDamageCache() {
        return damageCache;
    }

    /**
     * An {@link EventListener} implementation that is used to listen for the
     * controller to become in proper range of the victim.
     *
     * @author lare96 <http://github.com/lare96>
     */
    private static final class CombatDistanceListener extends EventListener {

        /**
         * The combat builder owned by the controller.
         */
        private final CombatBuilder builder;

        /**
         * The victim that will be listened for.
         */
        private final CharacterNode victim;

        /**
         * Create a new {@link CombatDistanceListener}.
         *
         * @param builder
         *            the combat builder owned by the controller.
         * @param victim
         *            the victim that will be listened for.
         */
        public CombatDistanceListener(CombatBuilder builder, CharacterNode victim) {
            super.attach(builder.getCharacter().getType() == NodeType.PLAYER ? ((Player) builder.getCharacter()) : ((Npc) builder
                .getCharacter()));
            this.builder = builder;
            this.victim = victim;
        }

        @Override
        public boolean canExecute() {
            builder.determineStrategy();
            builder.attackTimer = 0;
            builder.cooldown = 0;

            if (!builder.character.getPosition().isViewableFrom(victim.getPosition())) {
                builder.reset();
                this.cancel();
                return true;
            }

            if (builder.character.getType() == NodeType.NPC) {
                Npc npc = (Npc) builder.character;

                if (!npc.getPosition().isViewableFrom(npc.getOriginalPosition()) && npc.getDefinition().isRetreats()) {
                    npc.getMovementQueue().walk(npc.getOriginalPosition());
                    builder.reset();
                    this.cancel();
                    return true;
                }
            }
            return builder.character.getPosition().withinDistance(victim.getPosition(),
                builder.strategy.attackDistance(builder.getCharacter()));
        }

        @Override
        public void run() {
            builder.getCharacter().getMovementQueue().reset();
            builder.currentVictim = victim;

            if (builder.combatTask == null || !builder.combatTask.isRunning()) {
                builder.combatTask = new CombatSession(builder);
                World.submit(builder.combatTask);
            }
        }
    }
}
