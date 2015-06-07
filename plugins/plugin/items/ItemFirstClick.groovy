package plugin.items

import com.asteria.game.character.player.Player
import com.asteria.game.model.item.Item;
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.context.ItemFirstClickPlugin
import com.asteria.utility.RandomGen
import com.asteria.utility.Settings

@PluginSignature(ItemFirstClickPlugin.class)
final class ItemFirstClick implements PluginListener<ItemFirstClickPlugin> {

    private static RandomGen random = new RandomGen()

    @Override
    void run(Player player, ItemFirstClickPlugin context) {
        switch (context.item.id) {
            case 405:
                Item item = random.random Settings.CASKET_ITEMS
                if (player.inventory.add(item.copy())) {
                    player.inventory.remove(new Item(405), context.slot)
                    player.encoder.sendMessage "You open the casket and recieve an item!"
                }
                break
        }
    }
}
