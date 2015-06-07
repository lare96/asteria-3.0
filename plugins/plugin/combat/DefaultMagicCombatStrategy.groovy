package plugin.combat

import com.asteria.game.NodeType;
import com.asteria.game.character.combat.Combat
import com.asteria.game.character.combat.CombatSessionData
import com.asteria.game.character.combat.CombatStrategy
import com.asteria.game.character.combat.CombatType
import com.asteria.game.character.player.Player
import com.asteria.game.model.character.CharacterNode;
import com.asteria.game.model.character.npc.Npc;
import com.asteria.game.plugin.PluginSignature

@PluginSignature(CombatStrategy.class)
final class DefaultMagicCombatStrategy implements CombatStrategy {

    @Override
    boolean canAttack(CharacterNode character, CharacterNode victim) {
        if (character.type == NodeType.NPC)
            return true
        Player player = character as Player
        return player.castSpell.canCast(player)
    }

    @Override
    CombatSessionData attack(CharacterNode character, CharacterNode victim) {
        if (character.type == NodeType.PLAYER) {
            Player player = character as Player
            player.prepareSpell(player.castSpell, victim)
        } else if (character.type == NodeType.NPC) {
            Npc npc = character as Npc
            npc.prepareSpell(Combat.prepareSpellCast(npc).spell, victim)
        }

        if (character.currentlyCasting.maximumHit() == -1) {
            return new CombatSessionData(character, victim, CombatType.MAGIC, true)
        }
        return new CombatSessionData(character, victim, 1, CombatType.MAGIC, true)
    }

    @Override
    int attackDelay(CharacterNode character) {
        10
    }

    @Override
    int attackDistance(CharacterNode character) {
        8
    }

    @Override
    int[] getNpcs() {
        [13, 172, 174] as int[]
    }
}
