package com.asteria.game.character;

import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.task.Task;

/**
 * The parent class that handles the death process for all characters.
 *
 * @param <T>
 *            the type of character the death process is being executed for.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CharacterDeath<T extends CharacterNode> extends Task {

    /**
     * The character who has died and needs the death process.
     */
    private final T character;

    /**
     * The counter that will determine which part of the death process we are
     * on.
     */
    private int counter;

    /**
     * Creates a new {@link CharacterDeath}.
     *
     * @param character
     *            the character who has died and needs the death process.
     */
    public CharacterDeath(T character) {
        super(1, true);
        this.character = character;
    }

    /**
     * The part of the death process where the character is prepared for the
     * rest of the death process.
     *
     * @param character
     *            the character who has died.
     */
    public abstract void preDeath(T character);

    /**
     * The main part of the death process where the killer is found for the
     * character.
     *
     * @param character
     *            the character who has died.
     */
    public abstract void death(T character);

    /**
     * The last part of the death process where the character is reset.
     *
     * @param character
     *            the character who has died.
     */
    public abstract void postDeath(T character);

    @Override
    public final void execute() {
        if (counter == 0) {
            character.setDead(true);
            character.setPoisonDamage(0);
            character.getMovementQueue().reset();
            character.unfreeze();
        } else if (counter == 1) {
            preDeath(character);
        } else if (counter == 5) {
            death(character);
        } else if (counter == 6) {
            postDeath(character);
            character.setDead(false);
            this.cancel();
        }
        counter++;
    }

    @Override
    public final void onThrowable(Throwable t) {
        if (character.getType() == NodeType.PLAYER) {
            World.getPlayers().remove((Player) character);
        } else if (character.getType() == NodeType.NPC) {
            World.getNpcs().remove((Npc) character);
        }
    }
}