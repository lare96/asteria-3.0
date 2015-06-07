package plugin.combat

import com.asteria.game.NodeType
import com.asteria.game.character.Animation
import com.asteria.game.character.AnimationPriority
import com.asteria.game.character.CharacterNode
import com.asteria.game.character.combat.CombatSessionData
import com.asteria.game.character.combat.CombatStrategy
import com.asteria.game.character.combat.CombatType
import com.asteria.game.character.combat.weapon.FightType
import com.asteria.game.character.npc.Npc
import com.asteria.game.character.player.Player
import com.asteria.game.character.player.content.WeaponInterface
import com.asteria.game.item.Item
import com.asteria.game.item.container.Equipment
import com.asteria.game.plugin.PluginSignature

@PluginSignature(CombatStrategy.class)
final class DefaultMeleeCombatStrategy implements CombatStrategy {

    @Override
    boolean canAttack(CharacterNode character, CharacterNode victim) {
        true
    }

    @Override
    CombatSessionData attack(CharacterNode character, CharacterNode victim) {
        startAnimation character
        return new CombatSessionData(character, victim, 1, CombatType.MELEE, true)
    }

    @Override
    int attackDelay(CharacterNode character) {
        character.getAttackSpeed()
    }

    @Override
    int attackDistance(CharacterNode character) {
        if (character.type == NodeType.NPC)
            return 1
        if ((character as Player).weapon == WeaponInterface.HALBERD)
            return 2
        return 1
    }

    @Override
    int[] getNpcs() {
        [] as int[]
    }

    private def startAnimation(CharacterNode character) {
        if (character.type == NodeType.NPC) {
            Npc npc = character as Npc
            npc.animation new Animation(npc.getDefinition().attackAnimation)
        } else if (character.type == NodeType.PLAYER) {
            Player player = character as Player
            Item item = player.equipment.get Equipment.WEAPON_SLOT

            if (!player.specialActivated && item != null) {
                if (item.getDefinition().name.startsWith("Dragon dagger")) {
                    player.animation new Animation(402)
                } else if (item.getDefinition().name.startsWith("Dharoks")) {
                    if (player.getFightType() == FightType.BATTLEAXE_SMASH) {
                        player.animation new Animation(2067)
                    } else {
                        player.animation new Animation(2066)
                    }
                } else if (item.getDefinition().name.equals("Granite maul")) {
                    player.animation new Animation(1665)
                } else if (item.getDefinition().name.equals("Tzhaar-ket-om")) {
                    player.animation new Animation(2661)
                } else if (item.getDefinition().name.endsWith("wand")) {
                    player.animation new Animation(FightType.UNARMED_KICK.getAnimation())
                } else if (item.getDefinition().name.startsWith("Torags")) {
                    player.animation new Animation(2068)
                } else if (item.getDefinition().name.startsWith("Veracs")) {
                    player.animation new Animation(2062)
                } else {
                    player.animation new Animation(player.getFightType().getAnimation())
                }
            } else {
                player.animation new Animation(player.getFightType().getAnimation(), AnimationPriority.HIGH)
            }
        }
    }
}
