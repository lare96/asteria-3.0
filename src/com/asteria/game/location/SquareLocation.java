package com.asteria.game.location;

import com.asteria.utility.RandomGen;

/**
 * The location type that models any area in a square or rectangle shape.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class SquareLocation extends Location {

    /**
     * The south-west {@code X} corner of the box.
     */
    private final int swX;

    /**
     * The south-west {@code Y} corner of the box.
     */
    private final int swY;

    /**
     * The north-east {@code X} corner of the box.
     */
    private final int neX;

    /**
     * The north-east {@code Y} corner of the box.
     */
    private final int neY;

    /**
     * The {@code Z} level of the box.
     */
    private final int z;

    /**
     * Creates a new {@link SquareLocation}.
     *
     * @param swX
     *         the south-west {@code X} corner of the box.
     * @param swY
     *         the south-west {@code Y} corner of the box.
     * @param neX
     *         the north-east {@code X} corner of the box.
     * @param neY
     *         the north-east {@code Y} corner of the box.
     * @param z
     *         the {@code Z} level of the box.
     */
    public SquareLocation(int swX, int swY, int neX, int neY, int z) {
        this.swX = swX;
        this.swY = swY;
        this.neX = neX;
        this.neY = neY;
        this.z = z;
    }

    /**
     * Creates a new {@link SquareLocation} from the center position and
     * radius.
     *
     * @param x
     *         the center {@code X} coordinate.
     * @param y
     *         the center {@code Y} coordinate.
     * @param z
     *         the center {@code Z} coordinate.
     * @param radius
     *         the radius of this location from the center coordinates.
     */
    public SquareLocation(int x, int y, int z, int radius) {
        this(x - radius, y - radius, x + radius, y + radius, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof SquareLocation))
            return false;
        SquareLocation other = (SquareLocation) obj;
        return other.swX == swX && other.swY == swY && other.neX == neX &&
                other.neY == neY && other.z == z;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + neX;
        result = prime * result + neY;
        result = prime * result + swX;
        result = prime * result + swY;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean inLocation(Position position) {
        if (position.getZ() != z)
            return false;
        return position.getX() >= swX && position.getX() <= neX && position
                .getY() >= swY && position.getY() <= neY;
    }

    @Override
    public String toString() {
        return "SQUARE_LOCATION[swX= " + swX + ", swY= " + swY + ", neX= " +
                neX + ", neY= " + neY + "]";
    }

    @Override
    public Position random() {
        RandomGen r = new RandomGen();
        int x = r.nextInt((Math.max(swX, neX) - Math.min(swX, neX) + 1) +
                Math.min(swX, neX));
        int y = r.nextInt(Math.max(swY, neY) - Math.min(swY, neY) + 1) + Math
                .min(swY, neY);
        return new Position(x, y, z);
    }

    /**
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     *
     * @return the copy of this instance that does not hold any references.
     */
    public SquareLocation copy() {
        return new SquareLocation(swX, swY, neX, neY, z);
    }

    /**
     * Gets the south-west {@code X} corner of the box.
     *
     * @return the {@code X} corner of the box.
     */
    public int getSwX() {
        return swX;
    }

    /**
     * Gets the south-west {@code Y} corner of the box.
     *
     * @return the {@code Y} corner of the box.
     */
    public int getSwY() {
        return swY;
    }

    /**
     * Gets the north-east {@code X} corner of the box.
     *
     * @return the {@code X} corner of the box.
     */
    public int getNeX() {
        return neX;
    }

    /**
     * Gets the north-east {@code Y} corner of the box.
     *
     * @return the {@code Y} corner of the box.
     */
    public int getNeY() {
        return neY;
    }

    /**
     * Gets the {@code Z} level of the box.
     *
     * @return the {@code Z} level of the box.
     */
    public int getZ() {
        return z;
    }
}
