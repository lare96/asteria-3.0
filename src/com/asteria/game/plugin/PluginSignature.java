package com.asteria.game.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation serves as a container for the context that a plugin listener
 * will be executed in. The bootstrap grabs the {@link PluginContext} from this
 * container and maps a collection of plugin listeners to it. Every plugin
 * listener must have this annotation as its header or a
 * {@link PluginSignatureException} will be thrown.
 * 
 * @author lare96 <http://github.com/lare96>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PluginSignature {

    /**
     * The {@link PluginContext} that represents the context plugin listeners
     * will be stored and executed in.
     * 
     * @return the plugin signature.
     */
    public Class<? extends PluginContext> value();
}
