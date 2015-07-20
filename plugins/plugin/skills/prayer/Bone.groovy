package plugin.skills.prayer

import com.google.common.collect.ImmutableSet

enum Bone {
    BONES(526, 4.5),
    BAT_BONES(530, 5.2),
    MONKEY_BONES(3179, 5),
    WOLF_BONES(2859, 4.5),
    BIG_BONES(532, 15),
    BABYDRAGON_BONES(534, 30),
    DRAGON_BONES(536, 72)

    static final ImmutableSet<Bone> VALUES = ImmutableSet.copyOf(values())
    final int id
    final double experience

    Bone(int id, double experience) {
        this.id = id
        this.experience = experience
    }

    @Override
    String toString() {
        name().toLowerCase().replaceAll("_", " ")
    }

    static Bone getBone(int id) {
        return VALUES.find {it.id == id}
    }
}