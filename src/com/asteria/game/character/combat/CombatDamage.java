package com.asteria.game.character.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.asteria.game.NodeType;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.player.Player;
import com.asteria.utility.Stopwatch;

/**
 * A cache of players who have inflicted damage on a controller in a combat
 * session.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatDamage {

    /**
     * The map of players who have inflicted damage.
     */
    private Map<Player, DamageCounter> attackers = new HashMap<>();

    /**
     * Registers damage in the backing collection for {@code character}. This
     * method has no effect if the character isn't a {@link NodeType#PLAYER} or
     * {@code amount} is below {@code 0}.
     *
     * @param character
     *         the character to register damage for.
     * @param amount
     *         the amount of damage to register.
     */
    public void add(CharacterNode character, int amount) {
        if (character.getType() == NodeType.PLAYER && amount > 0) {
            Player player = (Player) character;
            DamageCounter counter = attackers.get(player);

            if (counter == null) {
                attackers.put(player, new DamageCounter(amount));
            } else {
                counter.incrementAmount(amount);
            }
        }
    }

    /**
     * Determines which player in the backing collection has inflicted the most
     * damage.
     *
     * @return the player who has inflicted the most damage, or an empty
     * optional if there are no entries.
     */
    public Optional<Player> calculateKiller() {
        int amount = 0;
        Player killer = null;
        for (Entry<Player, DamageCounter> entry : attackers.entrySet()) {
            DamageCounter counter = entry.getValue();
            Player player = entry.getKey();

            if (counter.isTimeout()) {
                continue;
            }
            if (player.isDead() || !player.getPosition().withinDistance(player.getPosition(), 25) || !player.isRegistered()) {
                continue;
            }
            if (counter.getAmount() > amount) {
                amount = counter.getAmount();
                killer = player;
            }
        }
        return Optional.ofNullable(killer);
    }

    /**
     * Clears all data from the backing collection.
     */
    public void clear() {
        attackers.clear();
    }

    /**
     * A counter that will track the amount of damage dealt and whether that
     * damaged has timed out or not.
     *
     * @author lare96 <http://github.com/lare96>
     */
    private static final class DamageCounter {

        /**
         * The amount of damage within this counter.
         */
        private int amount;

        /**
         * The stopwatch that will determine when a timeout occurs.
         */
        private final Stopwatch stopwatch;

        /**
         * Creates a new {@link DamageCounter}.
         *
         * @param amount
         *         the amount of damage within this counter.
         */
        public DamageCounter(int amount) {
            this.amount = amount;
            this.stopwatch = new Stopwatch().reset();
        }

        /**
         * Gets the amount of damage within this counter.
         *
         * @return the amount of damage.
         */
        public int getAmount() {
            return amount;
        }

        /**
         * Increments the amount of damage within this counter.
         *
         * @param amount
         *         the amount to increment by.
         */
        public void incrementAmount(int amount) {
            if (this.isTimeout()) {
                this.amount = 0;
            }
            this.amount += amount;
            this.stopwatch.reset();
        }

        /**
         * Determines if this counter has timed out or not.
         *
         * @return {@code true} if this counter has timed out, {@code false}
         * otherwise.
         */
        public boolean isTimeout() {
            return stopwatch.elapsed(Combat.DAMAGE_CACHE_TIMEOUT, TimeUnit.SECONDS);
        }
    }
}
