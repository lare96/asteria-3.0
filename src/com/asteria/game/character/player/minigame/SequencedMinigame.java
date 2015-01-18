package com.asteria.game.character.player.minigame;

import com.asteria.utility.MutableNumber;

/**
 * The class that provides all of the functionality needed for minigames cannot
 * usually be ran on their own meaning they are dependent on some sort of
 * sequencer or task.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public abstract class SequencedMinigame extends Minigame {

    /**
     * The counter that conceals the tick amount.
     */
    private final MutableNumber counter = new MutableNumber();

    /**
     * Creates a new {@link SequencedMinigame}.
     * 
     * @param name
     *            the current name of this minigame.
     */
    public SequencedMinigame(String name) {
        super(name, MinigameType.SEQUENCED);
    }

    /**
     * The method executed when this minigame is sequenced.
     */
    public abstract void onSequence();

    /**
     * The delay interval for the sequencing of this minigame.
     * 
     * @return the delay interval.
     */
    public abstract int delay();

    /**
     * Gets the counter that conceals the tick amount.
     * 
     * @return the tick amount.
     */
    public MutableNumber getCounter() {
        return counter;
    }
}
