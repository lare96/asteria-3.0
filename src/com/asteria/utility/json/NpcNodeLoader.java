package com.asteria.utility.json;

import java.util.Objects;

import com.asteria.game.World;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.location.Position;
import com.asteria.utility.JsonLoader;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all npc nodes.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class NpcNodeLoader extends JsonLoader {

    /**
     * Creates a new {@link NpcNodeLoader}.
     */
    public NpcNodeLoader() {
        super("./data/json/npcs/npc_nodes.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int id = reader.get("id").getAsInt();
        Position position = Objects.requireNonNull(builder.fromJson(reader.get("position").getAsJsonObject(), Position.class));
        boolean coordinate = reader.get("random-walk").getAsBoolean();
        int radius = reader.get("walk-radius").getAsInt();
        Preconditions.checkState(!(coordinate && radius == 0));
        Preconditions.checkState(!(!coordinate && radius > 0));
        Npc npc = new Npc(id, position);
        npc.setOriginalRandomWalk(coordinate);
        npc.getMovementCoordinator().setCoordinate(coordinate);
        npc.getMovementCoordinator().setRadius(radius);
        npc.setRespawn(true);
        if (!World.getNpcs().add(npc))
            throw new IllegalStateException("NPC could not be added to the world!");
    }
}
