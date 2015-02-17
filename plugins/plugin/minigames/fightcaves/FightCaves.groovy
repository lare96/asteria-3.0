package plugin.minigames.fightcaves

import java.util.concurrent.TimeUnit

import com.asteria.game.NodeType
import com.asteria.game.character.CharacterNode
import com.asteria.game.character.player.Player
import com.asteria.game.character.player.minigame.Minigame
import com.asteria.game.character.player.minigame.SequencedMinigame
import com.asteria.game.location.Position
import com.asteria.game.plugin.PluginSignature

@PluginSignature(Minigame.class)
final class FightCaves extends SequencedMinigame {

    FightCaves() {
        super("Fight Caves")
    }

    @Override
    void onSequence() {
        if(FightCavesHandler.timeoutCounter.elapsed(20, TimeUnit.MINUTES) && !FightCavesHandler.timeoutCounter.isStopped())
            FightCavesHandler.end true
        if(++FightCavesHandler.gameCounter >= FightCavesHandler.GAME_CYCLE_MINUTES && FightCavesHandler.players.size() == 0 && FightCavesHandler.awaiting.size() >= FightCavesHandler.PLAYERS_NEEDED)
            FightCavesHandler.start()
        FightCavesHandler.update()
    }

    @Override
    int delay() {
        100
    }

    @Override
    void onLogin(Player player) {
        onLogout player
    }

    @Override
    void onLogout(Player player) {
        FightCavesHandler.players.remove player
        player.combatBuilder.reset()
        player.move FightCavesHandler.DEATH_POSITION
        FightCavesHandler.end false
    }

    @Override
    void onDeath(Player player) {
        onLogout player
        player.encoder.sendContextMenu(3, "null")
        player.encoder.sendMessage "You have lost the battle inside the Fight Pits.."
        FightCavesHandler.end false
    }

    @Override
    void onKill(Player player, CharacterNode other) {
        FightCavesHandler.end false
    }

    @Override
    boolean contains(Player player) {
        FightCavesHandler.players.contains player
    }

    @Override
    Position deathPosition(Player player) {
        FightCavesHandler.DEATH_POSITION
    }

    @Override
    boolean canHit(Player player, CharacterNode other) {
        if(other.type == NodeType.PLAYER)
            return FightCavesHandler.players.contains (other as Player)
    }
}
