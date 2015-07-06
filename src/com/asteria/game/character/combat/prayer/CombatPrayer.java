package com.asteria.game.character.combat.prayer;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.asteria.game.World;
import com.asteria.game.character.Flag;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.utility.BitMask;
import com.asteria.utility.TextUtils;
import com.google.common.collect.ImmutableMap;

/**
 * The enumerated type whose elements represent the prayers that can be
 * activated and deactivated. This currently only has support for prayers
 * present in the {@code 317} protocol.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum CombatPrayer {
    THICK_SKIN(0, 20, -1, 1, 83, 3, 9),
    BURST_OF_STRENGTH(1, 20, -1, 4, 84, 4, 10),
    CLARITY_OF_THOUGHT(2, 20, -1, 7, 85, 5, 11),
    ROCK_SKIN(3, 10, -1, 10, 86, 9, 0),
    SUPERHUMAN_STRENGTH(4, 10, -1, 13, 87, 1, 10),
    IMPROVED_REFLEXES(5, 10, -1, 16, 88, 2, 11),
    RAPID_RESTORE(6, 29, -1, 19, 89),
    RAPID_HEAL(7, 29, -1, 22, 90),
    PROTECT_ITEM(8, 29, -1, 25, 91),
    STEEL_SKIN(9, 5, -1, 28, 92, 0, 3),
    ULTIMATE_STRENGTH(10, 5, -1, 31, 93, 1, 4),
    INCREDIBLE_REFLEXES(11, 5, -1, 34, 94, 2, 5),
    PROTECT_FROM_MAGIC(12, 5, 2, 37, 95, 13, 14, 15, 16, 17),
    PROTECT_FROM_MISSILES(13, 5, 1, 40, 96, 12, 14, 15, 16, 17),
    PROTECT_FROM_MELEE(14, 5, 0, 43, 97, 12, 13, 15, 16, 17),
    RETRIBUTION(15, 17, 3, 46, 98, 12, 13, 14, 16, 17),
    REDEMPTION(16, 6, 5, 49, 99, 12, 13, 14, 15, 17),
    SMITE(17, 7, 4, 52, 100, 12, 13, 14, 15, 16);

    /**
     * The cached array that will contain mappings of all the elements to their
     * identifiers.
     */
    public static final ImmutableMap<Integer, CombatPrayer> PRAYERS = ImmutableMap.<Integer, CombatPrayer> builder().putAll(
        Arrays.stream(values()).collect(Collectors.toMap($it -> $it.id, $it -> $it))).build();

    /**
     * The identification for this prayer.
     */
    private final int id;

    /**
     * The mask identification for this prayer.
     */
    private final int mask;

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
     * The combat prayers that will be automatically deactivated when this one
     * is activated.
     */
    private final int[] deactivate;

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
     * @param deactivate
     *            the combat prayers that will be automatically deactivated.
     */
    private CombatPrayer(int id, int drainRate, int headIcon, int level, int config, int... deactivate) {
        this.id = id;
        this.mask = BitMask.calcMask(id);
        this.drainRate = drainRate;
        this.headIcon = headIcon;
        this.level = level;
        this.config = config;
        this.deactivate = deactivate;
    }

    @Override
    public String toString() {
        return TextUtils.capitalize(name().toLowerCase().replaceAll("_", " "));
    }

    /**
     * Executed dynamically when this combat prayer is activated for
     * {@code player}.
     *
     * @param player
     *            the player that activated this prayer.
     */
    void onActivation(Player player) {

    }

    /**
     * Executed dynamically when this combat prayer is deactivated for
     * {@code player}.
     *
     * @param player
     *            the player that deactivated this prayer.
     */
    void onDeactivation(Player player) {

    }

    /**
     * Activates this combat prayer for {@code player}. If
     * {@code deactivateIfActivated} is flagged {@code true} then if this prayer
     * is already activated it will be deactivated instead.
     *
     * @param player
     *            the player to activate this prayer for.
     * @param deactivateIfActivated
     *            if this prayer should be deactivated, if it is already
     *            activated.
     */
    public final void activate(Player player, boolean deactivateIfActivated) {
        if (CombatPrayer.isActivated(player, this)) {
            if (deactivateIfActivated)
                deactivate(player);
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (player.getSkills()[Skills.PRAYER].getRealLevel() < level) {
            sb.append("You need a @blu@Prayer " + "level of " + level + " @bla@to use @blu@" + this + "@bla@.");
        } else if (player.getSkills()[Skills.PRAYER].getLevel() < 1) {
            sb.append("You need to recharge your prayer at an altar!");
        }
        if (sb.length() > 0) {
            player.getMessages().sendByteState(config, 0);
            player.getMessages().sendMessage(sb.toString());
            return;
        }
        if (player.getPrayerDrain() == null || !player.getPrayerDrain().isRunning()) {
            player.setPrayerDrain(new CombatPrayerTask(player));
            World.submit(player.getPrayerDrain());
        }
        Arrays.stream(deactivate).forEach(it -> PRAYERS.get(it).deactivate(player));
        player.getPrayerActive().set(mask);
        player.getMessages().sendByteState(config, 1);
        if (headIcon != -1) {
            player.setHeadIcon(headIcon);
            player.getFlags().set(Flag.APPEARANCE);
        }
        onActivation(player);
    }

    /**
     * Activates this combat prayer for {@code player}. If this prayer is
     * already activated it then this method does nothing when invoked.
     *
     * @param player
     *            the player to activate this prayer for.
     */
    public final void activate(Player player) {
        activate(player, false);
    }

    /**
     * Attempts to deactivate this prayer for {@code player}. If this prayer is
     * already deactivated then invoking this method does nothing.
     *
     * @param player
     *            the player to deactivate this prayer for.
     */
    public final void deactivate(Player player) {
        if (!CombatPrayer.isActivated(player, this))
            return;
        player.getPrayerActive().unset(mask);
        player.getMessages().sendByteState(config, 0);
        if (headIcon != -1) {
            player.setHeadIcon(-1);
            player.getFlags().set(Flag.APPEARANCE);
        }
        onDeactivation(player);
    }

    /**
     * Deactivates activated combat prayers for {@code player}. Combat prayers
     * that are already deactivated will be ignored.
     *
     * @param player
     *            the player to deactivate prayers for.
     */
    public static void deactivateAll(Player player) {
        PRAYERS.values().forEach(it -> it.deactivate(player));
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
        return player.getPrayerActive().has(prayer.mask);
    }

    /**
     * Retrieves the size of this enumerated type.
     *
     * @return the size of the enum.
     */
    public static int size() {
        return PRAYERS.size();
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

    /**
     * Gets the combat prayers that will be automatically deactivated when this
     * one is activated.
     * 
     * @return the deactivated combat prayers.
     */
    public int[] getDeactivate() {
        return deactivate;
    }

    /**
     * Gets the mask identification for this prayer.
     * 
     * @return the mask identification.
     */
    public int getMask() {
        return mask;
    }
}
