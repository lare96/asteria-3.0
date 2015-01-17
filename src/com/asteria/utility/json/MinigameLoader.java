package com.asteria.utility.json;

import java.util.Objects;

import com.asteria.game.character.player.minigame.Minigame;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.character.player.minigame.MinigameType;
import com.asteria.game.character.player.minigame.SequencedMinigame;
import com.asteria.utility.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all minigames.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class MinigameLoader extends JsonLoader {

    /**
     * Creates a new {@link MinigameLoader}.
     */
    public MinigameLoader() {
        super("./data/json/minigames/minigames.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        String name = Objects.requireNonNull(reader.get("class").getAsString());
        execute(name);
    }

    /**
     * Executes the loading of the minigame within {@code name}.
     * 
     * @param name
     *            the name and path to the class.
     * @throws IllegalStateException
     *             if the class does not have a superclass of {@link Minigame}
     *             or {@link SequencedMinigame}, or if the delay for a sequenced
     *             minigame is equal to {@code 0} or lower.
     */
    private static void execute(String name) {
        try {
            Class<?> c = Class.forName(name);
            if (!(c.getSuperclass() == Minigame.class) && !(c.getSuperclass() == SequencedMinigame.class))
                throw new IllegalStateException("The superclass of a minigame must be Minigame or SequencedMinigame!");
            Minigame m = (Minigame) c.newInstance();
            if (m.getType() == MinigameType.SEQUENCED) {
                if (((SequencedMinigame) m).delay() <= 0)
                    throw new IllegalStateException("Sequenced minigames must have delays greater than 0!");
            }
            MinigameHandler.MINIGAMES.add(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
