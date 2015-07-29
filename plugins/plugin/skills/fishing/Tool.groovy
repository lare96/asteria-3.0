package plugin.skills.fishing


import com.asteria.game.character.player.Player
import com.asteria.game.character.player.skill.Skill
import com.asteria.game.character.player.skill.Skills
import com.asteria.game.item.Item
import com.asteria.utility.RandomGen

enum Tool {
    NET(303, 1, -1, 0.30, 621, [Catchable.SHRIMP, Catchable.ANCHOVY]){
        @Override
        Catchable catchable() {
            Catchable.SHRIMP
        }
    },
    BIG_NET(305, 16, -1, 0.25, 620, [Catchable.MACKEREL, Catchable.OYSTER, Catchable.COD, Catchable.BASS, Catchable.CASKET]){
        @Override
        Item[] onCatch(Player player) {
            int amount = random.inclusive(1, 4)
            int slots = player.inventory.remaining()
            int counter = 0
            def items = new Item[amount]
            if (amount > slots)
                amount = slots
            amount.times {
                items[counter++] = new Item(calculate(player).id)
            }
            return items
        }

        @Override
        Catchable catchable() {
            Catchable.MACKEREL
        }
    },
    FISHING_ROD(307, 5, 313, 0.40, 622, [Catchable.SARDINE, Catchable.HERRING, Catchable.PIKE, Catchable.SLIMY_EEL, Catchable.CAVE_EEL, Catchable.LAVA_EEL]){
        @Override
        Catchable catchable() {
            Catchable.SARDINE
        }
    },
    FLY_FISHING_ROD(309, 20, 314, 0.45, 622, [Catchable.TROUT, Catchable.SALMON]){
        @Override
        Catchable catchable() {
            Catchable.TROUT
        }
    },
    HARPOON(311, 35, -1, 0.15, 618, [Catchable.TUNA, Catchable.SWORDFISH]){
        @Override
        Catchable catchable() {
            Catchable.TUNA
        }
    },
    SHARK_HARPOON(311, 76, -1, 0.05, 618, [Catchable.SHARK]){
        @Override
        Catchable catchable() {
            Catchable.SHARK
        }
    },
    LOBSTER_POT(301, 40, -1, 0.20, 619, [Catchable.LOBSTER]){
        @Override
        Catchable catchable() {
            Catchable.LOBSTER
        }
    }

    final int id
    final int level
    final int needed
    final double success
    final int animation
    final Catchable[] catchables
    static RandomGen random = new RandomGen()

    Tool(int id, int level, int needed, double success, int animation, List<Catchable> catchables) {
        this.id = id
        this.level = level
        this.needed = needed
        this.success = success
        this.animation = animation
        this.catchables = catchables.toArray()
    }

    @Override
    String toString() {
        name().toLowerCase().replaceAll("_", " ")
    }

    Catchable catchable() {
        null
    }

    Catchable calculate(Player player) {
        List<Catchable> success = new ArrayList<>(catchables.length)
        Skill skill = player.skills[Skills.FISHING]
        catchables.findAll {skill.reqLevel(it.level) && it.catchable(player) }.each { success.add it }
        Collections.shuffle(success, random.get())
        return success.find { random.success(it.chance) } ?: catchable()
    }

    Item[] onCatch(Player player) {
        [new Item(calculate(player).id)] as Item[]
    }
}