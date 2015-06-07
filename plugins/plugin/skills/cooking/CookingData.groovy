package plugin.skills.cooking

import com.asteria.game.character.player.Player
import com.asteria.game.model.item.ItemDefinition;

enum CookingData {
    SHRIMP(317, 1, 315, 34, 323, 30),
    SARDINE(327, 5, 325, 38, 369, 40),
    HERRING(345, 10, 347, 37, 357, 50),
    ANCHOVIES(321, 15, 319, 34, 323, 30),
    MACKEREL(353, 16, 355, 45, 357, 60),
    TROUT(335, 20, 333, 50, 343, 70),
    COD(341, 23, 339, 39, 343, 75),
    PIKE(349, 25, 351, 52, 343, 80),
    SLIMY_EEL(3379, 28, 3381, 56, 3383, 95),
    SALMON(331, 30, 329, 58, 343, 90),
    TUNA(359, 35, 361, 63, 367, 100),
    CAVE_EEL(5001, 38, 5003, 72, 5002, 115),
    LOBSTER(377, 40, 379, 74, 381, 120),
    BASS(363, 46, 365, 80, 367, 130),
    SWORDFISH(371, 50, 373, 86, 375, 140),
    LAVA_EEL(2148, 53, 2149, 89, 3383, 140),
    SHARK(383, 76, 385, 94, 387, 210)

    final int rawId
    final int level
    final int cookedId
    final int masterLevel
    final int burntId
    final double experience

    CookingData(int rawId, int level, int cookedId, int masterLevel, int burntId, double experience) {
        this.rawId = rawId
        this.level = level
        this.cookedId = cookedId
        this.masterLevel = masterLevel
        this.burntId = burntId
        this.experience = experience
    }

    @Override
    String toString() {
        name.toLowerCase().replaceAll("_", " ")
    }

    void openInterface(Player player) {
        player.encoder.sendChatInterface 1743
        player.encoder.sendItemModelOnInterface(13716, 190, rawId)
        player.encoder.sendString("\\n\\n\\n\\n\\n${ItemDefinition.DEFINITIONS[rawId].name}", 13717)
    }

    static CookingData getData(int id) {
        CookingData cook = null
        values().each {
            if (it.rawId == id) {
                cook = it
            }
        }
        return cook
    }
}