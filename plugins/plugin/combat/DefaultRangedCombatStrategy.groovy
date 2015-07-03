package plugin.combat

import com.asteria.game.NodeType
import com.asteria.game.character.*
import com.asteria.game.character.combat.Combat
import com.asteria.game.character.combat.CombatSessionData
import com.asteria.game.character.combat.CombatStrategy
import com.asteria.game.character.combat.CombatType
import com.asteria.game.character.combat.ranged.CombatRangedAmmo
import com.asteria.game.character.combat.ranged.CombatRangedBow
import com.asteria.game.character.combat.weapon.FightStyle
import com.asteria.game.character.npc.Npc
import com.asteria.game.character.player.Player
import com.asteria.game.character.player.content.WeaponInterface
import com.asteria.game.item.Item
import com.asteria.game.item.container.Equipment
import com.asteria.game.plugin.PluginSignature

@PluginSignature(CombatStrategy.class)
final class DefaultRangedCombatStrategy implements CombatStrategy {

    @Override
    boolean canAttack(CharacterNode character, CharacterNode victim) {
        if (character.type == NodeType.NPC)
            return true
        Player player = character as Player
        if (Combat.isCrystalBow(player))
            return true
        return checkAmmo(player)
    }

    @Override
    CombatSessionData attack(CharacterNode character, CharacterNode victim) {
        if (character.type == NodeType.NPC) {
            Npc npc = character as Npc
            CombatRangedAmmo ammo = Combat.prepareRangedAmmo npc
            character.animation new Animation(npc.getDefinition().attackAnimation)
            character.highGraphic new Graphic(ammo.graphic)
            new Projectile(character, victim, ammo.projectile, ammo.delay, ammo.speed, ammo.startHeight, ammo
                    .endHeight, 0).sendProjectile()
            return new CombatSessionData(character, victim, 1, CombatType.RANGED, true)
        }

        Player player = character as Player
        player.rangedAmmo = null
        player.fireAmmo = 0
        CombatRangedAmmo ammo = CombatRangedAmmo.getPlayerAmmo(player).get()
        if(!CombatRangedBow.canUse(player, ammo)) {
            player.combatBuilder.reset()
            return new CombatSessionData(character, victim, null, true)
        }
        player.rangedAmmo = ammo
        if (!Combat.isCrystalBow(player)) {
            decrementAmmo player
        }
        if (!player.specialActivated) {
            player.highGraphic new Graphic(ammo.graphic)
            new Projectile(character, victim, ammo.projectile, ammo.delay, ammo.speed, ammo.startHeight, ammo
                    .endHeight, 0).sendProjectile()
        }
        startAnimation player
        return new CombatSessionData(character, victim, 1, CombatType.RANGED, true)
    }

    @Override
    int attackDelay(CharacterNode character) {
        character.getAttackSpeed()
    }

    @Override
    int attackDistance(CharacterNode character) {
        if (character.type == NodeType.NPC)
            return 6
        Player player = character as Player
        return Combat.getRangedDistance(player.weapon) + (player.fightType.style == FightStyle.DEFENSIVE ? 2 : 0)
    }

    @Override
    int[] getNpcs() {
        [688] as int[]
    }

    private def startAnimation(Player player) {
        if (player.equipment.get(Equipment.WEAPON_SLOT).getDefinition().name.startsWith("Karils")) {
            player.animation new Animation(2075)
        } else {
            player.animation new Animation(player.fightType.animation)
        }
    }

    private boolean checkAmmo(Player player) {
        Item item = player.weapon == WeaponInterface.DART || player.weapon == WeaponInterface.KNIFE || player
                .weapon == WeaponInterface.JAVELIN || player.weapon == WeaponInterface.THROWNAXE ? player.equipment
                .get(Equipment.WEAPON_SLOT) : player.equipment.get(Equipment.ARROWS_SLOT)

        if (!Item.valid(item)) {
            player.messages.sendMessage "You do not have enough ammo to use this ranged weapon."
            player.combatBuilder.reset()
            return false
        }
        if (player.weapon == WeaponInterface.SHORTBOW || player.weapon == WeaponInterface.LONGBOW) {
            if (!Combat.isArrows(player)) {
                player.messages.sendMessage "You need to use arrows with your bow."
                player.combatBuilder.reset()
                return false
            }
        } else if (player.weapon == WeaponInterface.CROSSBOW) {
            if (player.equipment.get(Equipment.WEAPON_SLOT).getDefinition().name.startsWith("Karils") && !item
            .getDefinition().name.endsWith("rack")) {
                player.messages.sendMessage "You need to use bolt racks with this crossbow."
                player.combatBuilder.reset()
                return false
            } else if (!player.equipment.get(Equipment.WEAPON_SLOT).getDefinition().name.startsWith("Karils") && !Combat
            .isBolts(player)) {
                player.messages.sendMessage "You need to use bolts with your crossbow."
                player.combatBuilder.reset()
                return false
            }
        }
        return true
    }

    private def decrementAmmo(Player player) {
        int slot = player.weapon == WeaponInterface.SHORTBOW || player.weapon == WeaponInterface.LONGBOW || player
                .weapon == WeaponInterface.CROSSBOW ? Equipment.ARROWS_SLOT : Equipment.WEAPON_SLOT

        player.setFireAmmo player.equipment.get(slot).getId()
        player.equipment.get(slot).decrementAmount()

        if (player.equipment.get(slot).amount == 0) {
            player.messages.sendMessage "That was your last piece of ammo!"
            player.equipment.set(slot, null)

            if (slot == Equipment.WEAPON_SLOT) {
                WeaponInterface.execute(player, null)
            }
        }

        if (slot == Equipment.WEAPON_SLOT) {
            player.getFlags().set Flag.APPEARANCE
        }
        player.equipment.refresh()
    }
}
