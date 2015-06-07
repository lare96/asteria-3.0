package plugin.objects

import com.asteria.game.character.player.Player
import com.asteria.game.character.player.content.Spellbook
import com.asteria.game.character.player.content.ViewingOrb
import com.asteria.game.character.player.skill.Skills
import com.asteria.game.location.Position;
import com.asteria.game.model.character.Animation;
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.context.ObjectFirstClickPlugin

import plugin.minigames.fightcaves.FightCavesHandler

@PluginSignature(ObjectFirstClickPlugin.class)
final class ObjectFirstClick implements PluginListener<ObjectFirstClickPlugin> {

    @Override
    void run(Player player, ObjectFirstClickPlugin context) {
        switch (context.id) {
            case 9391:
                player.viewingOrb = new ViewingOrb(player, new Position(2398, 5150),
                        new Position(2384, 5157), new Position(2409, 5158), new Position(2388, 5138),
                        new Position(2411, 5137))
                player.viewingOrb.open()
                break
            case 9368:
                if (player.position.y <= 5167 && FightCavesHandler.players.remove(player)) {
                    player.move new Position(2399, 5169)
                    player.combatBuilder.reset()
                    FightCavesHandler.awaiting.add player
                    player.encoder.sendMessage "You forfeit the fight pits minigame!"
                    player.encoder.sendWalkable 2804
                    FightCavesHandler.display player
                    player.encoder.sendContextMenu(1, "null")
                    FightCavesHandler.end false
                }
                break
            case 9369:
                if (FightCavesHandler.awaiting.contains(player)) {
                    FightCavesHandler.awaiting.remove player
                    player.encoder.sendMessage "You exit the fight pits minigame waiting room!"
                    player.move new Position(2399, 5177)
                    player.encoder.sendWalkable(-1)
                } else if (!FightCavesHandler.awaiting.contains(player)) {
                    int minutes = FightCavesHandler.GAME_CYCLE_MINUTES - FightCavesHandler.gameCounter
                    FightCavesHandler.awaiting.add player
                    player.encoder.sendMessage "You enter the fight pits minigame waiting room!"
                    player.move new Position(2399, 5175)
                    player.encoder.sendWalkable 2804
                    FightCavesHandler.display player
                    FightCavesHandler.awaiting.each {
                        FightCavesHandler.display it
                    }
                }
                break
            case 3193:
            case 2213:
                player.bank.open()
                break
            case 409:
                int level = player.skills[Skills.PRAYER].realLevel
                if (player.skills[Skills.PRAYER].level < level) {
                    player.animation new Animation(645)
                    player.skills[Skills.PRAYER].setLevel(level, true)
                    player.encoder.sendMessage "You recharge your prayer points."
                    Skills.refresh(player, Skills.PRAYER)
                } else {
                    player.encoder.sendMessage "You already have full prayer points."
                }
                break
            case 6552:
                if (player.spellbook == Spellbook.ANCIENT) {
                    Spellbook.convert(player, Spellbook.NORMAL)
                } else if (player.spellbook == Spellbook.NORMAL) {
                    Spellbook.convert(player, Spellbook.ANCIENT)
                }
                break
        }
    }
}
