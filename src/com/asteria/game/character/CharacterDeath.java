package com.asteria.game.character;

import java.util.Arrays;
import java.util.Optional;

import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.task.Task;

/**
 * The parent class that handles the death process for all characters.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 * @param <T>
 *            the type of character the death process is being executed for.
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
    public void execute() {
        Optional<DeathStage> stage = DeathStage.getStage(counter++);
        stage.ifPresent(s -> {
            switch (s) {
            case JUST_DIED:
                character.setDead(true);
                character.setPoisonDamage(0);
                character.getMovementQueue().reset();
                character.unfreeze();
                break;
            case PRE_DEATH:
                preDeath(character);
                break;
            case DEATH:
                death(character);
                break;
            case POST_DEATH:
                postDeath(character);
                character.setDead(false);
                break;
            }
        });

        if (counter >= DeathStage.POST_DEATH.getTicks()) {
            this.cancel();
        }
    }

    @Override
    public void onThrowable(Throwable t) {
        if (character.getType() == NodeType.PLAYER) {
            World.getPlayers().remove((Player) character);
        } else if (character.getType() == NodeType.NPC) {
            World.getNpcs().remove((Npc) character);
        }
    }

    /**
     * The enumerated type whose elements represent each death stage in the
     * entire death process.
     * 
     * @author lare96 <http://www.rune-server.org/members/lare96/>
     */
    private enum DeathStage {
        JUST_DIED(0),
        PRE_DEATH(1),
        DEATH(5),
        POST_DEATH(6);

        /**
         * The amount of ticks needed to activate this death stage.
         */
        private final int ticks;

        /**
         * Creates a new {@link DeathStage}.
         * 
         * @param ticks
         *            the amount of ticks needed to activate this death stage.
         */
        private DeathStage(int ticks) {
            this.ticks = ticks;
        }

        /**
         * Gets the amount of ticks needed to activate this death stage.
         * 
         * @return the amount of ticks for this stage.
         */
        public final int getTicks() {
            return ticks;
        }

        /**
         * Gets the death stage element based on {@code ticks}.
         * 
         * @param ticks
         *            the amount of ticks to get the death stage with.
         * @return the death stage wrapped within an optional if present, or an
         *         empty optional if not present.
         */
        public static Optional<DeathStage> getStage(int ticks) {
            return Arrays.stream(DeathStage.values()).filter(s -> s.ticks == ticks).findFirst();
        }
    }
}