package com.asteria.game;

import java.util.Objects;

import com.asteria.game.location.Position;

/**
 * The parent class that represents anything that can be interacted with.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public abstract class Node {

    /**
     * The position of this node in the world.
     */
    private Position position;

    /**
     * The type of node that this node is.
     */
    private final NodeType type;

    /**
     * Determines if this node has been registered or not.
     */
    private boolean registered;

    /**
     * Creates a new {@link Node}.
     * 
     * @param position
     *            the position of this node in the world.
     * @param type
     *            the type of node that this node is.
     */
    public Node(Position position, NodeType type) {
        this.position = Objects.requireNonNull(position.copy());
        this.type = Objects.requireNonNull(type);
    }

    /**
     * The method executed when this node has been successfully registered.
     */
    public abstract void create();

    /**
     * The method executed when this node has been successfully unregistered.
     */
    public abstract void dispose();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Node))
            return false;
        Node other = (Node) obj;
        return position.equals(other.position) && registered == other.registered && type == other.type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + position.hashCode();
        result = prime * result + (registered ? 1231 : 1237);
        result = prime * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "NODE[type= " + type + ", registered= " + registered + "]";
    }

    /**
     * Gets the position of this node in the world.
     * 
     * @return the position of this node in the world.
     */
    public final Position getPosition() {
        return position;
    }

    /**
     * Sets the value for {@link Node#position}.
     * 
     * @param position
     *            the new value to set.
     */
    public final void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets the type of node that this node is.
     * 
     * @return the type of node that this node is.
     */
    public final NodeType getType() {
        return type;
    }

    /**
     * Determines if this node has been registered or not.
     * 
     * @return {@code true} if this node has been registered, {@code false}
     *         otherwise.
     */
    public final boolean isRegistered() {
        return registered;
    }

    /**
     * Sets the value for {@link Node#registered}.
     * 
     * @param registered
     *            the new value to set.
     */
    public final void setRegistered(boolean registered) {
        this.registered = registered;
    }
}