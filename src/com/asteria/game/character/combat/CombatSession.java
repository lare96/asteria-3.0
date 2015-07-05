package com.asteria.game.character.combat;

import java.util.Optional;

import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.asteria.game.character.combat.weapon.CombatSpecial;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.Minigame;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.location.Location;
import com.asteria.task.Task;

/**
 * The combat session controls the mechanics of when and how the controller of
 * the builder will attack.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatSession extends Task {

    /**
     * The builder assigned to this combat session.
     */
    private final CombatBuilder builder;

    /**
     * Create a new {@link CombatSession}.
     *
     * @param builder
     *            the builder assigned to this combat session.
     */
    public CombatSession(CombatBuilder builder) {
        super(1, false);
        super.attach(builder.getCharacter().getType() == NodeType.PLAYER ? ((Player) builder.getCharacter()) : ((Npc) builder
            .getCharacter()));
        this.builder = builder;
    }

    @Override
    public void execute() {
        if (builder.isCooldown()) {
            builder.decrementCooldown();
            builder.decrementAttackTimer();

            if (builder.getCooldown() == 0)
                builder.reset();
            return;
        }
        if (!sessionCanAttack()) {
            return;
        }
        if (builder.getCharacter().getType() == NodeType.PLAYER) {
            builder.determineStrategy();

            Player player = (Player) builder.getCharacter();
            if (player.isSpecialActivated() && player.getCombatSpecial() == CombatSpecial.GRANITE_MAUL)
                builder.clearAttackTimer();
        }
        builder.decrementAttackTimer();

        if (builder.getAttackTimer() < 1) {
            if (!Combat.checkAttackDistance(builder)) {
                return;
            }
            if (!builder.getStrategy().canAttack(builder.getCharacter(), builder.getVictim())) {
                return;
            }
            CombatSessionData data = builder.getStrategy().attack(builder.getCharacter(), builder.getVictim());

            if (builder.getCharacter().getType() == NodeType.PLAYER) {
                Player player = (Player) builder.getCharacter();
                player.getMessages().sendCloseWindows();
                if (player.isSpecialActivated() && player.getCastSpell() == null) {
                    data = player.getCombatSpecial().container(player, builder.getVictim());
                    CombatSpecial.drain(player, player.getCombatSpecial().getAmount());
                }
            }

            if (data.getType() != null) {
                builder.getVictim().getCombatBuilder().setLastAttacker(builder.getCharacter());
                builder.getVictim().getLastCombat().reset();

                if (data.getType() == CombatType.MAGIC && builder.getCharacter().getType() == NodeType.PLAYER) {
                    Player player = (Player) builder.getCharacter();

                    if (player.getCastSpell() != null && player.getAutocastSpell() != null || player.getAutocastSpell() == null) {
                        if (!player.isSpecialActivated())
                            player.getCombatBuilder().cooldown(false);
                        player.setCastSpell(null);
                        player.setFollowing(false);
                        builder.determineStrategy();
                    }
                }
                World.submit(new CombatSessionAttack(builder, data));
            }
            builder.resetAttackTimer();
            builder.getCharacter().faceCharacter(builder.getVictim());
        }
    }

    /**
     * Determines if the combat session can continue on, this method is invoked
     * before the attack timers are evaluated.
     *
     * @return {@code true} if this combat session can continue on,
     *         {@code false} otherwise.
     */
    private boolean sessionCanAttack() {
        if (!builder.getVictim().isRegistered() || !builder.getCharacter().isRegistered() || builder.getCharacter().isDead() || builder
            .getVictim().isDead()) {
            builder.reset();
            return false;
        }
        if (builder.getVictim().getType() == NodeType.PLAYER) {
            if (((Player) builder.getVictim()).getTeleportStage() > 0) {
                builder.cooldown(false);
                return false;
            }
        }
        if (!Location.inMultiCombat(builder.getCharacter()) && builder.isBeingAttacked() && !builder.getVictim().equals(
            builder.getLastAttacker())) {
            if (builder.getCharacter().getType() == NodeType.PLAYER)
                ((Player) builder.getCharacter()).getMessages().sendMessage("You are already under attack!");
            builder.reset();
            return false;
        }
        if (!Location.inMultiCombat(builder.getCharacter()) && builder.getVictim().getCombatBuilder().isBeingAttacked() && !builder
            .getVictim().getCombatBuilder().getLastAttacker().equals(builder.getCharacter())) {
            if (builder.getCharacter().getType() == NodeType.PLAYER)
                ((Player) builder.getCharacter()).getMessages().sendMessage("They are already under attack!");
            builder.reset();
            return false;
        }
        if (builder.getCharacter().getType() == NodeType.PLAYER) {
            Player player = (Player) builder.getCharacter();
            Optional<Minigame> optional = MinigameHandler.search(player);

            if (optional.isPresent()) {
                if (!optional.get().canHit(player, builder.getVictim())) {
                    return false;
                }
            } else if (Location.inWilderness(builder.getCharacter()) && !Location.inWilderness(builder.getVictim())) {
                player.getMessages().sendMessage("They are not in the wilderness!");
                builder.reset();
                return false;
            }
        }
        if (builder.getCharacter().getType() == NodeType.NPC) {
            Npc npc = (Npc) builder.getCharacter();

            if (builder.getVictim().getCombatBuilder().isCooldown() && !npc.getPosition().isViewableFrom(npc.getOriginalPosition()) || !builder
                .getVictim().getCombatBuilder().isBeingAttacked() && !npc.getPosition().isViewableFrom(npc.getOriginalPosition())) {
                builder.reset();
                return false;
            }
        }
        return true;
    }
}
