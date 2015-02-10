package plugin.combat

import com.asteria.game.NodeType
import com.asteria.game.character.Animation
import com.asteria.game.character.CharacterNode
import com.asteria.game.character.Flag
import com.asteria.game.character.Graphic
import com.asteria.game.character.Projectile
import com.asteria.game.character.combat.Combat
import com.asteria.game.character.combat.CombatSessionData
import com.asteria.game.character.combat.CombatStrategy
import com.asteria.game.character.combat.CombatType
import com.asteria.game.character.combat.ranged.CombatRangedAmmo
import com.asteria.game.character.combat.weapon.FightStyle
import com.asteria.game.character.npc.Npc
import com.asteria.game.character.player.Player
import com.asteria.game.character.player.content.WeaponInterface
import com.asteria.game.item.Item
import com.asteria.game.item.container.Equipment
import com.asteria.game.plugin.PluginContext

@PluginContext(CombatStrategy.class)
final class DefaultRangedCombatStrategy implements CombatStrategy {

    @Override
    boolean canAttack(CharacterNode character, CharacterNode victim) {
        if (character.getType() == NodeType.NPC)
            return true
        Player player =  character as Player
        if (Combat.isCrystalBow(player))
            return true
        return checkAmmo(player)
    }

    @Override
    CombatSessionData attack(CharacterNode character, CharacterNode victim) {
        if (character.getType() == NodeType.NPC) {
            Npc npc = character as Npc
            CombatRangedAmmo ammo = Combat.prepareRangedAmmo(npc)
            character.animation new Animation(npc.getDefinition().getAttackAnimation())
            character.highGraphic new Graphic(ammo.getGraphic())
            new Projectile(character, victim, ammo.getProjectile(), ammo.getDelay(), ammo.getSpeed(), ammo.getStartHeight(), ammo
                    .getEndHeight(), 0).sendProjectile()
            return new CombatSessionData(character, victim, 1, CombatType.RANGED, true)
        }

        Player player = character as Player
        player.setRangedAmmo null
        player.setFireAmmo 0
        startAnimation player
        CombatRangedAmmo ammo = CombatRangedAmmo.getPlayerAmmo(player).get()
        player.setRangedAmmo ammo
        if (!Combat.isCrystalBow(player)) {
            decrementAmmo player
        }
        if (!player.isSpecialActivated()) {
            player.highGraphic new Graphic(ammo.getGraphic())
            new Projectile(player, victim, ammo.getProjectile(), ammo.getDelay(), ammo.getSpeed(), ammo.getStartHeight(), ammo
                    .getEndHeight(), 0).sendProjectile()
        }
        return new CombatSessionData(character, victim, 1, CombatType.RANGED, true)
    }

    @Override
    int attackDelay(CharacterNode character) {
        character.getAttackSpeed()
    }

    @Override
    int attackDistance(CharacterNode character) {
        if (character.getType() == NodeType.NPC)
            return 6
        Player player = character as Player
        return Combat.getRangedDistance(player.getWeapon()) + (player.getFightType().getStyle() == FightStyle.DEFENSIVE ? 2 : 0)
    }

    @Override
    int[] getNpcs() {
        [688] as int[]
    }

    private def startAnimation(Player player) {
        if (player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition().getName().startsWith("Karils")) {
            player.animation new Animation(2075)
        } else {
            player.animation new Animation(player.getFightType().getAnimation())
        }
    }

    private boolean checkAmmo(Player player) {
        Item item = player.getWeapon() == WeaponInterface.DART || player.getWeapon() == WeaponInterface.KNIFE || player
                .getWeapon() == WeaponInterface.JAVELIN || player.getWeapon() == WeaponInterface.THROWNAXE ? player.getEquipment()
                .get(Equipment.WEAPON_SLOT) : player.getEquipment().get(Equipment.ARROWS_SLOT)

        if (!Item.valid(item)) {
            player.getEncoder().sendMessage "You do not have enough ammo to use this ranged weapon."
            player.getCombatBuilder().reset()
            return false
        }
        if (player.getWeapon() == WeaponInterface.SHORTBOW || player.getWeapon() == WeaponInterface.LONGBOW) {
            if (!Combat.isArrows(player)) {
                player.getEncoder().sendMessage "You need to use arrows with your bow."
                player.getCombatBuilder().reset()
                return false
            }
        } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
            if (player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition().getName().startsWith("Karils") && !item
            .getDefinition().getName().endsWith("rack")) {
                player.getEncoder().sendMessage "You need to use bolt racks with this crossbow."
                player.getCombatBuilder().reset()
                return false
            } else if (!player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition().getName().startsWith("Karils") && !Combat
            .isBolts(player)) {
                player.getEncoder().sendMessage "You need to use bolts with your crossbow."
                player.getCombatBuilder().reset()
                return false
            }
        }
        return true
    }

    private def decrementAmmo(Player player) {
        int slot = player.getWeapon() == WeaponInterface.SHORTBOW || player.getWeapon() == WeaponInterface.LONGBOW || player
                .getWeapon() == WeaponInterface.CROSSBOW ? Equipment.ARROWS_SLOT : Equipment.WEAPON_SLOT

        player.setFireAmmo player.getEquipment().get(slot).getId()
        player.getEquipment().get(slot).decrementAmount()

        if (player.getEquipment().get(slot).getAmount() == 0) {
            player.getEncoder().sendMessage "That was your last piece of ammo!"
            player.getEquipment().set(slot, null)

            if (slot == Equipment.WEAPON_SLOT) {
                WeaponInterface.execute(player, null)
            }
        }

        if (slot == Equipment.WEAPON_SLOT) {
            player.getFlags().set Flag.APPEARANCE
        }
        player.getEquipment().refresh()
    }
}
