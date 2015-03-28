package com.asteria.game.character.npc;

import com.asteria.game.World;
import com.asteria.game.character.Animation;
import com.asteria.game.character.AnimationPriority;
import com.asteria.game.character.CharacterDeath;
import com.asteria.game.character.npc.drop.NpcDropTable;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemNode;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.item.ItemNodeStatic;
import com.asteria.task.Task;
import com.asteria.task.TaskHandler;

import java.util.Optional;

/**
 * The character death implementation that handles NPC death.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDeath extends CharacterDeath<Npc> {

    /**
     * Creates a new {@link NpcDeath}.
     *
     * @param npc
     *         the NPC who has died and needs the death process.
     */
    public NpcDeath(Npc npc) {
        super(npc);
    }

    @Override
    public void preDeath(Npc character) {
        character.animation(new Animation(character.getDefinition()
                .getDeathAnimation(), AnimationPriority.HIGH));
    }

    @Override
    public void death(Npc character) {
        Optional<Player> killer = character.getCombatBuilder().getDamageCache
                ().calculateKiller();
        Optional<NpcDropTable> drops = Optional.ofNullable(NpcDropTable.DROPS
                .get(character.getId()));
        drops.ifPresent(t -> {
            Item[] dropItems = t.toItems(killer.orElse(null));
            for (Item drop : dropItems) {
                if (drop == null)
                    continue;
                ItemNodeManager.register(!killer.isPresent() ? new
                        ItemNodeStatic(drop, character.getPosition()) : new
                        ItemNode(drop, character.getPosition(), killer.get()));
            }
            killer.ifPresent(k -> MinigameHandler.search(k).ifPresent(m -> m
                    .onKill(k, character)));
        });
        World.getNpcs().remove(character);
    }

    @Override
    public void postDeath(Npc character) {
        if (character.isRespawn()) {
            TaskHandler.submit(new Task(character.getRespawnTime(), false) {
                @Override
                public void execute() {
                    Npc npc = new Npc(character.getId(), character
                            .getOriginalPosition());
                    npc.setRespawn(true);
                    npc.getMovementCoordinator().setCoordinate(character
                            .getMovementCoordinator().isCoordinate());
                    npc.getMovementCoordinator().setRadius(character
                            .getMovementCoordinator().getRadius());
                    World.getNpcs().add(npc);
                    this.cancel();
                }
            });
        }
    }
}
