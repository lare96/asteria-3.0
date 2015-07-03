package com.asteria.utility.json;

import com.asteria.net.NetworkConstants;
import com.asteria.utility.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all of the sizes of incoming
 * messages.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class MessageSizeLoader extends JsonLoader {

    /**
     * Creates a new {@link MessageSizeLoader}.
     */
    public MessageSizeLoader() {
        super("./data/json/io/message_sizes.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int opcode = reader.get("opcode").getAsInt();
        int size = reader.get("size").getAsInt();
        NetworkConstants.MESSAGE_SIZES[opcode] = size;
    }
}
