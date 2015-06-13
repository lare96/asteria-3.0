package com.asteria.game.character.combat.magic;

import java.util.Optional;
import java.util.function.Consumer;

import com.asteria.game.NodeType;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.combat.CombatType;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.location.Location;

/**
 * The {@link CombatSpell} extension with support for effects and the ability to
 * multicast characters within a certain radius.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CombatAncientSpell extends CombatSpell {

    @Override
    public final void executeOnHit(CharacterNode cast, CharacterNode castOn, boolean accurate, int damage) {
        if (accurate) {
            effect(cast, castOn, damage);
            if (radius() == 0 || !Location.inMultiCombat(castOn))
                return;
            if (castOn.getType() == NodeType.PLAYER) {
                Combat.damagePlayersWithin(cast, castOn.getPosition(), radius(), 1, CombatType.MAGIC, false, new Consumer<Player>() {
                    @Override
                    public void accept(Player t) {
                        cast.getCurrentlyCasting().endGraphic().ifPresent(t::graphic);
                        effect(cast, castOn, damage);
                    }
                });
            } else {
                Combat.damageNpcsWithin(cast, castOn.getPosition(), radius(), 1, CombatType.MAGIC, false, new Consumer<Npc>() {
                    @Override
                    public void accept(Npc t) {
                        cast.getCurrentlyCasting().endGraphic().ifPresent(t::graphic);
                        effect(cast, castOn, damage);
                    }
                });
            }
        }
    }

    @Override
    public final Optional<Item[]> equipmentRequired(Player player) {
        return Optional.empty();
    }

    /**
     * Executed when the spell casted by {@code cast} hits {@code castOn}.
     *
     * @param cast
     *            the character who casted the spell.
     * @param castOn
     *            the character who the spell was casted on.
     * @param damage
     *            the damage that was inflicted by the spell.
     */
    public abstract void effect(CharacterNode cast, CharacterNode castOn, int damage);

    /**
     * The radius of this spell for multicast support.
     *
     * @return the radius of this spell.
     */
    public abstract int radius();
}
