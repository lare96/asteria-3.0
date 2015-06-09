package com.asteria.game.character.npc;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.asteria.game.character.player.Player;
import com.asteria.game.location.Location;
import com.asteria.game.location.Position;

/**
 * Manages the behavior of aggressive {@link Npc}s including the way the
 * interact towards various {@link Player}s.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcAggression {

    /**
     * The absolute distance that players must be within to be targeted by
     * aggressive {@link Npc}s.
     */
    private static final int TARGET_DISTANCE = 6;

    /**
     * The time that has to be spent in a region before {@link Npc}s stop acting
     * aggressive towards a specific {@link Player}.
     */
    private static final int TOLERANCE_SECONDS = 600;

    /**
     * The hash collection the holds the npc identifiers of aggressive
     * {@link Npc}s.
     */
    public static final Set<Integer> AGGRESSIVE = new HashSet<>();

    /**
     * Prompts all aggressive {@link Npc}s to attack the unsuspecting
     * {@code player}, if they do not attack the player they will go back to
     * their default movement coordinate state.
     *
     * @param player
     *            the player that will be targeted.
     */
    public static void sequence(Player player) {
        for (Npc npc : player.getLocalNpcs()) {
            if (validate(npc, player)) {
                npc.getMovementQueue().reset();
                npc.getCombatBuilder().attack(player);
            } else {
                npc.getMovementCoordinator().setCoordinate(npc.isOriginalRandomWalk());
            }
        }
    }

    /**
     * Determines if {@code npc} is able to target {@code player}.
     *
     * @param npc
     *            the npc trying to target the player.
     * @param player
     *            the player that is being targeted by the NPC.
     * @return {@code true} if the player can be targeted, {@code false}
     *         otherwise.
     */
    private static boolean validate(Npc npc, Player player) {
        Position position = npc.getOriginalPosition();
        boolean wilderness = Location.inWilderness(npc) && Location.inWilderness(player);
        boolean tolerance = wilderness || npc.getDefinition().getCombatLevel() > 126 ? false : player.getTolerance().elapsed(
            TOLERANCE_SECONDS, TimeUnit.SECONDS);
        if (!AGGRESSIVE.contains(npc.getId()) && !wilderness || !npc.getDefinition().isAttackable())
            return false;
        if (!position.withinDistance(npc.getPosition(), TARGET_DISTANCE) || !position.withinDistance(player.getPosition(), TARGET_DISTANCE) && npc
            .getDefinition().isRetreats()) {
            npc.getMovementQueue().walk(position);
            npc.getCombatBuilder().reset();
            return false;
        }
        if (!Location.inMultiCombat(player) && player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked())
            return false;
        if (player.determineCombatLevel() > (npc.getDefinition().getCombatLevel() * 2) && !wilderness)
            return false;
        return !npc.getCombatBuilder().isAttacking() && !npc.getCombatBuilder().isBeingAttacked() && !tolerance;
    }
}
