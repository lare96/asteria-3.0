package com.asteria.utility.json;

import java.util.Objects;

import com.asteria.game.character.npc.NpcDefinition;
import com.asteria.utility.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all npc definitions.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class NpcDefinitionLoader extends JsonLoader {

    /**
     * Creates a new {@link NpcDefinitionLoader}.
     */
    public NpcDefinitionLoader() {
        super("./data/json/npcs/npc_definitions.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int index = reader.get("id").getAsInt();
        String name = Objects.requireNonNull(reader.get("name").getAsString());
        String description = Objects.requireNonNull(reader.get("examine").getAsString());
        int combatLevel = reader.get("combat").getAsInt();
        int size = reader.get("size").getAsInt();
        boolean attackable = reader.get("attackable").getAsBoolean();
        boolean aggressive = reader.get("aggressive").getAsBoolean();
        boolean retreats = reader.get("retreats").getAsBoolean();
        boolean poisonous = reader.get("poisonous").getAsBoolean();
        int respawnTime = reader.get("respawn").getAsInt();
        int maxHit = reader.get("maxHit").getAsInt();
        int hitpoints = reader.get("hitpoints").getAsInt();
        int attackSpeed = reader.get("attackSpeed").getAsInt();
        int attackAnim = reader.get("attackAnim").getAsInt();
        int defenceAnim = reader.get("defenceAnim").getAsInt();
        int deathAnim = reader.get("deathAnim").getAsInt();
        int attackBonus = reader.get("attackBonus").getAsInt();
        int meleeDefence = reader.get("defenceMelee").getAsInt();
        int rangedDefence = reader.get("defenceRange").getAsInt();
        int magicDefence = reader.get("defenceMage").getAsInt();

        NpcDefinition.DEFINITIONS[index] = new NpcDefinition(index, name, description, combatLevel, size, attackable, aggressive,
            retreats, poisonous, respawnTime, maxHit, hitpoints, attackSpeed, attackAnim, defenceAnim, deathAnim, attackBonus,
            meleeDefence, rangedDefence, magicDefence);
    }
}