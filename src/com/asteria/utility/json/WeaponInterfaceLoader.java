package com.asteria.utility.json;

import java.util.Objects;

import com.asteria.game.character.player.content.WeaponInterface;
import com.asteria.utility.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all weapon interfaces.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class WeaponInterfaceLoader extends JsonLoader {

    /**
     * Creates a new {@link WeaponInterfaceLoader}.
     */
    public WeaponInterfaceLoader() {
        super("./data/json/equipment/weapon_interfaces.json");

    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int id = reader.get("id").getAsInt();
        WeaponInterface interfaces = Objects.requireNonNull(builder.fromJson(reader.get("interface"), WeaponInterface.class));
        WeaponInterface.INTERFACES.put(id, interfaces);
    }
}
