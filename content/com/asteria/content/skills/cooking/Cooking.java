package com.asteria.content.skills.cooking;

import java.util.Optional;

import com.asteria.game.character.Animation;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.SkillData;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.character.player.skill.action.ProducingSkillAction;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemDefinition;
import com.asteria.game.location.Position;
import com.asteria.task.Task;
import com.asteria.utility.RandomGen;

/**
 * The producing skill action that handles the cooking process.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class Cooking extends ProducingSkillAction {

    /**
     * The flat cooking burn rate factor. The lower this number, the less chance
     * of burning food.
     */
    private static final double COOKING_BURN_RATE = 55.0;

    /**
     * The data for the food that is being cooked.
     */
    private final CookingData data;

    /**
     * The flag that determines if the player is cooking on a stove or fire.
     */
    private final boolean cookStove;

    /**
     * The counter that determines how many pieces of food will be cooked.
     */
    private int counter;

    /**
     * The flag that determines if the food was burned or not.
     */
    private boolean burned;

    /**
     * The random generator instance that will generate random numbers.
     */
    private final RandomGen random = new RandomGen();

    /**
     * Creates a new {@link Cooking}.
     * 
     * @param player
     *            the player that this skill action is for.
     * @param position
     *            the position the stove or fire are located on.
     * @param data
     *            the data for the food that is being cooked.
     * @param cookStove
     *            determines if the player is cooking on a stove or fire.
     * @param counter
     *            the counter that determines how many pieces of food will be
     *            cooked.
     */
    public Cooking(Player player, Position position, CookingData data, boolean cookStove, int counter) {
        super(player, Optional.of(position));
        this.data = data;
        this.cookStove = cookStove;
        this.counter = counter;
    }

    @Override
    public void onStop() {
        Player player = getPlayer();
        player.setCookData(null);
        player.setCookPosition(null);
        player.setUsingStove(false);
    }

    @Override
    public void onProduce(Task t, boolean success) {
        if (success) {
            Player player = getPlayer();
            player.animation(!cookStove ? new Animation(897) : new Animation(896));
            player.getEncoder().sendMessage(
                (burned ? "Oops! You accidently burn the " : "You cook the").concat(ItemDefinition.DEFINITIONS[data.getRawId()]
                    .getName() + "."));
            counter--;
            if (counter == 0)
                t.cancel();
        }
    }

    @Override
    public boolean canExecute() {
        if (!checkCooking())
            return false;
        burned = determineBurn();
        return true;
    }

    @Override
    public boolean init() {
        Player player = getPlayer();
        if (!checkCooking())
            return false;
        player.getEncoder().sendCloseWindows();
        return true;
    }

    @Override
    public Item removeItem() {
        return new Item(data.getRawId());
    }

    @Override
    public Item produceItem() {
        return burned ? new Item(data.getBurntId()) : new Item(data.getCookedId());
    }

    @Override
    public int experience() {
        return burned ? 0 : data.getExperience();
    }

    @Override
    public int delay() {
        return 4;
    }

    @Override
    public boolean instant() {
        return true;
    }

    @Override
    public SkillData skill() {
        return SkillData.COOKING;
    }

    /**
     * Determines if the food trying to be cooked will be burned.
     * 
     * @return {@code true} if it will be burned, {@code false} otherwise.
     */
    public boolean determineBurn() {
        Player player = getPlayer();
        if (player.getSkills()[Skills.COOKING].getLevel() >= data.getMasterLevel()) {
            return false;
        }
        double burn_chance = (COOKING_BURN_RATE - (cookStove ? 4.0 : 0.0));
        double cook_level = player.getSkills()[Skills.COOKING].getLevel();
        double lev_needed = data.getLevel();
        double burn_stop = data.getMasterLevel();
        double multi_a = (burn_stop - lev_needed);
        double burn_dec = (burn_chance / multi_a);
        double multi_b = (cook_level - lev_needed);
        burn_chance -= (multi_b * burn_dec);
        double randNum = random.nextDouble() * 100.0;
        return burn_chance <= randNum;
    }

    /**
     * Determines if the cooking skill can be executed.
     * 
     * @return {@code true} if the skill can be executed, {@code false}
     *         otherwise.
     */
    private boolean checkCooking() {

        // TODO: If cooking on fire, check if the fire still exists.
        Player player = getPlayer();
        if (counter == 0)
            return false;
        if (!player.getInventory().contains(data.getRawId())) {
            player.getEncoder().sendMessage("You don't have any " + data + " to cook.");
            return false;
        }
        if (!player.getSkills()[Skills.COOKING].reqLevel(data.getLevel())) {
            player.getEncoder().sendMessage("You need a cooking level of " + data.getLevel() + " to cook " + data + ".");
            return false;
        }
        return true;
    }
}
