package com.asteria.game.character.combat.magic;

import java.util.Iterator;
import java.util.Optional;

import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.Hit;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.location.Location;
import com.asteria.utility.RandomGen;

/**
 * The {@link CombatSpell} extension with support for effects and the ability to
 * multicast characters within a certain radius.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CombatAncientSpell extends CombatSpell {

    /**
     * The random generator instance that will generate random numbers.
     */
    private RandomGen random = new RandomGen();

    @Override
    public final void executeOnHit(CharacterNode cast, CharacterNode castOn, boolean accurate, int damage) {
        if (accurate) {
            effect(cast, castOn, damage);

            if (radius() == 0 || !Location.inMultiCombat(castOn)) {
                return;
            }

            Iterator<? extends CharacterNode> it = null;
            if (cast.getType() == NodeType.PLAYER && castOn.getType() == NodeType.PLAYER) {
                it = ((Player) cast).getLocalPlayers().iterator();
            } else if (cast.getType() == NodeType.PLAYER && castOn.getType() == NodeType.NPC) {
                it = ((Player) cast).getLocalNpcs().iterator();
            } else if (cast.getType() == NodeType.NPC && castOn.getType() == NodeType.NPC) {
                it = World.getNpcs().iterator();
            } else if (cast.getType() == NodeType.NPC && castOn.getType() == NodeType.PLAYER) {
                it = World.getPlayers().iterator();
            }

            while (it.hasNext()) {
                CharacterNode character = it.next();
                if (character == null || !character.getPosition().withinDistance(castOn.getPosition(), radius()) || character.equals(cast) || character
                    .equals(castOn) || character.getCurrentHealth() <= 0 || character.isDead()) {
                    continue;
                }
                cast.getCurrentlyCasting().endGraphic().ifPresent(character::graphic);
                int counter = random.inclusive(0, maximumHit());
                character.damage(new Hit(counter));
                character.getCombatBuilder().getDamageCache().add(cast, counter);
                effect(cast, character, counter);
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
