package com.asteria.game.character.combat.ranged;

import static com.asteria.game.character.combat.ranged.CombatRangedAmmo.ADAMANT_ARROW;
import static com.asteria.game.character.combat.ranged.CombatRangedAmmo.BRONZE_ARROW;
import static com.asteria.game.character.combat.ranged.CombatRangedAmmo.IRON_ARROW;
import static com.asteria.game.character.combat.ranged.CombatRangedAmmo.MITHRIL_ARROW;
import static com.asteria.game.character.combat.ranged.CombatRangedAmmo.RUNE_ARROW;
import static com.asteria.game.character.combat.ranged.CombatRangedAmmo.STEEL_ARROW;

import java.util.Arrays;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.item.container.Equipment;
import com.asteria.utility.CollectionUtils;
import com.asteria.utility.CollectionUtils.ImmutableMultimapBuilder;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

/**
 * The enumerated type that holds all of the ranged bows and the ammo they are
 * permitted to use.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public enum CombatRangedBow {
    SHORTBOW(841, BRONZE_ARROW, IRON_ARROW),
    LONGBOW(839, BRONZE_ARROW, IRON_ARROW),
    OAK_SHORTBOW(843, BRONZE_ARROW, IRON_ARROW, STEEL_ARROW),
    OAK_LONGBOW(845, BRONZE_ARROW, IRON_ARROW, STEEL_ARROW),
    WILLOW_SHORTBOW(849, BRONZE_ARROW, IRON_ARROW, STEEL_ARROW, MITHRIL_ARROW),
    WILLOW_LONGBOW(847, BRONZE_ARROW, IRON_ARROW, STEEL_ARROW, MITHRIL_ARROW),
    MAPLE_SHORTBOW(853, BRONZE_ARROW, IRON_ARROW, STEEL_ARROW, MITHRIL_ARROW, ADAMANT_ARROW),
    MAPLE_LONGBOW(851, BRONZE_ARROW, IRON_ARROW, STEEL_ARROW, MITHRIL_ARROW, ADAMANT_ARROW),
    YEW_SHORTBOW(857, BRONZE_ARROW, IRON_ARROW, STEEL_ARROW, MITHRIL_ARROW, ADAMANT_ARROW, RUNE_ARROW),
    YEW_LONGBOW(855, BRONZE_ARROW, IRON_ARROW, STEEL_ARROW, MITHRIL_ARROW, ADAMANT_ARROW, RUNE_ARROW),
    MAGIC_SHORTBOW(861, BRONZE_ARROW, IRON_ARROW, STEEL_ARROW, MITHRIL_ARROW, ADAMANT_ARROW, RUNE_ARROW),
    MAGIC_LONGBOW(859, BRONZE_ARROW, IRON_ARROW, STEEL_ARROW, MITHRIL_ARROW, ADAMANT_ARROW, RUNE_ARROW);

    /**
     * The multimap that holds all of our ranged weapons and their respective
     * ranged ammo that they are permitted to shoot. For this multimap we use a
     * {@link HashMultimap} instead of your traditional
     * {@link LinkedListMultimap} or {@link ArrayListMultimap} for constant time
     * when checking for arrows. This collection is immutable which means that
     * no elements can be added or removed from it.
     */
    private static final ImmutableMultimap<Integer, CombatRangedAmmo> WEAPONS = CollectionUtils
        .build(new ImmutableMultimapBuilder<Integer, CombatRangedAmmo>() {
            @Override
            public Multimap<Integer, CombatRangedAmmo> build() {
                Multimap<Integer, CombatRangedAmmo> map = HashMultimap.create();
                Arrays.stream(values()).forEach($it -> map.putAll($it.id, Arrays.asList($it.ammo)));
                return map;
            }
        });

    /**
     * The item identification for this ranged weapon.
     */
    private final int id;

    /**
     * The array of ammo that this ranged weapon is permitted to use.
     */
    private final CombatRangedAmmo[] ammo;

    /**
     * Creates a new {@link CombatRangedBow}.
     *
     * @param id
     *            the item identification for this ranged weapon.
     * @param ammo
     *            the array of ammo for this ranged weapon.
     */
    private CombatRangedBow(int id, CombatRangedAmmo... ammo) {
        this.id = id;
        this.ammo = ammo;
    }

    /**
     * The static boolean method that determines if {@code player} wielding
     * {@code bow} can fire {@code ammo} with it.
     * 
     * @param player
     *            the player this is being determined for.
     * @param ammo
     *            the ammo the player is using.
     * @return {@code true} if the player can use the ammo, {@code false}
     *         otherwise.
     */
    public static boolean canUse(Player player, CombatRangedAmmo ammo) {
        Item $it = player.getEquipment().get(Equipment.WEAPON_SLOT);
        if ($it == null)
            return true;
        ImmutableCollection<CombatRangedAmmo> values = WEAPONS.get($it.getId());
        if (values.isEmpty())
            return true;
        if (!values.contains(ammo)) {
            boolean needsPlural = !ammo.toString().endsWith("s");
            player.getMessages().sendMessage("You cannot use " + ammo + "" + (needsPlural ? "s" : "") + " with this ranged weapon.");
            return false;
        }
        return true;
    }

    /**
     * Gets the item identification for this ranged weapon.
     * 
     * @return the item identification.
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the array of ammo that this ranged weapon is permitted to use.
     * 
     * @return the array of ammo for this ranged weapon.
     */
    public final CombatRangedAmmo[] getAmmo() {
        return ammo;
    }
}
