package plugin.npcs

import com.asteria.game.character.player.Player
import com.asteria.game.location.Position;
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.context.NpcSecondClickPlugin

import plugin.skills.fishing.Fishing
import plugin.skills.fishing.Tool

@PluginSignature(NpcSecondClickPlugin.class)
final class NpcSecondClick implements PluginListener<NpcSecondClickPlugin> {

    @Override
    void execute(Player player, NpcSecondClickPlugin context) {
        Position position = context.npc.position.copy()
        switch (context.npc.id) {
            case 309:
            case 310:
            case 311:
            case 314:
            case 315:
            case 317:
            case 318:
                Fishing fishing = new Fishing(player, Tool.FISHING_ROD, position)
                fishing.start()
                break
            case 312:
                Fishing fishing = new Fishing(player, Tool.HARPOON, position)
                fishing.start()
                break
            case 313:
                Fishing fishing = new Fishing(player, Tool.SHARK_HARPOON, position)
                fishing.start()
                break
            case 316:
            case 319:
                Fishing fishing = new Fishing(player, Tool.FISHING_ROD, position)
                fishing.start()
                break
        }
    }
}