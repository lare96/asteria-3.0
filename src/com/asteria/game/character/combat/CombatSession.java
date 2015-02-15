package com.asteria.game.character.combat;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import com.asteria.game.NodeType;
import com.asteria.game.character.combat.prayer.CombatPrayer;
import com.asteria.game.character.combat.weapon.CombatSpecial;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.Minigame;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.location.Location;
import com.asteria.task.Task;
import com.asteria.task.TaskHandler;
import com.asteria.utility.RandomGen;
import com.asteria.utility.Settings;

/**
 * The combat session controls the mechanics of when and how the controller of
 * the builder will attack.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class CombatSession extends Task {

    /**
     * The random generator instance that will generate random numbers.
     */
    private final RandomGen random = new RandomGen();

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
                player.getEncoder().sendCloseWindows();
                if (player.isSpecialActivated() && player.getCastSpell() == null) {
                    data = player.getCombatSpecial().container(player, builder.getVictim());
                    CombatSpecial.drain(player, player.getCombatSpecial().getAmount());
                }
            }

            if (data.getType() != null) {
                if (data.getHits().length > 0) {
                    applyPrayerEffects(data);
                }
                builder.getVictim().getCombatBuilder().setLastAttacker(builder.getCharacter());
                builder.getVictim().getLastCombat().reset();

                if (data.getType() == CombatType.MAGIC && builder.getCharacter().getType() == NodeType.PLAYER) {
                    Player player = (Player) builder.getCharacter();

                    if (!player.isAutocast()) {
                        if (!player.isSpecialActivated())
                            player.getCombatBuilder().cooldown(false);
                        player.setCastSpell(null);
                        player.setFollowing(false);
                        builder.determineStrategy();
                    }
                }
                TaskHandler.submit(new CombatSessionAttack(builder, data));
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
                ((Player) builder.getCharacter()).getEncoder().sendMessage("You are already under attack!");
            builder.reset();
            return false;
        }
        if (!Location.inMultiCombat(builder.getCharacter()) && builder.getVictim().getCombatBuilder().isBeingAttacked() && !builder
            .getVictim().getCombatBuilder().getLastAttacker().equals(builder.getCharacter())) {
            if (builder.getCharacter().getType() == NodeType.PLAYER)
                ((Player) builder.getCharacter()).getEncoder().sendMessage("They are already under attack!");
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
                player.getEncoder().sendMessage("They are not in the wilderness!");
                builder.reset();
                return false;
            }
        }
        if (builder.getCharacter().getType() == NodeType.NPC) {
            Npc npc = (Npc) builder.getCharacter();

            if (builder.getVictim().getCombatBuilder().isCooldown() && !npc.getPosition().isViewableFrom(
                npc.getOriginalPosition()) || !builder.getVictim().getCombatBuilder().isBeingAttacked() && !npc.getPosition()
                .isViewableFrom(npc.getOriginalPosition())) {
                builder.reset();
                return false;
            }
        }
        return true;
    }

    /**
     * Applies combat prayer accuracy and damage reductions before executing the
     * {@link CombatSessionAttack}.
     * 
     * @param data
     *            the data for this combat session.
     * @throws IllegalStateException
     *             if the character node type is invalid.
     */
    private void applyPrayerEffects(CombatSessionData data) {
        if (!data.isCheckAccuracy()) {
            return;
        }
        if (builder.getVictim().getType() != NodeType.PLAYER) {
            return;
        }
        if (Combat.isFullVeracs(builder.getCharacter())) {
            if (Settings.DEBUG && builder.getCharacter().getType() == NodeType.PLAYER)
                ((Player) builder.getCharacter()).getEncoder().sendMessage(
                    "[DEBUG]: Chance of opponents prayer cancelling hit [0%:" + Combat.PRAYER_ACCURACY_REDUCTION + "%]");
            return;
        }
        Player player = (Player) builder.getVictim();

        if (CombatPrayer.isActivated(player, CombatPrayer.getProtectingPrayer(data.getType()))) {
            switch (builder.getCharacter().getType()) {
            case PLAYER:
                for (CombatHit h : data.getHits()) {
                    int hit = h.getHit().getDamage();
                    double mod = Math.abs(1 - Combat.PRAYER_DAMAGE_REDUCTION);
                    h.getHit().setDamage((int) (hit * mod));
                    if (Settings.DEBUG)
                        player.getEncoder().sendMessage(
                            "[DEBUG]: Damage reduced by opponents prayer [" + (hit - h.getHit().getDamage()) + "]");
                    mod = Math.round(random.nextDouble() * 100.0) / 100.0;
                    if (Settings.DEBUG)
                        player
                            .getEncoder()
                            .sendMessage(
                                "[DEBUG]: Chance of opponents prayer cancelling hit [" + mod + "/" + Combat.PRAYER_ACCURACY_REDUCTION + "]");
                    if (mod <= Combat.PRAYER_ACCURACY_REDUCTION) {
                        h.setAccurate(false);
                    }
                }
                break;
            case NPC:
                Arrays.stream(data.getHits()).filter(Objects::nonNull).forEach(h -> h.setAccurate(false));
                break;
            default:
                throw new IllegalStateException("Invalid character node type!");
            }
        }
    }
}
