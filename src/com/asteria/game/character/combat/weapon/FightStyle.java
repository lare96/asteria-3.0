package com.asteria.game.character.combat.weapon;

import com.asteria.game.character.combat.CombatType;
import com.asteria.game.character.player.skill.Skills;

/**
 * The enumerated type whose elements represent the fighting styles.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum FightStyle {
    ACCURATE {
        @Override
        public int[] skills(CombatType type) {
            return type == CombatType.RANGED ? new int[] { Skills.RANGED } : new int[] { Skills.ATTACK };
        }
    },
    AGGRESSIVE {
        @Override
        public int[] skills(CombatType type) {
            return type == CombatType.RANGED ? new int[] { Skills.RANGED } : new int[] { Skills.STRENGTH };
        }
    },
    DEFENSIVE {
        @Override
        public int[] skills(CombatType type) {
            return type == CombatType.RANGED ? new int[] { Skills.RANGED, Skills.DEFENCE } : new int[] { Skills.DEFENCE };
        }
    },
    CONTROLLED {
        @Override
        public int[] skills(CombatType type) {
            return new int[] { Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE };
        }
    };

    /**
     * Determines which skills will be trained by this fighting style based on
     * {@code type}.
     *
     * @param type
     *            the combat type being used to attack.
     * @return the skills that will be trained.
     */
    public abstract int[] skills(CombatType type);
}