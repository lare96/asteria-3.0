package plugin.npcs

import java.util.function.Consumer

import plugin.skills.fishing.Fishing
import plugin.skills.fishing.Tool

import com.asteria.game.character.player.Player
import com.asteria.game.character.player.dialogue.DialogueChainBuilder
import com.asteria.game.character.player.dialogue.GiveItemDialogue
import com.asteria.game.character.player.dialogue.NpcDialogue
import com.asteria.game.character.player.dialogue.OptionDialogue
import com.asteria.game.character.player.dialogue.OptionType
import com.asteria.game.character.player.dialogue.PlayerDialogue
import com.asteria.game.item.Item
import com.asteria.game.location.Position
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.context.NpcFirstClickPlugin
import com.asteria.game.shop.Shop

@PluginSignature(NpcFirstClickPlugin.class)
final class NpcFirstClick implements PluginListener<NpcFirstClickPlugin> {

    @Override
    void run(Player player, NpcFirstClickPlugin context) {
        int id = context.npc.id
        Position position = context.npc.position.copy()
        switch (id) {
            case 460:
                DialogueChainBuilder builder = player.dialogueChain
                builder.append new NpcDialogue(id, "Hello, this is the new dialogue system. It",
                        "should be easier to use.")
                builder.append new PlayerDialogue("Cool, I'm loving it already!")
                builder.append new NpcDialogue(id, "Would you like some money?")
                builder.append new OptionDialogue(["Yes please!", "No thanks."]) {
                    @Override
                    public Optional<Consumer<OptionType>> getOptionListener() {
                        return Optional
                                .of(new Consumer<OptionType>() {
                                    @Override
                                    public void accept(OptionType t) {
                                        if (t == OptionType.FIRST_OPTION) {
                                            builder.append new GiveItemDialogue(new Item(995, 1000),
                                                    "You receive 1000 gold coins.")
                                            builder.advance()
                                        } else if (t == OptionType.SECOND_OPTION) {
                                            player.encoder.sendCloseWindows()
                                        }
                                    }
                                })
                    }
                }
                builder.advance()
                break
            case 520:
                Shop.SHOPS.get("General Store").openShop player
                break
            case 233:
            case 234:
            case 235:
            case 236:
                Fishing fishing = new Fishing(player, Tool.FISHING_ROD, position)
                fishing.start()
                break
            case 309:
            case 310:
            case 311:
            case 314:
            case 315:
            case 317:
            case 318:
                Fishing fishing = new Fishing(player, Tool.FLY_FISHING_ROD, position)
                fishing.start()
                break
            case 312:
                Fishing fishing = new Fishing(player, Tool.LOBSTER_POT, position)
                fishing.start()
                break
            case 313:
                Fishing fishing = new Fishing(player, Tool.BIG_NET, position)
                fishing.start()
                break
            case 316:
            case 319:
                Fishing fishing = new Fishing(player, Tool.NET, position)
                fishing.start()
                break
        }
    }
}
