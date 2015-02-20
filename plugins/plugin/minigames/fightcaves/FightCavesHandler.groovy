package plugin.minigames.fightcaves

import com.asteria.game.World
import com.asteria.game.character.Animation
import com.asteria.game.character.AnimationPriority
import com.asteria.game.character.Flag
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
    static final int PLAYERS_NEEDED = 2
    static String currentChampion = null
    static Queue<Player> awaiting = new ArrayDeque<>()
    static Set<Player> players = new HashSet<>()
    static int gameCounter = 0
    static Stopwatch timeoutCounter = new Stopwatch()

    private FightCavesHandler() {
        throw new UnsupportedOperationException("This class cannot be instantiated!")
    }

    static def start() {
        timeoutCounter.reset()
        RandomGen random = new RandomGen()
        Player player = null
        while((player = awaiting.poll()) != null) {
            if(player.viewingOrb != null) {
                player.viewingOrb.close()
                player.viewingOrb = null
            }
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
        if(players.size() > 1 && !timeout)
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
                it.animation new Animation(65535, AnimationPriority.HIGH)
                CombatPrayer.deactivateAll it
                Skills.restoreAll it
                it.move FightCavesHandler.DEATH_POSITION
                it.encoder.sendMessage "The round took too long, no one has won!"
            }
        } else {
            def player = World.getPlayer currentChampion
            if(player.present) {
                player.get().skullIcon = -1
                player.get().flags.set Flag.APPEARANCE
            }
            players.each {
                if(!it.registered)
                    return
                currentChampion = it.getFormatUsername()
                it.inventory.add new Item(6529, 100)
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
                it.skullIcon = Player.RED_SKULL
                it.flags.set Flag.APPEARANCE
            }
        }
        players.clear()
        gameCounter = 0
        timeoutCounter.stop()
    }

    static def display(Player player) {
        int minutes = GAME_CYCLE_MINUTES - gameCounter
        minutes = minutes < 0 ? 1 : minutes
        String champion = currentChampion ?: "Xil"
        player.encoder.sendString("Current champion: JalYt-Ket-${champion}", 2805)
        player.encoder.sendString(players.size() >= 1 ? "Game currently in progress!" : awaiting.size() <= 1 ? "Waiting for more players!" : "Minutes Left: ${minutes}", 2806)
        if(champion != null)
            player.encoder.sendByteState(560, champion.equalsIgnoreCase(player.username) ? 0 : 1)
    }

    static boolean remove(Player player) {
        awaiting.remove player
    }

    static boolean isChampion(Player player) {
        player.username.equalsIgnoreCase(currentChampion)
    }
}
