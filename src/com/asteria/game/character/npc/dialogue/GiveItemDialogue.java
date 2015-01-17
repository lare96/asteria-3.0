package com.asteria.game.character.npc.dialogue;

import com.asteria.game.item.Item;

/**
 * The dialogue chain entry that gives the player an item.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class GiveItemDialogue implements DialogueChain {

    /**
     * The item to give to the player during this chain.
     */
    private final Item item;

    /**
     * The text to display when the item is given.
     */
    private final String text;

    /**
     * Creates a new {@link GiveItemDialogue}.
     * 
     * @param item
     *            the item to give to the player during this chain.
     * @param text
     *            the text to display when the item is given.
     */
    public GiveItemDialogue(Item item, String text) {
        this.item = item;
        this.text = text;
    }

    @Override
    public void accept(DialogueChainBuilder dialogue) {
        dialogue.getPlayer().getEncoder().sendString(text, 308);
        dialogue.getPlayer().getEncoder().sendItemModelOnInterface(307, 200, item.getId());
        dialogue.getPlayer().getEncoder().sendChatInterface(306);
        dialogue.getPlayer().getInventory().add(item);
    }
}