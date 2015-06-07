package com.asteria.game.character.combat.prayer;

import java.util.Arrays;

import com.asteria.game.World;
import com.asteria.game.character.Flag;
import com.asteria.game.character.combat.CombatType;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.utility.TextUtils;

/**
 * The enumerated type whose elements represent the prayers that can be
 * activated and deactivated. This currently only has support for prayers
 * present in the {@code 317} protocol.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum CombatPrayer {
    THICK_SKIN(0, 20, -1, 1, 83) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.ROCK_SKIN.deactivate(player);
            CombatPrayer.STEEL_SKIN.deactivate(player);
            player.getPrayerActive()[CombatPrayer.THICK_SKIN.getId()] = true;
            player.getEncoder().sendByteState(CombatPrayer.THICK_SKIN.getConfig(), 1);
        }
    },
    BURST_OF_STRENGTH(1, 20, -1, 4, 84) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.SUPERHUMAN_STRENGTH.deactivate(player);
            CombatPrayer.ULTIMATE_STRENGTH.deactivate(player);
            player.getPrayerActive()[CombatPrayer.BURST_OF_STRENGTH.getId()] = true;
            player.getEncoder().sendByteState(CombatPrayer.BURST_OF_STRENGTH.getConfig(), 1);
        }
    },
    CLARITY_OF_THOUGHT(2, 20, -1, 7, 85) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.IMPROVED_REFLEXES.deactivate(player);
            CombatPrayer.INCREDIBLE_REFLEXES.deactivate(player);
            player.getPrayerActive()[CombatPrayer.CLARITY_OF_THOUGHT.getId()] = true;
            player.getEncoder().sendByteState(CombatPrayer.CLARITY_OF_THOUGHT.getConfig(), 1);
        }
    },
    ROCK_SKIN(3, 10, -1, 10, 86) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.STEEL_SKIN.deactivate(player);
            CombatPrayer.THICK_SKIN.deactivate(player);
            player.getPrayerActive()[CombatPrayer.ROCK_SKIN.getId()] = true;
            player.getEncoder().sendByteState(CombatPrayer.ROCK_SKIN.getConfig(), 1);
        }
    },
    SUPERHUMAN_STRENGTH(4, 10, -1, 13, 87) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.BURST_OF_STRENGTH.deactivate(player);
            CombatPrayer.ULTIMATE_STRENGTH.deactivate(player);
            player.getPrayerActive()[CombatPrayer.SUPERHUMAN_STRENGTH.getId()] = true;
            player.getEncoder().sendByteState(CombatPrayer.SUPERHUMAN_STRENGTH.getConfig(), 1);
        }
    },
    IMPROVED_REFLEXES(5, 10, -1, 16, 88) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.CLARITY_OF_THOUGHT.deactivate(player);
            CombatPrayer.INCREDIBLE_REFLEXES.deactivate(player);
            player.getPrayerActive()[CombatPrayer.IMPROVED_REFLEXES.getId()] = true;
            player.getEncoder().sendByteState(CombatPrayer.IMPROVED_REFLEXES.getConfig(), 1);
        }
    },
    RAPID_RESTORE(6, 29, -1, 19, 89) {
        @Override
        protected void onActivation(Player player) {
            player.getPrayerActive()[CombatPrayer.RAPID_RESTORE.getId()] = true;
            player.getEncoder().sendByteState(CombatPrayer.RAPID_RESTORE.getConfig(), 1);
        }
    },
    RAPID_HEAL(7, 29, -1, 22, 90) {
        @Override
        protected void onActivation(Player player) {
            player.getPrayerActive()[CombatPrayer.RAPID_HEAL.getId()] = true;
            player.getEncoder().sendByteState(CombatPrayer.RAPID_HEAL.getConfig(), 1);
        }
    },
    PROTECT_ITEM(8, 29, -1, 25, 91) {
        @Override
        protected void onActivation(Player player) {
            player.getPrayerActive()[CombatPrayer.PROTECT_ITEM.getId()] = true;
            player.getEncoder().sendByteState(CombatPrayer.PROTECT_ITEM.getConfig(), 1);
        }
    },
    STEEL_SKIN(9, 5, -1, 28, 92) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.THICK_SKIN.deactivate(player);
            CombatPrayer.ROCK_SKIN.deactivate(player);
            player.getPrayerActive()[CombatPrayer.STEEL_SKIN.getId()] = true;
            player.getEncoder().sendByteState(CombatPrayer.STEEL_SKIN.getConfig(), 1);
        }
    },
    ULTIMATE_STRENGTH(10, 5, -1, 31, 93) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.BURST_OF_STRENGTH.deactivate(player);
            CombatPrayer.SUPERHUMAN_STRENGTH.deactivate(player);
            player.getPrayerActive()[CombatPrayer.ULTIMATE_STRENGTH.getId()] = true;
            player.getEncoder().sendByteState(CombatPrayer.ULTIMATE_STRENGTH.getConfig(), 1);
        }
    },
    INCREDIBLE_REFLEXES(11, 5, -1, 34, 94) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.IMPROVED_REFLEXES.deactivate(player);
            CombatPrayer.CLARITY_OF_THOUGHT.deactivate(player);
            player.getPrayerActive()[CombatPrayer.INCREDIBLE_REFLEXES.getId()] = true;
            player.getEncoder().sendByteState(CombatPrayer.INCREDIBLE_REFLEXES.getConfig(), 1);
        }
    },
    PROTECT_FROM_MAGIC(12, 5, 2, 37, 95) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.PROTECT_FROM_MISSILES.deactivate(player);
            CombatPrayer.PROTECT_FROM_MELEE.deactivate(player);
            CombatPrayer.REDEMPTION.deactivate(player);
            CombatPrayer.RETRIBUTION.deactivate(player);
            CombatPrayer.SMITE.deactivate(player);
            player.getPrayerActive()[CombatPrayer.PROTECT_FROM_MAGIC.getId()] = true;
            player.setHeadIcon(CombatPrayer.PROTECT_FROM_MAGIC.getHeadIcon());
            player.getEncoder().sendByteState(CombatPrayer.PROTECT_FROM_MAGIC.getConfig(), 1);
            player.getFlags().set(Flag.APPEARANCE);
        }

        @Override
        protected void onDeactivation(Player player) {
            super.onDeactivation(player);
            player.setHeadIcon(-1);
            player.getFlags().set(Flag.APPEARANCE);
        }
    },
    PROTECT_FROM_MISSILES(13, 5, 1, 40, 96) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.REDEMPTION.deactivate(player);
            CombatPrayer.PROTECT_FROM_MAGIC.deactivate(player);
            CombatPrayer.PROTECT_FROM_MELEE.deactivate(player);
            CombatPrayer.RETRIBUTION.deactivate(player);
            CombatPrayer.SMITE.deactivate(player);
            player.getPrayerActive()[CombatPrayer.PROTECT_FROM_MISSILES.getId()] = true;
            player.setHeadIcon(CombatPrayer.PROTECT_FROM_MISSILES.getHeadIcon());
            player.getEncoder().sendByteState(CombatPrayer.PROTECT_FROM_MISSILES.getConfig(), 1);
            player.getFlags().set(Flag.APPEARANCE);
        }

        @Override
        protected void onDeactivation(Player player) {
            super.onDeactivation(player);
            player.setHeadIcon(-1);
            player.getFlags().set(Flag.APPEARANCE);
        }
    },
    PROTECT_FROM_MELEE(14, 5, 0, 43, 97) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.PROTECT_FROM_MISSILES.deactivate(player);
            CombatPrayer.PROTECT_FROM_MAGIC.deactivate(player);
            CombatPrayer.REDEMPTION.deactivate(player);
            CombatPrayer.RETRIBUTION.deactivate(player);
            CombatPrayer.SMITE.deactivate(player);
            player.getPrayerActive()[CombatPrayer.PROTECT_FROM_MELEE.getId()] = true;
            player.setHeadIcon(CombatPrayer.PROTECT_FROM_MELEE.getHeadIcon());
            player.getEncoder().sendByteState(CombatPrayer.PROTECT_FROM_MELEE.getConfig(), 1);
            player.getFlags().set(Flag.APPEARANCE);
        }

        @Override
        protected void onDeactivation(Player player) {
            super.onDeactivation(player);
            player.setHeadIcon(-1);
            player.getFlags().set(Flag.APPEARANCE);
        }
    },
    RETRIBUTION(15, 17, 3, 46, 98) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.PROTECT_FROM_MISSILES.deactivate(player);
            CombatPrayer.PROTECT_FROM_MAGIC.deactivate(player);
            CombatPrayer.PROTECT_FROM_MELEE.deactivate(player);
            CombatPrayer.REDEMPTION.deactivate(player);
            CombatPrayer.SMITE.deactivate(player);
            player.getPrayerActive()[CombatPrayer.RETRIBUTION.getId()] = true;
            player.setHeadIcon(CombatPrayer.RETRIBUTION.getHeadIcon());
            player.getEncoder().sendByteState(CombatPrayer.RETRIBUTION.getConfig(), 1);
            player.getFlags().set(Flag.APPEARANCE);
        }

        @Override
        protected void onDeactivation(Player player) {
            super.onDeactivation(player);
            player.setHeadIcon(-1);
            player.getFlags().set(Flag.APPEARANCE);
        }
    },
    REDEMPTION(16, 6, 5, 49, 99) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.PROTECT_FROM_MISSILES.deactivate(player);
            CombatPrayer.PROTECT_FROM_MAGIC.deactivate(player);
            CombatPrayer.PROTECT_FROM_MELEE.deactivate(player);
            CombatPrayer.RETRIBUTION.deactivate(player);
            CombatPrayer.SMITE.deactivate(player);
            player.getPrayerActive()[CombatPrayer.REDEMPTION.getId()] = true;
            player.setHeadIcon(CombatPrayer.REDEMPTION.getHeadIcon());
            player.getEncoder().sendByteState(CombatPrayer.REDEMPTION.getConfig(), 1);
            player.getFlags().set(Flag.APPEARANCE);
        }

        @Override
        protected void onDeactivation(Player player) {
            super.onDeactivation(player);
            player.setHeadIcon(-1);
            player.getFlags().set(Flag.APPEARANCE);
        }
    },
    SMITE(17, 7, 4, 52, 100) {
        @Override
        protected void onActivation(Player player) {
            CombatPrayer.PROTECT_FROM_MISSILES.deactivate(player);
            CombatPrayer.PROTECT_FROM_MAGIC.deactivate(player);
            CombatPrayer.PROTECT_FROM_MELEE.deactivate(player);
            CombatPrayer.RETRIBUTION.deactivate(player);
            CombatPrayer.REDEMPTION.deactivate(player);
            player.getPrayerActive()[CombatPrayer.SMITE.getId()] = true;
            player.setHeadIcon(CombatPrayer.SMITE.getHeadIcon());
            player.getEncoder().sendByteState(CombatPrayer.SMITE.getConfig(), 1);
            player.getFlags().set(Flag.APPEARANCE);
        }

        @Override
        protected void onDeactivation(Player player) {
            super.onDeactivation(player);
            player.setHeadIcon(-1);
            player.getFlags().set(Flag.APPEARANCE);
        }
    };

    /**
     * The identification for this prayer.
     */
    private final int id;

    /**
     * The amount of ticks it takes for prayer to be drained.
     */
    private final int drainRate;

    /**
     * The head icon present when this prayer is activated.
     */
    private final int headIcon;

    /**
     * The level required to use this prayer.
     */
    private final int level;

    /**
     * The config to make the prayer button light up when activated.
     */
    private final int config;

    /**
     * Creates a new {@link CombatPrayer}.
     *
     * @param id
     *            the identification for this prayer.
     * @param drainRate
     *            the amount of ticks it takes for prayer to be drained.
     * @param headIcon
     *            the head icon present when this prayer is activated.
     * @param level
     *            the level required to use this prayer.
     * @param config
     *            the config to make the prayer button light up when activated.
     */
    private CombatPrayer(int id, int drainRate, int headIcon, int level, int config) {
        this.id = id;
        this.drainRate = drainRate;
        this.headIcon = headIcon;
        this.level = level;
        this.config = config;
    }

    /**
     * Executed when this prayer is activated through the
     * {@link CombatPrayer#activate} method.
     *
     * @param player
     *            the player that activated this prayer.
     */
    protected abstract void onActivation(Player player);

    /**
     * Executed when this prayer is deactivated through the
     * {@link CombatPrayer#deactivate} method.
     *
     * @param player
     *            the player that deactivated this prayer.
     */
    protected void onDeactivation(Player player) {
        player.getPrayerActive()[id] = false;
        player.getEncoder().sendByteState(config, 0);
    }

    /**
     * Attempts to activate this prayer for {@code player}.
     *
     * @param player
     *            the player to activate this prayer for.
     * @return {@code true} if the activation was successful, {@code false}
     *         otherwise.
     */
    public final boolean activate(Player player) {
        if (CombatPrayer.isActivated(player, this)) {
            return false;
        } else if (player.getSkills()[Skills.PRAYER].getRealLevel() < level) {
            player.getEncoder().sendChatboxString(
                "You need a @blu@Prayer " + "level of " + level + " @bla@to use @blu@" + TextUtils.capitalize(name().toLowerCase()
                    .replaceAll("_", " ")) + "@bla@.");
            player.getEncoder().sendByteState(config, 0);
            return false;
        } else if (player.getSkills()[Skills.PRAYER].getLevel() < 1) {
            player.getEncoder().sendMessage("You've run out of prayer points!");
            player.getEncoder().sendByteState(config, 0);
            return false;
        }
        if (player.getPrayerDrain() == null || !player.getPrayerDrain().isRunning()) {
            player.setPrayerDrain(new CombatPrayerTask(player));
            World.submit(player.getPrayerDrain());
        }
        onActivation(player);
        return true;
    }

    /**
     * Attempts to activate this prayer and if unsuccessful, then attempts to
     * deactivate this prayer.
     *
     * @param player
     *            the player this will be done for.
     */
    public final void activateOrDeactivate(Player player) {
        if (!activate(player))
            deactivate(player);
    }

    /**
     * Attempts to deactivate this prayer for {@code player}.
     *
     * @param player
     *            the player to deactivate this prayer for.
     * @return {@code true} if the deactivation was successful, {@code false}
     *         otherwise.
     */
    public final boolean deactivate(Player player) {
        if (!CombatPrayer.isActivated(player, this))
            return false;
        onDeactivation(player);
        return true;
    }

    /**
     * Deactivates all activated prayers for {@code player}.
     *
     * @param player
     *            the player to deactivate all prayers for.
     */
    public static void deactivateAll(Player player) {
        Arrays.stream(CombatPrayer.values()).filter(c -> CombatPrayer.isActivated(player, c)).forEach(c -> c.deactivate(player));
    }

    /**
     * Resets all of the prayer configurations back to their default states.
     *
     * @param player
     *            the player to reset the configurations for.
     */
    public static void resetPrayerGlows(Player player) {
        Arrays.stream(CombatPrayer.values()).forEach(c -> player.getEncoder().sendByteState(c.getConfig(), 0));
    }

    /**
     * Determines if the {@code prayer} is activated for the {@code player}.
     *
     * @param player
     *            the player's prayers to check.
     * @param prayer
     *            the prayer to check is active.
     * @return {@code true} if the prayer is activated for the player,
     *         {@code false} otherwise.
     */
    public static boolean isActivated(Player player, CombatPrayer prayer) {
        return player.getPrayerActive()[prayer.getId()];
    }

    /**
     * Gets the corresponding combat prayer to {@code type}.
     *
     * @param type
     *            the combat type to get the prayer for.
     * @return the corresponding combat prayer.
     * @throws IllegalArgumentException
     *             if the combat type is invalid.
     */
    public static CombatPrayer getProtectingPrayer(CombatType type) {
        switch (type) {
        case MELEE:
            return CombatPrayer.PROTECT_FROM_MELEE;
        case MAGIC:
            return CombatPrayer.PROTECT_FROM_MAGIC;
        case RANGED:
            return CombatPrayer.PROTECT_FROM_MISSILES;
        default:
            throw new IllegalArgumentException("Invalid combat type: " + type);
        }
    }

    /**
     * Retrieves the size of this enumerated type.
     *
     * @return the size of the enum.
     */
    public static int size() {
        return CombatPrayer.values().length;
    }

    /**
     * Gets the identification for this prayer.
     *
     * @return the identification.
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the amount of ticks it takes for prayer to be drained.
     *
     * @return the amount of ticks.
     */
    public final int getDrainRate() {
        return drainRate;
    }

    /**
     * Gets the head icon present when this prayer is activated.
     *
     * @return the head icon.
     */
    public final int getHeadIcon() {
        return headIcon;
    }

    /**
     * Gets the level required to use this prayer.
     *
     * @return the level required.
     */
    public final int getLevel() {
        return level;
    }

    /**
     * Gets the config to make the prayer button light up when activated.
     *
     * @return the config for the prayer button.
     */
    public final int getConfig() {
        return config;
    }
}
