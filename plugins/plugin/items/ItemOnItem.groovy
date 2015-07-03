package plugin.items

import com.asteria.game.character.player.Player
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.context.ItemOnItemPlugin

@PluginSignature(ItemOnItemPlugin.class)
final class ItemOnItem implements PluginListener<ItemOnItemPlugin> {

    @Override
    void execute(Player player, ItemOnItemPlugin context) {
    }
}
