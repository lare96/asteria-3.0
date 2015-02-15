package com.asteria.game.character.combat;

import com.asteria.game.NodeType;
import com.asteria.game.character.Animation;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.Graphic;
import com.asteria.game.character.Hit;
import com.asteria.game.character.combat.prayer.CombatPrayer;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemNode;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.location.Location;
import com.asteria.task.Task;
import com.asteria.utility.RandomGen;

/**
 * An attack on the builder's victim that is sent completely separate from the
 * main combat session.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class CombatSessionAttack extends Task {

    /**
     * The random generator instance that will generate random numbers.
     */
    private final RandomGen random = new RandomGen();

    /**
     * The builder this attack is executed for.
     */
    private final CombatBuilder builder;

    /**
     * The combat data from the combat session.
     */
    private CombatSessionData data;

    /**
     * The total amount of damage dealt during this attack.
     */
    private int counter;

    /**
     * Creates a new {@link CombatSessionAttack}.
     * 
     * @param builder
     *            the builder this attack is executed for.
     * @param data
     *            the combat data from the combat session.
     */
    public CombatSessionAttack(CombatBuilder builder, CombatSessionData data) {
        super(Combat.getDelay(data.getType()), data.getType() == CombatType.MELEE);
        this.builder = builder;
        this.data = data;
    }

    @Override
    public void execute() {
        CharacterNode attacker = builder.getCharacter();
        CharacterNode victim = builder.getVictim();

        if (attacker.isDead() || !attacker.isRegistered() || victim.isDead() || !victim.isRegistered()) {
            this.cancel();
            return;
        }
        data = data.preAttack();

        if (data.getHits().length != 0 && data.getType() != CombatType.MAGIC || data.isAccurate()) {
            victim.getCombatBuilder().getDamageCache().add(attacker, (counter = data.attack()));
        }
        handleExperience();

        if (!data.isAccurate()) {
            if (data.getType() == CombatType.MAGIC) {
                victim.graphic(new Graphic(85));
                attacker.getCurrentlyCasting().executeOnHit(attacker, victim, false, 0);
                attacker.setCurrentlyCasting(null);
            }
        } else if (data.isAccurate()) {
            handleArmorEffects();
            handlePrayerEffects();
            attacker.onSuccessfulHit(victim, data.getType());

            if (data.getType() == CombatType.MAGIC) {
                attacker.getCurrentlyCasting().endGraphic().ifPresent(victim::graphic);
                attacker.getCurrentlyCasting().executeOnHit(attacker, victim, true, counter);
                attacker.setCurrentlyCasting(null);
            } else if (data.getType() == CombatType.RANGED && attacker.getType() == NodeType.PLAYER && random.nextBoolean()) {
                Player player = (Player) attacker;
                if (player.getFireAmmo() > 0) {
                    ItemNodeManager.register(new ItemNode(new Item(player.getFireAmmo()), victim.getPosition(), player), true);
                    player.setFireAmmo(0);
                }
            }
        }

        if (victim.getType() == NodeType.PLAYER) {
            victim.animation(new Animation(404));
        } else if (victim.getType() == NodeType.NPC) {
            victim.animation(new Animation(((Npc) victim).getDefinition().getDefenceAnimation()));
        }
        data.postAttack(counter);

        if (victim.isAutoRetaliate() && !victim.getCombatBuilder().isAttacking()) {
            victim.getCombatBuilder().attack(attacker);
        }
        this.cancel();
    }

    /**
     * Handles all armor effects that take place upon a successful attack.
     */
    private void handleArmorEffects() {
        if (random.nextInt(4) == 0) {
            if (Combat.isFullGuthans(builder.getCharacter())) {
                builder.getVictim().graphic(new Graphic(398));
                builder.getCharacter().healCharacter(counter);
                return;
            }
            if (builder.getVictim().getType() == NodeType.PLAYER) {
                Player victim = (Player) builder.getVictim();

                if (Combat.isFullTorags(builder.getCharacter())) {
                    victim.getRunEnergy().decrementAndGet(random.inclusive(1, 100));
                    victim.graphic(new Graphic(399));
                } else if (Combat.isFullAhrims(builder.getCharacter()) && victim.getSkills()[Skills.STRENGTH].getLevel() >= victim
                    .getSkills()[Skills.STRENGTH].getRealLevel()) {
                    victim.getSkills()[Skills.STRENGTH].decreaseLevel(random.inclusive(1, 10));
                    Skills.refresh(victim, Skills.STRENGTH);
                    victim.graphic(new Graphic(400));
                } else if (Combat.isFullKarils(builder.getCharacter()) && victim.getSkills()[Skills.AGILITY].getLevel() >= victim
                    .getSkills()[Skills.AGILITY].getRealLevel()) {
                    victim.graphic(new Graphic(401));
                    victim.getSkills()[Skills.AGILITY].decreaseLevel(random.inclusive(1, 10));
                    Skills.refresh(victim, Skills.AGILITY);
                }
            }
        }
    }

    /**
     * Handles all prayer effects that take place upon a successful attack.
     */
    private void handlePrayerEffects() {
        if (builder.getVictim().getType() == NodeType.PLAYER && data.getHits().length != 0) {
            Player victim = (Player) builder.getVictim();

            if (CombatPrayer.isActivated(victim, CombatPrayer.REDEMPTION) && victim.getSkills()[Skills.HITPOINTS].getLevel() <= (victim
                .getSkills()[Skills.HITPOINTS].getRealLevel() / 10)) {
                int heal = (int) (victim.getSkills()[Skills.HITPOINTS].getRealLevel() * Combat.REDEMPTION_PRAYER_HEAL);
                victim.getSkills()[Skills.HITPOINTS].increaseLevel(random.inclusive(1, heal));
                victim.graphic(new Graphic(436));
                victim.getSkills()[Skills.PRAYER].setLevel(0, true);
                victim.getEncoder().sendMessage("You've run out of prayer points!");
                CombatPrayer.deactivateAll(victim);
                Skills.refresh(victim, Skills.PRAYER);
                Skills.refresh(victim, Skills.HITPOINTS);
                return;
            }
            if (builder.getCharacter().getType() == NodeType.PLAYER) {
                if (CombatPrayer.isActivated(victim, CombatPrayer.RETRIBUTION) && victim.getSkills()[Skills.HITPOINTS].getLevel() < 1) {
                    victim.graphic(new Graphic(437));

                    if (Location.inWilderness(victim) || MinigameHandler.contains(victim) && !Location.inMultiCombat(victim)) {
                        if (builder.getCharacter().getPosition().withinDistance(victim.getPosition(), Combat.RETRIBUTION_RADIUS)) {
                            builder.getCharacter().damage(new Hit(random.inclusive(Combat.MAXIMUM_RETRIBUTION_DAMAGE)));
                        }
                    } else if (Location.inWilderness(victim) || MinigameHandler.contains(victim) && Location
                        .inMultiCombat(victim)) {
                        for (Player player : victim.getLocalPlayers()) {
                            if (player == null) {
                                continue;
                            }

                            if (!player.equals(victim) && player.getPosition().withinDistance(victim.getPosition(),
                                Combat.RETRIBUTION_RADIUS)) {
                                player.damage(new Hit(random.inclusive(Combat.MAXIMUM_RETRIBUTION_DAMAGE)));
                            }
                        }
                    }
                }
                if (CombatPrayer.isActivated((Player) builder.getCharacter(), CombatPrayer.SMITE)) {
                    victim.getSkills()[Skills.PRAYER].decreaseLevel(counter / 4);
                    Skills.refresh(victim, Skills.PRAYER);
                }
            }
        }
    }

    /**
     * Handles the distribution of experience for the amount of damage dealt in
     * this combat session attack.
     */
    private void handleExperience() {
        if (data.getExperience().length == 0 && data.getType() != CombatType.MAGIC) {
            return;
        }
        if (builder.getCharacter().getType() == NodeType.PLAYER) {
            Player player = (Player) builder.getCharacter();
            int exp = 0;
            int hitpointsExp = 0;

            if (data.getType() == CombatType.MAGIC) {
                exp = (int) (counter * 4d) + builder.getCharacter().getCurrentlyCasting().baseExperience();
                hitpointsExp = (int) (exp / 3d);

                Skills.experience(player, exp, Skills.MAGIC);
                Skills.experience(player, hitpointsExp, Skills.HITPOINTS);
                return;
            }
            exp = (int) ((counter * 4d) / data.getExperience().length);
            hitpointsExp = (int) (exp / 3d);

            for (int amount : data.getExperience()) {
                Skills.experience(player, exp, amount);
            }
            Skills.experience(player, hitpointsExp, Skills.HITPOINTS);
        }
    }
}
