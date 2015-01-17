package com.asteria.content.skills.fishing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;
import com.asteria.utility.RandomGen;

/**
 * The enumerated type whose elements represent the fishing tools.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public enum Tool {
    NET(303, 1, -1, 0.30, 621, Catchable.SHRIMP, Catchable.ANCHOVY) {
        @Override
        public Catchable catchable() {
            return Catchable.SHRIMP;
        }
    },
    BIG_NET(305, 16, -1, 0.25, 620, Catchable.MACKEREL, Catchable.OYSTER, Catchable.COD, Catchable.BASS, Catchable.CASKET) {
        @Override
        public Item[] onCatch(Player player) {
            int amount = random.inclusive(1, 4);
            int slots = player.getInventory().remaining();
            int counter = 0;
            Item[] items = new Item[amount];
            if (amount > slots)
                amount = slots;
            for (int i = 0; i < amount; i++) {
                items[counter++] = new Item(calculate(player).getId());
            }
            return items;
        }

        @Override
        public Catchable catchable() {
            return Catchable.MACKEREL;
        }
    },
    FISHING_ROD(307, 5, 313, 0.40, 622, Catchable.SARDINE, Catchable.HERRING, Catchable.PIKE, Catchable.SLIMY_EEL, Catchable.CAVE_EEL, Catchable.LAVA_EEL) {
        @Override
        public Catchable catchable() {
            return Catchable.SARDINE;
        }
    },
    FLY_FISHING_ROD(309, 20, 314, 0.45, 622, Catchable.TROUT, Catchable.SALMON) {
        @Override
        public Catchable catchable() {
            return Catchable.TROUT;
        }
    },
    HARPOON(311, 35, -1, 0.15, 618, Catchable.TUNA, Catchable.SWORDFISH) {
        @Override
        public Catchable catchable() {
            return Catchable.TUNA;
        }
    },
    SHARK_HARPOON(311, 76, -1, 0.05, 618, Catchable.SHARK) {
        @Override
        public Catchable catchable() {
            return Catchable.SHARK;
        }
    },
    LOBSTER_POT(301, 40, -1, 0.20, 619, Catchable.LOBSTER) {
        @Override
        public Catchable catchable() {
            return Catchable.LOBSTER;
        }
    };

    /**
     * The identification for this tool.
     */
    private final int id;

    /**
     * The level needed to be able to use this tool.
     */
    private final int level;

    /**
     * The item needed to be able to use this tool.
     */
    private final int needed;

    /**
     * The harvest success rate of this tool.
     */
    private final double success;

    /**
     * The animation performed when using this tool.
     */
    private final int animation;

    /**
     * The catchables able to be caught with this tool.
     */
    private final Catchable[] catchables;

    /**
     * The random generator instance that will generate random numbers.
     */
    private static RandomGen random = new RandomGen();

    /**
     * Creates a new {@link Tool}.
     * 
     * @param id
     *            the identification for this tool.
     * @param level
     *            the level needed to be able to use this tool.
     * @param needed
     *            the item needed to be able to use this tool.
     * @param success
     *            the harvest success rate of this tool.
     * @param animation
     *            the animation performed when using this tool.
     * @param catchables
     *            the catchables able to be caught with this tool.
     */
    private Tool(int id, int level, int needed, double success, int animation, Catchable... catchables) {
        this.id = id;
        this.level = level;
        this.needed = needed;
        this.success = success;
        this.animation = animation;
        this.catchables = catchables;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replaceAll("_", " ");
    }

    /**
     * The catchable that will be returned if no other catchable has been
     * calculated.
     * 
     * @return the default catchable for this tool.
     */
    public abstract Catchable catchable();

    /**
     * Calculates the next catchable that will be caught by this tool.
     * 
     * @param player
     *            the player this catchable will be caught for.
     * @return the calculated catchable.
     */
    public Catchable calculate(Player player) {
        List<Catchable> success = new ArrayList<>(catchables.length);
        Stream<Catchable> stream = Arrays.stream(catchables);
        stream.filter(c -> c.getLevel() <= player.getSkills()[Skills.FISHING].getLevel() && c.catchable(player)).forEach(
            success::add);
        Collections.shuffle(success, random);
        return success.stream().filter(c -> random.roll(c.getChance())).findFirst().orElse(catchable());
    }

    /**
     * The method executed that returns the catchables that will be caught.
     * 
     * @param player
     *            the player to return the catchables for.
     * @return the calculated catchables wrapped in an item array.
     */
    public Item[] onCatch(Player player) {
        return new Item[] { new Item(calculate(player).getId()) };
    }

    /**
     * Gets the item id of this tool.
     * 
     * @return the item id.
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the level you need to be to use this tool.
     * 
     * @return the level needed.
     */
    public final int getLevel() {
        return level;
    }

    /**
     * Gets the id of an item needed to use this tool.
     * 
     * @return the item needed.
     */
    public final int getNeeded() {
        return needed;
    }

    /**
     * Gets the speed of this tool.
     * 
     * @return the speed.
     */
    public final double getSuccess() {
        return success;
    }

    /**
     * Gets the animation performed when using this tool.
     * 
     * @return the animation.
     */
    public final int getAnimation() {
        return animation;
    }

    /**
     * Gets the fish you can catch with this tool.
     * 
     * @return the fish available.
     */
    public final Catchable[] getFish() {
        return catchables;
    }
}