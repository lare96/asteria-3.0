package com.asteria.utility.json;

import java.util.Objects;

import com.asteria.game.location.Position;
import com.asteria.game.object.ObjectDirection;
import com.asteria.game.object.ObjectNode;
import com.asteria.game.object.ObjectNodeManager;
import com.asteria.game.object.ObjectType;
import com.asteria.utility.JsonLoader;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all object nodes.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class ObjectNodeLoader extends JsonLoader {

    /**
     * Create a new {@link ObjectNodeLoader}.
     */
    public ObjectNodeLoader() {
        super("./data/json/objects/object_nodes.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int id = reader.get("id").getAsInt();
        Position position = Objects.requireNonNull(builder.fromJson(reader.get("position"), Position.class));
        ObjectDirection face = Objects.requireNonNull(ObjectDirection.valueOf(reader.get("direction").getAsString()));
        ObjectType type = Objects.requireNonNull(ObjectType.valueOf(reader.get("type").getAsString()));
        Preconditions.checkState(!ObjectNodeManager.REMOVE_OBJECTS.contains(position));
        ObjectNodeManager.register(new ObjectNode(id, position, face, type));
    }
}