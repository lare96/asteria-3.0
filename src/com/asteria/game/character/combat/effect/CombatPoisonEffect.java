package com.asteria.game.character.combat.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.Hit;
import com.asteria.game.character.HitType;
import com.asteria.game.character.PoisonType;
import com.asteria.game.character.npc.NpcDefinition;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;

/**
 * The combat effect applied when a character needs to be poisoned.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatPoisonEffect extends CombatEffect {

    /**
     * The collection of weapons mapped to their respective poison types.
     */
    public static final Map<Integer, PoisonType> TYPES = new HashMap<>();

    /**
     * The character this effect is being applied to.
     */
    private final CharacterNode character;

    /**
     * The poison type this effect will use.
     */
    private final PoisonType type;

    /**
     * The amount of times this player has been hit.
     */
    private int amount;

    /**
     * Creates a new {@link CombatPoisonEffect}.
     *
     * @param character
     *            the character this effect is being applied to.
     * @param type
     *            the poison type this effect will use.
     */
    public CombatPoisonEffect(CharacterNode character, PoisonType type) {
        super(character, 30);
        this.character = character;
        this.type = type;
    }

    @Override
    public boolean apply() {
        if (character.isPoisoned() || type == null)
            return false;
        if (character.getType() == NodeType.PLAYER) {
            Player player = (Player) character;
            if (player.getPoisonImmunity().get() > 0)
                return false;
            player.getEncoder().sendMessage("You have been poisoned!");
        }
        character.getPoisonDamage().set(type.getDamage());
        return true;
    }

    @Override
    public boolean removeOn() {
        return !character.isPoisoned();
    }

    @Override
    public void sequence() {
        amount--;
        character.damage(new Hit(character.getPoisonDamage().get(), HitType.POISON));
        if (amount == 0) {
            amount = 4;
            character.getPoisonDamage().decrementAndGet();
        }
    }

    @Override
    public void onLogin() {
        if (character.isPoisoned()) {
            World.submit(this);
        }
    }

    /**
     * Gets the {@link PoisonType} for {@code item} wrapped in an optional. If a
     * poison type doesn't exist for the item then an empty optional is
     * returned.
     *
     * @param item
     *            the item to get the poison type for.
     * @return the poison type for this item wrapped in an optional, or an empty
     *         optional if no poison type exists.
     */
    public static Optional<PoisonType> getPoisonType(Item item) {
        if (item == null || item.getId() < 1 || item.getAmount() < 1)
            return Optional.empty();
        return Optional.ofNullable(TYPES.get(item.getId()));
    }

    /**
     * Gets the {@link PoisonType} for {@code npc} wrapped in an optional. If a
     * poison type doesn't exist for the NPC then an empty optional is returned.
     *
     * @param npc
     *            the NPC to get the poison type for.
     * @return the poison type for this NPC wrapped in an optional, or an empty
     *         optional if no poison type exists.
     */
    public static Optional<PoisonType> getPoisonType(int npc) {
        NpcDefinition def = NpcDefinition.DEFINITIONS[npc];
        if (def == null || !def.isAttackable() || !def.isPoisonous())
            return Optional.empty();
        if (def.getCombatLevel() < 75)
            return Optional.of(PoisonType.DEFAULT_NPC);
        if (def.getCombatLevel() < 200)
            return Optional.of(PoisonType.STRONG_NPC);
        return Optional.of(PoisonType.SUPER_NPC);
    }
}
