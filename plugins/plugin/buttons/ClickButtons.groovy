package plugin.buttons


import java.util.concurrent.TimeUnit
import java.util.function.Function
import java.util.logging.Logger

import plugin.skills.cooking.Cooking

import com.asteria.game.World
import com.asteria.game.character.combat.magic.CombatSpells
import com.asteria.game.character.combat.prayer.CombatPrayer
import com.asteria.game.character.combat.weapon.FightType
import com.asteria.game.character.player.Player
import com.asteria.game.character.player.content.Spellbook
import com.asteria.game.character.player.content.TradeStage
import com.asteria.game.character.player.content.WeaponInterface
import com.asteria.game.character.player.dialogue.OptionType
import com.asteria.game.character.player.minigame.Minigame
import com.asteria.game.character.player.minigame.MinigameHandler
import com.asteria.game.item.container.Equipment
import com.asteria.game.location.Position
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.context.ButtonClickPlugin
import com.asteria.task.Task
import com.asteria.task.TaskHandler
import com.asteria.utility.LoggerUtils
import com.asteria.utility.Settings

@PluginSignature(ButtonClickPlugin.class)
final class ClickButtons implements PluginListener<ButtonClickPlugin> {

    private static Logger logger = LoggerUtils.getLogger(ClickButtons.class)

    @Override
    void run(Player player, ButtonClickPlugin context) {
        int button = context.getId()
        switch (button) {
            case 53152:
                if (player.cookData != null) {
                    Cooking cooking = new Cooking(player, player.cookPosition, player.cookData, player.usingStove, 1)
                    cooking.start()
                }
                break
            case 53151:
                if (player.cookData != null) {
                    Cooking cooking = new Cooking(player, player.cookPosition, player.cookData, player.usingStove, 5)
                    cooking.start()
                }
                break
            case 53149:
                if (player.cookData != null) {
                    int amount = player.inventory.amount player.cookData.rawId
                    Cooking cooking = new Cooking(player, player.cookPosition, player.cookData, player.usingStove,
                            amount)
                    cooking.start()
                }
                break
            case 50235:
            case 4140:
                player.teleport new Position(3094, 3243)
                break
            case 21233:
                CombatPrayer.THICK_SKIN.activateOrDeactivate player
                break
            case 21234:
                CombatPrayer.BURST_OF_STRENGTH.activateOrDeactivate player
                break
            case 21235:
                CombatPrayer.CLARITY_OF_THOUGHT.activateOrDeactivate player
                break
            case 21236:
                CombatPrayer.ROCK_SKIN.activateOrDeactivate player
                break
            case 21237:
                CombatPrayer.SUPERHUMAN_STRENGTH.activateOrDeactivate player
                break
            case 21238:
                CombatPrayer.IMPROVED_REFLEXES.activateOrDeactivate player
                break
            case 21239:
                CombatPrayer.RAPID_RESTORE.activateOrDeactivate player
                break
            case 21240:
                CombatPrayer.RAPID_HEAL.activateOrDeactivate player
                break
            case 21241:
                CombatPrayer.PROTECT_ITEM.activateOrDeactivate player
                break
            case 21242:
                CombatPrayer.STEEL_SKIN.activateOrDeactivate player
                break
            case 21243:
                CombatPrayer.ULTIMATE_STRENGTH.activateOrDeactivate player
                break
            case 21244:
                CombatPrayer.INCREDIBLE_REFLEXES.activateOrDeactivate player
                break
            case 21245:
                CombatPrayer.PROTECT_FROM_MAGIC.activateOrDeactivate player
                break
            case 21246:
                CombatPrayer.PROTECT_FROM_MISSILES.activateOrDeactivate player
                break
            case 21247:
                CombatPrayer.PROTECT_FROM_MELEE.activateOrDeactivate player
                break
            case 2171:
                CombatPrayer.RETRIBUTION.activateOrDeactivate player
                break
            case 2172:
                CombatPrayer.REDEMPTION.activateOrDeactivate player
                break
            case 2173:
                CombatPrayer.SMITE.activateOrDeactivate player
                break
            case 48177:
                if (player.acceptAid) {
                    player.encoder.sendMessage "Accept aid has been turned off."
                    player.acceptAid = false
                }
                break
            case 48176:
                if (!player.acceptAid) {
                    player.encoder.sendMessage "Accept aid has been turned on."
                    player.acceptAid = true
                }
                break
            case 150:
                if (!player.autoRetaliate) {
                    player.autoRetaliate = true
                    player.encoder.sendMessage "Auto retaliate has been turned on!"
                }
                break
            case 151:
                if (player.autoRetaliate) {
                    player.autoRetaliate = false
                    player.encoder.sendMessage "Auto retaliate has been turned off!"
                }
                break
            case 56109:
                if (player.dialogueChain.executeOptions(OptionType.FIRST_OPTION))
                    return
                switch (player.option) {
                }
                break
            case 56110:
                if (player.dialogueChain.executeOptions(OptionType.SECOND_OPTION))
                    return
                switch (player.option) {
                }
                break

            case 9167:
                if (player.dialogueChain.executeOptions(OptionType.FIRST_OPTION))
                    return
                switch (player.option) {
                }
                break
            case 9168:
                if (player.dialogueChain.executeOptions(OptionType.SECOND_OPTION))
                    return
                switch (player.option) {
                }
                break
            case 9169:
                if (player.dialogueChain.executeOptions(OptionType.THIRD_OPTION))
                    return
                switch (player.option) {
                }
                break

            case 32017:
                if (player.dialogueChain.executeOptions(OptionType.FIRST_OPTION))
                    return
                switch (player.option) {
                }
                break
            case 32018:
                if (player.dialogueChain.executeOptions(OptionType.SECOND_OPTION))
                    return
                switch (player.option) {
                }
                break
            case 32019:
                if (player.dialogueChain.executeOptions(OptionType.THIRD_OPTION))
                    return
                switch (player.option) {
                }
                break
            case 32020:
                if (player.dialogueChain.executeOptions(OptionType.FOURTH_OPTION))
                    return
                switch (player.option) {
                }
                break

            case 32029:
                if (player.dialogueChain.executeOptions(OptionType.FIRST_OPTION))
                    return
                switch (player.option) {
                }
                break
            case 32030:
                if (player.dialogueChain.executeOptions(OptionType.SECOND_OPTION))
                    return
                switch (player.option) {
                }
                break
            case 32031:
                if (player.dialogueChain.executeOptions(OptionType.THIRD_OPTION))
                    return
                switch (player.option) {
                }
                break
            case 32032:
                if (player.dialogueChain.executeOptions(OptionType.FOURTH_OPTION))
                    return
                switch (player.option) {
                }
                break
            case 32033:
                if (player.dialogueChain.executeOptions(OptionType.FIFTH_OPTION))
                    return
                switch (player.option) {
                }
                break
            case 9154:
                if (!MinigameHandler.execute(player, true, new Function<Minigame, Boolean>() {
                    @Override
                    Boolean apply(Minigame t) {
                        t.canLogout(player)
                    }
                }))
                    return
                if (!player.lastCombat.elapsed(10, TimeUnit.SECONDS)) {
                    player.encoder.sendMessage "You must wait 10 seconds after combat before logging out."
                    return
                }
                World.players.remove(player)
                break
            case 153:
                if (player.runEnergy.get() == 0)
                    return
                player.movementQueue.running = true
                player.encoder.sendByteState(173, 1)
                break
            case 152:
                player.movementQueue.running = false
                player.encoder.sendByteState(173, 0)
                break
            case 21011:
                player.withdrawAsNote = false
                break
            case 21010:
                player.withdrawAsNote = true
                break
            case 31195:
                player.insertItem = true
                break
            case 31194:
                player.insertItem = false
                break
            case 13092:
                if (player.tradeSession.inTradeSession()) {
                    Player partner = player.tradeSession.other

                    if (!partner.tradeSession.inTradeSession())
                        return
                    if (partner.inventory.remaining() < player.tradeSession.container.size()) {
                        String username = partner.getFormatUsername()
                        player.encoder.sendMessage "${username} does not have enough free slots for this many items."
                        return
                    }
                    player.tradeSession.stage = TradeStage.FIRST_ACCEPT
                    player.encoder.sendString("Waiting for other player...", 3431)
                    partner.encoder.sendString("Other player has accepted", 3431)
                    if (player.tradeSession.getStage() == TradeStage.FIRST_ACCEPT && partner.tradeSession.getStage() == TradeStage.FIRST_ACCEPT) {
                        partner.tradeSession.execute TradeStage.FIRST_ACCEPT
                        player.tradeSession.execute TradeStage.FIRST_ACCEPT
                    }
                }
                break
            case 13218:
                if (player.tradeSession.inTradeSession()) {
                    Player partner = player.tradeSession.other
                    if (!partner.tradeSession.inTradeSession())
                        return
                    player.tradeSession.stage = TradeStage.FINAL_ACCEPT
                    partner.encoder.sendString("Other player has accepted.", 3535)
                    player.encoder.sendString("Waiting for other player...", 3535)

                    if (player.tradeSession.getStage() == TradeStage.FINAL_ACCEPT && partner.tradeSession.getStage() == TradeStage.FINAL_ACCEPT) {
                        player.tradeSession.execute TradeStage.FINAL_ACCEPT
                        partner.tradeSession.execute TradeStage.FINAL_ACCEPT
                    }
                }
                break

            case 1080: // staff
                player.fightType = FightType.STAFF_BASH
                break
            case 1079:
                player.fightType = FightType.STAFF_POUND
                break
            case 1078:
                player.fightType = FightType.STAFF_FOCUS
                break
            case 1177: // warhammer
                player.fightType = FightType.WARHAMMER_POUND
                break
            case 1176:
                player.fightType = FightType.WARHAMMER_PUMMEL
                break
            case 1175:
                player.fightType = FightType.WARHAMMER_BLOCK
                break
            case 3014: // scythe
                player.fightType = FightType.SCYTHE_REAP
                break
            case 3017:
                player.fightType = FightType.SCYTHE_CHOP
                break
            case 3016:
                player.fightType = FightType.SCYTHE_JAB
                break
            case 3015:
                player.fightType = FightType.SCYTHE_BLOCK
                break
            case 6168: // battle axe
                player.fightType = FightType.BATTLEAXE_CHOP
                break
            case 6171:
                player.fightType = FightType.BATTLEAXE_HACK
                break
            case 6170:
                player.fightType = FightType.BATTLEAXE_SMASH
                break
            case 6169:
                player.fightType = FightType.BATTLEAXE_BLOCK
                break
            case 6221: // crossbow
                player.fightType = FightType.CROSSBOW_ACCURATE
                break
            case 6220:
                player.fightType = FightType.CROSSBOW_RAPID
                break
            case 6219:
                player.fightType = FightType.CROSSBOW_LONGRANGE
                break
            case 6236: // shortbow & longbow
                if (player.weapon == WeaponInterface.SHORTBOW) {
                    player.fightType = FightType.SHORTBOW_ACCURATE
                } else if (player.weapon == WeaponInterface.LONGBOW) {
                    player.fightType = FightType.LONGBOW_ACCURATE
                }
                break
            case 6235:
                if (player.weapon == WeaponInterface.SHORTBOW) {
                    player.fightType = FightType.SHORTBOW_RAPID
                } else if (player.weapon == WeaponInterface.LONGBOW) {
                    player.fightType = FightType.LONGBOW_RAPID
                }
                break
            case 6234:
                if (player.weapon == WeaponInterface.SHORTBOW) {
                    player.fightType = FightType.SHORTBOW_LONGRANGE
                } else if (player.weapon == WeaponInterface.LONGBOW) {
                    player.fightType = FightType.LONGBOW_LONGRANGE
                }
                break
            case 8234: // dagger & sword
                if (player.weapon == WeaponInterface.DAGGER) {
                    player.fightType = FightType.DAGGER_STAB
                } else if (player.weapon == WeaponInterface.SWORD) {
                    player.fightType = FightType.SWORD_STAB
                }
                break
            case 8237:
                if (player.weapon == WeaponInterface.DAGGER) {
                    player.fightType = FightType.DAGGER_LUNGE
                } else if (player.weapon == WeaponInterface.SWORD) {
                    player.fightType = FightType.SWORD_LUNGE
                }
                break
            case 8236:
                if (player.weapon == WeaponInterface.DAGGER) {
                    player.fightType = FightType.DAGGER_SLASH
                } else if (player.weapon == WeaponInterface.SWORD) {
                    player.fightType = FightType.SWORD_SLASH
                }
                break
            case 8235:
                if (player.weapon == WeaponInterface.DAGGER) {
                    player.fightType = FightType.DAGGER_BLOCK
                } else if (player.weapon == WeaponInterface.SWORD) {
                    player.fightType = FightType.SWORD_BLOCK
                }
                break
            case 9125: // scimitar & longsword
                if (player.weapon == WeaponInterface.SCIMITAR) {
                    player.fightType = FightType.SCIMITAR_CHOP
                } else if (player.weapon == WeaponInterface.LONGSWORD) {
                    player.fightType = FightType.LONGSWORD_CHOP
                }
                break
            case 9128:
                if (player.weapon == WeaponInterface.SCIMITAR) {
                    player.fightType = FightType.SCIMITAR_SLASH
                } else if (player.weapon == WeaponInterface.LONGSWORD) {
                    player.fightType = FightType.LONGSWORD_SLASH
                }
                break
            case 9127:
                if (player.weapon == WeaponInterface.SCIMITAR) {
                    player.fightType = FightType.SCIMITAR_LUNGE
                } else if (player.weapon == WeaponInterface.LONGSWORD) {
                    player.fightType = FightType.LONGSWORD_LUNGE
                }
                break
            case 9126:
                if (player.weapon == WeaponInterface.SCIMITAR) {
                    player.fightType = FightType.SCIMITAR_BLOCK
                } else if (player.weapon == WeaponInterface.LONGSWORD) {
                    player.fightType = FightType.LONGSWORD_BLOCK
                }
                break
            case 14218: // mace
                player.fightType = FightType.MACE_POUND
                break
            case 14221:
                player.fightType = FightType.MACE_PUMMEL
                break
            case 14220:
                player.fightType = FightType.MACE_SPIKE
                break
            case 14219:
                player.fightType = FightType.MACE_BLOCK
                break
            case 17102: // knife, thrownaxe, dart & javelin
                if (player.weapon == WeaponInterface.KNIFE) {
                    player.fightType = FightType.KNIFE_ACCURATE
                } else if (player.weapon == WeaponInterface.THROWNAXE) {
                    player.fightType = FightType.THROWNAXE_ACCURATE
                } else if (player.weapon == WeaponInterface.DART) {
                    player.fightType = FightType.DART_ACCURATE
                } else if (player.weapon == WeaponInterface.JAVELIN) {
                    player.fightType = FightType.JAVELIN_ACCURATE
                }
                break
            case 17101:
                if (player.weapon == WeaponInterface.KNIFE) {
                    player.fightType = FightType.KNIFE_RAPID
                } else if (player.weapon == WeaponInterface.THROWNAXE) {
                    player.fightType = FightType.THROWNAXE_RAPID
                } else if (player.weapon == WeaponInterface.DART) {
                    player.fightType = FightType.DART_RAPID
                } else if (player.weapon == WeaponInterface.JAVELIN) {
                    player.fightType = FightType.JAVELIN_RAPID
                }
                break
            case 17100:
                if (player.weapon == WeaponInterface.KNIFE) {
                    player.fightType = FightType.KNIFE_LONGRANGE
                } else if (player.weapon == WeaponInterface.THROWNAXE) {
                    player.fightType = FightType.THROWNAXE_LONGRANGE
                } else if (player.weapon == WeaponInterface.DART) {
                    player.fightType = FightType.DART_LONGRANGE
                } else if (player.weapon == WeaponInterface.JAVELIN) {
                    player.fightType = FightType.JAVELIN_LONGRANGE
                }
                break
            case 18077: // spear
                player.fightType = FightType.SPEAR_LUNGE
                break
            case 18080:
                player.fightType = FightType.SPEAR_SWIPE
                break
            case 18079:
                player.fightType = FightType.SPEAR_POUND
                break
            case 18078:
                player.fightType = FightType.SPEAR_BLOCK
                break
            case 18103: // 2h sword
                player.fightType = FightType.TWOHANDEDSWORD_CHOP
                break
            case 15106:
                player.fightType = FightType.TWOHANDEDSWORD_SLASH
                break
            case 18105:
                player.fightType = FightType.TWOHANDEDSWORD_SMASH
                break
            case 18104:
                player.fightType = FightType.TWOHANDEDSWORD_BLOCK
                break
            case 21200: // pickaxe
                player.fightType = FightType.PICKAXE_SPIKE
                break
            case 21203:
                player.fightType = FightType.PICKAXE_IMPALE
                break
            case 21202:
                player.fightType = FightType.PICKAXE_SMASH
                break
            case 21201:
                player.fightType = FightType.PICKAXE_BLOCK
                break
            case 30088: // claws
                player.fightType = FightType.CLAWS_CHOP
                break
            case 30091:
                player.fightType = FightType.CLAWS_SLASH
                break
            case 30090:
                player.fightType = FightType.CLAWS_LUNGE
                break
            case 30089:
                player.fightType = FightType.CLAWS_BLOCK
                break
            case 33018: // halberd
                player.fightType = FightType.HALBERD_JAB
                break
            case 33020:
                player.fightType = FightType.HALBERD_SWIPE
                break
            case 33016:
                player.fightType = FightType.HALBERD_FEND
                break
            case 22228: // unarmed
                player.fightType = FightType.UNARMED_PUNCH
                break
            case 22230:
                player.fightType = FightType.UNARMED_KICK
                break
            case 22229:
                player.fightType = FightType.UNARMED_BLOCK
                break
            case 48010: // whip
                player.fightType = FightType.WHIP_FLICK
                break
            case 48009:
                player.fightType = FightType.WHIP_LASH
                break
            case 48008:
                player.fightType = FightType.WHIP_DEFLECT
                break
            case 24017:
            case 7212:
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                break
            case 1093:
            case 1094:
            case 1097:
                if (player.autocast) {
                    player.castSpell = null
                    player.autocastSpell = null
                    player.autocast = false
                    player.encoder.sendByteState(108, 0)
                } else if (!player.autocast) {
                    if (player.getEquipment().getId(Equipment.WEAPON_SLOT) == 4675) {
                        if (player.getSpellbook() != Spellbook.ANCIENT) {
                            player.encoder.sendMessage "You can only autocast ancient magics with this staff."
                            return
                        }

                        player.encoder.sendSidebarInterface(0, 1689)
                    } else {
                        if (player.getSpellbook() != Spellbook.NORMAL) {
                            player.encoder.sendMessage "You can only autocast standard magics with this staff."
                            return
                        }

                        player.encoder.sendSidebarInterface(0, 1829)
                    }
                }
                break

            case 51133:
                player.autocastSpell = CombatSpells.SMOKE_RUSH.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51185:
                player.autocastSpell = CombatSpells.SHADOW_RUSH.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51091:
                player.autocastSpell = CombatSpells.BLOOD_RUSH.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 24018:
                player.autocastSpell = CombatSpells.ICE_RUSH.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51159:
                player.autocastSpell = CombatSpells.SMOKE_BURST.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51211:
                player.autocastSpell = CombatSpells.SHADOW_BURST.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51111:
                player.autocastSpell = CombatSpells.BLOOD_BURST.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51069:
                player.autocastSpell = CombatSpells.ICE_BURST.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51146:
                player.autocastSpell = CombatSpells.SMOKE_BLITZ.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51198:
                player.autocastSpell = CombatSpells.SHADOW_BLITZ.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51102:
                player.autocastSpell = CombatSpells.BLOOD_BLITZ.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51058:
                player.autocastSpell = CombatSpells.ICE_BLITZ.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51172:
                player.autocastSpell = CombatSpells.SMOKE_BARRAGE.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51224:
                player.autocastSpell = CombatSpells.SHADOW_BARRAGE.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51122:
                player.autocastSpell = CombatSpells.BLOOD_BARRAGE.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 51080:
                player.autocastSpell = CombatSpells.ICE_BARRAGE.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7038:
                player.autocastSpell = CombatSpells.WIND_STRIKE.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7039:
                player.autocastSpell = CombatSpells.WATER_STRIKE.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7040:
                player.autocastSpell = CombatSpells.EARTH_STRIKE.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7041:
                player.autocastSpell = CombatSpells.FIRE_STRIKE.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7042:
                player.autocastSpell = CombatSpells.WIND_BOLT.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7043:
                player.autocastSpell = CombatSpells.WATER_BOLT.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7044:
                player.autocastSpell = CombatSpells.EARTH_BOLT.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7045:
                player.autocastSpell = CombatSpells.FIRE_BOLT.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7046:
                player.autocastSpell = CombatSpells.WIND_BLAST.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7047:
                player.autocastSpell = CombatSpells.WATER_BLAST.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7048:
                player.autocastSpell = CombatSpells.EARTH_BLAST.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7049:
                player.autocastSpell = CombatSpells.FIRE_BLAST.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7050:
                player.autocastSpell = CombatSpells.WIND_WAVE.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7051:
                player.autocastSpell = CombatSpells.WATER_WAVE.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7052:
                player.autocastSpell = CombatSpells.EARTH_WAVE.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 7053:
                player.autocastSpell = CombatSpells.FIRE_WAVE.getSpell()
                player.autocast = true
                player.encoder.sendSidebarInterface(0, player.weapon.getId())
                player.encoder.sendByteState(108, 3)
                break
            case 29138:
            case 29038:
            case 29063:
            case 29113:
            case 29163:
            case 29188:
            case 29213:
            case 29238:
            case 30007:
            case 48023:
            case 33033:
            case 30108:
                if (player.combatSpecial== null) {
                    return
                }

                if (player.specialActivated) {
                    player.encoder.sendByteState(301, 0)
                    player.specialActivated = false
                } else {
                    if (player.specialPercentage.value < player.combatSpecial.amount) {
                        player.encoder.sendMessage "You do not have enough special energy left!"
                        return
                    }

                    player.encoder.sendByteState(301, 1)
                    player.specialActivated = true

                    TaskHandler.submit(new Task(1, false) {
                                @Override
                                void execute() {
                                    if (!player.specialActivated) {
                                        this.cancel()
                                        return
                                    }

                                    player.combatSpecial.onActivation(player, player.combatBuilder.currentVictim)
                                    this.cancel()
                                }
                            }.attach(player))
                }
                break
            default:
                if (Settings.DEBUG)
                    logger.info "Unhandled button: ${button}"
                break
        }
    }
}
