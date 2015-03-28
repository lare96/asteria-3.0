package com.asteria.game.character.combat.weapon;

import com.asteria.game.NodeType;
import com.asteria.game.character.Animation;
import com.asteria.game.character.AnimationPriority;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.Graphic;
import com.asteria.game.character.Hit;
import com.asteria.game.character.MovementQueue;
import com.asteria.game.character.Projectile;
import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.combat.CombatSessionData;
import com.asteria.game.character.combat.CombatType;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.content.WeaponInterface;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.container.Equipment;
import com.asteria.game.location.Location;
import com.asteria.task.Task;
import com.asteria.task.TaskHandler;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * The enumerated type whose elements represent the combat special attacks.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum CombatSpecial {
    DRAGON_DAGGER(new int[] {1215, 1231, 5680,
            5698}, 25, 1.15, 1.25, CombatType.MELEE, WeaponInterface.DAGGER) {
        @Override
        public CombatSessionData container(Player player, CharacterNode
                target) {
            player.animation(new Animation(1062, AnimationPriority.HIGH));
            player.highGraphic(new Graphic(252));
            return new CombatSessionData(player, target, 2, CombatType.MELEE,
                    true);
        }
    },
    GRANITE_MAUL(new int[] {
            4153}, 50, 1.5, 1, CombatType.MELEE, WeaponInterface.WARHAMMER) {
        @Override
        public CombatSessionData container(Player player, CharacterNode
                target) {
            player.animation(new Animation(1667, AnimationPriority.HIGH));
            player.highGraphic(new Graphic(337));
            return new CombatSessionData(player, target, 1, CombatType.MELEE,
                    true);
        }
    },
    ABYSSAL_WHIP(new int[] {
            4151}, 50, 1, 1, CombatType.MELEE, WeaponInterface.WHIP) {
        @Override
        public CombatSessionData container(Player player, CharacterNode
                target) {
            player.animation(new Animation(1658, AnimationPriority.HIGH));
            target.highGraphic(new Graphic(341));
            return new CombatSessionData(player, target, 1, CombatType.MELEE,
                    false);
        }
    },
    DRAGON_LONGSWORD(new int[] {
            1305}, 25, 1.25, 1.25, CombatType.MELEE, WeaponInterface
            .LONGSWORD) {
        @Override
        public CombatSessionData container(Player player, CharacterNode
                target) {
            player.animation(new Animation(1058, AnimationPriority.HIGH));
            player.highGraphic(new Graphic(248));
            return new CombatSessionData(player, target, 1, CombatType.MELEE,
                    true);
        }
    },
    MAGIC_SHORTBOW(new int[] {
            861}, 50, 1, 1.1, CombatType.RANGED, WeaponInterface.SHORTBOW) {
        @Override
        public CombatSessionData container(Player player, CharacterNode
                target) {
            player.animation(new Animation(426, AnimationPriority.HIGH));
            player.highGraphic(new Graphic(250));
            new Projectile(player, target, 249, 44, 3, 43, 31, 0)
                    .sendProjectile();

            TaskHandler.submit(new Task(1, false) {
                @Override
                public void execute() {
                    player.animation(new Animation(426, AnimationPriority
                            .HIGH));
                    player.highGraphic(new Graphic(250));
                    new Projectile(player, target, 249, 44, 3, 43, 31, 0)
                            .sendProjectile();
                    this.cancel();
                }
            });
            return new CombatSessionData(player, target, 2, CombatType
                    .RANGED, true);
        }
    },
    MAGIC_LONGBOW(new int[] {
            859}, 35, 1, 5, CombatType.RANGED, WeaponInterface.LONGBOW) {
        @Override
        public CombatSessionData container(Player player, CharacterNode
                target) {
            player.animation(new Animation(426, AnimationPriority.HIGH));
            player.highGraphic(new Graphic(250));
            new Projectile(player, target, 249, 44, 3, 43, 31, 0)
                    .sendProjectile();
            return new CombatSessionData(player, target, 1, CombatType
                    .RANGED, true);
        }
    },
    DRAGON_BATTLEAXE(new int[] {
            1377}, 100, 1, 1, CombatType.MELEE, WeaponInterface.BATTLEAXE) {
        @Override
        public void onActivation(Player player, CharacterNode target) {
            int newStrength = (int) (player.getSkills()[Skills.STRENGTH]
                    .getRealLevel() * 0.2);
            int newAttack = (int) (player.getSkills()[Skills.ATTACK]
                    .getRealLevel() * 0.1);
            int newDefence = (int) (player.getSkills()[Skills.DEFENCE]
                    .getRealLevel() * 0.1);
            int newRanged = (int) (player.getSkills()[Skills.RANGED]
                    .getRealLevel() * 0.1);
            int newMagic = (int) (player.getSkills()[Skills.MAGIC]
                    .getRealLevel() * 0.1);
            player.graphic(new Graphic(246));
            player.animation(new Animation(1056));
            player.forceChat("Raarrrrrgggggghhhhhhh!");
            player.getSkills()[Skills.STRENGTH].increaseLevel(newStrength);
            player.getSkills()[Skills.ATTACK].decreaseLevel(newAttack);
            player.getSkills()[Skills.DEFENCE].decreaseLevel(newDefence);
            player.getSkills()[Skills.RANGED].decreaseLevel(newRanged);
            player.getSkills()[Skills.MAGIC].decreaseLevel(newMagic);
            Skills.refresh(player, Skills.STRENGTH);
            Skills.refresh(player, Skills.ATTACK);
            Skills.refresh(player, Skills.DEFENCE);
            Skills.refresh(player, Skills.RANGED);
            Skills.refresh(player, Skills.MAGIC);
            player.getCombatBuilder().cooldown(true);
            CombatSpecial.drain(player, DRAGON_BATTLEAXE.amount);
        }

        @Override
        public CombatSessionData container(Player player, CharacterNode
                target) {
            throw new UnsupportedOperationException("Dragon battleaxe does " +
                    "not have a special attack!");
        }
    },
    DRAGON_SPEAR(new int[] {1249, 1263, 5716,
            5730}, 25, 1, 1, CombatType.MELEE, WeaponInterface.SPEAR) {
        @Override
        public CombatSessionData container(Player player, CharacterNode
                target) {
            player.animation(new Animation(1064, AnimationPriority.HIGH));
            player.graphic(new Graphic(253));
            return new CombatSessionData(player, target, 1, CombatType.MELEE,
                    true) {
                @Override
                public void postAttack(int counter) {
                    if (target.getType() == NodeType.PLAYER) {
                        target.getMovementQueue().walk(MovementQueue
                                .DIRECTION_DELTA_X[player.getLastDirection()
                                ], MovementQueue.DIRECTION_DELTA_Y[player
                                .getLastDirection()]);
                    }
                    target.graphic(new Graphic(80));
                    target.getMovementListener().append(() -> target.freeze(6));
                }
            };
        }
    },
    DRAGON_MACE(new int[] {
            1434}, 25, 1.45, 0.9, CombatType.MELEE, WeaponInterface.MACE) {
        @Override
        public CombatSessionData container(Player player, CharacterNode
                target) {
            player.animation(new Animation(1060, AnimationPriority.HIGH));
            player.highGraphic(new Graphic(251));
            return new CombatSessionData(player, target, 1, CombatType.MELEE,
                    true);
        }
    },
    DRAGON_SCIMITAR(new int[] {
            4587}, 55, 1, 1, CombatType.MELEE, WeaponInterface.SCIMITAR) {
        @Override
        public CombatSessionData container(Player player, CharacterNode
                target) {
            player.animation(new Animation(1872, AnimationPriority.HIGH));
            player.highGraphic(new Graphic(347));
            return new CombatSessionData(player, target, 1, CombatType.MELEE,
                    true);
        }
    },
    DRAGON_2H_SWORD(new int[] {
            7158}, 60, 1, 1, CombatType.MELEE, WeaponInterface
            .TWO_HANDED_SWORD) {
        @Override
        public CombatSessionData container(Player player, CharacterNode
                target) {
            player.animation(new Animation(3157, AnimationPriority.HIGH));
            player.graphic(new Graphic(559));
            return new CombatSessionData(player, target, 1, CombatType.MELEE,
                    false) {
                @Override
                public void postAttack(int counter) {
                    if (Location.inMultiCombat(player)) {
                        Set<? extends CharacterNode> local = null;
                        if (target.getType() == NodeType.PLAYER) {
                            local = player.getLocalPlayers();
                        }
                        else if (target.getType() == NodeType.NPC) {
                            local = player.getLocalNpcs();
                        }

                        for (CharacterNode character : local) {
                            if (character == null) {
                                continue;
                            }

                            if (character.getPosition().withinDistance(target
                                    .getPosition(), 1) && !character.equals
                                    (target) && !character.equals(player) &&
                                    character.getCurrentHealth() > 0 &&
                                    !character.isDead()) {
                                Hit hit = Combat.calculateRandomHit(player,
                                        target, CombatType.MELEE);
                                character.damage(hit);
                                character.getCombatBuilder().getDamageCache()
                                        .add(player, hit.getDamage());
                            }
                        }
                    }
                }
            };
        }
    },
    DRAGON_HALBERD(new int[] {
            3204}, 30, 1.1, 1, CombatType.MELEE, WeaponInterface.HALBERD) {
        @Override
        public CombatSessionData container(Player player, CharacterNode
                target) {
            player.animation(new Animation(1203, AnimationPriority.HIGH));
            player.highGraphic(new Graphic(282));
            return new CombatSessionData(player, target, 2, CombatType.MELEE,
                    true);
        }
    };

    /**
     * The identifiers for the weapons that perform this special.
     */
    private final int[] ids;

    /**
     * The amount of special energy drained by this attack.
     */
    private final int amount;

    /**
     * The strength bonus added when performing this special attack.
     */
    private final double strength;

    /**
     * The accuracy bonus added when performing this special attack.
     */
    private final double accuracy;

    /**
     * The combat type used when performing this special attack.
     */
    private final CombatType combat;

    /**
     * The weapon type used when performing this special attack.
     */
    private final WeaponInterface weapon;

    /**
     * Creates a new {@link CombatSpecial}.
     *
     * @param ids
     *         the identifiers for the weapons that perform this special.
     * @param amount
     *         the amount of special energy drained by this attack.
     * @param strength
     *         the strength bonus added when performing this special attack.
     * @param accuracy
     *         the accuracy bonus added when performing this special attack.
     * @param combat
     *         the combat type used when performing this special attack.
     * @param weapon
     *         the weapon type used when performing this special attack.
     */
    private CombatSpecial(int[] ids, int amount, double strength, double
            accuracy, CombatType combat, WeaponInterface weapon) {
        this.ids = ids;
        this.amount = amount;
        this.strength = strength;
        this.accuracy = accuracy;
        this.combat = combat;
        this.weapon = weapon;
    }

    /**
     * Executes exactly when {@code player} activates the special bar.
     *
     * @param player
     *         the player who activated the special bar.
     * @param target
     *         the target when activating the special attack bar, will be
     *         {@code null} if the player is not in combat.
     */
    public void onActivation(Player player, CharacterNode target) {

    }

    /**
     * The combat data that will be used to make an attack on {@code target}.
     *
     * @param player
     *         the player who is making an attack.
     * @param target
     *         the main target of the attack.
     * @return the combat data.
     */
    public abstract CombatSessionData container(Player player, CharacterNode
            target);

    /**
     * Drains the special bar for {@code player}.
     *
     * @param player
     *         the player who's special bar will be drained.
     * @param amount
     *         the amount of energy to drain from the special bar.
     */
    public static void drain(Player player, int amount) {
        player.getSpecialPercentage().decrementAndGet(amount, 0);
        CombatSpecial.updateSpecialAmount(player);
        player.getEncoder().sendByteState(301, 0);
        player.setSpecialActivated(false);
    }

    /**
     * Restores the special bar for {@code player}.
     *
     * @param player
     *         the player who's special bar will be restored.
     * @param amount
     *         the amount of energy to restore to the special bar.
     */
    public static void restore(Player player, int amount) {
        player.getSpecialPercentage().incrementAndGet(amount, 100);
        CombatSpecial.updateSpecialAmount(player);
    }

    /**
     * Updates the special bar with the amount of special energy {@code player}
     * has.
     *
     * @param player
     *         the player who's special bar will be updated.
     */
    public static void updateSpecialAmount(Player player) {
        if (player.getWeapon().getSpecialBar() == -1 || player.getWeapon()
                .getSpecialMeter() == -1) {
            return;
        }

        int specialCheck = 10;
        int specialBar = player.getWeapon().getSpecialMeter();
        int specialAmount = player.getSpecialPercentage().get() / 10;

        for (int i = 0; i < 10; i++) {
            player.getEncoder().sendUpdateSpecial(--specialBar, specialAmount
                    >= specialCheck ? 500 : 0);
            specialCheck--;
        }
    }

    /**
     * Updates the weapon interface with a special bar if needed.
     *
     * @param player
     *         the player to update the interface for.
     */
    public static void assign(Player player) {
        if (player.getWeapon().getSpecialBar() == -1) {
            player.setCombatSpecial(null);
            return;
        }

        Optional<CombatSpecial> special = Arrays.stream(CombatSpecial.values
                ()).filter(c -> Arrays.stream(c.getIds()).anyMatch(id ->
                player.getEquipment().getId(Equipment.WEAPON_SLOT) == id))
                .findFirst();
        if (special.isPresent()) {
            player.getEncoder().sendInterfaceLayer(player.getWeapon()
                    .getSpecialBar(), false);
            player.setCombatSpecial(special.get());
            return;
        }

        player.getEncoder().sendInterfaceLayer(player.getWeapon()
                .getSpecialBar(), true);
        player.setCombatSpecial(null);
    }

    /**
     * Gets the identifiers for the weapons that perform this special.
     *
     * @return the identifiers for the weapons.
     */
    public final int[] getIds() {
        return ids;
    }

    /**
     * Gets the amount of special energy drained by this attack.
     *
     * @return the amount of special energy drained.
     */
    public final int getAmount() {
        return amount;
    }

    /**
     * Gets the strength bonus added when performing this special attack.
     *
     * @return the strength bonus.
     */
    public final double getStrength() {
        return strength;
    }

    /**
     * Gets the accuracy bonus added when performing this special attack.
     *
     * @return the accuracy bonus.
     */
    public final double getAccuracy() {
        return accuracy;
    }

    /**
     * Gets the combat type used when performing this special attack.
     *
     * @return the combat type.
     */
    public final CombatType getCombat() {
        return combat;
    }

    /**
     * Gets the weapon type used when performing this special attack.
     *
     * @return the weapon type.
     */
    public final WeaponInterface getWeapon() {
        return weapon;
    }
}
