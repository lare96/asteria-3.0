package com.asteria.game.character.player.content;

import java.util.Optional;

import com.asteria.game.character.Animation;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.Skill;
import com.asteria.game.character.player.skill.SkillData;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;
import com.asteria.utility.RandomGen;

/**
 * The enumerated type managing consumable food types.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author lare96 <http://github.com/lare96>
 */
public enum FoodConsumable {
    SHRIMP(3, 315),
    LOBSTER(12, 379),
    MANTA_RAY(22, 391),
    MONKFISH(16, 7946),
    MACKREL(6, 355),
    SALMON(9, 329),
    SEA_TURTLE(22, 397),
    SHARK(20, 385),
    SWORDFISH(14, 373),
    TROUT(7, 333),
    TUNA(10, 361),
    TUNA_POTATO(22, 7060),
    ANCHOVIES(2, 319),
    CABBAGE(2, 1965),
    CRAYFISH(2, 13432),
    EQUA_LEAVES(2, 2128),
    ONION(2, 1957),
    BANANA(2, 1957),
    CHEESE(2, 1985),
    DWELLBERRIES(2, 2126),
    JANGERBERRIES(2, 247),
    LIME(2, 2120),
    LEMON(2, 2102),
    ORANGE(2, 2108),
    SPICY_TOMATO(2, 9994),
    SPINACH_ROLL(2, 1969),
    TOMATO(2, 1982),
    ROAST_BIRD_MEAT(2, 9980),
    COOKED_MEAT(2, 2142),
    UGTHANKI_MEAT(2, 1861),
    SARDINE(2, 325),
    BREAD(2, 2309),
    COOKED_RABBIT(2, 3228),
    FROGSPAWN_GUMBO(2, 10961),
    COOKED_CHICKEN(2, 2140),
    KARAMBWANJI(2, 3151),
    CHOCOLATE_BAR(2, 1973),
    SPICY_MINCED_MEAT(2, 9996),
    ROE(2, 11324),
    WHITE_TREE_FRUIT(2, 6469),
    HERRING(2, 347),
    BAKED_POTATO(2, 6701),
    RED_BANANA(2, 7572),
    SLICED_RED_BANANA(2, 7574),
    TCHIKI_MONKEY_NUTS(2, 7573),
    TCHIKI_NUT_PASTE(2, 7575),
    SPICY_SUACE(2, 7072),
    MINCED_MEAT(2, 7070),
    FROG_SPAWN(2, 5004),
    TOAD_CRUNCHIES(2, 9538),
    BAGUETTE(2, 6961),
    FILLETS(2, 10969),
    EEL_SUSHI(2, 10971),
    GRUBS_A_LA_MODE(2, 10966),
    MUSHROOMS(2, 10968),
    LOACH(2, 10970),
    ROST_FROG(2, 10967),
    GIANT_CARP(2, 337),
    GIANT_FROG_LEGS(2, 4517),
    COD(4, 339),
    PIKE(4, 351),
    BASS(9, 365),
    STRAWBERRY(3, 5323),
    COOKED_SWEETCORN(6, 5986),
    WATERMELON_SLICE(5, 5984),
    CAKE(4, 1891, 1893, 1895),
    CHOCOLATE_CAKE(5, 1897, 1899, 1901),
    PLAIN_PIZZA(7, 2289, 2291),
    MEAT_PIZZA(8, 2293, 2295),
    ANCHOVY_PIZZA(9, 2297, 2299),
    PINEAPPLE_PIZZA(11, 2301, 2303),
    REDBERRY_PIE(2, 2325, 2333) {
        @Override
        public long getDelay() {
            return 600;
        }
    },
    APPLE_PIE(7, 2323, 2335) {
        @Override
        public long getDelay() {
            return 600;
        }
    },
    MEAT_PIE(6, 2327, 2331) {
        @Override
        public long getDelay() {
            return 600;
        }
    },
    KEBAB(-1, 1971) {
        @Override
        public void onEffect(Player player) {
            RandomGen random = new RandomGen();
            Skill skill = player.getSkills()[Skills.HITPOINTS];
            int realLevel = skill.getRealLevel();
            if (random.floatRandom(100F) >= 61.24F) {
                int healAmount = Math.round((10 * 100F) / realLevel);
                skill.increaseLevel(healAmount, realLevel);
                player.getEncoder().sendMessage("It restores some life points" + ".");
                return;
            }
            if (random.floatRandom(100F) >= 21.12F) {
                skill.increaseLevel(random.inclusive(10, 20), realLevel);
                player.getEncoder().sendMessage("That was a good kebab. You " + "feel a lot better.");
                return;
            }
            if (random.floatRandom(100F) >= 8.71F) {
                player.getEncoder().sendMessage("The kebab didn't seem to do " + "a lot.");
                return;
            }
            if (random.floatRandom(100F) >= 3.65F) {
                skill.increaseLevel(30, realLevel);
                player.getSkills()[Skills.ATTACK].increaseLevel(random.exclusive(3));
                player.getSkills()[Skills.STRENGTH].increaseLevel(random.exclusive(3));
                player.getSkills()[Skills.DEFENCE].increaseLevel(random.exclusive(3));
                player.getEncoder().sendMessage("Wow, that was an amazing " + "kebab! You feel really invigorated.");
                return;
            }
            if (random.floatRandom(100F) >= 3.28F) {
                player.getSkills()[Skills.ATTACK].decreaseLevel(random.exclusive(3));
                player.getSkills()[Skills.STRENGTH].decreaseLevel(random.exclusive(3));
                player.getSkills()[Skills.DEFENCE].decreaseLevel(random.exclusive(3));
                player.getEncoder().sendMessage("That tasted a bit dodgy. You" + " feel a bit ill.");
                return;
            }
            if (random.floatRandom(100F) >= 2.00F) {
                int id = random.inclusiveExcludes(0, player.getSkills().length, Skills.HITPOINTS);
                Skill randomSkill = player.getSkills()[id];
                randomSkill.decreaseLevel(random.exclusive(3));
                player.getEncoder().sendMessage("Eating the kebab has damaged" +
                        " your " + SkillData.values()[id] + " stat.");
                return;
            }
        }
    };

    /**
     * The amount of hit points this food heals.
     */
    private final int healAmount;

    /**
     * The identifiers which represent this food type.
     */
    private final int[] ids;

    /**
     * Creates a new {@link FoodConsumable}.
     *
     * @param healAmount
     *         the amount of hit points this food heals.
     * @param ids
     *         the identifiers which represent this food type.
     */
    private FoodConsumable(int healAmount, int... ids) {
        this.ids = ids;
        this.healAmount = healAmount;
    }

    @Override
    public final String toString() {
        return name().toLowerCase().replace("_", " ");
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
        Optional<FoodConsumable> food = forId(item.getId());
        // TODO: Check duel rule for no food.
        if (!food.isPresent() || player.isDead() || !player.getEatingTimer().elapsed(food.get().getDelay()))
            return false;
        player.animation(new Animation(829));
        player.getEatingTimer().reset();
        player.getPotionTimer().reset();
        player.getInventory().remove(item, slot);

        Optional<Item> replacement = getReplacementItem(item);
        if (replacement.isPresent()) {
            player.getInventory().set(slot, replacement.get());
            player.getInventory().refresh();
        }
        player.getEncoder().sendMessage(food.get().getMessage());
        food.get().onEffect(player);
        Skills.refresh(player, Skills.HITPOINTS);
        return true;
    }

    /**
     * The method executed after the player has successfully consumed this
     * food.
     * This method may be overridden to provide a different functionality for
     * such foods as kebabs.
     *
     * @param player
     *         the player that has consumed the food.
     */
    public void onEffect(Player player) {
        Skill skill = player.getSkills()[Skills.HITPOINTS];
        int realLevel = skill.getRealLevel();
        if (skill.getLevel() >= realLevel) {
            return;
        }
        skill.increaseLevel(getHealAmount(), realLevel);
        player.getEncoder().sendMessage("It healed some health.");
    }

    /**
     * Retrieves the delay before consuming another food type. This method may
     * be overridden to provide a different functionality for foods that have a
     * different delay.
     *
     * @return the delay before consuming another food type.
     */
    public long getDelay() {
        return 600 * 3;
    }

    /**
     * Retrieves the chatbox message printed when a food is consumed. This
     * method may be overridden to provide a different functionality for foods
     * which have a different chatbox message.
     *
     * @return the chatbox message printed when a food is consumed.
     */
    public String getMessage() {
        return (ids.length > 1 ? "You eat a slice of the " : "You eat the ") + toString() + ".";
    }

    /**
     * Retrieves the replacement item for {@code item}.
     *
     * @param item
     *         the item to retrieve the replacement item for.
     * @return the replacement item wrapped in an optional, or an empty optional
     * if no replacement item is available.
     */
    private static Optional<Item> getReplacementItem(Item item) {
        Optional<FoodConsumable> food = forId(item.getId());
        if (food.isPresent()) {
            int length = food.get().getIds().length;
            for (int index = 0; index < length; index++) {
                if (food.get().getIds()[index] == item.getId() && index + 1 < length) {
                    return Optional.of(new Item(food.get().getIds()[index + 1]));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves the food consumable element for {@code id}.
     *
     * @param id
     *         the id that the food consumable is attached to.
     * @return the food consumable wrapped in an optional, or an empty optional
     * if no food consumable was found.
     */
    private static Optional<FoodConsumable> forId(int id) {
        for (FoodConsumable food : FoodConsumable.values()) {
            for (int foodId : food.getIds()) {
                if (id == foodId) {
                    return Optional.of(food);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Gets the amount of hit points this food heals.
     *
     * @return the amount this food heals.
     */
    public final int getHealAmount() {
        return healAmount;
    }

    /**
     * Gets the identifiers which represent this food type.
     *
     * @return the identifiers for this food.
     */
    public final int[] getIds() {
        return ids;
    }
}