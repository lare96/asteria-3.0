package com.asteria.content.combat.strategy;

import com.asteria.game.NodeType;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.combat.CombatSessionData;
import com.asteria.game.character.combat.CombatStrategy;
import com.asteria.game.character.combat.CombatType;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;

/**
 * The default combat strategy implementation for all characters that attack
 * with magic.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class DefaultMagicCombatStrategy implements CombatStrategy {

    @Override
    public boolean canAttack(CharacterNode character, CharacterNode victim) {
        if (character.getType() == NodeType.NPC)
            return true;
        Player player = (Player) character;
        return player.getCastSpell().canCast(player);
    }

    @Override
    public CombatSessionData attack(CharacterNode character, CharacterNode victim) {
        if (character.getType() == NodeType.PLAYER) {
            Player player = (Player) character;
            player.prepareSpell(player.getCastSpell(), victim);
        } else if (character.getType() == NodeType.NPC) {
            Npc npc = (Npc) character;
            npc.prepareSpell(Combat.prepareSpellCast(npc).getSpell(), victim);
        }

        if (character.getCurrentlyCasting().maximumHit() == -1) {
            return new CombatSessionData(character, victim, CombatType.MAGIC, true);
        }
        return new CombatSessionData(character, victim, 1, CombatType.MAGIC, true);
    }

    @Override
    public int attackDelay(CharacterNode character) {
        return 10;
    }

    @Override
    public int attackDistance(CharacterNode character) {
        return 8;
    }
}