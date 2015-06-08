package plugin.items

import com.asteria.game.character.player.Player
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.context.ItemOnPlayerPlugin

@PluginSignature(ItemOnPlayerPlugin.class)
final class ItemOnPlayer implements PluginListener<ItemOnPlayerPlugin> {

    @Override
    void run(Player player, ItemOnPlayerPlugin context) {
    }
}
