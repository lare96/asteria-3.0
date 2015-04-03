package plugin.skills.prayer

enum Bone {
    BONES(526, 4.5),
    BAT_BONES(530, 5.2),
    MONKEY_BONES(3179, 5),
    WOLF_BONES(2859, 4.5),
    BIG_BONES(532, 15),
    BABYDRAGON_BONES(534, 30),
    DRAGON_BONES(536, 72)

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
        Bone bone = null
        values().each {
            if (it.id == id)
                bone = it
        }
        return bone
    }
}