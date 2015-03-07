package com.asteria.game.character.npc;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.asteria.game.character.player.Player;
import com.asteria.game.location.Location;
import com.asteria.game.location.Position;

/**
 * The static utility class that handles the behavior of aggressive NPCs within
 * a certain radius of players.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcAggression {

    /**
     * The absolute distance that players must be within to be targeted by
     * aggressive NPCs.
     */
    private static final int TARGET_DISTANCE = 6;

    /**
     * The time that has to be spent in a region before NPCs stop acting
     * aggressive.
     */
    private static final int TOLERANCE_SECONDS = 600;

    /**
     * The hash collection the holds the npc identifiers of aggressive NPCs.
     */
    public static final Set<Integer> AGGRESSIVE = new HashSet<>();

    /**
     * The sequencer that will prompt all aggressive NPCs to attack
     * {@code player}.
     * 
     * @param player
     *            the player that will be targeted by aggressive NPCs.
     */
    public static void sequence(Player player) {
        for (Npc npc : player.getLocalNpcs()) {
            if (validate(npc, player)) {
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
        if (!AGGRESSIVE.contains(npc.getId()))
            return false;
        if (!Location.inMultiCombat(player) && player.getCombatBuilder().isAttacking() || player.getCombatBuilder()
            .isBeingAttacked())
            return false;
        if (player.determineCombatLevel() > (npc.getDefinition().getCombatLevel() * 2) && !Location.inWilderness(player))
            return false;
        return position.withinDistance(player.getPosition(), TARGET_DISTANCE) && !npc.getCombatBuilder().isAttacking() && !npc
            .getCombatBuilder().isBeingAttacked() && !player.getTolerance().elapsed(TOLERANCE_SECONDS, TimeUnit.SECONDS);
    }
}
