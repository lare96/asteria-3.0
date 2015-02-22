package com.asteria.game.character.combat.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.asteria.game.NodeType;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.Hit;
import com.asteria.game.character.HitType;
import com.asteria.game.character.PoisonType;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.task.TaskHandler;
import com.asteria.utility.RandomGen;

/**
 * The combat effect applied when a character needs to be poisoned.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class CombatPoisonEffect extends CombatEffect {

    /**
     * The collection of weapons mapped to their respective poison types.
     */
    public static final Map<Integer, PoisonType> TYPES = new HashMap<>();

    /**
     * The random generator instance that will generate random numbers.
     */
    private final RandomGen random = new RandomGen();

    /**
     * The character this effect is being applied to.
     */
    private final CharacterNode character;

    /**
     * The poison type this effect will use.
     */
    private final PoisonType type;

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
        if (type.apply(random)) {
            if (character.getType() == NodeType.PLAYER) {
                Player player = (Player) character;
                if (player.getPoisonImmunity().get() > 0)
                    return false;
                player.getEncoder().sendMessage("You have been poisoned!");
            }
            character.setPoisonDamage(type.getDamage());
            return true;
        }
        return false;
    }

    @Override
    public boolean removeOn() {
        return !character.isPoisoned();
    }

    @Override
    public void sequence() {
        int counter = random.nextBoolean() ? character.getPoisonDamage() : character.getAndDecrementPoisonDamage();
        character.damage(new Hit(counter, HitType.POISON));
    }

    @Override
    public void onLogin() {
        if (character.isPoisoned()) {
            TaskHandler.submit(this);
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
}
