package com.asteria.game.location;

import java.util.Arrays;

import com.asteria.game.Node;

/**
 * The class that represents an area located anywhere in the world.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class Location {

    /**
     * An array of locations representing the wilderness areas.
     */
    public static final Location[] WILDERNESS = { new SquareLocation(2941, 3518, 3392, 3966, 0) };

    /**
     * An array of locations representing multi-combat areas.
     */
    public static final Location[] MULTIPLE_COMBAT = {};

    /**
     * Determines if the specified position is in this location.
     *
     * @param position
     *            the position to determine if in this location.
     * @return {@code true} if the position is in this location, {@code false}
     *         otherwise.
     */
    public abstract boolean inLocation(Position position);

    /**
     * Generates a pseudo-random position from within this location.
     *
     * @return the random position that was generated.
     * @throws UnsupportedOperationException
     *             by default, if this method is not overridden.
     */
    public Position random() {
        throw new UnsupportedOperationException("No algorithm to generate a " + "pseudo-random position from this location!");
    }

    /**
     * Determines if the specified position is in <b>all</b> of the specified
     * locations.
     *
     * @param position
     *            the position to determine if in the locations.
     * @param location
     *            the locations to determine if in this position.
     * @return {@code true} if the position is in all of these locations,
     *         {@code false} otherwise.
     */
    public static boolean inAllLocation(Position position, Location... location) {
        return Arrays.stream(location).allMatch(l -> l.inLocation(position));
    }

    /**
     * Determines if the specified position is in <b>any</b> of the specified
     * locations.
     *
     * @param position
     *            the position to determine if in the locations.
     * @param location
     *            the locations to determine if in this position.
     * @return {@code true} if the position is in any of these locations,
     *         {@code false} otherwise.
     */
    public static boolean inAnyLocation(Position position, Location... location) {
        return Arrays.stream(location).anyMatch(l -> l.inLocation(position));
    }

    /**
     * Determines if {@code node} is in any of the multicombat locations.
     *
     * @param node
     *            the node to determine if in the locations.
     * @return {@code true} if the node is in any of these locations,
     *         {@code false} otherwise.
     */
    public static boolean inMultiCombat(Node node) {
        return Location.inAnyLocation(node.getPosition(), Location.MULTIPLE_COMBAT);
    }

    /**
     * Determines if {@code node} is in any of the wilderness locations.
     *
     * @param node
     *            the node to determine if in the locations.
     * @return {@code true} if the node is in any of these locations,
     *         {@code false} otherwise.
     */
    public static boolean inWilderness(Node node) {
        return Location.inAnyLocation(node.getPosition(), Location.WILDERNESS);
    }
}
