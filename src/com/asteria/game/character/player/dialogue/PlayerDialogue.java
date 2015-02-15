package com.asteria.game.character.player.dialogue;

/**
 * The dialogue chain entry that sends the player a dialogue from a player.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class PlayerDialogue implements DialogueChain {

    /**
     * The expression that this player will display.
     */
    private final Expression expression;

    /**
     * The text that will be displayed on the dialogue.
     */
    private final String[] text;

    /**
     * Creates a new {@link PlayerDialogue}.
     * 
     * @param expression
     *            the expression that this player will display.
     * @param text
     *            the text that will be displayed on the dialogue.
     */
    public PlayerDialogue(Expression expression, String... text) {
        this.expression = expression;
        this.text = text;
    }

    /**
     * Creates a new {@link PlayerDialogue} with the default expression.
     * 
     * @param text
     *            the text that will be displayed on the dialogue.
     */
    public PlayerDialogue(String... text) {
        this(Expression.CALM, text);
    }

    @Override
    public void accept(DialogueChainBuilder dialogue) {
        Dialogues.playerDialogue(dialogue.getPlayer(), expression, text);
    }
}
