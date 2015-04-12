package com.asteria.game.location;

import com.asteria.utility.RandomGen;

/**
 * The container class that represents a coordinate anywhere in the world.
 *
 * @author lare96 <http://github.com/lare96>
 * @author blakeman8192
 */
public class Position {

    /**
     * The {@code X} coordinate.
     */
    private int x;

    /**
     * The {@code Y} coordinate.
     */
    private int y;

    /**
     * The {@code Z} coordinate.
     */
    private int z;

    /**
     * Creates a new {@link Position}.
     *
     * @param x
     *            the {@code X} coordinate.
     * @param y
     *            the {@code Y} coordinate.
     * @param z
     *            the {@code Z} coordinate.
     */
    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a new {@link Position} with the {@code Z} coordinate value as
     * {@code 0}.
     *
     * @param x
     *            the {@code X} coordinate.
     * @param y
     *            the {@code Y} coordinate.
     */
    public Position(int x, int y) {
        this(x, y, 0);
    }

    @Override
    public String toString() {
        return "POSITION[x= " + x + ", y= " + y + ", z= " + z + "]";
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Position))
            return false;
        Position other = (Position) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        return true;
    }

    /**
     * Returns the delta coordinates. Note that the returned position is not an
     * actual position, instead it's values represent the delta values between
     * the two arguments.
     *
     * @param a
     *            the first position.
     * @param b
     *            the second position.
     * @return the delta coordinates contained within a position.
     */
    public static Position delta(Position a, Position b) {
        return new Position(b.x - a.x, b.y - a.y);
    }

    /**
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     *
     * @return the copy of this instance that does not hold any references.
     */
    public Position copy() {
        return new Position(x, y, z);
    }

    /**
     * Determines if this position is viewable from {@code other}.
     *
     * @param other
     *            the other position to determine if viewable from.
     * @return {@code true} if this position is viewable, {@code false}
     *         otherwise.
     */
    public final boolean isViewableFrom(Position other) {
        if (this.getZ() != other.getZ())
            return false;
        Position p = Position.delta(this, other);
        return p.x <= 14 && p.x >= -15 && p.y <= 14 && p.y >= -15;
    }

    /**
     * Determines if this position is within {@code amount} distance of
     * {@code other}.
     *
     * @param other
     *            the position to check the distance for.
     * @param amount
     *            the distance to check.
     * @return {@code true} if this position is within the distance,
     *         {@code false} otherwise.
     */
    public final boolean withinDistance(Position other, int amount) {
        if (this.z != other.z)
            return false;
        return Math.abs(other.x - this.x) <= amount && Math.abs(other.y - this.y) <= amount;
    }

    /**
     * Increments the {@code X}, {@code Y}, and {@code Z} coordinate values
     * within this container by {@code amountX}, {@code amountY}, and
     * {@code amountZ}.
     *
     * @param amountX
     *            the amount to increment the {@code X} coordinate by.
     * @param amountY
     *            the amount to increment the {@code Y} coordinate by.
     * @param amountZ
     *            the amount to increment the {@code Z} coordinate by.
     * @return an instance of this position.
     */
    public final Position move(int amountX, int amountY, int amountZ) {
        this.x += amountX;
        this.y += amountY;
        this.z += amountZ;
        return this;
    }

    /**
     * Increments the {@code X} and {@code Y} coordinate values within this
     * container by {@code amountX} and {@code amountY}.
     *
     * @param amountX
     *            the amount to increment the {@code X} coordinate by.
     * @param amountY
     *            the amount to increment the {@code Y} coordinate by.
     * @return an instance of this position.
     */
    public final Position move(int amountX, int amountY) {
        return move(amountX, amountY, 0);
    }

    /**
     * Increments the {@code X} and {@code Y} coordinate values within this
     * container by random amounts positive and negative of {@code amount}.
     *
     * @return an instance of this position.
     */
    public final Position random(int amount) {
        RandomGen random = new RandomGen();
        int x = random.inclusive(amount);
        int y = random.inclusive(amount);
        switch (random.inclusive(3)) {
        case 1:
            return move(-x, -y);
        case 2:
            return move(-x, y);
        case 3:
            return move(x, -y);
        default:
            return move(x, y);
        }
    }

    /**
     * Gets the {@code X} coordinate of the region containing this position.
     *
     * @return the {@code X} coordinate of the region.
     */
    public final int getRegionX() {
        return (x >> 3) - 6;
    }

    /**
     * Gets the {@code Y} coordinate of the region containing this position.
     *
     * @return the {@code Y} coordinate of the region
     */
    public final int getRegionY() {
        return (y >> 3) - 6;
    }

    /**
     * Gets the local {@code X} coordinate relative to {@code base}.
     *
     * @param base
     *            the relative base position.
     * @return the local {@code X} coordinate.
     */
    public final int getLocalX(Position base) {
        return x - 8 * base.getRegionX();
    }

    /**
     * Gets the local {@code Y} coordinate relative to {@code base}.
     *
     * @param base
     *            the relative base position.
     * @return the local {@code Y} coordinate.
     */
    public final int getLocalY(Position base) {
        return y - 8 * base.getRegionY();
    }

    /**
     * Gets the local {@code X} coordinate relative to this position.
     *
     * @return the local {@code X} coordinate.
     */
    public final int getLocalX() {
        return getLocalX(this);
    }

    /**
     * Gets the local {@code Y} coordinate relative to this Position.
     *
     * @return the local {@code Y} coordinate.
     */
    public final int getLocalY() {
        return getLocalY(this);
    }

    /**
     * Gets the {@code X} region chunk relative to this position.
     *
     * @return the {@code X} region chunk.
     */
    public final int getChunkX() {
        return (x >> 6);
    }

    /**
     * Gets the {@code Y} region chunk relative to this position.
     *
     * @return the {@code Y} region chunk.
     */
    public final int getChunkY() {
        return (y >> 6);
    }

    /**
     * Gets the region identification relative to this position.
     *
     * @return the region identification.
     */
    public final int getRegion() {
        return ((getChunkX() << 8) + getChunkY());
    }

    /**
     * Gets the {@code X} coordinate.
     *
     * @return the {@code X} coordinate.
     */
    public final int getX() {
        return x;
    }

    /**
     * Sets the {@code X} coordinate.
     *
     * @param x
     *            the new {@code X} coordinate.
     */
    public final void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the {@code Y} coordinate.
     *
     * @return the {@code Y} coordinate.
     */
    public final int getY() {
        return y;
    }

    /**
     * Sets the {@code Y} coordinate.
     *
     * @param y
     *            the new {@code Y} coordinate.
     */
    public final void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the {@code Z} coordinate.
     *
     * @return the {@code Z} coordinate.
     */
    public final int getZ() {
        return z;
    }

    /**
     * Sets the {@code Z} coordinate.
     *
     * @param z
     *            the new {@code Z} coordinate.
     */
    public final void setZ(int z) {
        this.z = z;
    }
}
