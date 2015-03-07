package com.asteria.game.character.combat.weapon;

import com.asteria.game.character.combat.Combat;

/**
 * The enumerated type whose elements represent the fighting types.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public enum FightType {
    STAFF_BASH(406, 43, 0, Combat.ATTACK_CRUSH, FightStyle.ACCURATE),
    STAFF_POUND(406, 43, 1, Combat.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    STAFF_FOCUS(406, 43, 2, Combat.ATTACK_CRUSH, FightStyle.DEFENSIVE),
    WARHAMMER_POUND(401, 43, 0, Combat.ATTACK_CRUSH, FightStyle.ACCURATE),
    WARHAMMER_PUMMEL(401, 43, 1, Combat.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    WARHAMMER_BLOCK(401, 43, 2, Combat.ATTACK_CRUSH, FightStyle.DEFENSIVE),
    SCYTHE_REAP(408, 43, 0, Combat.ATTACK_SLASH, FightStyle.ACCURATE),
    SCYTHE_CHOP(451, 43, 1, Combat.ATTACK_STAB, FightStyle.AGGRESSIVE),
    SCYTHE_JAB(412, 43, 2, Combat.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    SCYTHE_BLOCK(408, 43, 3, Combat.ATTACK_SLASH, FightStyle.DEFENSIVE),
    BATTLEAXE_CHOP(1833, 43, 0, Combat.ATTACK_SLASH, FightStyle.ACCURATE),
    BATTLEAXE_HACK(1833, 43, 1, Combat.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    BATTLEAXE_SMASH(401, 43, 2, Combat.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    BATTLEAXE_BLOCK(1833, 43, 3, Combat.ATTACK_SLASH, FightStyle.DEFENSIVE),
    CROSSBOW_ACCURATE(427, 43, 0, Combat.ATTACK_RANGED, FightStyle.ACCURATE),
    CROSSBOW_RAPID(427, 43, 1, Combat.ATTACK_RANGED, FightStyle.AGGRESSIVE),
    CROSSBOW_LONGRANGE(427, 43, 2, Combat.ATTACK_RANGED, FightStyle.DEFENSIVE),
    SHORTBOW_ACCURATE(426, 43, 0, Combat.ATTACK_RANGED, FightStyle.ACCURATE),
    SHORTBOW_RAPID(426, 43, 1, Combat.ATTACK_RANGED, FightStyle.AGGRESSIVE),
    SHORTBOW_LONGRANGE(426, 43, 2, Combat.ATTACK_RANGED, FightStyle.DEFENSIVE),
    LONGBOW_ACCURATE(426, 43, 0, Combat.ATTACK_RANGED, FightStyle.ACCURATE),
    LONGBOW_RAPID(426, 43, 1, Combat.ATTACK_RANGED, FightStyle.AGGRESSIVE),
    LONGBOW_LONGRANGE(426, 43, 2, Combat.ATTACK_RANGED, FightStyle.DEFENSIVE),
    DAGGER_STAB(400, 43, 0, Combat.ATTACK_STAB, FightStyle.ACCURATE),
    DAGGER_LUNGE(400, 43, 1, Combat.ATTACK_STAB, FightStyle.AGGRESSIVE),
    DAGGER_SLASH(451, 43, 2, Combat.ATTACK_STAB, FightStyle.AGGRESSIVE),
    DAGGER_BLOCK(400, 43, 3, Combat.ATTACK_STAB, FightStyle.DEFENSIVE),
    SWORD_STAB(412, 43, 0, Combat.ATTACK_STAB, FightStyle.ACCURATE),
    SWORD_LUNGE(412, 43, 1, Combat.ATTACK_STAB, FightStyle.AGGRESSIVE),
    SWORD_SLASH(451, 43, 2, Combat.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    SWORD_BLOCK(412, 43, 3, Combat.ATTACK_STAB, FightStyle.DEFENSIVE),
    SCIMITAR_CHOP(451, 43, 0, Combat.ATTACK_SLASH, FightStyle.ACCURATE),
    SCIMITAR_SLASH(451, 43, 1, Combat.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    SCIMITAR_LUNGE(412, 43, 2, Combat.ATTACK_STAB, FightStyle.CONTROLLED),
    SCIMITAR_BLOCK(451, 43, 3, Combat.ATTACK_SLASH, FightStyle.DEFENSIVE),
    LONGSWORD_CHOP(451, 43, 0, Combat.ATTACK_SLASH, FightStyle.ACCURATE),
    LONGSWORD_SLASH(451, 43, 1, Combat.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    LONGSWORD_LUNGE(412, 43, 2, Combat.ATTACK_STAB, FightStyle.CONTROLLED),
    LONGSWORD_BLOCK(451, 43, 3, Combat.ATTACK_SLASH, FightStyle.DEFENSIVE),
    MACE_POUND(1833, 43, 0, Combat.ATTACK_CRUSH, FightStyle.ACCURATE),
    MACE_PUMMEL(401, 43, 1, Combat.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    MACE_SPIKE(412, 43, 2, Combat.ATTACK_STAB, FightStyle.CONTROLLED),
    MACE_BLOCK(401, 43, 3, Combat.ATTACK_CRUSH, FightStyle.DEFENSIVE),
    KNIFE_ACCURATE(806, 43, 0, Combat.ATTACK_RANGED, FightStyle.ACCURATE),
    KNIFE_RAPID(806, 43, 1, Combat.ATTACK_RANGED, FightStyle.AGGRESSIVE),
    KNIFE_LONGRANGE(806, 43, 2, Combat.ATTACK_RANGED, FightStyle.DEFENSIVE),
    SPEAR_LUNGE(2080, 43, 0, Combat.ATTACK_STAB, FightStyle.CONTROLLED),
    SPEAR_SWIPE(2081, 43, 1, Combat.ATTACK_SLASH, FightStyle.CONTROLLED),
    SPEAR_POUND(2082, 43, 2, Combat.ATTACK_CRUSH, FightStyle.CONTROLLED),
    SPEAR_BLOCK(2080, 43, 3, Combat.ATTACK_STAB, FightStyle.DEFENSIVE),
    TWOHANDEDSWORD_CHOP(407, 43, 0, Combat.ATTACK_SLASH, FightStyle.ACCURATE),
    TWOHANDEDSWORD_SLASH(407, 43, 1, Combat.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    TWOHANDEDSWORD_SMASH(406, 43, 2, Combat.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    TWOHANDEDSWORD_BLOCK(407, 43, 3, Combat.ATTACK_SLASH, FightStyle.DEFENSIVE),
    PICKAXE_SPIKE(412, 43, 0, Combat.ATTACK_STAB, FightStyle.ACCURATE),
    PICKAXE_IMPALE(412, 43, 1, Combat.ATTACK_STAB, FightStyle.AGGRESSIVE),
    PICKAXE_SMASH(401, 43, 2, Combat.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    PICKAXE_BLOCK(412, 43, 3, Combat.ATTACK_STAB, FightStyle.DEFENSIVE),
    CLAWS_CHOP(451, 43, 0, Combat.ATTACK_SLASH, FightStyle.ACCURATE),
    CLAWS_SLASH(451, 43, 1, Combat.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    CLAWS_LUNGE(412, 43, 2, Combat.ATTACK_STAB, FightStyle.CONTROLLED),
    CLAWS_BLOCK(451, 43, 3, Combat.ATTACK_SLASH, FightStyle.DEFENSIVE),
    HALBERD_JAB(412, 43, 0, Combat.ATTACK_STAB, FightStyle.CONTROLLED),
    HALBERD_SWIPE(440, 43, 1, Combat.ATTACK_SLASH, FightStyle.AGGRESSIVE),
    HALBERD_FEND(412, 43, 2, Combat.ATTACK_STAB, FightStyle.DEFENSIVE),
    UNARMED_PUNCH(422, 43, 0, Combat.ATTACK_CRUSH, FightStyle.ACCURATE),
    UNARMED_KICK(423, 43, 1, Combat.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
    UNARMED_BLOCK(422, 43, 2, Combat.ATTACK_CRUSH, FightStyle.DEFENSIVE),
    WHIP_FLICK(1658, 43, 0, Combat.ATTACK_SLASH, FightStyle.ACCURATE),
    WHIP_LASH(1658, 43, 1, Combat.ATTACK_SLASH, FightStyle.CONTROLLED),
    WHIP_DEFLECT(1658, 43, 2, Combat.ATTACK_SLASH, FightStyle.DEFENSIVE),
    THROWNAXE_ACCURATE(806, 43, 0, Combat.ATTACK_RANGED, FightStyle.ACCURATE),
    THROWNAXE_RAPID(806, 43, 1, Combat.ATTACK_RANGED, FightStyle.AGGRESSIVE),
    THROWNAXE_LONGRANGE(806, 43, 2, Combat.ATTACK_RANGED, FightStyle.DEFENSIVE),
    DART_ACCURATE(806, 43, 0, Combat.ATTACK_RANGED, FightStyle.ACCURATE),
    DART_RAPID(806, 43, 1, Combat.ATTACK_RANGED, FightStyle.AGGRESSIVE),
    DART_LONGRANGE(806, 43, 2, Combat.ATTACK_RANGED, FightStyle.DEFENSIVE),
    JAVELIN_ACCURATE(806, 43, 0, Combat.ATTACK_RANGED, FightStyle.ACCURATE),
    JAVELIN_RAPID(806, 43, 2, Combat.ATTACK_RANGED, FightStyle.AGGRESSIVE),
    JAVELIN_LONGRANGE(806, 43, 3, Combat.ATTACK_RANGED, FightStyle.DEFENSIVE);

    /**
     * The animation executed when this type is active.
     */
    private final int animation;

    /**
     * The parent config identification.
     */
    private final int parent;

    /**
     * The child config identification.
     */
    private final int child;

    /**
     * The type of bonus this type will apply.
     */
    private final int bonus;

    /**
     * The style active when this type is active.
     */
    private final FightStyle style;

    /**
     * Creates a new {@link FightType}.
     * 
     * @param animation
     *            the animation executed when this type is active.
     * @param parent
     *            the parent config identification.
     * @param child
     *            the child config identification.
     * @param bonus
     *            the type of bonus this type will apply.
     * @param style
     *            the style active when this type is active.
     */
    private FightType(int animation, int parent, int child, int bonus, FightStyle style) {
        this.animation = animation;
        this.parent = parent;
        this.child = child;
        this.bonus = bonus;
        this.style = style;
    }

    /**
     * Determines the corresponding bonus for this fight type.
     * 
     * @return the corresponding.
     */
    public final int getCorrespondingBonus() {
        switch (bonus) {
        case Combat.ATTACK_CRUSH:
            return Combat.DEFENCE_CRUSH;
        case Combat.ATTACK_MAGIC:
            return Combat.DEFENCE_MAGIC;
        case Combat.ATTACK_RANGED:
            return Combat.DEFENCE_RANGED;
        case Combat.ATTACK_SLASH:
            return Combat.DEFENCE_SLASH;
        case Combat.ATTACK_STAB:
            return Combat.DEFENCE_STAB;
        default:
            return Combat.DEFENCE_CRUSH;
        }
    }

    /**
     * Gets the animation executed when this type is active.
     * 
     * @return the animation executed.
     */
    public final int getAnimation() {
        return animation;
    }

    /**
     * Gets the parent config identification.
     * 
     * @return the parent config.
     */
    public final int getParent() {
        return parent;
    }

    /**
     * Gets the child config identification.
     * 
     * @return the child config.
     */
    public final int getChild() {
        return child;
    }

    /**
     * Gets the type of bonus this type will apply
     * 
     * @return the bonus type.
     */
    public final int getBonus() {
        return bonus;
    }

    /**
     * Gets the style active when this type is active.
     * 
     * @return the fighting style.
     */
    public final FightStyle getStyle() {
        return style;
    }
}