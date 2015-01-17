/* Copyright (c) 2009 Graham Edgecombe, Blake Beaupain and Brett Russell
 *
 * More information about Hyperion may be found on this website:
 *    http://hyperion.grahamedgecombe.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.asteria.game.location;

/**
 * Represents a tile to copy in the palette.
 * 
 * @author Graham Edgecombe
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class PaletteTile {

    /**
     * X coordinate.
     */
    private final int x;

    /**
     * Y coordinate.
     */
    private final int y;

    /**
     * Z coordinate.
     */
    private final int z;

    /**
     * Rotation.
     */
    private final int rot;

    /**
     * Creates a tile.
     * 
     * @param x
     *            The x coordinate.
     * @param y
     *            The y coordinate.
     */
    public PaletteTile(int x, int y) {
        this(x, y, 0);
    }

    /**
     * Creates a tile.
     * 
     * @param x
     *            The x coordinate.
     * @param y
     *            The y coordinate.
     * @param z
     *            The z coordinate.
     */
    public PaletteTile(int x, int y, int z) {
        this(x, y, z, Palette.DIRECTION_NORMAL);
    }

    /**
     * Creates a tile.
     * 
     * @param x
     *            The x coordinate.
     * @param y
     *            The y coordinate.
     * @param z
     *            The z coordinate.
     * @param rot
     *            The rotation.
     */
    public PaletteTile(int x, int y, int z, int rot) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rot = rot;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + rot;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PaletteTile))
            return false;
        PaletteTile other = (PaletteTile) obj;
        if (rot != other.rot)
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        return true;
    }

    /**
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     * 
     * @return the copy of this instance that does not hold any references.
     */
    public PaletteTile copy() {
        return new PaletteTile(x, y, z, rot);
    }

    /**
     * Gets the x coordinate.
     * 
     * @return The x coordinate.
     */
    public int getX() {
        return x / 8;
    }

    /**
     * Gets the y coordinate.
     * 
     * @return The y coordinate.
     */
    public int getY() {
        return y / 8;
    }

    /**
     * Gets the z coordinate.
     * 
     * @return The z coordinate.
     */
    public int getZ() {
        return z % 4;
    }

    /**
     * Gets the rotation.
     * 
     * @return The rotation.
     */
    public int getRotation() {
        return rot % 4;
    }
}