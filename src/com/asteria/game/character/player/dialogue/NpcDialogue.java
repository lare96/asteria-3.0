package com.asteria.game.character.player.dialogue;

/**
 * The dialogue chain entry that sends the player a dialogue from an NPC.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDialogue implements DialogueChain {

    /**
     * The identifier for the NPC sending this dialogue.
     */
    private final int npc;

    /**
     * The expression that this NPC will display.
     */
    private final Expression expression;

    /**
     * The text that will be displayed on the dialogue.
     */
    private final String[] text;

    /**
     * Creates a new {@link NpcDialogue}.
     *
     * @param npc
     *         the identifier for the NPC sending this dialogue.
     * @param expression
     *         the expression that this NPC will display.
     * @param text
     *         the text that will be displayed on the dialogue.
     */
    public NpcDialogue(int npc, Expression expression, String... text) {
        this.npc = npc;
        this.expression = expression;
        this.text = text;
    }

    /**
     * Creates a new {@link NpcDialogue} with the default expression.
     *
     * @param npc
     *         the identifier for the NPC sending this dialogue.
     * @param text
     *         the text that will be displayed on the dialogue.
     */
    public NpcDialogue(int npc, String... text) {
        this(npc, Expression.CALM, text);
    }

    @Override
    public void accept(DialogueChainBuilder dialogue) {
        Dialogues.npcDialogue(dialogue.getPlayer(), expression, npc, text);
    }
}
