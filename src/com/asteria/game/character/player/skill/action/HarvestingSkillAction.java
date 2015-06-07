package com.asteria.game.character.player.skill.action;

import java.util.Optional;

import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;
import com.asteria.game.location.Position;
import com.asteria.game.task.Task;
import com.asteria.utility.RandomGen;
import com.google.common.base.Preconditions;

/**
 * The skill action that represents an action where items are periodically added
 * to and removed from an inventory based on a success factor. This type of
 * skill action is more complicated and requires that a player have the items to
 * be removed and the space for the items to harvest.
 * <p>
 * <p>
 * The skills that may use this type skill action include, but are not limited
 * to {@code FISHING} and {@code WOODCUTTING}.
 *
 * @author lare96 <http://github.com/lare96>
 * @see SkillAction
 * @see DestructionSkillAction
 * @see ProducingSkillAction
 */
public abstract class HarvestingSkillAction extends SkillAction {

    /**
     * The factor boost that determines the success rate for harvesting based on
     * skill level. The higher the number the less frequently harvest will be
     * obtained. A value higher than {@code 99} or lower than {@code 0} will
     * throw an {@link IllegalStateException}.
     */
    private static final int SUCCESS_FACTOR = 10;

    /**
     * The random generator instance that will generate random numbers.
     */
    private final RandomGen random = new RandomGen();

    /**
     * Creates a new {@link HarvestingSkillAction}.
     *
     * @param player
     *            the player this skill action is for.
     * @param position
     *            the position the player should face.
     */
    public HarvestingSkillAction(Player player, Optional<Position> position) {
        super(player, position);
    }

    @Override
    public final void execute(Task t) {
        Preconditions.checkState(SUCCESS_FACTOR >= 0 && SUCCESS_FACTOR <= 99, "Invalid success factor for harvesting!");
        Player player = getPlayer();
        int factor = (player.getSkills()[skill().getId()].getLevel() / SUCCESS_FACTOR);
        double boost = (factor * 0.01);
        if (random.roll((successFactor() + boost))) {
            Optional<Item[]> removeItems = removeItems();
            Item[] harvestItems = harvestItems();

            if (removeItems.isPresent()) {
                if (!player.getInventory().containsAll(removeItems.get())) {
                    player.getEncoder().sendMessage("You do not have the " + "required items to perform this!");
                    t.cancel();
                    return;
                }
            }
            for (Item item : harvestItems) {
                if (player.getInventory().add(item)) {
                    player.getEncoder().sendMessage("You get some " + item.getDefinition().getName() + ".");
                    Skills.experience(player, experience(), skill().getId());
                    removeItems.ifPresent(player.getInventory()::removeAll);
                    onHarvest(t, item, true);
                } else {
                    onHarvest(t, item, false);
                    player.getEncoder().sendMessage("You do not have any " + "space left in your inventory!");
                    t.cancel();
                    return;
                }
            }
        }
    }

    @Override
    public int delay() {
        return 1;
    }

    /**
     * The method executed upon harvest of the items.
     *
     * @param t
     *            the task executing this method.
     * @param item
     *            the item being harvested.
     * @param success
     *            determines if the harvest was successful or not.
     */
    public void onHarvest(Task t, Item item, boolean success) {

    }

    /**
     * The success factor for the harvest. The higher the number means the more
     * frequently harvest will be obtained.
     *
     * @return the success factor.
     */
    public abstract double successFactor();

    /**
     * The items to be removed upon a successful harvest.
     *
     * @return the items to be removed.
     */
    public abstract Optional<Item[]> removeItems();

    /**
     * The items to be harvested upon a successful harvest.
     *
     * @return the items to be harvested.
     */
    public abstract Item[] harvestItems();
}
