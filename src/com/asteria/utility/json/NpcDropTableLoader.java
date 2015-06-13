package com.asteria.utility.json;

import java.util.Arrays;
import java.util.Objects;

import com.asteria.game.character.npc.drop.NpcDrop;
import com.asteria.game.character.npc.drop.NpcDropTable;
import com.asteria.utility.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all npc drops.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDropTableLoader extends JsonLoader {

    /**
     * Creates a new {@link NpcDropTableLoader}.
     */
    public NpcDropTableLoader() {
        super("./data/json/npcs/npc_drops.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int[] array = builder.fromJson(reader.get("ids"), int[].class);
        NpcDrop[] dynamic = Objects.requireNonNull(builder.fromJson(reader.get("dynamic"), NpcDrop[].class));
        NpcDrop[] rare = Objects.requireNonNull(builder.fromJson(reader.get("rare"), NpcDrop[].class));
        Arrays.stream(array).forEach(id -> NpcDropTable.DROPS.put(id, new NpcDropTable(array, dynamic, rare)));
    }
}
