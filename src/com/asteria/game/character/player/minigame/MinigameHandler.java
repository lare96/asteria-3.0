package com.asteria.game.character.player.minigame;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import com.asteria.game.character.player.Player;
import com.asteria.task.Task;
import com.asteria.task.TaskHandler;

/**
 * The class that contains methods to handle the functionality of minigames.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class MinigameHandler extends Task {

    /**
     * The hash collection of active minigames.
     */
    public static final Set<Minigame> MINIGAMES = new HashSet<>();

    /**
     * Creates a new {@link MinigameHandler}.
     */
    public MinigameHandler() {
        super(1, false);
    }

    @Override
    public void execute() {
        for (Minigame m : MINIGAMES) {
            if (m.getType() != MinigameType.SEQUENCED)
                continue;
            SequencedMinigame sequencer = (SequencedMinigame) m;
            if (sequencer.getCounter().incrementAndGet() == sequencer.delay()) {
                sequencer.onSequence();
                sequencer.getCounter().set(0);
            }
        }
    }

    @Override
    public void onCancel() {
        TaskHandler.submit(new MinigameHandler());
    }

    /**
     * The method that executes {@code action} for {@code player}.
     * 
     * @param player
     *            the player to execute the action for.
     * @param action
     *            the backed minigame action to execute.
     */
    public static void execute(Player player, Consumer<Minigame> action) {
        Optional<Minigame> minigame = search(player);
        minigame.ifPresent(action::accept);
    }

    /**
     * The method that executes {@code function} for {@code player} that returns
     * a result.
     * 
     * @param player
     *            the player to execute the function for.
     * @param defaultValue
     *            the default value to return if the player isn't in a minigame.
     * @param function
     *            the function to execute that returns a result.
     */
    public static <T> T execute(Player player, T defaultValue, Function<Minigame, T> function) {
        Optional<Minigame> minigame = search(player);
        if (!minigame.isPresent())
            return defaultValue;
        return function.apply(minigame.get());
    }

    /**
     * Determines if {@code player} is in any minigame.
     * 
     * @param player
     *            the player to determine this for.
     * @return {@code true} if the player is in a minigame, {@code false}
     *         otherwise.
     */
    public static boolean contains(Player player) {
        return search(player).isPresent();
    }

    /**
     * Retrieves the minigame that {@code player} is currently in.
     * 
     * @param player
     *            the player to determine the minigame for.
     * @return the minigame that the player is currently in.
     */
    public static Optional<Minigame> search(Player player) {
        return MINIGAMES.stream().filter(m -> m.contains(player)).findFirst();
    }
}
