package com.asteria.game.character.combat.effect;

import com.google.common.collect.ImmutableSet;

/**
 * The enumerated type whose values represent the collection of different combat
 * effect types.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public enum CombatEffectType {

    /**
     * The combat poison effect, handles the poisoning of characters.
     */
    POISON(new CombatPoisonEffect()),

    /**
     * The combat skull effect, handles the skulling of players.
     */
    SKULL(new CombatSkullEffect()),

    /**
     * The combat teleblock effect, handles the teleblocking of players.
     */
    TELEBLOCK(new CombatTeleblockEffect());

    /**
     * The immutable set that holds the elements of this enumerated type, to
     * prevent repeated calls to {@code values()}.
     */
    public static final ImmutableSet<CombatEffectType> TYPES = ImmutableSet.copyOf(values());

    /**
     * The combat effect that contains the data for this type.
     */
    private final CombatEffect effect;

    /**
     * Creates a new {@link CombatEffectType}.
     *
     * @param effect
     *            the combat effect for this type.
     */
    private CombatEffectType(CombatEffect effect) {
        this.effect = effect;
    }

    /**
     * Gets the combat effect that contains the data for this type.
     * 
     * @return the combat effect for this type.
     */
    public final CombatEffect getEffect() {
        return effect;
    }
}
