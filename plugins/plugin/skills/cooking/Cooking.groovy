package plugin.skills.cooking

import com.asteria.game.character.player.Player
import com.asteria.game.character.player.skill.SkillData
import com.asteria.game.character.player.skill.action.ProducingSkillAction
import com.asteria.game.character.player.skill.action.SkillAction
import com.asteria.game.location.Position;
import com.asteria.game.model.character.Animation;
import com.asteria.game.model.item.Item;
import com.asteria.game.model.item.ItemDefinition;
import com.asteria.game.plugin.PluginSignature
import com.asteria.task.Task
import com.asteria.utility.RandomGen

import static com.asteria.game.character.player.skill.Skills.COOKING

@PluginSignature(SkillAction.class)
final class Cooking extends ProducingSkillAction {

    private static final double COOKING_BURN_RATE = 55.0
    private final CookingData data
    private final boolean cookStove
    private int counter
    private boolean burned
    private final RandomGen random = new RandomGen()

    Cooking(Player player, Position position, CookingData data, boolean cookStove, int counter) {
        super(player, Optional.of(position))
        this.data = data
        this.cookStove = cookStove
        this.counter = counter
    }

    @Override
    void onStop() {
        player.cookData = null
        player.cookPosition = null
        player.usingStove = false
    }

    @Override
    void onProduce(Task t, boolean success) {
        if (success) {
            player.animation(!cookStove ? new Animation(897) : new Animation(896))
            player.encoder.sendMessage(
                    (burned ? "Oops! You accidently burn the " : "You cook the ").concat("${ItemDefinition.DEFINITIONS[data.rawId].name}."))
            counter--
            if (counter == 0)
                t.cancel()
        }
    }

    @Override
    boolean canExecute() {
        if (!checkCooking())
            return false
        burned = determineBurn()
        return true
    }

    @Override
    boolean init() {
        if (!checkCooking())
            return false
        player.encoder.sendCloseWindows()
        return true
    }

    @Override
    Item removeItem() {
        new Item(data.rawId)
    }

    @Override
    Item produceItem() {
        burned ? new Item(data.burntId) : new Item(data.cookedId)
    }

    @Override
    double experience() {
        burned ? 0 : data.experience
    }

    @Override
    int delay() {
        4
    }

    @Override
    boolean instant() {
        true
    }

    @Override
    SkillData skill() {
        SkillData.COOKING
    }

    private boolean determineBurn() {
        if (player.skills[COOKING].level >= data.masterLevel) {
            return false
        }
        double burn_chance = (COOKING_BURN_RATE - (cookStove ? 4.0 : 0.0))
        double cook_level = player.skills[COOKING].level
        double lev_needed = data.level
        double burn_stop = data.masterLevel
        double multi_a = (burn_stop - lev_needed)
        double burn_dec = (burn_chance / multi_a)
        double multi_b = (cook_level - lev_needed)
        burn_chance -= (multi_b * burn_dec)
        double randNum = random.nextDouble() * 100.0
        return burn_chance <= randNum
    }

    private boolean checkCooking() {
        // TODO: If cooking on fire, check if the fire still exists.
        if (counter == 0)
            return false
        if (!player.inventory.contains(data.rawId)) {
            player.encoder.sendMessage "You don't have any ${data} to cook."
            return false
        }
        if (!player.skills[COOKING].reqLevel(data.level)) {
            player.encoder.sendMessage "You need a cooking level of ${data.level} to cook ${data}."
            return false
        }
        return true
    }
}
