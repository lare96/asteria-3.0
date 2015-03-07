package com.asteria.game.plugin.context;

import com.asteria.game.location.Position;
import com.asteria.game.plugin.PluginContext;

/**
 * The plugin context for the second object click packet.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class ObjectSecondClickPlugin implements PluginContext {

    /**
     * The identifier for the object that was clicked.
     */
    private final int id;

    /**
     * The position of the object that was clicked.
     */
    private final Position position;

    /**
     * The size of the object that was clicked.
     */
    private final int size;

    /**
     * Creates a new {@link ObjectSecondClickPlugin}.
     * 
     * @param id
     *            the identifier for the object that was clicked.
     * @param position
     *            the position of the object that was clicked.
     * @param size
     *            the size of the object that was clicked.
     */
    public ObjectSecondClickPlugin(int id, Position position, int size) {
        this.id = id;
        this.position = position;
        this.size = size;
    }

    /**
     * Gets the identifier for the object that was clicked.
     * 
     * @return the object identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the position of the object that was clicked.
     * 
     * @return the position of the object.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gets the size of the object that was clicked.
     * 
     * @return the size of the object.
     */
    public int getSize() {
        return size;
    }
}
