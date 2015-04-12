package com.asteria.game.character.player.skill.action;

import java.util.Optional;

import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemDefinition;
import com.asteria.game.location.Position;
import com.asteria.task.Task;

/**
 * The skill action that represents an action where one item is removed from an
 * inventory and lost forever. This type of skill action is very basic and only
 * requires that a player have the item to destruct in their inventory.
 * <p>
 * <p>
 * The skills that may use this type skill action include, but are not limited
 * to {@code PRAYER}.
 *
 * @author lare96 <http://github.com/lare96>
 * @see SkillAction
 * @see HarvestingSkillAction
 * @see ProducingSkillAction
 */
public abstract class DestructionSkillAction extends SkillAction {

    /**
     * Creates a new {@link DestructionSkillAction}.
     *
     * @param player
     *            the player this skill action is for.
     * @param position
     *            the position the player should face.
     */
    public DestructionSkillAction(Player player, Optional<Position> position) {
        super(player, position);
    }

    @Override
    public boolean init() {
        Player player = getPlayer();
        String name = ItemDefinition.DEFINITIONS[destructItem().getId()].getName();
        if (!player.getInventory().contains(destructItem().getId())) {
            player.getEncoder().sendMessage("You do not have any " + name + "" + " in your inventory.");
            return false;
        }
        return true;
    }

    @Override
    public final void execute(Task t) {
        Player player = getPlayer();
        if (player.getInventory().remove(destructItem())) {
            onDestruct(t, true);
            Skills.experience(player, experience(), skill().getId());
            return;
        }
        onDestruct(t, false);
        t.cancel();
    }

    /**
     * The method executed upon destruction of the item.
     *
     * @param t
     *            the task executing this method.
     * @param success
     *            determines if the destruction was successful or not.
     */
    public void onDestruct(Task t, boolean success) {

    }

    /**
     * The item that will be removed upon destruction.
     *
     * @return the item that will be removed.
     */
    public abstract Item destructItem();
}
