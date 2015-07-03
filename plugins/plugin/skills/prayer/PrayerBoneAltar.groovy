package plugin.skills.prayer

import com.asteria.game.character.Animation
import com.asteria.game.character.player.Player
import com.asteria.game.character.player.skill.SkillData
import com.asteria.game.character.player.skill.action.DestructionSkillAction
import com.asteria.game.character.player.skill.action.SkillAction
import com.asteria.game.item.Item
import com.asteria.game.location.Position
import com.asteria.game.plugin.PluginSignature
import com.asteria.task.Task

@PluginSignature(SkillAction.class)
final class PrayerBoneAltar extends DestructionSkillAction {

    private final Bone bone

    PrayerBoneAltar(Player player, Bone bone, Position position) {
        super(player, Optional.of(position))
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
            player.animation new Animation(894)
            player.messages.sendLocalGraphic(624, position.get(), 0)
            player.messages.sendMessage "You offer the ${bone} to the gods... they seem pleased."
        }
    }

    @Override
    int delay() {
        6
    }

    @Override
    boolean instant() {
        true
    }

    @Override
    double experience() {
        (bone.experience * 2)
    }

    @Override
    SkillData skill() {
        SkillData.PRAYER
    }
}
