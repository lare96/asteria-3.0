package com.asteria.content.skills.prayer;

import java.util.Optional;

import com.asteria.game.character.Animation;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.skill.SkillData;
import com.asteria.game.character.player.skill.action.DestructionSkillAction;
import com.asteria.game.item.Item;
import com.asteria.game.location.Position;
import com.asteria.task.Task;

/**
 * The destruction skill action that handles the bones on altar process.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class PrayerBoneAltar extends DestructionSkillAction {

    /**
     * The bone that will be used on the altar by the player.
     */
    private final Bone bone;

    /**
     * Creates a new {@link PrayerBoneAltar}.
     * 
     * @param player
     *            the player this skill action is for.
     * @param bone
     *            the bone to be used on the altar.
     * @param position
     *            the position of the altar.
     */
    public PrayerBoneAltar(Player player, Bone bone, Position position) {
        super(player, Optional.of(position));
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
            player.animation(new Animation(894));
            player.getEncoder().sendLocalGraphic(624, super.getPosition().get(), 0);
            player.getEncoder().sendMessage("You offer the " + bone + " to the gods... they seem pleased.");
        }
    }

    @Override
    public int delay() {
        return 6;
    }

    @Override
    public boolean instant() {
        return true;
    }

    @Override
    public int experience() {
        return (bone.getExperience() * 2);
    }

    @Override
    public SkillData skill() {
        return SkillData.PRAYER;
    }
}
