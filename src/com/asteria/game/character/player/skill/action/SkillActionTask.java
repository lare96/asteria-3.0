package com.asteria.game.character.player.skill.action;

import com.asteria.game.character.player.IOState;
import com.asteria.game.character.player.Player;
import com.asteria.task.Task;

/**
 * The skill action task whose job is to process and execute the various
 * functions of a skill action. The skill action tasks are the core of what make
 * skills function the way they do.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class SkillActionTask extends Task {

    /**
     * The skill action dedicated to this task.
     */
    private final SkillAction action;

    /**
     * The player this skill action task is for.
     */
    private final Player player;

    /**
     * The counter that determines how many ticks have passed.
     */
    private int counter;

    /**
     * Create a new {@link SkillAction}.
     * 
     * @param action
     *            the skill action dedicated to this task.
     */
    public SkillActionTask(SkillAction action) {
        super(1, action.instant());
        this.action = action;
        this.player = action.getPlayer();
    }

    @Override
    public void onSubmit() {
        if (!action.init()) {
            this.cancel();
            return;
        }
        player.getMovementQueue().reset();
    }

    @Override
    public void execute() {
        boolean active = player.getSkillEvent()[action.skill().getIndex()];
        if (player.getSession().getState() == IOState.LOGGED_OUT || !active) {
            this.cancel();
            return;
        }
        if (++counter >= action.delay()) {
            if (!action.canExecute()) {
                this.cancel();
                return;
            }
            action.execute(this);
            action.getPosition().ifPresent(player::facePosition);
            counter = 0;
        }
        action.animation().ifPresent(player::animation);
    }

    @Override
    public void onCancel() {
        player.getSkillEvent()[action.skill().getIndex()] = false;
        player.animation(null);
        action.onStop();
    }
}
