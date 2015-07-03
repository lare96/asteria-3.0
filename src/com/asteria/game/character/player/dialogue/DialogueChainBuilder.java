package com.asteria.game.character.player.dialogue;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;

import com.asteria.game.character.player.Player;

/**
 * The dialogue chain builder that contains functions for building and managing
 * dialogues.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class DialogueChainBuilder {

    /**
     * The player that this chain builder is dedicated to.
     */
    private final Player player;

    /**
     * The queue of dialogue entries in this chain builder.
     */
    private final Queue<DialogueChain> chain = new ArrayDeque<>();

    /**
     * The option listener for this chain builder.
     */
    private Optional<Consumer<OptionType>> optionListener = Optional.empty();

    /**
     * Creates a new {@link DialogueChainBuilder}.
     *
     * @param player
     *            the player that this chain builder is dedicated to.
     */
    public DialogueChainBuilder(Player player) {
        this.player = player;
    }

    /**
     * Appends {@code entry} to this chain builder.
     *
     * @param entry
     *            the dialogue chain to append to this chain builder.
     * @return an instance of this chain builder.
     */
    public DialogueChainBuilder append(DialogueChain entry) {
        chain.add(entry);
        return this;
    }

    /**
     * Appends {@code optionListener} to this chain builder.
     *
     * @param optionListener
     *            the option listener to append to this builder.
     * @return an instance of this chain builder.
     */
    protected void append(Optional<Consumer<OptionType>> optionListener) {
        this.optionListener = optionListener;
    }

    /**
     * Advances this chain builder by one dialogue chain.
     *
     * @throws IllegalStateException
     *             if this dialogue chain builder is empty.
     */
    public void advance() {
        DialogueChain entry = chain.poll();
        if (entry == null) {
            player.getMessages().sendCloseWindows();
        } else {
            entry.accept(this);
        }
    }

    /**
     * Interrupts this chain builder by clearing the backing chain list and
     * resetting the cursor value.
     */
    public void interrupt() {
        if (!chain.isEmpty())
            chain.clear();
        optionListener = Optional.empty();
    }

    /**
     * Executes the dialogue option listener with {@code type} if it is
     * available.
     *
     * @param type
     *            the dialogue option type.
     * @return {@code true} if the option was executed, {@code false} otherwise.
     */
    public boolean executeOptions(OptionType type) {
        if (optionListener.isPresent()) {
            optionListener.get().accept(type);
            optionListener = Optional.empty();
            return true;
        }
        return false;
    }

    /**
     * The size of the backing chain list in this chain builder.
     *
     * @return the size of this chain builder.
     */
    public int size() {
        return chain.size();
    }

    /**
     * Gets the player that this chain builder is dedicated to.
     *
     * @return the player for this chain builder.
     */
    public Player getPlayer() {
        return player;
    }
}
