package com.asteria.game.character.player.skill.action;

import java.util.Optional;

import com.asteria.game.character.Animation;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.SkillData;
import com.asteria.game.location.Position;
import com.asteria.game.plugin.PluginContext;
import com.asteria.task.Task;
import com.asteria.task.TaskHandler;

/**
 * The skill action that is the parent class of all other skill actions. This
 * type of skill action is extremely broad and should almost <b>always</b> be
 * extended only to add additional and more specific functionality.
 * <p>
 * <p>
 * To reiterate, no actual skills should be extending this skill action.
 *
 * @author lare96 <http://github.com/lare96>
 * @see HarvestingSkillAction
 * @see DestructionSkillAction
 * @see ProducingSkillAction
 */
public abstract class SkillAction implements PluginContext {

    /**
     * The player this skill action is for.
     */
    private final Player player;

    /**
     * The position the player should face.
     */
    private final Optional<Position> position;

    /**
     * Creates a new {@link DestructionSkillAction}.
     *
     * @param player
     *         the player this skill action is for.
     * @param position
     *         the position the player should face.
     */
    public SkillAction(Player player, Optional<Position> position) {
        this.player = player;
        this.position = position;
    }

    /**
     * Starts this skill action by submitting a new skill action task.
     */
    public final void start() {
        if (!player.isSkillAction()) {
            player.setSkillAction(true);
            TaskHandler.submit(new SkillActionTask(this));
        }
    }

    /**
     * The delay intervals of this skill action in ticks.
     *
     * @return the delay intervals.
     */
    public abstract int delay();

    /**
     * Determines if this skill action should be executed instantly rather than
     * after the delay.
     *
     * @return {@code true} if this skill action should be instant,
     * {@code false} otherwise.
     */
    public abstract boolean instant();

    /**
     * Initializes this skill action and performs any pre-checks.
     *
     * @return {@code true} if the skill action can proceed, {@code false}
     * otherwise.
     */
    public abstract boolean init();

    /**
     * Determines if this skill can be executed.
     *
     * @return {@code true} if this skill can be executed, {@code false}
     * otherwise.
     */
    public abstract boolean canExecute();

    /**
     * The method executed when the delay has elapsed.
     *
     * @param t
     *         the task executing this skill action.
     */
    public abstract void execute(Task t);

    /**
     * The experience given from this skill action.
     *
     * @return the experience given.
     */
    public abstract double experience();

    /**
     * The skill that this skill action is for.
     *
     * @return the skill data.
     */
    public abstract SkillData skill();

    /**
     * The method executed when this skill action is stopped.
     */
    public void onStop() {

    }

    /**
     * The animation played periodically during this skill action.
     *
     * @return the animation played.
     */
    public Optional<Animation> animation() {
        return Optional.empty();
    }

    /**
     * Gets the player this skill action is for.
     *
     * @return the player.
     */
    public final Player getPlayer() {
        return player;
    }

    /**
     * Gets the position the player should face.
     *
     * @return the position.
     */
    public final Optional<Position> getPosition() {
        return position;
    }
}
