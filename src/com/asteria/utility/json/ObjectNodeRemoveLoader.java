package com.asteria.utility.json;

import java.util.Objects;

import com.asteria.game.location.Position;
import com.asteria.game.object.ObjectNodeManager;
import com.asteria.utility.JsonLoader;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads the removal of object nodes.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class ObjectNodeRemoveLoader extends JsonLoader {

    /**
     * Create a new {@link ObjectNodeRemoveLoader}.
     */
    public ObjectNodeRemoveLoader() {
        super("./data/json/objects/object_remove.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        Position position = Objects.requireNonNull(builder.fromJson(reader.get("position"), Position.class));
        Preconditions.checkState(!ObjectNodeManager.getObject(position).isPresent());
        ObjectNodeManager.REMOVE_OBJECTS.add(position);
    }
}
