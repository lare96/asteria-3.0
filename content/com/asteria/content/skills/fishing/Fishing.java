package com.asteria.content.skills.fishing;

import java.util.Optional;

import com.asteria.game.character.Animation;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.SkillData;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.character.player.skill.action.HarvestingSkillAction;
import com.asteria.game.item.Item;
import com.asteria.game.location.Position;
import com.asteria.task.Task;
import com.asteria.utility.RandomGen;

/**
 * The harvesting skill action that handles the fishing process.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class Fishing extends HarvestingSkillAction {

    /**
     * The tool that will be used to catch items.
     */
    private final Tool tool;

    /**
     * The random generator instance that will generate random numbers.
     */
    private final RandomGen random = new RandomGen();

    /**
     * Creates a new {@link Fishing}.
     * 
     * @param player
     *            the player this skill action is for.
     * @param tool
     *            the tool that will be used to catch items.
     * @param position
     *            the position of the fishing spot.
     */
    public Fishing(Player player, Tool tool, Position position) {
        super(player, Optional.of(position));
        this.tool = tool;
    }

    @Override
    public boolean canExecute() {
        if (!checkFishing() || random.nextInt(50) == 0)
            return false;
        return true;
    }

    @Override
    public void onHarvest(Task t, Item item, boolean success) {
        if (success) {
            Catchable c = Catchable.getCatchable(item.getId()).get();
            Skills.experience(getPlayer(), c.getExperience(), skill().getId());
        }
    }

    @Override
    public boolean init() {
        Player player = getPlayer();
        if (!checkFishing())
            return false;
        player.getEncoder().sendMessage("You begin to fish...");
        player.animation(new Animation(tool.getAnimation()));
        return true;
    }

    @Override
    public Item[] harvestItems() {
        return tool.onCatch(getPlayer());
    }

    @Override
    public double successFactor() {
        return tool.getSuccess();
    }

    @Override
    public Optional<Animation> animation() {
        return Optional.of(new Animation(tool.getAnimation()));
    }

    @Override
    public Optional<Item[]> removeItems() {
        if (tool.getNeeded() <= 0)
            return Optional.empty();
        return Optional.of(new Item[] { new Item(tool.getNeeded(), 1) });
    }

    @Override
    public boolean instant() {
        return false;
    }

    @Override
    public int experience() {
        return 0; // Experience handled elsewhere.
    }

    @Override
    public SkillData skill() {
        return SkillData.FISHING;
    }

    /**
     * Determines if the fishing skill can be executed.
     * 
     * @return {@code true} if the skill can be executed, {@code false}
     *         otherwise.
     */
    private boolean checkFishing() {
        Player player = getPlayer();
        if (!player.getInventory().contains(tool.getId())) {
            player.getEncoder().sendMessage("You need a " + tool + " to fish here!");
            return false;
        }
        if (tool.getNeeded() > 0) {
            if (!player.getInventory().contains(tool.getNeeded())) {
                player.getEncoder().sendMessage("You do not have enough bait.");
                return false;
            }
        }
        if (player.getInventory().remaining() < 1) {
            player.getEncoder().sendMessage("You do not have any space left in your inventory.");
            return false;
        }
        if (!player.getSkills()[Skills.FISHING].reqLevel(tool.getLevel())) {
            player.getEncoder().sendMessage("You must have a fishing level of " + tool.getLevel() + " to use this tool.");
            return false;
        }
        return true;
    }
}
