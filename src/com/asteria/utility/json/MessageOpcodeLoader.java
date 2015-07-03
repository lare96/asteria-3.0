package com.asteria.utility.json;

import java.util.Arrays;
import java.util.Objects;

import com.asteria.net.NetworkConstants;
import com.asteria.net.message.InputMessageListener;
import com.asteria.utility.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all incoming messages.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class MessageOpcodeLoader extends JsonLoader {

    /**
     * Creates a new {@link MessageOpcodeLoader}.
     */
    public MessageOpcodeLoader() {
        super("./data/json/io/message_opcodes.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int[] opcodes = builder.fromJson(reader.get("opcodes").getAsJsonArray(), int[].class);
        String name = Objects.requireNonNull(reader.get("class").getAsString());
        boolean invalid = Arrays.stream(opcodes).anyMatch(op -> op < 0 || op > NetworkConstants.MESSAGES.length);
        if (invalid)
            throw new IllegalStateException("Invalid message opcode!");
        execute(opcodes, name);
    }

    /**
     * Executes the loading of the message within {@code name} for
     * {@code opcodes}.
     *
     * @param opcodes
     *            the opcodes of the message.
     * @param name
     *            the name and path to the class.
     * @throws IllegalStateException
     *             if the class isn't implementing {@link InputMessageListener}.
     */
    private static void execute(int[] opcodes, String name) {
        try {
            Class<?> c = Class.forName(name);
            if (!(Arrays.stream(c.getInterfaces()).anyMatch($it -> $it == InputMessageListener.class)))
                throw new IllegalStateException("Class must be implementing InputMessageListener!");
            InputMessageListener message = (InputMessageListener) c.newInstance();
            Arrays.stream(opcodes).forEach(op -> NetworkConstants.MESSAGES[op] = message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
