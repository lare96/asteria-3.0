package plugin.items

import plugin.skills.cooking.CookingData

import com.asteria.game.character.player.Player
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.context.ItemOnObjectPlugin

@PluginSignature(ItemOnObjectPlugin.class)
final class ItemOnObject implements PluginListener<ItemOnObjectPlugin>{

    @Override
    void run(Player player, ItemOnObjectPlugin context) {
        switch (context.id) {
            case 114:
                CookingData c =  CookingData.getData(context.item.id)
                if(c == null)
                    return
                player.usingStove = true
                player.cookData = c
                player.cookPosition = context.position
                c.openInterface player
                break
            case 2732:
                CookingData c =  CookingData.getData(context.item.id)
                if(c == null)
                    return
                player.usingStove = false
                player.cookData = c
                player.cookPosition = context.position
                c.openInterface player
                break
        }

        switch (context.item.id) {
        }
    }
}
