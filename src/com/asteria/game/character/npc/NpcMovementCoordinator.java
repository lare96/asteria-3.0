package com.asteria.game.character.npc;

import com.asteria.utility.RandomGen;

/**
 * The movement coordinator that makes all {@link Npc}s pseudo-randomly move
 * within a radius of their original positions.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcMovementCoordinator {

    /**
     * The NPC that this coordinator is dedicated to.
     */
    private final Npc npc;

    /**
     * The random generator instance that will generate random numbers.
     */
    private static RandomGen random = new RandomGen();

    /**
     * Determines if the NPC is flagged to walk randomly.
     */
    private boolean coordinate;

    /**
     * The radius that the NPC will be bounded to.
     */
    private int radius;

    /**
     * The current coordinate state of this NPC.
     */
    private State state = State.HOME;

    /**
     * Creates a new {@link NpcMovementCoordinator}.
     *
     * @param npc
     *            the NPC that this coordinator is dedicated to.
     */
    public NpcMovementCoordinator(Npc npc) {
        this.npc = npc;
    }

    /**
     * The sequencer for this coordinator that forces this NPC to move.
     */
    public void sequence() {
        if (!coordinate || npc.getCombatBuilder().isAttacking() || npc.getCombatBuilder().isBeingAttacked())
            return;
        if (random.inclusive(13) == 5 && npc.getMovementQueue().isMovementDone()) {
            switch (state) {
            case HOME:
                npc.getMovementQueue().walk(npc.getPosition().copy().random(radius));
                state = State.AWAY;
                break;
            case AWAY:
                npc.getMovementQueue().walk(npc.getOriginalPosition());
                state = State.HOME;
                break;
            }
        }
    }

    /**
     * Determines if the NPC is flagged to walk randomly.
     *
     * @return {@code true} if the NPC will walk randomly, {@code false}
     *         otherwise.
     */
    public boolean isCoordinate() {
        return coordinate;
    }

    /**
     * Sets the value for {@link NpcMovementCoordinator#coordinate}.
     *
     * @param coordinate
     *            the new value to set.
     */
    public void setCoordinate(boolean coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Gets the radius that the NPC will be bounded to.
     *
     * @return the radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Sets the value for {@link NpcMovementCoordinator#radius}.
     *
     * @param radius
     *            the new value to set.
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * The enumerated type whose elements represent the movement coordinate
     * states for NPCs.
     *
     * @author lare96 <http://github.com/lare96>
     */
    private enum State {
        HOME,
        AWAY
    }
}