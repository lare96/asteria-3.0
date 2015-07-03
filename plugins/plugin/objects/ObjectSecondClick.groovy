package plugin.objects

import com.asteria.game.character.player.Player
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.context.ObjectSecondClickPlugin

@PluginSignature(ObjectSecondClickPlugin.class)
final class ObjectSecondClick implements PluginListener<ObjectSecondClickPlugin> {

    @Override
    void execute(Player player, ObjectSecondClickPlugin context) {
    }
}
