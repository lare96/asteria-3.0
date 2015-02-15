package com.asteria.game.plugin;

/**
 * The exception thrown when a {@link PluginSignature} is invalid in a
 * {@link PluginListener}.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class PluginSignatureException extends RuntimeException {

    /**
     * The serial version UID for serialization.
     */
    private static final long serialVersionUID = -64840505315888134L;

    /**
     * Creates a new {@link PluginSignatureException}.
     * 
     * @param clazz
     *            the class that the plugin signature is missing from.
     */
    public PluginSignatureException(Class<?> clazz) {
        super("Plugin signature invalid or missing in " + clazz.getSimpleName() + ".class");
    }
}
