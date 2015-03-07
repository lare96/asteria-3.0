package com.asteria.utility.json;

import java.util.Arrays;
import java.util.Objects;

import com.asteria.network.packet.PacketDecoder;
import com.asteria.utility.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all incoming packets.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class PacketOpcodeLoader extends JsonLoader {

    /**
     * Creates a new {@link PacketOpcodeLoader}.
     */
    public PacketOpcodeLoader() {
        super("./data/json/io/packet_opcodes.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int[] opcodes = builder.fromJson(reader.get("opcodes").getAsJsonArray(), int[].class);
        String name = Objects.requireNonNull(reader.get("class").getAsString());
        boolean invalid = Arrays.stream(opcodes).anyMatch(op -> op < 0 || op > PacketDecoder.PACKETS.length);
        if (invalid)
            throw new IllegalStateException("Invalid packet opcode!");
        execute(opcodes, name);
    }

    /**
     * Executes the loading of the packet within {@code name} for
     * {@code opcodes}.
     * 
     * @param opcodes
     *            the opcodes of the packet.
     * @param name
     *            the name and path to the class.
     * @throws IllegalStateException
     *             if the superclass of the class isn't {@link PacketDecoder}.
     */
    private static void execute(int[] opcodes, String name) {
        try {
            Class<?> c = Class.forName(name);
            if (!(c.getSuperclass() == PacketDecoder.class))
                throw new IllegalStateException("Class must have PacketDecoder as its superclass!");
            PacketDecoder packet = (PacketDecoder) c.newInstance();
            Arrays.stream(opcodes).forEach(op -> PacketDecoder.PACKETS[op] = packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
