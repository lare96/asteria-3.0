package com.asteria.game;

import java.util.Objects;

import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.ItemNode;
import com.asteria.game.location.Position;
import com.asteria.game.object.ObjectNode;

/**
 * The parent class that represents anything that can be interacted with. This
 * includes {@link ItemNode}s, {@link ObjectNode}s, {@link Player}s, and
 * {@link Npc}s.
 *
 * @author lare96 <http://github.com/lare96>
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
        this.position = position.copy();
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