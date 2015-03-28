package com.asteria.game.character.player.content;

import com.google.common.base.Preconditions;

/**
 * The container class that represents a forced movement.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ForcedMovement {

    /**
     * The amount to move either east or west.
     */
    private final int amountX;

    /**
     * The amount to move either north or south.
     */
    private final int amountY;

    /**
     * The direction to turn when moving, the direction that the protocol
     * recognizes are listed below.
     * <p>
     * <p>
     * <table BORDER CELLPADDING=3 CELLSPACING=1>
     * <tr>
     * <td></td>
     * <td ALIGN=CENTER><em>Direction value</em></td>
     * </tr>
     * <tr>
     * <td>NORTH</td>
     * <td>0</td>
     * </tr>
     * <tr>
     * <td>EAST</td>
     * <td>1</td>
     * </tr>
     * <tr>
     * <td>SOUTH</td>
     * <td>2</td>
     * </tr>
     * <tr>
     * <td>WEST</td>
     * <td>3</td>
     * </tr>
     * </table>
     */
    private final int direction;

    /**
     * Creates a new {@link ForcedMovement}.
     *
     * @param amountX
     *         the amount to move either east or west.
     * @param amountY
     *         the amount to move either north or south.
     * @param direction
     *         the direction to turn when moving.
     */
    public ForcedMovement(int amountX, int amountY, int direction) {
        Preconditions.checkArgument(direction >= 0 && direction <= 3);
        this.amountX = amountX;
        this.amountY = amountY;
        this.direction = direction;
    }

    /**
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy is <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     *
     * @return a reference-free copy of this instance.
     */
    public ForcedMovement copy() {
        return new ForcedMovement(amountX, amountY, direction);
    }

    /**
     * Gets the amount to move either east or west.
     *
     * @return the {@code X} amount to move.
     */
    public int getAmountX() {
        return amountX;
    }

    /**
     * Gets the amount to move either north or south.
     *
     * @return the {@code Y} amount to move.
     */
    public int getAmountY() {
        return amountY;
    }

    /**
     * Gets the direction to turn when moving.
     *
     * @return the direction to turn.
     */
    public int getDirection() {
        return direction;
    }
}
