package com.asteria.network.packet.impl;

import com.asteria.game.World;
import com.asteria.game.character.Animation;
import com.asteria.game.character.Graphic;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.Rights;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemDefinition;
import com.asteria.game.location.Location;
import com.asteria.game.location.Position;
import com.asteria.game.location.SquareLocation;
import com.asteria.game.object.ObjectDirection;
import com.asteria.game.object.ObjectNode;
import com.asteria.game.object.ObjectNodeManager;
import com.asteria.network.ConnectionHandler;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;

/**
 * The packet that is sent from the client when the player chats anything
 * beginning with '::'.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class CommandPacket extends PacketDecoder {

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        String command = buf.getString().toLowerCase();
        String[] cmd = command.split(" ");

        // All commands are currently for 'developers' only, which is the
        // highest rank. For all of the other ranks look at the 'Rights'
        // class.
        if (player.getRights().greater(Rights.ADMINISTRATOR)) {
            switch (cmd[0]) {
            case "war":
                double radiusMod = 0.045;
                int monsters = Integer.parseInt(cmd[1]);
                int npcId = Integer.parseInt(cmd[2]);
                int npcId2 = Integer.parseInt(cmd[3]);
                Location l = new SquareLocation(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(),
                    (int) radiusMod * monsters);

                for (int i = 0; i < monsters; i++) {
                    World.getNpcs().add(new Npc(npcId, l.random()));
                }
                for (int i = 0; i < monsters; i++) {
                    World.getNpcs().add(new Npc(npcId2, l.random()));
                }
                player.getEncoder().sendMessage("You have started a war with " + (monsters * 2) + " monsters!");
                break;
            case "teleto":
                World.getPlayer(cmd[1].replaceAll("_", " ")).ifPresent(p -> {
                    player.move(p.getPosition());
                    player.getEncoder().sendMessage("You teleport to " + p.getFormatUsername() + "'s position.");
                });
                break;
            case "teletome":
                World.getPlayer(cmd[1].replaceAll("_", " ")).ifPresent(p -> {
                    p.move(player.getPosition());
                    p.getEncoder().sendMessage("You have been teleported to " + player.getFormatUsername() + "'s position.");
                });
                break;
            case "ipban":
                Player ipban = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);

                if (ipban != null && ipban.getRights().less(Rights.ADMINISTRATOR) && !ipban.equals(player)) {
                    player.getEncoder().sendMessage("Successfully IP banned " + player);
                    ConnectionHandler.addIPBan(ipban.getSession().getHost());
                    World.getPlayers().remove(ipban);
                }
                break;
            case "ban":
                Player ban = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);

                if (ban != null && ban.getRights().less(Rights.MODERATOR) && !ban.equals(player)) {
                    player.getEncoder().sendMessage("Successfully banned " + player);
                    ban.setBanned(true);
                    World.getPlayers().remove(ban);
                }
                break;
            case "master":
                for (int i = 0; i < player.getSkills().length; i++) {
                    Skills.experience(player, (Integer.MAX_VALUE - player.getSkills()[i].getExperience()), i);
                }
                break;
            case "tele":
                int x = Integer.parseInt(cmd[1]);
                int y = Integer.parseInt(cmd[2]);
                player.move(new Position(x, y, 0));
                break;
            case "npc":
                World.getNpcs().add(new Npc(Integer.parseInt(cmd[1]), player.getPosition()));
                break;
            case "dummy":
                Npc npc = new Npc(Integer.parseInt(cmd[1]), player.getPosition());
                npc.setCurrentHealth(100000);
                npc.setAutoRetaliate(false);
                World.getNpcs().add(npc);
                break;
            case "music":
                int id = Integer.parseInt(cmd[1]);
                player.getEncoder().sendMusic(id);
                break;
            case "item":
                String item = cmd[1].replaceAll("_", " ");
                int amount = Integer.parseInt(cmd[2]);
                player.getEncoder().sendMessage("Searching...");

                int count = 0;
                int bankCount = 0;
                boolean addedToBank = false;
                for (ItemDefinition i : ItemDefinition.DEFINITIONS) {
                    if (i == null || i.isNoted()) {
                        continue;
                    }

                    if (i.getName().toLowerCase().contains(item)) {
                        if (player.getInventory().spaceFor(new Item(i.getId(), amount))) {
                            player.getInventory().add(new Item(i.getId(), amount));
                        } else {
                            player.getBank().deposit(new Item(i.getId(), amount));
                            addedToBank = true;
                            bankCount++;
                        }
                        count++;
                    }
                }

                if (count == 0) {
                    player.getEncoder().sendMessage("Item [" + item + "] not found!");
                } else {
                    player.getEncoder().sendMessage("Item [" + item + "] found on " + count + " occurances.");
                }

                if (addedToBank) {
                    player.getEncoder().sendMessage(bankCount + " items were banked due to lack of inventory space!");
                }
                break;
            case "interface":
                player.getEncoder().sendInterface(Integer.parseInt(cmd[1]));
                break;
            case "sound":
                player.getEncoder().sendSound(Integer.parseInt(cmd[1]), 0, Integer.parseInt(cmd[2]));
                break;
            case "mypos":
                player.getEncoder().sendMessage("You are at: " + player.getPosition());
                break;
            case "pickup":
                player.getInventory().add(new Item(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2])));
                break;
            case "empty":
                player.getInventory().clear();
                player.getInventory().refresh();
                break;
            case "emptybank":
                player.getBank().clear();
                player.getBank().refresh();
                break;
            case "bank":
                player.getBank().open();
                break;
            case "emote":
                player.animation(new Animation(Integer.parseInt(cmd[1])));
                break;
            case "players":
                amount = World.getPlayers().size();
                player.getEncoder().sendMessage(
                    amount == 1 ? "There is currently 1 player online!" : "There are currently " + size + " players online!");
                break;
            case "gfx":
                player.graphic(new Graphic(Integer.parseInt(cmd[1])));
                break;
            case "object":
                ObjectNodeManager.register(new ObjectNode(Integer.parseInt(cmd[1]), player.getPosition(), ObjectDirection.SOUTH));
                break;
            default:
                player.getEncoder().sendMessage("Command [::" + cmd[0] + "] does not exist!");
                break;
            }
        }
    }
}
