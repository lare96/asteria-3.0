package plugin.skills.fishing

import static com.asteria.game.character.player.skill.Skills.FISHING

import com.asteria.game.character.Animation
import com.asteria.game.character.player.Player
import com.asteria.game.character.player.skill.SkillData
import com.asteria.game.character.player.skill.Skills
import com.asteria.game.character.player.skill.action.HarvestingSkillAction
import com.asteria.game.character.player.skill.action.SkillAction
import com.asteria.game.item.Item
import com.asteria.game.location.Position
import com.asteria.game.plugin.PluginSignature
import com.asteria.task.Task
import com.asteria.utility.RandomGen

@PluginSignature(SkillAction.class)
final class Fishing extends HarvestingSkillAction {

    private final Tool tool
    private final RandomGen random = new RandomGen()

    Fishing(Player player, Tool tool, Position position) {
        super(player, Optional.of(position))
        this.tool = tool
    }

    @Override
    boolean canExecute() {
        if (!checkFishing() || random.nextInt(50) == 0)
            return false
        return true
    }

    @Override
    void onHarvest(Task t, Item item, boolean success) {
        if (success) {
            Catchable c = Catchable.getCatchable(item.id)
            Skills.experience(player, c.experience, skill().id)
        }
    }

    @Override
    boolean init() {
        if (!checkFishing())
            return false
        player.encoder.sendMessage "You begin to fish..."
        player.animation new Animation(tool.animation)
        return true
    }

    @Override
    Item[] harvestItems() {
        tool.onCatch(player)
    }

    @Override
    double successFactor() {
        tool.success
    }

    @Override
    Optional<Animation> animation() {
        Optional.of(new Animation(tool.animation))
    }

    @Override
    Optional<Item[]> removeItems() {
        if (tool.needed <= 0)
            return Optional.empty()
        return Optional.of([new Item(tool.needed, 1)] as Item[])
    }

    @Override
    boolean instant() {
        false
    }

    @Override
    int experience() {
        0 // Experience handled elsewhere.
    }

    @Override
    SkillData skill() {
        SkillData.FISHING
    }

    @Override
    void onStop() {
        player.animation null
    }

    private boolean checkFishing() {
        if (!player.inventory.contains(tool.id)) {
            player.encoder.sendMessage "You need a ${tool} to fish here!"
            return false
        }
        if (tool.needed > 0) {
            if (!player.inventory.contains(tool.needed)) {
                player.encoder.sendMessage "You do not have enough bait."
                return false
            }
        }
        if (player.inventory.remaining() < 1) {
            player.encoder.sendMessage "You do not have any space left in your inventory."
            return false
        }
        if (!player.skills[FISHING].reqLevel(tool.level)) {
            player.encoder.sendMessage "You must have a Fishing level of ${tool.level} to use this tool."
            return false
        }
        return true
    }
}

