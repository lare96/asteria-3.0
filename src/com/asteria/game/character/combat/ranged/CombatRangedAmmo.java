package com.asteria.game.character.combat.ranged;

import java.util.Arrays;
import java.util.Optional;

import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.content.WeaponInterface;
import com.asteria.game.item.container.Equipment;

/**
 * The enumerated type whose elements represent the ranged ammo that can be used
 * in combat.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum CombatRangedAmmo {
    BRONZE_ARROW(7, 10, 44, 3, 43, 31, 19),
    IRON_ARROW(10, 9, 44, 3, 43, 31, 18),
    STEEL_ARROW(16, 11, 44, 3, 43, 31, 20),
    MITHRIL_ARROW(22, 12, 44, 3, 43, 31, 21),
    ADAMANT_ARROW(31, 13, 44, 3, 43, 31, 22),
    RUNE_ARROW(49, 15, 44, 3, 43, 31, 24),
    CRYSTAL_ARROW(58, 249, 44, 3, 43, 31, 250),
    BOLTS(10, 27, 44, 3, 43, 31, 28),
    BARBED_BOLTS(12, 27, 44, 3, 43, 31, 28),
    OPAL_BOLTS(14, 27, 44, 3, 43, 31, 28),
    PEARL_BOLTS(48, 27, 44, 3, 43, 31, 28),
    BOLT_RACK(55, 27, 44, 3, 43, 31, 28),
    BRONZE_KNIFE(3, 212, 33, 3, 45, 37, 219),
    IRON_KNIFE(4, 213, 33, 3, 45, 37, 220),
    STEEL_KNIFE(7, 214, 33, 3, 45, 37, 221),
    BLACK_KNIFE(8, 215, 33, 3, 45, 37, 222),
    MITHRIL_KNIFE(10, 216, 33, 3, 45, 37, 223),
    ADAMANT_KNIFE(14, 217, 33, 3, 45, 37, 224),
    RUNE_KNIFE(24, 218, 33, 3, 45, 37, 225),
    BRONZE_DART(1, 226, 40, 2, 45, 37, 232),
    IRON_DART(3, 227, 40, 2, 45, 37, 233),
    STEEL_DART(4, 228, 40, 2, 45, 37, 234),
    BLACK_DART(6, 273, 40, 2, 45, 37, 273),
    MITHRIL_DART(7, 229, 40, 2, 45, 37, 235),
    ADAMANT_DART(10, 230, 40, 2, 45, 37, 236),
    RUNE_DART(14, 231, 40, 2, 45, 37, 237),
    BRONZE_JAVELIN(6, 200, 40, 2, 45, 37, 206),
    IRON_JAVELIN(10, 201, 40, 2, 45, 37, 207),
    STEEL_JAVELIN(12, 202, 40, 2, 45, 37, 208),
    MITHRIL_JAVELIN(18, 203, 40, 2, 45, 37, 209),
    ADAMANT_JAVELIN(28, 204, 40, 2, 45, 37, 210),
    RUNE_JAVELIN(42, 205, 40, 2, 45, 37, 211),
    BRONZE_THROWNAXE(5, 35, 44, 3, 43, 31, 43),
    IRON_THROWNAXE(7, 36, 44, 3, 43, 31, 42),
    STEEL_THROWNAXE(11, 37, 44, 3, 43, 31, 44),
    MITHRIL_THROWNAXE(16, 38, 44, 3, 43, 31, 45),
    ADAMANT_THROWNAXE(23, 39, 44, 3, 43, 31, 46),
    RUNE_THROWNAXE(26, 41, 44, 3, 43, 31, 48),
    TOKTZ_XIL_UL(50, 442, 44, 3, 43, 31, 0);

    /**
     * The strength of this ranged ammo.
     */
    private final int strength;

    /**
     * The projectile identification for this ranged ammo.
     */
    private final int projectile;

    /**
     * The delay of the projectile for this ranged ammo.
     */
    private final int delay;

    /**
     * The speed of the projectile for this ranged ammo.
     */
    private final int speed;

    /**
     * The starting height of the projectile for this ranged ammo.
     */
    private final int startHeight;

    /**
     * The ending height of the projectile for this ranged ammo.
     */
    private final int endHeight;

    /**
     * The graphic for this ranged ammo.
     */
    private final int graphic;

    /**
     * Creates a new {@link CombatRangedAmmo}.
     *
     * @param strength
     *            the strength of this ranged ammo.
     * @param projectile
     *            the projectile identification for this ranged ammo.
     * @param delay
     *            the delay of the projectile for this ranged ammo.
     * @param speed
     *            the speed of the projectile for this ranged ammo.
     * @param startHeight
     *            the starting height of the projectile for this ranged ammo.
     * @param endHeight
     *            the ending height of the projectile for this ranged ammo.
     * @param graphic
     *            the graphic for this ranged ammo.
     */
    private CombatRangedAmmo(int strength, int projectile, int delay, int speed, int startHeight, int endHeight, int graphic) {
        this.strength = strength;
        this.projectile = projectile;
        this.delay = delay;
        this.speed = speed;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.graphic = graphic;
    }

    @Override
    public final String toString() {
        String s = name().toLowerCase().replaceAll("_", " ");
        return this == CombatRangedAmmo.TOKTZ_XIL_UL ? "toktz-xil-ul" : s;
    }

    /**
     * Gets the ammo from {@code player} that's currently in the weapon or
     * arrows slot.
     *
     * @param player
     *            the player who's ammo will be retrieved.
     * @return the ammo wrapped in an optional, or an empty optional if no ammo
     *         was found.
     */
    public static Optional<CombatRangedAmmo> getPlayerAmmo(Player player) {
        int slot = player.getWeapon() == WeaponInterface.SHORTBOW || player.getWeapon() == WeaponInterface.LONGBOW || player.getWeapon() == WeaponInterface.CROSSBOW
            ? Equipment.ARROWS_SLOT : Equipment.WEAPON_SLOT;
        if (Combat.isCrystalBow(player))
            return Optional.of(CombatRangedAmmo.CRYSTAL_ARROW);
        if (player.getEquipment().get(slot) == null)
            return Optional.empty();
        return Arrays.stream(CombatRangedAmmo.values()).filter(
            c -> player.getEquipment().get(slot).getDefinition().getName().toLowerCase().contains(c.toString())).findFirst();
    }

    /**
     * Gets the strength of this ranged ammo.
     *
     * @return the strength.
     */
    public final int getStrength() {
        return strength;
    }

    /**
     * Gets the projectile identification for this ranged ammo.
     *
     * @return the projectile identification.
     */
    public final int getProjectile() {
        return projectile;
    }

    /**
     * Gets the delay of the projectile for this ranged ammo.
     *
     * @return the projectile delay.
     */
    public final int getDelay() {
        return delay;
    }

    /**
     * Gets the speed of the projectile for this ranged ammo.
     *
     * @return the projectile speed.
     */
    public final int getSpeed() {
        return speed;
    }

    /**
     * Gets the starting height of the projectile for this ranged ammo.
     *
     * @return the projectile starting height.
     */
    public final int getStartHeight() {
        return startHeight;
    }

    /**
     * Gets the ending height of the projectile for this ranged ammo.
     *
     * @return the projectile ending height.
     */
    public final int getEndHeight() {
        return endHeight;
    }

    /**
     * Gets the graphic for this ranged ammo.
     *
     * @return the graphic.
     */
    public final int getGraphic() {
        return graphic;
    }
}
