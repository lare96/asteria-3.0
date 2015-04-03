package com.asteria.utility.json;

import java.util.Objects;

import com.asteria.game.item.Item;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.item.ItemNodeStatic;
import com.asteria.game.item.ItemPolicy;
import com.asteria.game.location.Position;
import com.asteria.utility.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all item nodes.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemNodeLoader extends JsonLoader {

    /**
     * Creates a new {@link ItemNodeLoader}.
     */
    public ItemNodeLoader() {
        super("./data/json/items/item_nodes.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int id = reader.get("id").getAsInt();
        int amount = reader.get("amount").getAsInt();
        Position position = Objects.requireNonNull(builder.fromJson(reader.get("position"), Position.class));
        ItemNodeManager.register(new ItemNodeStatic(new Item(id, amount), position, ItemPolicy.TIMEOUT));
    }
}
