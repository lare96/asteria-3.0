package com.asteria.game.character.player.content;

import java.util.stream.IntStream;

import com.asteria.game.character.Flag;
import com.asteria.game.character.player.Player;
import com.asteria.game.location.Position;

/**
 * The container class that handles the opening, closing, and navigation of
 * viewing orbs.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class ViewingOrb {

    /**
     * The player that is viewing the orb.
     */
    private final Player player;

    /**
     * The starting position of the player viewing the orb.
     */
    private final Position start;

    /**
     * The centre position corresponding to the viewing orb.
     */
    private final Position centre;

    /**
     * The north-west position corresponding to the viewing orb.
     */
    private final Position northWest;

    /**
     * The north-east position corresponding to the viewing orb.
     */
    private final Position northEast;

    /**
     * The south-west position corresponding to the viewing orb.
     */
    private final Position southWest;

    /**
     * The south-east position corresponding to the viewing orb.
     */
    private final Position southEast;

    /**
     * Creates a new {@link ViewingOrb}.
     * 
     * @param player
     *            the player that is viewing the orb.
     * @param centre
     *            the centre position corresponding to the viewing orb.
     * @param northWest
     *            the north-west position corresponding to the viewing orb.
     * @param northEast
     *            the north-east position corresponding to the viewing orb.
     * @param southWest
     *            the south-west position corresponding to the viewing orb.
     * @param southEast
     *            the south-east position corresponding to the viewing orb.
     */
    public ViewingOrb(Player player, Position centre, Position northWest, Position northEast, Position southWest,
        Position southEast) {
        this.player = player;
        this.start = player.getPosition().copy();
        this.centre = centre;
        this.northWest = northWest;
        this.northEast = northEast;
        this.southWest = southWest;
        this.southEast = southEast;
    }

    /**
     * Opens the viewing orb navigation interface in the sidebar.
     */
    public void open() {
        IntStream.rangeClosed(0, 13).filter(v -> v != 7 && v != 10).forEach(v -> player.getEncoder().sendSidebarInterface(v, -1));
        player.getEncoder().sendSidebarInterface(10, 3209);
        player.getEncoder().sendForceTab(10);
        player.getEncoder().sendString("@yel@Centre", 15239);
        player.getEncoder().sendString("@yel@North-West", 15240);
        player.getEncoder().sendString("@yel@North-East", 15241);
        player.getEncoder().sendString("@yel@South-East", 15242);
        player.getEncoder().sendString("@yel@South-West", 15243);
        player.getMovementQueue().setLockMovement(true);
        player.setVisible(false);
        player.setDisabled(true);
        player.getEncoder().sendMinimapState(2);
        player.setPlayerNpc(2982);
        player.getFlags().set(Flag.APPEARANCE);
        move("Centre", 15239, centre);
    }

    /**
     * Closes the viewing orb navigation interface in the sidebar and teleports
     * the player back to the starting position.
     */
    public void close() {
        player.getEncoder().sendSidebarInterface(1, 3917);
        player.getEncoder().sendSidebarInterface(2, 638);
        player.getEncoder().sendSidebarInterface(3, 3213);
        player.getEncoder().sendSidebarInterface(4, 1644);
        player.getEncoder().sendSidebarInterface(5, 5608);
        player.getEncoder().sendSidebarInterface(6, player.getSpellbook().getId());
        player.getEncoder().sendSidebarInterface(8, 5065);
        player.getEncoder().sendSidebarInterface(9, 5715);
        player.getEncoder().sendSidebarInterface(10, 2449);
        player.getEncoder().sendSidebarInterface(11, 904);
        player.getEncoder().sendSidebarInterface(12, 147);
        player.getEncoder().sendSidebarInterface(13, 962);
        player.getEncoder().sendSidebarInterface(0, 2423);
        player.getMovementQueue().setLockMovement(false);
        player.setVisible(true);
        player.setDisabled(false);
        player.setPlayerNpc(-1);
        player.getFlags().set(Flag.APPEARANCE);
        player.getEncoder().sendMinimapState(0);
        player.move(start);
    }

    /**
     * Moves the player to {@code position} and updates the navigation interface
     * with the position that the player is being moved to.
     * 
     * @param positionName
     *            the name of the position being moved to.
     * @param positionLineId
     *            the interface string id of the position being moved to.
     * @param position
     *            the position being moved to.
     */
    public void move(String positionName, int positionLineId, Position position) {
        if (position.equals(player.getPosition()))
            return;
        player.getEncoder().sendString("@yel@Centre", 15239);
        player.getEncoder().sendString("@yel@North-West", 15240);
        player.getEncoder().sendString("@yel@North-East", 15241);
        player.getEncoder().sendString("@yel@South-East", 15242);
        player.getEncoder().sendString("@yel@South-West", 15243);
        player.getEncoder().sendString("@whi@" + positionName, positionLineId);
        player.move(position);
    }

    /**
     * Gets the starting position of the player viewing the orb.
     * 
     * @return the starting position.
     */
    public Position getStart() {
        return start;
    }

    /**
     * Gets the centre position corresponding to the viewing orb.
     * 
     * @return the centre position.
     */
    public Position getCentre() {
        return centre;
    }

    /**
     * Gets the north-west position corresponding to the viewing orb.
     * 
     * @return the north-west position.
     */
    public Position getNorthWest() {
        return northWest;
    }

    /**
     * Gets the north-east position corresponding to the viewing orb.
     * 
     * @return the north-east position.
     */
    public Position getNorthEast() {
        return northEast;
    }

    /**
     * Gets the south-west position corresponding to the viewing orb.
     * 
     * @return the south-west position.
     */
    public Position getSouthWest() {
        return southWest;
    }

    /**
     * Gets the south-east position corresponding to the viewing orb.
     * 
     * @return the south-east position.
     */
    public Position getSouthEast() {
        return southEast;
    }
}
