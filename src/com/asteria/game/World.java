package com.asteria.game;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.asteria.Server;
import com.asteria.game.character.CharacterList;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.service.ConcurrentUpdateService;
import com.asteria.game.service.SequentialUpdateService;

/**
 * The static utility class that contains functions to manage and process game
 * characters.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class World {

    /**
     * The collection of active players.
     */
    private static CharacterList<Player> players = new CharacterList<>(2000);

    /**
     * The collection of active NPCs.
     */
    private static CharacterList<Npc> npcs = new CharacterList<>(5000);

    /**
     * The method that executes the update sequence for all in game characters
     * every {@code 600}ms. The update sequence may either run sequentially or
     * concurrently depending on the type of engine selected by the server.
     * 
     * @throws Exception
     *             if any errors occur during the update sequence.
     */
    public static void sequence() throws Exception {
        Runnable updateService = Server.getBuilder().isParallelEngine() ? new ConcurrentUpdateService()
            : new SequentialUpdateService();
        updateService.run();
    }

    /**
     * Returns a player within an optional whose name hash is equal to
     * {@code username}.
     * 
     * @param username
     *            the name hash to check the collection of players for.
     * @return the player within an optional if found, or an empty optional if
     *         not found.
     */
    public static Optional<Player> getPlayer(long username) {
        return players.search(player -> player.getUsernameHash() == username);
    }

    /**
     * Returns a player within an optional whose name is equal to
     * {@code username}.
     * 
     * @param username
     *            the name to check the collection of players for.
     * @return the player within an optional if found, or an empty optional if
     *         not found.
     */
    public static Optional<Player> getPlayer(String username) {
        return players.search(player -> player.getUsername().equals(username));
    }

    public static Set<CharacterNode> getCharacters() {
        Set<CharacterNode> characters = new HashSet<>();
        players.forEach(characters::add);
        npcs.forEach(characters::add);
        return characters;
    }

    /**
     * Gets the collection of active players.
     * 
     * @return the active players.
     */
    public static CharacterList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the collection of active npcs.
     * 
     * @return the active npcs.
     */
    public static CharacterList<Npc> getNpcs() {
        return npcs;
    }
}