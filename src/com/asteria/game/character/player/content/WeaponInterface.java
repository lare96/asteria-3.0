package com.asteria.game.character.player.content;

import java.util.HashMap;
import java.util.Map;

import com.asteria.game.character.combat.weapon.CombatSpecial;
import com.asteria.game.character.combat.weapon.FightType;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;

/**
 * The enumerated type whose elements represent the weapon interfaces.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum WeaponInterface {
    STAFF(328, 331, 6, new FightType[] {FightType.STAFF_BASH,
            FightType.STAFF_POUND, FightType.STAFF_FOCUS}),
    WARHAMMER(425, 428, 6, new FightType[] {FightType.WARHAMMER_POUND,
            FightType.WARHAMMER_PUMMEL, FightType.WARHAMMER_BLOCK}, 7474, 7486),
    SCYTHE(776, 779, 6, new FightType[] {FightType.SCYTHE_REAP,
            FightType.SCYTHE_CHOP, FightType.SCYTHE_JAB,
            FightType.SCYTHE_BLOCK}),
    BATTLEAXE(1698, 1701, 6, new FightType[] {FightType.BATTLEAXE_CHOP,
            FightType.BATTLEAXE_HACK, FightType.BATTLEAXE_SMASH,
            FightType.BATTLEAXE_BLOCK}, 7499, 7511),
    CROSSBOW(1749, 1752, 5, new FightType[] {FightType.CROSSBOW_ACCURATE,
            FightType.CROSSBOW_RAPID,
            FightType.CROSSBOW_LONGRANGE}, 7524, 7536),
    SHORTBOW(1764, 1767, 5, new FightType[] {FightType.SHORTBOW_ACCURATE,
            FightType.SHORTBOW_RAPID,
            FightType.SHORTBOW_LONGRANGE}, 7549, 7561),
    LONGBOW(1764, 1767, 6, new FightType[] {FightType.LONGBOW_ACCURATE,
            FightType.LONGBOW_RAPID, FightType.LONGBOW_LONGRANGE}, 7549, 7561),
    DAGGER(2276, 2279, 5, new FightType[] {FightType.DAGGER_STAB,
            FightType.DAGGER_LUNGE, FightType.DAGGER_SLASH,
            FightType.DAGGER_BLOCK}, 7574, 7586),
    SWORD(2276, 2279, 5, new FightType[] {FightType.SWORD_STAB,
            FightType.SWORD_LUNGE, FightType.SWORD_SLASH,
            FightType.SWORD_BLOCK}, 7574, 7586),
    SCIMITAR(2423, 2426, 5, new FightType[] {FightType.SCIMITAR_CHOP,
            FightType.SCIMITAR_SLASH, FightType.SCIMITAR_LUNGE,
            FightType.SCIMITAR_BLOCK}, 7599, 7611),
    LONGSWORD(2423, 2426, 6, new FightType[] {FightType.LONGSWORD_CHOP,
            FightType.LONGSWORD_SLASH, FightType.LONGSWORD_LUNGE,
            FightType.LONGSWORD_BLOCK}, 7599, 7611),
    MACE(3796, 3799, 4, new FightType[] {FightType.MACE_POUND,
            FightType.MACE_PUMMEL, FightType.MACE_SPIKE,
            FightType.MACE_BLOCK}, 7624, 7636),
    KNIFE(4446, 4449, 4, new FightType[] {FightType.KNIFE_ACCURATE,
            FightType.KNIFE_RAPID, FightType.KNIFE_LONGRANGE}, 7649, 7661),
    SPEAR(4679, 4682, 6, new FightType[] {FightType.SPEAR_LUNGE,
            FightType.SPEAR_SWIPE, FightType.SPEAR_POUND,
            FightType.SPEAR_BLOCK}, 7674, 7686),
    TWO_HANDED_SWORD(4705, 4708, 6, new FightType[] {
            FightType.TWOHANDEDSWORD_CHOP, FightType.TWOHANDEDSWORD_SLASH,
            FightType.TWOHANDEDSWORD_SMASH,
            FightType.TWOHANDEDSWORD_BLOCK}, 7699, 7711),
    PICKAXE(5570, 5573, 6, new FightType[] {FightType.PICKAXE_SPIKE,
            FightType.PICKAXE_IMPALE, FightType.PICKAXE_SMASH,
            FightType.PICKAXE_BLOCK}),
    CLAWS(7762, 7765, 4, new FightType[] {FightType.CLAWS_CHOP,
            FightType.CLAWS_SLASH, FightType.CLAWS_LUNGE,
            FightType.CLAWS_BLOCK}, 7800, 7812),
    HALBERD(8460, 8463, 6, new FightType[] {FightType.HALBERD_JAB,
            FightType.HALBERD_SWIPE, FightType.HALBERD_FEND}, 8493, 8505),
    UNARMED(5855, 5857, 6, new FightType[] {FightType.UNARMED_PUNCH,
            FightType.UNARMED_KICK, FightType.UNARMED_BLOCK}),
    WHIP(12290, 12293, 4, new FightType[] {FightType.WHIP_FLICK,
            FightType.WHIP_LASH, FightType.WHIP_DEFLECT}, 12323, 12335),
    THROWNAXE(4446, 4449, 6, new FightType[] {FightType.THROWNAXE_ACCURATE,
            FightType.THROWNAXE_RAPID,
            FightType.THROWNAXE_LONGRANGE}, 7649, 7661),
    DART(4446, 4449, 3, new FightType[] {FightType.DART_ACCURATE,
            FightType.DART_RAPID, FightType.DART_LONGRANGE}, 7649, 7661),
    JAVELIN(4446, 4449, 6, new FightType[] {FightType.JAVELIN_ACCURATE,
            FightType.JAVELIN_RAPID, FightType.JAVELIN_LONGRANGE}, 7649, 7661);

    /**
     * The hash collection of weapon interfaces.
     */
    public static final Map<Integer, WeaponInterface> INTERFACES = new HashMap<>();

    /**
     * The identification of the interface that will be displayed.
     */
    private final int id;

    /**
     * The identification of the line the weapon name will be displayed on.
     */
    private final int nameLine;

    /**
     * The base attack speed of weapons using this interface.
     */
    private final int speed;

    /**
     * The fight types that correspond with this interface.
     */
    private final FightType[] fightTypes;

    /**
     * The identification of the special bar for this interface.
     */
    private final int specialBar;

    /**
     * The identification of the special meter for this interface.
     */
    private final int specialMeter;

    /**
     * Creates a new {@link WeaponInterface}.
     *
     * @param id
     *         the identification of the interface that will be displayed.
     * @param nameLine
     *         the identification of the line the weapon name will be
     *         displayed on.
     * @param speed
     *         the base attack speed of weapons using this interface.
     * @param fightTypes
     *         the fight types that correspond with this interface.
     * @param specialBar
     *         the identification of the special bar for this interface.
     * @param specialMeter
     *         the identification of the special meter for this interface.
     */
    private WeaponInterface(int id, int nameLine, int speed, FightType[] fightTypes, int specialBar, int specialMeter) {
        this.id = id;
        this.nameLine = nameLine;
        this.speed = speed;
        this.fightTypes = fightTypes;
        this.specialBar = specialBar;
        this.specialMeter = specialMeter;
    }

    /**
     * Creates a new {@link WeaponInterface} for interfaces that have no
     * special
     * bar or meter.
     *
     * @param id
     *         the identification of the interface that will be displayed.
     * @param nameLine
     *         the identification of the line the weapon name will be
     *         displayed on.
     * @param speed
     *         the base attack speed of weapons using this interface.
     * @param fightTypes
     *         the fight types that correspond with this interface.
     */
    private WeaponInterface(int id, int nameLine, int speed, FightType[] fightTypes) {
        this(id, nameLine, speed, fightTypes, -1, -1);
    }

    /**
     * The method executed when weapon {@code item} is equipped or unequipped
     * that assigns a weapon interface to {@code player}.
     *
     * @param player
     *         the player equipping the item.
     * @param item
     *         the item the player is equipping, or {@code null} if a weapon
     *         was unequipped.
     */
    public static void execute(Player player, Item item) {
        if (item == null) {
            player.getEncoder().sendSidebarInterface(0, WeaponInterface.UNARMED.id);
            player.getEncoder().sendString("Unarmed", WeaponInterface.UNARMED.nameLine);
            player.setWeapon(WeaponInterface.UNARMED);
            CombatSpecial.assign(player);
            return;
        }
        WeaponInterface weapon = INTERFACES.get(item.getId());
        if (weapon == WeaponInterface.UNARMED) {
            player.getEncoder().sendSidebarInterface(0, weapon.id);
            player.getEncoder().sendString("Unarmed", weapon.nameLine);
            player.setWeapon(WeaponInterface.UNARMED);
            return;
        } else if (weapon == WeaponInterface.CROSSBOW) {
            player.getEncoder().sendString("Weapon: ", weapon.nameLine - 1);
        } else if (weapon == WeaponInterface.WHIP) {
            player.getEncoder().sendString("Weapon: ", weapon.nameLine - 1);
        }
        player.getEncoder().sendItemModelOnInterface(weapon.id + 1, 200, item.getId());
        player.getEncoder().sendSidebarInterface(0, weapon.id);
        player.getEncoder().sendString(item.getDefinition().getName(), weapon.nameLine);
        player.setWeapon(weapon);
        CombatSpecial.assign(player);
        CombatSpecial.updateSpecialAmount(player);
        for (FightType type : weapon.getFightTypes()) {
            if (type.getStyle() == player.getFightType().getStyle()) {
                player.setFightType(type);
                player.getEncoder().sendByteState(player.getFightType().getParent(), player.getFightType().getChild());
                return;
            }
        }
        player.setFightType(player.getWeapon().getFightTypes()[0]);
        player.getEncoder().sendByteState(player.getFightType().getParent(), player.getFightType().getChild());
    }

    /**
     * Gets the identification of the interface that will be displayed.
     *
     * @return the identification of the interface.
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the identification of the line the weapon name will be displayed
     * on.
     *
     * @return the identification of the name line.
     */
    public final int getNameLine() {
        return nameLine;
    }

    /**
     * Gets the base attack speed of weapons using this interface.
     *
     * @return the base attack speed.
     */
    public final int getSpeed() {
        return speed;
    }

    /**
     * Gets the fight types that correspond with this interface.
     *
     * @return the fight types that correspond.
     */
    public final FightType[] getFightTypes() {
        return fightTypes;
    }

    /**
     * Gets the identification of the special bar for this interface.
     *
     * @return the identification of the special bar.
     */
    public final int getSpecialBar() {
        return specialBar;
    }

    /**
     * Gets the identification of the special meter for this interface.
     *
     * @return the identification of the special meter.
     */
    public final int getSpecialMeter() {
        return specialMeter;
    }
}
