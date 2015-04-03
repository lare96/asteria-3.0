package com.asteria.game.location;

/**
 * The location type that models any area in a circle or oval shape.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CircleLocation extends Location {

    /**
     * The center {@code X} coordinate.
     */
    private final int x;

    /**
     * The center {@code Y} coordinate.
     */
    private final int y;

    /**
     * The center {@code Z} coordinate.
     */
    private final int z;

    /**
     * The radius of this location from the center coordinates.
     */
    private final int radius;

    /**
     * Creates a new {@link CircleLocation}.
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
    public CircleLocation(int x, int y, int z, int radius) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + radius;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof CircleLocation))
            return false;
        CircleLocation other = (CircleLocation) obj;
        return other.x == x && other.y == y && other.z == z && other.radius == radius;
    }

    @Override
    public boolean inLocation(Position position) {
        if (position.getZ() != z)
            return false;
        return Math.pow((position.getX() - x), 2) + Math.pow((position.getY() - y), 2) <= Math.pow(radius, 2);
    }

    @Override
    public String toString() {
        return "CIRCLE_LOCATION[x= " + x + ", y= " + y + ", z= " + z + ", " +
                "radius= " + radius + "]";
    }

    /**
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     *
     * @return the copy of this instance that does not hold any references.
     */
    public CircleLocation copy() {
        return new CircleLocation(x, y, z, radius);
    }

    /**
     * Gets the center {@code X} coordinate.
     *
     * @return the center {@code X} coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the center {@code Y} coordinate.
     *
     * @return the center {@code Y} coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the center {@code Z} coordinate.
     *
     * @return the center {@code Z} coordinate.
     */
    public int getZ() {
        return z;
    }

    /**
     * Gets the radius of this location from the center coordinates.
     *
     * @return the radius of this location from the center coordinates.
     */
    public int getRadius() {
        return radius;
    }
}
