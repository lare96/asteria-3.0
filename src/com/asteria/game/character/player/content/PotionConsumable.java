package com.asteria.game.character.player.content;

import static com.asteria.game.character.player.skill.Skills.ATTACK;
import static com.asteria.game.character.player.skill.Skills.DEFENCE;
import static com.asteria.game.character.player.skill.Skills.HITPOINTS;
import static com.asteria.game.character.player.skill.Skills.MAGIC;
import static com.asteria.game.character.player.skill.Skills.PRAYER;
import static com.asteria.game.character.player.skill.Skills.RANGED;
import static com.asteria.game.character.player.skill.Skills.STRENGTH;

import java.util.Optional;

import com.asteria.game.character.Animation;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.Skill;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;
import com.asteria.task.Task;
import com.asteria.task.TaskHandler;

/**
 * The enumerated type managing consumable potion types.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author lare96 <http://github.com/lare96>
 */
public enum PotionConsumable {
    RANGE_POTIONS(2444, 169, 171, 173) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onBasicEffect(player, RANGED, BoostType.NORMAL);
        }
    },
    ENERGY_POTIONS(3008, 3010, 3012, 3014) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onEnergyEffect(player, false);
        }
    },
    SUPER_ENERGY_POTIONS(3016, 3018, 3020, 3022) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onEnergyEffect(player, true);
        }
    },
    MAGIC_POTIONS(3040, 3042, 3044, 3046) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onBasicEffect(player, MAGIC, BoostType.NORMAL);
        }
    },
    DEFENCE_POTIONS(2432, 133, 135, 137) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onBasicEffect(player, DEFENCE, BoostType.NORMAL);
        }
    },
    STRENGTH_POTIONS(113, 115, 117, 119) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onBasicEffect(player, STRENGTH, BoostType.NORMAL);
        }
    },
    ATTACK_POTIONS(2428, 121, 123, 125) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onBasicEffect(player, ATTACK, BoostType.NORMAL);
        }
    },
    SUPER_DEFENCE_POTIONS(2442, 163, 165, 167) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onBasicEffect(player, DEFENCE, BoostType.SUPER);
        }
    },
    SUPER_ATTACK_POTIONS(2436, 145, 147, 149) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onBasicEffect(player, ATTACK, BoostType.SUPER);
        }
    },
    SUPER_STRENGTH_POTIONS(2440, 157, 159, 161) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onBasicEffect(player, STRENGTH, BoostType.SUPER);
        }
    },
    SUPER_RESTORE_POTIONS(3024, 3026, 3028, 3030) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onPrayerEffect(player, true);
        }
    },
    PRAYER_POTIONS(2434, 139, 141, 143) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onPrayerEffect(player, false);
        }
    },
    ANTI_FIRE_POTIONS(2452, 2454, 2456, 2458) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onAntiFireEffect(player);
        }
    },
    ANTIPOISON_POTIONS(2446, 175, 177, 179) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onAntiPoisonEffect(player, false);
        }
    },
    SUPER_ANTIPOISON_POTIONS(2448, 181, 183, 185) {
        @Override
        public void onEffect(Player player) {
            PotionConsumable.onAntiPoisonEffect(player, true);
        }
    };

    /**
     * The default item representing the final potion dose.
     */
    private static final Item VIAL = new Item(229);

    /**
     * The identifiers which represent this potion type.
     */
    private final int[] ids;

    /**
     * Create a new {@link PotionConsumable}.
     *
     * @param ids
     *         the identifiers which represent this potion type.
     */
    private PotionConsumable(int... ids) {
        this.ids = ids;
    }

    /**
     * Attempts to consume {@code item} in {@code slot} for {@code player}.
     *
     * @param player
     *         the player attempting to consume the item.
     * @param item
     *         the item being consumed by the player.
     * @param slot
     *         the slot the player is consuming from.
     * @return {@code true} if the item was consumed, {@code false} otherwise.
     */
    public static boolean consume(Player player, Item item, int slot) {
        Optional<PotionConsumable> potion = forId(item.getId());
        // TODO: Check duel rule for no potions.
        if (!potion.isPresent() || player.isDead() || !player.getPotionTimer
                ().elapsed(1200))
            return false;
        player.animation(new Animation(829));
        player.getPotionTimer().reset();
        player.getEatingTimer().reset();
        player.getInventory().remove(item, slot);
        player.getInventory().add(getReplacementItem(item));
        potion.get().onEffect(player);
        return true;
    }

    /**
     * The method that executes the prayer potion action.
     *
     * @param player
     *         the player to do this action for.
     * @param restorePotion
     *         {@code true} if this potion is a restore potion, {@code false}
     *         otherwise.
     */
    private static void onPrayerEffect(Player player, boolean restorePotion) {
        Skill skill = player.getSkills()[PRAYER];
        int realLevel = skill.getRealLevel();

        skill.increaseLevel((int) (realLevel * .33), realLevel);

        if (restorePotion) {
            skill.increaseLevel(1, realLevel);
            onRestoreEffect(player);
        }
        Skills.refresh(player, PRAYER);
    }

    /**
     * The method that executes the anti-poison potion action.
     *
     * @param player
     *         the player to do this action for.
     * @param superPotion
     *         {@code true} if this potion is a super potion, {@code false}
     *         otherwise.
     */
    private static void onAntiPoisonEffect(Player player, boolean superPotion) {
        if (player.isPoisoned()) {
            player.setPoisonDamage(0);
            player.getEncoder().sendMessage("You have been cured of your " +
                    "poison!");
        }
        if (superPotion) {
            if (player.getPoisonImmunity().get() <= 0) {
                player.getEncoder().sendMessage("You have been granted " +
                        "immunity against poison.");
                player.getPoisonImmunity().incrementAndGet(500);
                TaskHandler.submit(new Task(50, false) {
                    @Override
                    public void execute() {
                        player.getPoisonImmunity().decrementAndGet(50);
                        if (player.getPoisonImmunity().get() <= 50) {
                            player.getEncoder().sendMessage("Your resistance " +
                                    "to poison is about to wear off!");
                        }
                        else if (player.getPoisonImmunity().get() <= 0) {
                            this.cancel();
                        }
                    }

                    @Override
                    public void onCancel() {
                        player.getEncoder().sendMessage("Your resistance to " +
                                "poison has worn off!");
                        player.getPoisonImmunity().set(0);
                    }
                }.attach(player));
            }
            else if (player.getPoisonImmunity().get() > 0) {
                player.getEncoder().sendMessage("Your immunity against poison" +
                        " has been restored!");
                player.getPoisonImmunity().set(500);
            }
        }
    }

    /**
     * The method that executes the energy potion action.
     *
     * @param player
     *         the player to do this action for.
     * @param superPotion
     *         {@code true} if this potion is a super potion, {@code false}
     *         otherwise.
     */
    private static void onEnergyEffect(Player player, boolean superPotion) {
        int amount = superPotion ? 100 : 50;
        player.getRunEnergy().incrementAndGet(amount, 100);
        player.getEncoder().sendString(player.getRunEnergy() + "%", 149);
    }

    /**
     * The method that executes the restore potion action.
     *
     * @param player
     *         the player to do this action for.
     */
    private static void onRestoreEffect(Player player) {
        for (int index = 0; index <= 6; index++) {
            if ((index == PRAYER) || (index == HITPOINTS)) {
                continue;
            }
            Skill skill = player.getSkills()[index];
            int realLevel = skill.getRealLevel();

            skill.increaseLevel((int) (realLevel * .33), realLevel);
            Skills.refresh(player, index);
        }
    }

    /**
     * The method that executes the anti-fire potion action.
     *
     * @param player
     *         the player to do this action for.
     */
    private static void onAntiFireEffect(Player player) {
        int count = player.getFireImmunity().get();
        player.getEncoder().sendMessage(count <= 0 ? "You have been granted " +
                "immunity against dragon fire." : "Your immunity against " +
                "dragon fire has been restored.");
        if (count <= 0) {
            TaskHandler.submit(new Task(30, false) {
                @Override
                public void execute() {
                    player.getFireImmunity().decrementAndGet(30);
                    if (player.getFireImmunity().get() == 30) {
                        player.getEncoder().sendMessage("Your resistance to " +
                                "dragon fire is about to wear off!");
                    }
                    else if (player.getFireImmunity().get() <= 0) {
                        this.cancel();
                    }
                }

                @Override
                public void onCancel() {
                    player.getEncoder().sendMessage("Your resistance to " +
                            "dragon fire has worn off!");
                    player.getFireImmunity().set(0);
                }
            }.attach(player));
        }
        player.getFireImmunity().set(360);
    }

    /**
     * The method that executes the basic effect potion action that will
     * increment the level of {@code skill}.
     *
     * @param player
     *         the player to do this action for.
     */
    private static void onBasicEffect(Player player, int skill, BoostType
            type) {
        Skill s = player.getSkills()[skill];
        int realLevel = s.getRealLevel();
        int boostLevel = Math.round(realLevel * type.getAmount());
        int cap = realLevel + boostLevel;
        if (type == BoostType.NORMAL) {
            boostLevel += 1;
        }
        if ((s.getLevel() + boostLevel) > (realLevel + boostLevel + 1)) {
            boostLevel = (realLevel + boostLevel) - s.getLevel();
        }
        s.increaseLevel(boostLevel, cap);
        Skills.refresh(player, skill);
    }

    /**
     * Retrieves the replacement item for {@code item}.
     *
     * @param item
     *         the item to retrieve the replacement item for.
     * @return the replacement item wrapped in an optional, or an empty optional
     * if no replacement item is available.
     */
    private static Item getReplacementItem(Item item) {
        Optional<PotionConsumable> potion = forId(item.getId());
        if (potion.isPresent()) {
            int length = potion.get().getIds().length;
            for (int index = 0; index < length; index++) {
                if (potion.get().getIds()[index] == item.getId() && index + 1
                        < length) {
                    return new Item(potion.get().getIds()[index + 1]);
                }
            }
        }
        return VIAL;
    }

    /**
     * Retrieves the potion consumable element for {@code id}.
     *
     * @param id
     *         the id that the potion consumable is attached to.
     * @return the potion consumable wrapped in an optional, or an empty
     * optional if no potion consumable was found.
     */
    private static Optional<PotionConsumable> forId(int id) {
        for (PotionConsumable potion : PotionConsumable.values()) {
            for (int potionId : potion.getIds()) {
                if (id == potionId) {
                    return Optional.of(potion);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * The method executed when this potion type activated.
     *
     * @param player
     *         the player to execute this effect for.
     */
    public abstract void onEffect(Player player);

    /**
     * Gets the identifiers which represent this potion type.
     *
     * @return the identifiers for this potion.
     */
    public final int[] getIds() {
        return ids;
    }

    /**
     * The enumerated type whose elements represent the boost types for
     * potions.
     *
     * @author Ryley Kimmel <ryley.kimmel@live.com>
     * @author lare96 <http://github.com/lare96>
     */
    private enum BoostType {
        NORMAL(.13F),
        SUPER(.20F);

        /**
         * The amount this type will boost by.
         */
        private final float amount;

        /**
         * Creates a new {@link BoostType}.
         *
         * @param boostAmount
         *         the amount this type will boost by.
         */
        private BoostType(float boostAmount) {
            this.amount = boostAmount;
        }

        /**
         * Gets the amount this type will boost by.
         *
         * @return the boost amount.
         */
        public final float getAmount() {
            return amount;
        }
    }
}
