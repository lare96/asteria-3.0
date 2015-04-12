package com.asteria.game.character.combat.magic;

import java.util.Optional;

import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;

/**
 * The {@link CombatSpell} extension with support for effects and no damage.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CombatEffectSpell extends CombatSpell {

    @Override
    public final int maximumHit() {
        return -1;
    }

    @Override
    public final Optional<Item[]> equipmentRequired(Player player) {
        return Optional.empty();
    }

    @Override
    public final void executeOnHit(CharacterNode cast, CharacterNode castOn, boolean accurate, int damage) {
        if (accurate) {
            effect(cast, castOn);
        }
    }

    /**
     * Executed when the spell casted by {@code cast} hits {@code castOn}.
     *
     * @param cast
     *            the character who casted the spell.
     * @param castOn
     *            the character who the spell was casted on.
     */
    public abstract void effect(CharacterNode cast, CharacterNode castOn);
}
