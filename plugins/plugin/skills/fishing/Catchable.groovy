package plugin.skills.fishing

import com.asteria.game.character.player.Player
import com.asteria.game.location.Location
import com.google.common.collect.ImmutableSet

enum Catchable {

    // TODO: Some way of catching Manta ray?

    SHRIMP(317, 1, 0.85, 10),
    SARDINE(327, 5, 0.8, 20),
    HERRING(345, 10, 0.85, 30),
    ANCHOVY(321, 15, 0.45, 40),
    MACKEREL(353, 16, 0.9, 20),
    CASKET(405, 16, 0.01, 100),
    OYSTER(407, 16, 0.05, 80),
    TROUT(335, 20, 0.95, 50),
    COD(341, 23, 0.9, 45),
    PIKE(349, 25, 0.9, 60),
    SLIMY_EEL(3379, 28, 0.05, 65){
        @Override
        boolean catchable(Player player) {
            Location.inWilderness(player)
        }
    },
    SALMON(331, 30, 0.75, 70),
    TUNA(359, 35, 0.95, 80),
    CAVE_EEL(5001, 38, 0.07, 80){
        @Override
        boolean catchable(Player player) {
            Location.inWilderness(player)
        }
    },
    LOBSTER(377, 40, 0.85, 90),
    BASS(363, 46, 0.5, 100),
    SWORDFISH(371, 50, 0.75, 100),
    LAVA_EEL(2148, 53, 0.85, 60){
        @Override
        boolean catchable(Player player) {
            Location.inWilderness(player)
        }
    },
    SHARK(383, 76, 0.7, 110)

    static final ImmutableSet<Catchable> VALUES = ImmutableSet.copyOf(values())
    final int id
    final int level
    final double chance
    final double experience

    Catchable(int id, int level, double chance, double experience) {
        this.id = id
        this.level = level
        this.chance = chance
        this.experience = experience
    }

    @Override
    String toString() {
        name().toLowerCase().replaceAll("_", " ")
    }

    boolean catchable(Player player) {
        true
    }

    static Catchable getCatchable(int id) {
        return VALUES.find {it.id == id}
    }
}