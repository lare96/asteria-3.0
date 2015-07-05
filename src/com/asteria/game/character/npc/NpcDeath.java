package com.asteria.game.character.npc;

import java.util.Optional;

import com.asteria.game.World;
import com.asteria.game.character.Animation;
import com.asteria.game.character.AnimationPriority;
import com.asteria.game.character.CharacterDeath;
import com.asteria.game.character.npc.drop.NpcDropManager;
import com.asteria.game.character.player.Player;
import com.asteria.task.Task;

/**
 * The {@link CharacterDeath} implementation that is dedicated to managing the
 * death process for all {@link Npc}s.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDeath extends CharacterDeath<Npc> {

    /**
     * Creates a new {@link NpcDeath}.
     *
     * @param npc
     *            the NPC who has died and needs the death process.
     */
    public NpcDeath(Npc npc) {
        super(npc);
    }

    @Override
    public void preDeath(Npc character) {
        character.animation(new Animation(character.getDefinition().getDeathAnimation(), AnimationPriority.HIGH));
    }

    @Override
    public void death(Npc character) {
        Optional<Player> killer = character.getCombatBuilder().getDamageCache().calculateKiller();
        NpcDropManager.dropItems(killer, character);
        World.getNpcs().remove(character);
    }

    @Override
    public void postDeath(Npc character) {
        if (character.isRespawn()) {
            World.submit(new Task(character.getDefinition().getRespawnTime(), false) {
                @Override
                public void execute() {
                    Npc npc = new Npc(character.getId(), character.getOriginalPosition());
                    npc.setRespawn(true);
                    npc.getMovementCoordinator().setCoordinate(character.getMovementCoordinator().isCoordinate());
                    npc.getMovementCoordinator().setRadius(character.getMovementCoordinator().getRadius());
                    World.getNpcs().add(npc);
                    this.cancel();
                }
            });
        }
    }
}
