package com.asteria.game.character.combat.effect;

import com.asteria.game.character.CharacterNode;
import com.asteria.task.Task;

/**
 * An effect that usually takes places as a result of combat.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CombatEffect extends Task {

    /**
     * The character this effect is being applied to.
     */
    private final CharacterNode character;

    /**
     * Creates a new {@link CombatEffect}.
     * 
     * @param character
     *            the character this effect is being applied to
     * @param delay
     *            the delay for how often this effect will be sequenced.
     */
    public CombatEffect(CharacterNode character, int delay) {
        super(delay, false);
        super.attach(character);
        this.character = character;
    }

    /**
     * Applies this effect to {@code character}.
     * 
     * @return {@code true} if the effect could be applied, {@code false}
     *         otherwise.
     */
    public abstract boolean apply();

    /**
     * Removes this effect from {@code character} if needed.
     * 
     * @return {@code true} if this effect should be stopped, {@code false}
     *         otherwise.
     */
    public abstract boolean removeOn();

    /**
     * Provides processing for this effect on {@code character}.
     */
    public abstract void sequence();

    /**
     * Executed on login to re-apply the effect to {@code character}.
     */
    public abstract void onLogin();

    @Override
    public final void execute() {
        if (removeOn() || !character.isRegistered()) {
            this.cancel();
            return;
        }
        sequence();
    }
}
