package com.asteria.game.character.player.skill.action;

import java.util.Optional;

import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;
import com.asteria.game.location.Position;
import com.asteria.task.Task;

/**
 * The skill action that represents an action where one item in an inventory is
 * replaced with a new one. This type of skill action is somewhat basic and
 * requires that a player have the item to be removed.
 * <p>
 * <p>
 * The skills that may use this type skill action include, but are not limited
 * to {@code COOKING}.
 *
 * @author lare96 <http://github.com/lare96>
 * @see SkillAction
 * @see DestructionSkillAction
 * @see HarvestingSkillAction
 */
public abstract class ProducingSkillAction extends SkillAction {

    /**
     * Creates a new {@link ProducingSkillAction}.
     *
     * @param player
     *            the player this skill action is for.
     * @param position
     *            the position the player should face.
     */
    public ProducingSkillAction(Player player, Optional<Position> position) {
        super(player, position);
    }

    @Override
    public final void execute(Task t) {
        Player player = getPlayer();
        Item removeItem = removeItem();
        if (player.getInventory().remove(removeItem)) {
            Skills.experience(player, experience(), skill().getId());
            player.getInventory().add(produceItem());
            onProduce(t, true);
        } else {
            player.getEncoder().sendMessage("You do not have any " + removeItem.getDefinition().getName() + " left.");
            t.cancel();
            onProduce(t, false);
            return;
        }
    }

    /**
     * The method executed upon production of an item.
     *
     * @param t
     *            the task executing this method.
     * @param success
     *            determines if the production was successful or not.
     */
    public void onProduce(Task t, boolean success) {

    }

    /**
     * The item that will be removed upon production.
     *
     * @return the item that will be removed.
     */
    public abstract Item removeItem();

    /**
     * The item that will be added upon production.
     *
     * @return the item that will be added.
     */
    public abstract Item produceItem();
}
