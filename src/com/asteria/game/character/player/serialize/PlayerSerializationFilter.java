package com.asteria.game.character.player.serialize;

import java.lang.reflect.Modifier;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * The serialization exclusion strategy that will filter certain fields from
 * being serialized by the player serializer.
 *
 * @author lare96 <http://github.com/lare96>
 */
public class PlayerSerializationFilter implements ExclusionStrategy {

    @Override
    public boolean shouldSkipClass(Class<?> c) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes attr) {
        return attr.hasModifier(Modifier.STATIC) || attr.hasModifier(Modifier.TRANSIENT) || attr.getAnnotation(SerializationExclude.class) != null;
    }
}
