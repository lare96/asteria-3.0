package com.asteria.utility.json;

import com.asteria.network.packet.PacketDecoder;
import com.asteria.utility.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all of the sizes of
 * incoming
 * packets.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class PacketSizeLoader extends JsonLoader {

    /**
     * Creates a new {@link PacketSizeLoader}.
     */
    public PacketSizeLoader() {
        super("./data/json/io/packet_sizes.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int opcode = reader.get("opcode").getAsInt();
        int size = reader.get("size").getAsInt();
        PacketDecoder.PACKET_SIZES[opcode] = size;
    }
}
