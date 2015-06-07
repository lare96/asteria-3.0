package plugin.skills.prayer

import com.asteria.game.character.Animation
import com.asteria.game.character.player.Player
import com.asteria.game.character.player.skill.SkillData
import com.asteria.game.character.player.skill.action.DestructionSkillAction
import com.asteria.game.character.player.skill.action.SkillAction
import com.asteria.game.item.Item
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.task.Task

@PluginSignature(SkillAction.class)
final class PrayerBoneBury extends DestructionSkillAction {

    private final Bone bone

    PrayerBoneBury(Player player, Bone bone) {
        super(player, Optional.empty())
        this.bone = bone
    }

    @Override
    boolean canExecute() {
        true
    }

    @Override
    Item destructItem() {
        new Item(bone.id)
    }

    @Override
    void onDestruct(Task t, boolean success) {
        if (success) {
            player.animation new Animation(827)
            player.encoder.sendSound(380, 0, 0)
            player.encoder.sendMessage "You bury the ${bone}."
            player.buryTimer.reset()
        }
        t.cancel()
    }

    @Override
    int delay() {
        0
    }

    @Override
    boolean instant() {
        true
    }

    @Override
    boolean init() {
        super.init() && player.buryTimer.elapsed(1200)
    }

    @Override
    double experience() {
        bone.experience
    }

    @Override
    SkillData skill() {
        SkillData.PRAYER
    }
}