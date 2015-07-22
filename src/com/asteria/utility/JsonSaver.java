package com.asteria.utility;

import java.io.FileWriter;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * A utility class used for constructing and writing {@code JSON} files.
 * <p>
 * <p>
 * And an example of usage:
 * 
 * <pre>
 * JsonSaver json = new JsonSaver();
 * 
 * for (Player player : players) {
 *     json.current().addProperty(&quot;name&quot;, player.getUsername());
 *     json.current().addProperty(&quot;value1&quot;, 1);
 *     json.current().addProperty(&quot;value2&quot;, true);
 *     json.split();
 * }
 * 
 * json.publish(&quot;./data/some_player_database.json&quot;);
 * </pre>
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class JsonSaver {

    /**
     * A gson builder, allows us to turn {@code Object}s into {@code JSON}
     * format and vice-versa.
     */
    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    /**
     * An array that will hold all of our sub-tables.
     */
    private final JsonArray array = new JsonArray();

    /**
     * The flag that determines if only one table can exist.
     */
    private final boolean singletonTable;

    /**
     * A writer that acts as a sub-table, instantiated after each
     * {@code split()}.
     */
    private JsonObject currentWriter = new JsonObject();

    /**
     * Creates a new {@link JsonSaver}.
     *
     * @param singletonTable
     *            determines if only one table can exist.
     */
    public JsonSaver(boolean singletonTable) {
        this.singletonTable = singletonTable;
    }

    /**
     * Creates a new {@link JsonSaver} that can have an infinite amount of
     * tables.
     */
    public JsonSaver() {
        this(false);
    }

    /**
     * Adds the data within {@code currentWriter} to the internal
     * {@link JsonArray} then instantiates a new writer, effectively splitting
     * the data up into tables. If this instance is a {@code singletonTable},
     * throws an {@link IllegalStateException}.
     * 
     * @throws IllegalStateException
     *             if this instance is only allowed one internal table.
     */
    public void split() {
        Preconditions.checkState(!singletonTable, "JsonSaver instance is a singleton table!");
        array.add(currentWriter);
        currentWriter = new JsonObject();
    }

    /***
     * Gets the current {@link JsonObject} that is writing data.
     * 
     * @return the current writer.
     */
    public JsonObject current() {
        return currentWriter;
    }

    /**
     * Publishes the contents of this {@link JsonSaver} to the file at
     * {@code path}.
     * 
     * @param path
     *            the path to publish the contents.
     */
    public void publish(String path) {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <strong>Invocation of this function is expensive and should be cached or
     * avoided whenever possible.</strong> This function will call
     * {@code split()} if the {@code currentWriter} has unsplit elements added
     * to it.
     * <p>
     * <p>
     * This function returns the contents of this class in pretty printed
     * {@code JSON} format.
     */
    @Override
    public String toString() {
        if (singletonTable)
            return gson.toJson(currentWriter);
        if (currentWriter.entrySet().size() > 0)
            split();
        return gson.toJson(array);
    }
}
