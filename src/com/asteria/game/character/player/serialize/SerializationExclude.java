package com.asteria.game.character.player.serialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation that signifies to the player serializer that the underlying
 * field should not be serialized.
 * 
 * @author lare96 <http://github.com/lare96>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SerializationExclude {

}
