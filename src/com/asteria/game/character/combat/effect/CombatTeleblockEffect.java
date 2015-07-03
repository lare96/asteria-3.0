package com.asteria.game.character.combat.effect;

import com.asteria.game.NodeType;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.player.Player;

/**
 * The combat effect applied when a player needs to be teleblocked.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatTeleblockEffect extends CombatEffect {

    /**
     * Creates a new {@link CombatTeleblockEffect}.
     */
    public CombatTeleblockEffect() {
        super(50);
    }

    @Override
    public boolean apply(CharacterNode c) {
        if (c.getType() == NodeType.PLAYER) {
            Player player = (Player) c;
            if (player.getTeleblockTimer().get() > 0) {
                return false;
            }
            player.getTeleblockTimer().set(3000);
            player.getMessages().sendMessage("You have just been teleblocked!");
            return true;
        }
        return false;
    }

    @Override
    public boolean removeOn(CharacterNode c) {
        if (c.getType() == NodeType.PLAYER) {
            Player player = (Player) c;
            if (player.getTeleblockTimer().get() <= 0) {
                player.getMessages().sendMessage("You feel the effects of the teleblock spell go away.");
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public void process(CharacterNode c) {
        if (c.getType() == NodeType.PLAYER) {
            Player player = (Player) c;
            player.getTeleblockTimer().decrementAndGet(50, 0);
        }
    }

    @Override
    public boolean onLogin(CharacterNode c) {
        if (c.getType() == NodeType.PLAYER) {
            Player player = (Player) c;
            if (player.getTeleblockTimer().get() > 0)
                return true;
        }
        return false;
    }
}
