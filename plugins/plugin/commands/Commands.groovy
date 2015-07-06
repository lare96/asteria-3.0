package plugin.commands

import com.asteria.game.World
import com.asteria.game.character.Animation
import com.asteria.game.character.Flag
import com.asteria.game.character.Graphic
import com.asteria.game.character.npc.Npc
import com.asteria.game.character.npc.drop.NpcDropManager
import com.asteria.game.character.npc.drop.NpcDropTable
import com.asteria.game.character.player.Player
import com.asteria.game.character.player.Rights
import com.asteria.game.character.player.serialize.PlayerSerialization
import com.asteria.game.character.player.skill.SkillData
import com.asteria.game.character.player.skill.Skills
import com.asteria.game.item.Item
import com.asteria.game.item.ItemDefinition
import com.asteria.game.item.container.Bank
import com.asteria.game.location.Position
import com.asteria.game.object.ObjectDirection
import com.asteria.game.object.ObjectNode
import com.asteria.game.object.ObjectNodeManager
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.context.CommandPlugin
import com.asteria.net.ConnectionHandler

@PluginSignature(CommandPlugin.class)
final class Commands implements PluginListener<CommandPlugin> {

    @Override
    void execute(Player player, CommandPlugin context) {
        String[] cmd = context.text

        // All commands are currently for 'developers' only, which is the
        // highest rank. For all of the other ranks look at the 'Rights'
        // class.
        if (player.rights.greater(Rights.ADMINISTRATOR)) {
            switch (cmd[0]) {
                case "s":
                    int id = Integer.parseInt cmd[1]
                    int amount = Integer.parseInt cmd[2]
                    NpcDropTable table = NpcDropManager.TABLES.get id
                    Bank bank = new Bank(player)
                    amount.times {
                        table.toItems(player).each { bank.deposit it }
                    }
                    bank.open();
                    break
                case "pnpc":
                    int id = Integer.parseInt cmd[1]
                    player.playerNpc = id
                    player.flags.set Flag.APPEARANCE
                    break
                case "invisible":
                    player.visible = false
                    break
                case "visible":
                    player.visible = true
                    break
                case "save":
                    World.players.each {
                        World.getService().submit({
                            ->
                            new PlayerSerialization(it).serialize()
                        })
                    }
                    player.messages.sendMessage "Character files have been saved for everyone online!"
                    break
                case "setlevel":
                    String skill = cmd[1]
                    int level = Integer.parseInt cmd[2]
                    SkillData.values().each {
                        if (it.toString().equalsIgnoreCase(skill)) {
                            player.skills[it.id].setLevel(level, false)
                            Skills.refresh(player, it.id)
                        }
                    }
                    break
                case "teleto":
                    Player p = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null)
                    if (p == null)
                        return
                    player.move p.position
                    player.messages.sendMessage "You teleport to ${p.getFormatUsername()}'s position."
                    break
                case "teletome":
                    Player p = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null)
                    if (p == null)
                        return
                    p.move player.position
                    p.messages.sendMessage "You have been teleported to ${player.getFormatUsername()}'s position."
                    break
                case "ipban":
                    Player ipban = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null)

                    if (ipban != null && ipban.rights.less(Rights.ADMINISTRATOR) && ipban != player) {
                        player.messages.sendMessage "Successfully IP banned ${ipban.getFormatUsername()}"
                        ConnectionHandler.addIPBan ipban.session.host
                        World.players.remove ipban
                    }
                    break
                case "ban":
                    Player ban = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null)

                    if (ban != null && ban.rights.less(Rights.MODERATOR) && ban != player) {
                        player.messages.sendMessage "Successfully banned ${ban.getFormatUsername()}"
                        ban.banned = true
                        World.players.remove ban
                    }
                    break
                case "master":
                    player.skills.length.times {
                        Skills.experience(player, (Integer.MAX_VALUE - player.skills[it].experience), it)
                    }
                    break
                case "tele":
                    int x = Integer.parseInt cmd[1]
                    int y = Integer.parseInt cmd[2]
                    player.move new Position(x, y, 0)
                    break
                case "npc":
                    Npc n = new Npc(Integer.parseInt(cmd[1]), player.position)
                    if(cmd.length == 3 && cmd[2].equals("true"))
                        n.respawn = true
                    World.npcs.add n
                    break
                case "dummy":
                    Npc npc = new Npc(Integer.parseInt(cmd[1]), player.position)
                    npc.currentHealth = 100000
                    npc.autoRetaliate = false
                    World.npcs.add npc
                    break
                case "music":
                    int id = Integer.parseInt cmd[1]
                    player.messages.sendMusic id
                    break
                case "item":
                    String item = cmd[1].replaceAll("_", " ")
                    int amount = Integer.parseInt cmd[2]
                    player.messages.sendMessage "Searching..."
                    int occurances = 0
                    int bankCount = 0
                    boolean addedToBank = false
                    ItemDefinition.DEFINITIONS.each {
                        if (it == null || it.isNoted())
                            return
                        if (it.name.toLowerCase().contains(item)) {
                            if (player.inventory.spaceFor(new Item(it.getId(), amount))) {
                                player.inventory.add new Item(it.getId(), amount)
                            } else {
                                player.bank.deposit new Item(it.getId(), amount)
                                addedToBank = true
                                bankCount++
                            }
                            occurances++
                        }
                    }

                    if (occurances == 0) {
                        player.messages.sendMessage "Item [${item}] not found!"
                    } else {
                        player.messages.sendMessage "Item [${item}] found on ${occurances} occurances."
                    }

                    if (addedToBank) {
                        player.messages.sendMessage bankCount + " items were banked due to lack of inventory space!"
                    }
                    break
                case "interface":
                    player.messages.sendInterface Integer.parseInt(cmd[1])
                    break
                case "sound":
                    player.messages.sendSound(Integer.parseInt(cmd[1]), 0, Integer.parseInt(cmd[2]))
                    break
                case "mypos":
                    player.messages.sendMessage "You are at: ${player.position}"
                    break
                case "pickup":
                    player.inventory.add new Item(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]))
                    break
                case "empty":
                    player.inventory.clear()
                    player.inventory.refresh()
                    break
                case "emptybank":
                    player.bank.clear()
                    player.bank.refresh()
                    break
                case "bank":
                    player.bank.open()
                    break
                case "emote":
                    player.animation new Animation(Integer.parseInt(cmd[1]))
                    break
                case "players":
                    int players = World.players.size()
                    player.messages.sendMessage
                    players == 1 ? "There is currently 1 player online!" : "There are currently ${players} players online!"
                    break
                case "gfx":
                    player.graphic new Graphic(Integer.parseInt(cmd[1]))
                    break
                case "object":
                    ObjectNodeManager.register new ObjectNode(Integer.parseInt(cmd[1]), player.position, ObjectDirection.SOUTH)
                    break
                default:
                    player.messages.sendMessage "Command [::" + cmd[0] + "] does not exist!"
                    break
            }
        }
    }
}
