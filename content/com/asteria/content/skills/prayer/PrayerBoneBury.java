package com.asteria.content.skills.prayer;

import java.util.Optional;

import com.asteria.game.character.Animation;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.SkillData;
import com.asteria.game.character.player.skill.action.DestructionSkillAction;
import com.asteria.game.item.Item;
import com.asteria.task.Task;

/**
 * The destruction skill action that handles the burying bones process.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class PrayerBoneBury extends DestructionSkillAction {

    /**
     * The bone that will be buried by the player.
     */
    private final Bone bone;

    /**
     * Creates a new {@link PrayerBoneBury}.
     * 
     * @param player
     *            the player this skill action is for.
     * @param bone
     *            the bone that will be buried by the player.
     */
    public PrayerBoneBury(Player player, Bone bone) {
        super(player, Optional.empty());
        this.bone = bone;
    }

    @Override
    public boolean canExecute() {
        return true;
    }

    @Override
    public Item destructItem() {
        return new Item(bone.getId());
    }

    @Override
    public void onDestruct(Task t, boolean success) {
        if (success) {
            Player player = getPlayer();
            player.animation(new Animation(827));
            player.getEncoder().sendSound(380, 0, 0);
            player.getEncoder().sendMessage("You bury the " + bone + ".");
            player.getBuryTimer().reset();
        }
    }

    @Override
    public int delay() {
        return 0;
    }

    @Override
    public boolean instant() {
        return true;
    }

    @Override
    public boolean init() {
        return super.init() && getPlayer().getBuryTimer().elapsed(1200);
    }

    @Override
    public int experience() {
        return bone.getExperience();
    }

    @Override
    public SkillData skill() {
        return SkillData.PRAYER;
    }
}