package com.asteria.game.object;

import java.util.Objects;

import com.asteria.game.Node;
import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.asteria.game.location.Position;

/**
 * The node that represents an object anywhere in the world.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ObjectNode extends Node {

    /**
     * The identification of this object.
     */
    private final int id;

    /**
     * The direction this object is facing.
     */
    private final ObjectDirection direction;

    /**
     * The type of object that this is.
     */
    private final ObjectType objectType;

    /**
     * Creates a new {@link ObjectNode}.
     *
     * @param id
     *         the identification of the object.
     * @param position
     *         the position of this object in the world.
     * @param direction
     *         the direction this object is facing.
     * @param type
     *         the type of object that this is.
     */
    public ObjectNode(int id, Position position, ObjectDirection direction,
                      ObjectType type) {
        super(position, NodeType.OBJECT);
        this.id = id;
        this.direction = Objects.requireNonNull(direction);
        this.objectType = Objects.requireNonNull(type);
    }

    /**
     * Creates a new {@link ObjectNode} with the default {@code objectType}.
     *
     * @param id
     *         the identification of the object.
     * @param position
     *         the position of this object in the world.
     * @param direction
     *         the direction this object is facing.
     */
    public ObjectNode(int id, Position position, ObjectDirection direction) {
        this(id, position, direction, ObjectType.DEFAULT);
    }

    @Override
    public void create() {
        World.getPlayers().forEach(p -> {
            if (super.getPosition().withinDistance(p.getPosition(), 60)) {
                p.getEncoder().sendObject(this);
            }
        });
    }

    @Override
    public void dispose() {
        World.getPlayers().forEach(p -> p.getEncoder().sendRemoveObject(super
                .getPosition()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((direction == null) ? 0 : direction
                .hashCode());
        result = prime * result + id;
        result = prime * result + ((objectType == null) ? 0 : objectType
                .hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof ObjectNode))
            return false;
        ObjectNode other = (ObjectNode) obj;
        if (direction != other.direction)
            return false;
        if (id != other.id)
            return false;
        if (objectType != other.objectType)
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
    public ObjectNode copy() {
        return new ObjectNode(id, super.getPosition(), direction, objectType);
    }

    /**
     * Gets the identification of this object.
     *
     * @return the identification.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the direction this object is facing.
     *
     * @return the direction.
     */
    public ObjectDirection getDirection() {
        return direction;
    }

    /**
     * Gets the type of object that this is.
     *
     * @return the type of object.
     */
    public ObjectType getObjectType() {
        return objectType;
    }
}
