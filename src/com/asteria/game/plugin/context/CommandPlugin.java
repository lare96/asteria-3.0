package com.asteria.game.plugin.context;

import com.asteria.game.plugin.PluginContext;

/**
 * The plugin context for the player command packet.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CommandPlugin implements PluginContext {

    /**
     * The command text broken up between spaces.
     */
    private final String[] text;

    /**
     * Creates a new {@link CommandPlugin}.
     *
     * @param text
     *         the command text broken up between spaces.
     */
    public CommandPlugin(String[] text) {
        this.text = text;
    }

    /**
     * Gets the command text broken up between spaces.
     *
     * @return the command text.
     */
    public String[] getText() {
        return text;
    }
}
