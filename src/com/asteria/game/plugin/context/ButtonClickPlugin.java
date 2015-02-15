package com.asteria.game.plugin.context;

import com.asteria.game.plugin.PluginContext;

/**
 * The plugin context for the clicking buttons packet.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class ButtonClickPlugin implements PluginContext {

    /**
     * The identifier for the button that was clicked.
     */
    private final int id;

    /**
     * Creates a new {@link ButtonClickPlugin}.
     * 
     * @param id
     *            the identifier for the button that was clicked.
     */
    public ButtonClickPlugin(int id) {
        this.id = id;
    }

    /**
     * Gets the identifier for the button that was clicked.
     * 
     * @return the identifier for the button.
     */
    public int getId() {
        return id;
    }
}
