package plugin.items

import com.asteria.game.character.player.Player
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.context.ItemOnItemPlugin

@PluginSignature(ItemOnItemPlugin.class)
final class ItemOnItem implements PluginListener<ItemOnItemPlugin> {

    @Override
    void run(Player player, ItemOnItemPlugin context) {
        switch (context.itemUsed.id) {
        }
        switch (context.itemOn.id) {
        }
    }
}
