package plugin.items

import com.asteria.game.character.player.Player
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.context.ItemFirstClickPlugin

@PluginSignature(ItemFirstClickPlugin.class)
final class ItemFirstClick implements PluginListener<ItemFirstClickPlugin> {

    @Override
    void execute(Player player, ItemFirstClickPlugin context) {
        switch (context.item.id) {
        }
    }
}
