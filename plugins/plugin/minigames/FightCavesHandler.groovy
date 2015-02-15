package plugin.minigames

import com.asteria.game.World
import com.asteria.game.character.Animation
import com.asteria.game.character.combat.prayer.CombatPrayer
import com.asteria.game.character.player.Player
import com.asteria.game.character.player.skill.Skills
import com.asteria.game.item.Item
import com.asteria.game.location.Position
import com.asteria.utility.RandomGen
import com.asteria.utility.Stopwatch

final class FightCavesHandler {

    static final Position DEATH_POSITION = new Position(2399, 5177)
    static final int GAME_CYCLE_MINUTES = 5
    static String currentChampion = "Xil"
    static Queue<Player> awaiting = new ArrayDeque<>()
    static Set<Player> players = new HashSet<>()
    static int gameCounter = 0
    private static Stopwatch timeoutCounter = new Stopwatch()

    private FightCavesHandler() {
        throw new UnsupportedOperationException()
    }

    static def start() {
        timeoutCounter.reset()
        RandomGen random = new RandomGen()
        Player player = null
        while((player = awaiting.poll()) != null) {
            player.encoder.sendContextMenu(3, "Attack")
            player.encoder.sendWalkable(-1)
            player.move new Position(2392 + random.exclusive(12), 5139 + random.exclusive(25))
            player.encoder.sendMessage "The fight pits have begun, good luck!"
            players.add player
        }
    }

    static def update() {
        awaiting.each { display it }
    }

    static def end(boolean timeout) {
        if(players.size() > 1)
            return
        if(timeout) {
            players.each {
                if(!it.registered)
                    return
                it.encoder.sendContextMenu(3, "null")
                it.encoder.sendWalkable(-1)
                it.combatBuilder.reset()
                it.specialPercentage.value = 100
                it.specialActivated = false
                it.animation new Animation(65535)
                CombatPrayer.deactivateAll it
                Skills.restoreAll it
                it.move FightCavesHandler.DEATH_POSITION
                it.encoder.sendMessage "The round took too long, no one has won!"
            }
        } else {
            players.each {
                if(!it.registered)
                    return
                currentChampion = it.getFormatUsername()
                it.inventory.add new Item(995, 50000)
                it.encoder.sendMessage "Congratulations, you have won the fight pits minigame!"
                World.message "${currentChampion} has won the fight pits minigame!"
                it.encoder.sendContextMenu(3, "null")
                it.encoder.sendWalkable(-1)
                it.combatBuilder.reset()
                it.specialPercentage.value = 100
                it.specialActivated = false
                it.animation new Animation(65535)
                CombatPrayer.deactivateAll it
                Skills.restoreAll it
                it.move FightCavesHandler.DEATH_POSITION
            }
        }
        players.clear()
        gameCounter = 0
        timeoutCounter.stop()
    }

    static def display(Player player) {
        int minutes = GAME_CYCLE_MINUTES - gameCounter
        player.encoder.sendString("Current champion: JalYt-Ket-${currentChampion}", 2805)
        player.encoder.sendString(players.size() >= 1 ? "Game currently in progress!" : awaiting.size() <= 1 ? "Waiting for more players!" : "Minutes Left: ${minutes}", 2806)
        player.encoder.sendByteState(560, currentChampion.equalsIgnoreCase(player.username) ? 0 : 1)
    }

    static boolean remove(Player player) {
        awaiting.remove player
    }
}
