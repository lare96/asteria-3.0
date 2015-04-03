package plugin.combat

import com.asteria.game.NodeType
import com.asteria.game.World
import com.asteria.game.character.Animation
import com.asteria.game.character.CharacterNode
import com.asteria.game.character.Hit
import com.asteria.game.character.combat.*
import com.asteria.game.character.combat.magic.CombatSpells
import com.asteria.game.character.combat.prayer.CombatPrayer
import com.asteria.game.character.npc.Npc
import com.asteria.game.character.player.Player
import com.asteria.game.location.Location
import com.asteria.game.location.SquareLocation
import com.asteria.game.plugin.PluginSignature
import com.asteria.network.packet.PacketEncoder
import com.asteria.task.Task
import com.asteria.task.TaskHandler
import com.asteria.utility.RandomGen

@PluginSignature(CombatStrategy.class)
final class KingBlackDragonCombatStrategy implements CombatStrategy {

    private static RandomGen random = new RandomGen()

    @Override
    boolean canAttack(CharacterNode character, CharacterNode victim) {
        character.type == NodeType.NPC
    }

    @Override
    CombatSessionData attack(CharacterNode character, CharacterNode victim) {
        def data = character.position.withinDistance(victim.position, 2) ? [CombatType.MELEE, CombatType.RANGED, CombatType.MAGIC]
                : [CombatType.RANGED, CombatType.MAGIC]
        CombatType c = random.random(data)
        return type(character, victim, c)
    }

    @Override
    int attackDelay(CharacterNode character) {
        6
    }

    @Override
    int attackDistance(CharacterNode character) {
        8
    }

    @Override
    int[] getNpcs() {
        [50] as int[]
    }

    private CombatSessionData melee(CharacterNode character, victim) {
        def animation = [80, 91, 80]
        character.animation new Animation(random.random(animation))
        return new CombatSessionData(character, victim, 1
                , CombatType.MELEE, true)
    }

    private CombatSessionData magic(CharacterNode character, CharacterNode victim) {
        character.animation new Animation(81)
        TaskHandler.submit(new Task(1, false) {
            @Override
            void execute() {
                this.cancel()
                if (!character.registered || !victim.registered || victim.dead)
                    return
                CombatSpells.FIRE_BLAST.spell.projectile(character, victim).get().sendProjectile()
            }
        })
        character.currentlyCasting = CombatSpells.FIRE_BLAST.spell
        return new CombatSessionData(character, victim, 1, CombatType.MAGIC, false) {
            @Override
            CombatSessionData preAttack() {
                if (this.type == CombatType.MAGIC && victim.type == NodeType.PLAYER) {
                    Player player = victim as Player
                    if (!player.equipment.contains(1540) && player.fireImmunity.value <= 0) {
                        Arrays.fill(hits, null)
                        this.hits[0] = new CombatHit(new Hit(random.inclusive(35, 80)), true)
                        this.accurate = true
                        player.encoder.sendMessage "You do not have any protection against the dragonfire, the attack burns you!"
                    } else {
                        player.encoder.sendMessage "You are protected against the dragonfire."
                    }
                }
                return this
            }
        }
    }

    private CombatSessionData ranged(CharacterNode character, CharacterNode victim) {
        character.animation new Animation(81)
        def p = victim.position.copy()
        Location location = new SquareLocation(p.x, p.y, p.z, 5)
        TaskHandler.submit(new Task(2, false) {
            @Override
            void execute() {
                this.cancel()
                if (!character.registered || !victim.registered || victim.dead) {
                    return
                }
                10.times {
                    PacketEncoder.sendAllGraphic(446, location.random(), 0)
                }
                for (CharacterNode c : World.getCharacters()) {
                    if (location.inLocation(c.position) && c != victim && c != character) {
                        if (c.type == NodeType.NPC) {
                            if (!(c as Npc).getDefinition().attackable)
                                continue
                        }
                        int amount = Combat.calculateRandomHit(character, c, CombatType.RANGED).damage
                        if (amount > 40)
                            amount = 40
                        int half = (amount / 2) as int
                        int quarter = (amount / 4) as int
                        if (c.type == NodeType.PLAYER) {
                            if (CombatPrayer.isActivated((c as Player), CombatPrayer.PROTECT_FROM_MISSILES))
                                amount = 0
                        }
                        c.damage(new Hit(half), new Hit(quarter))
                        c.combatBuilder.damageCache.add(c, half + quarter)
                    }
                }
            }
        })
        return new CombatSessionData(character, victim, 2, CombatType.RANGED, true)
    }

    private CombatSessionData type(character, victim, type) {
        switch (type) {
            case CombatType.MELEE:
                return melee(character, victim)
            case CombatType.MAGIC:
                return magic(character, victim)
            case CombatType.RANGED:
                return ranged(character, victim)
        }
    }
}
