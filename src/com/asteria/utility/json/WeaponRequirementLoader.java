package com.asteria.utility.json;

import java.util.Objects;

import com.asteria.game.character.player.content.Requirement;
import com.asteria.utility.JsonLoader;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all weapon level
 * requirements.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class WeaponRequirementLoader extends JsonLoader {

    /**
     * Creates a new {@link WeaponRequirementLoader}.
     */
    public WeaponRequirementLoader() {
        super("./data/json/equipment/weapon_requirements.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int id = reader.get("id").getAsInt();
        Requirement[] requirements = Objects.requireNonNull(builder.fromJson(reader.get("requirements"), Requirement[].class));
        Preconditions.checkState(requirements.length > 0);
        Requirement.REQUIREMENTS.put(id, requirements);
    }
}
