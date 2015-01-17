package com.asteria.utility.json;

import java.util.Objects;

import com.asteria.game.character.player.content.WeaponAnimation;
import com.asteria.utility.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all weapon animations.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class WeaponAnimationLoader extends JsonLoader {

    /**
     * Creates a new {@link WeaponAnimationLoader}.
     */
    public WeaponAnimationLoader() {
        super("./data/json/equipment/weapon_animations.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int id = reader.get("id").getAsInt();
        WeaponAnimation animation = Objects.requireNonNull(builder.fromJson(reader.get("animation"), WeaponAnimation.class));
        WeaponAnimation.ANIMATIONS.put(id, animation);
    }
}
