package com.asteria.game.character.npc.dialogue;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * The dialogue chain entry that sends the player an option dialogue.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class OptionDialogue implements DialogueChain {

    /**
     * The different options that will be displayed.
     */
    private final String[] options;

    /**
     * Creates a new {@link OptionDialogue}.
     * 
     * @param options
     *            the different options that will be displayed.
     */
    public OptionDialogue(String... options) {
        this.options = options;
    }

    @Override
    public void accept(DialogueChainBuilder dialogue) {
        dialogue.interrupt();
        Dialogues.optionDialogue(dialogue.getPlayer(), options);
        dialogue.append(getOptionListener());
    }
    
    /**
     * Retrieves the option listener for this option dialogue entry. This can be
     * overridden to provide functionality for the displayed options.
     * 
     * @return the option listener.
     */
    public Optional<Consumer<OptionType>> getOptionListener() {
        return Optional.empty();
    }
}
