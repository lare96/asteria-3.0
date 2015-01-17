package com.asteria.game.service;

import com.asteria.game.World;
import com.asteria.game.character.npc.NpcUpdating;
import com.asteria.game.character.player.PlayerUpdating;

/**
 * The sequential update service that will execute the update sequence.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class SequentialUpdateService implements Runnable {

    @Override
    public void run() {

        // Update movement for players.
        World.getPlayers().forEach(player -> {
            try {
                player.sequence();
                player.getMovementQueue().sequence();
            } catch (Exception e) {
                e.printStackTrace();
                World.getPlayers().remove(player);
            }
        });

        // Update movement for npcs.
        World.getNpcs().forEach(npc -> {
            try {
                npc.sequence();
                npc.getMovementQueue().sequence();
            } catch (Exception e) {
                e.printStackTrace();
                World.getNpcs().remove(npc);
            }
        });

        // Update players for players, and npcs for players.
        World.getPlayers().forEach(player -> {
            try {
                PlayerUpdating.update(player);
                NpcUpdating.update(player);
            } catch (Exception e) {
                e.printStackTrace();
                World.getPlayers().remove(player);
            }
        });

        // Prepare players for the next update sequence.
        World.getPlayers().forEach(player -> {
            try {
                player.reset();
                player.setCachedUpdateBlock(null);
                player.getSession().getPacketCount().getAndSet(0);
            } catch (Exception e) {
                e.printStackTrace();
                World.getPlayers().remove(player);
            }
        });

        // Prepare npcs for the next update sequence.
        World.getNpcs().forEach(npc -> {
            try {
                npc.reset();
            } catch (Exception e) {
                e.printStackTrace();
                World.getNpcs().remove(npc);
            }
        });
    }
}