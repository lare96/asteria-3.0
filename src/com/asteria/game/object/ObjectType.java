package com.asteria.game.object;

/**
 * The enumerated type whose elements represent all of the object types.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Maxi <http://www.rune-server.org/members/maxi/>
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public enum ObjectType {
    STRAIGHT_WALL(0),
    DIAGONAL_CORNER_WALL(1),
    ENTIRE_WALL(2),
    WALL_CORNER(3),
    STRAIGHT_INSIDE_WALL_DECORATION(4),
    STRAIGHT_OUTSIDE_WALL_DECORATION(5),
    DIAGONAL_OUTSIDE_WALL_DECORATION(6),
    DIAGONAL_INSIDE_WALL_DECORATION(7),
    DIAGONAL_INTERIOR_WALL_DECORATION(8),
    DIAGONAL_WALL(9),
    DEFAULT(10),
    WALKABLE_DEFAULT(11),
    STRAIGHT_SLOPED_ROOF(12),
    DIAGONAL_SLOPED_ROOF(13),
    DIAGONAL_SLOPED_CONNECTING_ROOF(14),
    STRAIGHT_SLOPED_CORNER_CONNECTING_ROOF(15),
    STRAIGHT_SLOPED_CORNER_ROOF(16),
    STRAIGHT_FLAT_TOP_ROOF(17),
    STRAIGHT_BOTTOM_EDGE_ROOF(18),
    DIAGONAL_BOTTOM_EDGE_CONNECTING_ROOF(19),
    STRAIGHT_BOTTOM_EDGE_CONNECTING_ROOF(20),
    STRAIGHT_BOTTOM_EDGE_CONNECTING_CORNER_ROOF(21),
    GROUND_PROP(22);

    /**
     * The identification of this type.
     */
    private final int id;

    /**
     * Creates a new {@link ObjectType}.
     * 
     * @param id
     *            the identification of this type
     */
    private ObjectType(int id) {
        this.id = id;
    }

    /**
     * Gets the identification of this type.
     * 
     * @return the identification of this type.
     */
    public final int getId() {
        return id;
    }
}
