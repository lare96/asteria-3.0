package com.asteria.game.character.combat;

import java.util.HashMap;
import java.util.Map;

import com.asteria.game.NodeType;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.Hit;
import com.asteria.game.character.MovementQueue;
import com.asteria.game.character.combat.effect.CombatEffect;
import com.asteria.game.character.combat.magic.CombatSpells;
import com.asteria.game.character.combat.magic.CombatWeaken;
import com.asteria.game.character.combat.prayer.CombatPrayer;
import com.asteria.game.character.combat.ranged.CombatRangedAmmo;
import com.asteria.game.character.combat.weapon.FightStyle;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.content.WeaponInterface;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;
import com.asteria.game.item.container.Equipment;
import com.asteria.game.location.Position;
import com.asteria.task.TaskHandler;
import com.asteria.utility.RandomGen;
import com.asteria.utility.Settings;

import plugin.combat.DefaultMagicCombatStrategy;
import plugin.combat.DefaultMeleeCombatStrategy;
import plugin.combat.DefaultRangedCombatStrategy;

/**
 * A collection of utility methods and constants related to combat.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Combat {

    /**
     * The amount of time it takes in seconds for cached damage to timeout.
     */
    public static final long DAMAGE_CACHE_TIMEOUT = 60;

    /**
     * The percentage at which damage is reduced by combat protection prayers.
     */
    public static final double PRAYER_DAMAGE_REDUCTION = .20;

    /**
     * The percentage at which accuracy is reduced by combat protection
     * prayers.
     */
    public static final double PRAYER_ACCURACY_REDUCTION = .255;

    /**
     * The percentage at which hitpoints will be healed by from the prayer
     * level
     * when using redemption.
     */
    public static final double REDEMPTION_PRAYER_HEAL = .25;

    /**
     * The maximum amount of damage that retribution can inflict.
     */
    public static final int MAXIMUM_RETRIBUTION_DAMAGE = 15;

    /**
     * The radius in which the retribution effect will take place.
     */
    public static final int RETRIBUTION_RADIUS = 5;

    /**
     * The attack stab bonus identifier.
     */
    public static final int ATTACK_STAB = 0;

    /**
     * The attack slash bonus identifier.
     */
    public static final int ATTACK_SLASH = 1;

    /**
     * The attack crush bonus identifier.
     */
    public static final int ATTACK_CRUSH = 2;

    /**
     * The attack magic bonus identifier.
     */
    public static final int ATTACK_MAGIC = 3;

    /**
     * The attack ranged bonus identifier.
     */
    public static final int ATTACK_RANGED = 4;

    /**
     * The defence stab bonus identifier.
     */
    public static final int DEFENCE_STAB = 5;

    /**
     * The defence slash bonus identifier.
     */
    public static final int DEFENCE_SLASH = 6;

    /**
     * The defence crush bonus identifier.
     */
    public static final int DEFENCE_CRUSH = 7;

    /**
     * The defence magic bonus identifier.
     */
    public static final int DEFENCE_MAGIC = 8;

    /**
     * The defence ranged bonus identifier.
     */
    public static final int DEFENCE_RANGED = 9;

    /**
     * The strength bonus identifier.
     */
    public static final int BONUS_STRENGTH = 10;

    /**
     * The prayer bonus identifier.
     */
    public static final int BONUS_PRAYER = 11;

    /**
     * The names of all the bonuses in their exact identified slots.
     */
    public static final String[] BONUS_NAMES = {"Stab", "Slash", "Crush",
            "Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range",
            "Strength", "Prayer"};

    /**
     * The hash collection of all the NPCs mapped to their combat strategies.
     */
    public static final Map<Integer, CombatStrategy> STRATEGIES = new HashMap<>();

    /**
     * The random generator instance that will generate random numbers.
     */
    private static RandomGen random = new RandomGen();

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException
     *         if this class is instantiated.
     */
    private Combat() {
        throw new UnsupportedOperationException("This class cannot be " + "instantiated!");
    }

    /**
     * Determines the combat strategy for {@code npc}.
     *
     * @param npc
     *         the npc to determine the combat strategy for.
     * @return
     */
    public static CombatStrategy determineStrategy(int npc) {
        CombatStrategy combat = STRATEGIES.get(npc);
        if (combat == null)
            return Combat.newDefaultMeleeStrategy();
        return combat;
    }

    /**
     * Determines which spell {@code npc} will use when they have the
     * {@link DefaultMagicCombatStrategy} combat strategy.
     *
     * @param npc
     *         the npc that needs a spell.
     * @return the spell that the npc will cast.
     */
    public static CombatSpells prepareSpellCast(Npc npc) {
        switch (npc.getId()) {
            case 13:
            case 172:
            case 174:
                return random.random(new CombatSpells[] {CombatSpells.WEAKEN,
                        CombatSpells.FIRE_STRIKE, CombatSpells.EARTH_STRIKE,
                        CombatSpells.WATER_STRIKE});
            default:
                return CombatSpells.FIRE_STRIKE;
        }
    }

    /**
     * Determines which ammo {@code npc} will use when they have the
     * {@link DefaultRangedCombatStrategy} combat strategy.
     *
     * @param npc
     *         the npc that needs a spell.
     * @return the spell that the npc will cast.
     */
    public static CombatRangedAmmo prepareRangedAmmo(Npc npc) {
        switch (npc.getId()) {
            case 688:
                return CombatRangedAmmo.BRONZE_ARROW;
            default:
                return CombatRangedAmmo.STEEL_ARROW;
        }
    }

    /**
     * Determines if {@code character} is wearing full veracs.
     *
     * @param character
     *         the character to determine this for.
     * @return {@code true} if this character is wearing full veracs,
     * {@code false} otherwise.
     */
    public static boolean isFullVeracs(CharacterNode character) {
        return character.getType() == NodeType.NPC ? ((Npc) character).getDefinition().getName().equals("Verac the Defiled") : ((Player) character).getEquipment().containsAll(4753, 4757, 4759, 4755);
    }

    /**
     * Determines if {@code character} is wearing full dharoks.
     *
     * @param character
     *         the character to determine this for.
     * @return {@code true} if this character is wearing full dharoks,
     * {@code false} otherwise.
     */
    public static boolean isFullDharoks(CharacterNode character) {
        return character.getType() == NodeType.NPC ? ((Npc) character).getDefinition().getName().equals("Dharok the Wretched") : ((Player) character).getEquipment().containsAll(4716, 4720, 4722, 4718);
    }

    /**
     * Determines if {@code character} is wearing full karils.
     *
     * @param character
     *         the character to determine this for.
     * @return {@code true} if this character is wearing full karils,
     * {@code false} otherwise.
     */
    public static boolean isFullKarils(CharacterNode character) {
        return character.getType() == NodeType.NPC ? ((Npc) character).getDefinition().getName().equals("Karil the Tainted") : ((Player) character).getEquipment().containsAll(4732, 4736, 4738, 4734);
    }

    /**
     * Determines if {@code character} is wearing full ahrims.
     *
     * @param character
     *         the character to determine this for.
     * @return {@code true} if this character is wearing full ahrims,
     * {@code false} otherwise.
     */
    public static boolean isFullAhrims(CharacterNode character) {
        return character.getType() == NodeType.NPC ? ((Npc) character).getDefinition().getName().equals("Ahrim the Blighted") : ((Player) character).getEquipment().containsAll(4708, 4712, 4714, 4710);
    }

    /**
     * Determines if {@code character} is wearing full torags.
     *
     * @param character
     *         the character to determine this for.
     * @return {@code true} if this character is wearing full torags,
     * {@code false} otherwise.
     */
    public static boolean isFullTorags(CharacterNode character) {
        return character.getType() == NodeType.NPC ? ((Npc) character).getDefinition().getName().equals("Torag the Corrupted") : ((Player) character).getEquipment().containsAll(4745, 4749, 4751, 4747);
    }

    /**
     * Determines if {@code character} is wearing full guthans.
     *
     * @param character
     *         the character to determine this for.
     * @return {@code true} if this character is wearing full guthans,
     * {@code false} otherwise.
     */
    public static boolean isFullGuthans(CharacterNode character) {
        return character.getType() == NodeType.NPC ? ((Npc) character).getDefinition().getName().equals("Guthan the Infested") : ((Player) character).getEquipment().containsAll(4724, 4728, 4730, 4726);
    }

    /**
     * Determines if {@code player} is wielding a crystal bow.
     *
     * @param player
     *         the player to determine this for.
     * @return {@code true} if the player is wielding a crystal bow,
     * {@code false} otherwise.
     */
    public static boolean isCrystalBow(Player player) {
        Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
        if (item == null)
            return false;
        return item.getDefinition().getName().toLowerCase().contains("crystal" + " bow");
    }

    /**
     * Determines if {@code player} has any arrows equipped.
     *
     * @param player
     *         the player to determine this for.
     * @return {@code true} if the player has any arrows equipped, {@code false}
     * otherwise.
     */
    public static boolean isArrows(Player player) {
        Item item;
        if ((item = player.getEquipment().get(Equipment.ARROWS_SLOT)) == null) {
            return false;
        }
        return !(!item.getDefinition().getName().endsWith("arrow") && !item.getDefinition().getName().endsWith("arrow(p)") && !item.getDefinition().getName().endsWith("arrow(p+)") && !item.getDefinition().getName().endsWith("arrow(p++)"));
    }

    /**
     * Determines if {@code player} has any bolts equipped.
     *
     * @param player
     *         the player to determine this for.
     * @return {@code true} if the player has any bolts equipped, {@code false}
     * otherwise.
     */
    public static boolean isBolts(Player player) {
        Item item;
        if ((item = player.getEquipment().get(Equipment.ARROWS_SLOT)) == null) {
            return false;
        }
        return !(!item.getDefinition().getName().endsWith("bolts") && !item.getDefinition().getName().endsWith("bolts(p)") && !item.getDefinition().getName().endsWith("bolts(p+)") && !item.getDefinition().getName().endsWith("bolts(p++)"));
    }

    /**
     * Gets the ranged distance based on {@code weapon}.
     *
     * @param weapon
     *         the weapon you have equipped.
     * @return the ranged distance.
     * @throws IllegalArgumentException
     *         if the weapon interface type is invalid.
     */
    public static int getRangedDistance(WeaponInterface weapon) {
        switch (weapon) {
            case DART:
            case THROWNAXE:
                return 4;
            case KNIFE:
            case JAVELIN:
                return 5;
            case CROSSBOW:
            case LONGBOW:
                return 8;
            case SHORTBOW:
                return 7;
            default:
                throw new IllegalArgumentException("Invalid weapon interface " + "type!");
        }
    }

    /**
     * Gets the delay for the specified {@code type}.
     *
     * @param type
     *         the combat type to retrieve the delay for.
     * @return the delay for the combat type.
     * @throws IllegalArgumentException
     *         if the combat type is invalid.
     */
    public static int getDelay(CombatType type) {
        switch (type) {
            case MELEE:
                return 1;
            case RANGED:
                return 2;
            case MAGIC:
                return 3;
            default:
                throw new IllegalArgumentException("Invalid combat type!");
        }
    }

    /**
     * Applies the {@code effect} in any context.
     *
     * @param effect
     *         the effect that must be applied.
     * @return {@code true} if it was successfully applied, {@code false}
     * otherwise.
     */
    public static boolean effect(CombatEffect effect) {
        if (effect.apply()) {
            TaskHandler.submit(effect);
            return true;
        }
        return false;
    }

    /**
     * Calculates the combat level difference for wilderness player vs. player
     * combat.
     *
     * @param combatLevel
     *         the combat level of the first person.
     * @param otherCombatLevel
     *         the combat level of the other person.
     * @return the combat level difference.
     */
    public static int combatLevelDifference(int combatLevel, int otherCombatLevel) {
        if (combatLevel > otherCombatLevel) {
            return (combatLevel - otherCombatLevel);
        } else if (otherCombatLevel > combatLevel) {
            return (otherCombatLevel - combatLevel);
        } else {
            return 0;
        }
    }

    /**
     * Calculates a pseudo-random hit for {@code character} based on
     * {@code victim} and {@code type}.
     *
     * @param character
     *         the character this hit is being calculated for.
     * @param victim
     *         the victim of this hit that will be used as a factor.
     * @param type
     *         the character's combat type that will be used as a factor.
     * @return the generated hit, will most likely return a different result if
     * called on two different occasions even with the same arguments.
     * @throws IllegalArgumentException
     *         if the combat type is invalid.
     */
    public static Hit calculateRandomHit(CharacterNode character, CharacterNode victim, CombatType type) {
        switch (type) {
            case MELEE:
                return new Hit(random.inclusive(1, Combat.calculateMaxMeleeHit(character, victim)));
            case RANGED:
                return new Hit(random.inclusive(1, Combat.calculateMaxRangedHit(character, victim)));
            case MAGIC:
                if (Settings.DEBUG && character.getType() == NodeType.PLAYER)
                    ((Player) character).getEncoder().sendMessage("[DEBUG]: " +
                            "Maximum hit this turn is [" + character.getCurrentlyCasting().maximumHit() + "].");
                return new Hit(random.inclusive(0, character.getCurrentlyCasting().maximumHit()));
            default:
                throw new IllegalArgumentException("Invalid combat type!");
        }
    }

    /**
     * Determines if {@code attacker}'s attack will be successful.
     *
     * @param attacker
     *         the attacker that this will be determined for.
     * @param victim
     *         the victim of the attacker.
     * @param type
     *         the combat type used by the attacker.
     * @return {@code true} if the hit was accurate, {@code false} otherwise.
     */
    public static boolean isAccurate(CharacterNode attacker, CharacterNode victim, CombatType type) {
        boolean veracEffect = false;

        if (type == CombatType.MELEE) {
            if (Combat.isFullVeracs(attacker)) {
                if (random.nextInt(8) == 3) {
                    veracEffect = true;
                }
            }
        }

        double prayerMod = 1;
        double equipmentBonus = 1;
        double specialBonus = 1;
        int styleBonus = 0;
        int bonusType = -1;
        if (attacker.getType() == NodeType.PLAYER) {
            Player player = (Player) attacker;

            equipmentBonus = type == CombatType.MAGIC ? player.getBonus()[Combat.ATTACK_MAGIC] : player.getBonus()[player.getFightType().getBonus()];
            bonusType = player.getFightType().getCorrespondingBonus();
            if (type == CombatType.MELEE) {
                if (CombatPrayer.isActivated(player, CombatPrayer.CLARITY_OF_THOUGHT)) {
                    prayerMod = 1.05;
                } else if (CombatPrayer.isActivated(player, CombatPrayer.IMPROVED_REFLEXES)) {
                    prayerMod = 1.10;
                } else if (CombatPrayer.isActivated(player, CombatPrayer.INCREDIBLE_REFLEXES)) {
                    prayerMod = 1.15;
                }
            } else if (type == CombatType.RANGED) {
                // XXX: Ranged prayers here.

            } else if (type == CombatType.MAGIC) {
                // XXX: Magic prayers here.
            }

            if (player.getFightType().getStyle() == FightStyle.ACCURATE) {
                styleBonus = 3;
            } else if (player.getFightType().getStyle() == FightStyle.CONTROLLED) {
                styleBonus = 1;
            }

            if (player.isSpecialActivated()) {
                specialBonus = player.getCombatSpecial().getAccuracy();
            }
        }

        double attackCalc = Math.floor(equipmentBonus + attacker.getBaseAttack(type)) + 8;
        attackCalc *= prayerMod;
        attackCalc += styleBonus;

        if (equipmentBonus < -67) {
            attackCalc = random.exclusive(8) == 0 ? attackCalc : 0;
        }
        attackCalc *= specialBonus;

        equipmentBonus = 1;
        prayerMod = 1;
        styleBonus = 0;
        if (victim.getType() == NodeType.PLAYER) {
            Player player = (Player) victim;

            if (bonusType == -1) {
                equipmentBonus = type == CombatType.MAGIC ? player.getBonus()[Combat.DEFENCE_MAGIC] : player.getSkills()[Skills.DEFENCE].getLevel();
            } else {
                equipmentBonus = type == CombatType.MAGIC ? player.getBonus()[Combat.DEFENCE_MAGIC] : player.getBonus()[bonusType];
            }

            if (CombatPrayer.isActivated(player, CombatPrayer.THICK_SKIN)) {
                prayerMod = 1.05;
            } else if (CombatPrayer.isActivated(player, CombatPrayer.ROCK_SKIN)) {
                prayerMod = 1.10;
            } else if (CombatPrayer.isActivated(player, CombatPrayer.STEEL_SKIN)) {
                prayerMod = 1.15;
            }

            if (player.getFightType().getStyle() == FightStyle.DEFENSIVE) {
                styleBonus = 3;
            } else if (player.getFightType().getStyle() == FightStyle.CONTROLLED) {
                styleBonus = 1;
            }
        }

        double defenceCalc = Math.floor(equipmentBonus + victim.getBaseDefence(type)) + 8;
        defenceCalc *= prayerMod;
        defenceCalc += styleBonus;

        if (equipmentBonus < -67) {
            defenceCalc = random.exclusive(8) == 0 ? defenceCalc : 0;
        }
        if (veracEffect) {
            defenceCalc = 0;
        }
        double A = Math.floor(attackCalc);
        double D = Math.floor(defenceCalc);
        double hitSucceed = A < D ? (A - 1.0) / (2.0 * D) : 1.0 - (D + 1.0) / (2.0 * A);
        hitSucceed = hitSucceed >= 1.0 ? 0.99 : hitSucceed <= 0.0 ? 0.01 : hitSucceed;

        if (attacker.getType() == NodeType.PLAYER && Settings.DEBUG) {
            ((Player) attacker).getEncoder().sendMessage("[DEBUG]: Your roll " +
                    "[" + (Math.round(attackCalc * 1000.0) / 1000.0) + "] : " +
                    "Victim's roll [" + (Math.round(defenceCalc * 1000.0) / 1000.0) + "] : Chance to hit [" + (100 * Math.round(hitSucceed * 1000.0) / 1000.0) + "%]");
        }
        return hitSucceed >= random.nextDouble();
    }

    /**
     * Calculates the maximum hit that can be dealt using melee for
     * {@code character}.
     *
     * @param character
     *         the character to calculate the max hit for.
     * @param victim
     *         the victim being attacked.
     * @return the maximum hit this character can deal.
     */
    private static int calculateMaxMeleeHit(CharacterNode character, CharacterNode victim) {
        int maxHit = 0;

        if (character.getType() == NodeType.NPC) {
            Npc npc = (Npc) character;
            maxHit = npc.getDefinition().getMaxHit();
            if (npc.getWeakenedBy() == CombatWeaken.STRENGTH_LOW || npc.getWeakenedBy() == CombatWeaken.STRENGTH_HIGH)
                maxHit -= (int) ((npc.getWeakenedBy().getRate()) * (maxHit));
            return maxHit;
        }

        Player player = (Player) character;
        double specialMultiplier = 1;
        double prayerMultiplier = 1;
        // TODO: void melee = 1.2, slayer helm = 1.15, salve amulet = 1.15,
        // salve amulet(e) = 1.2
        double otherBonusMultiplier = 1;
        int strengthLevel = player.getSkills()[Skills.STRENGTH].getLevel();
        int attackLevel = player.getSkills()[Skills.ATTACK].getLevel();
        int combatStyleBonus = 0;

        if (CombatPrayer.isActivated(player, CombatPrayer.BURST_OF_STRENGTH)) {
            prayerMultiplier = 1.05;
        } else if (CombatPrayer.isActivated(player, CombatPrayer.SUPERHUMAN_STRENGTH)) {
            prayerMultiplier = 1.1;
        } else if (CombatPrayer.isActivated(player, CombatPrayer.ULTIMATE_STRENGTH)) {
            prayerMultiplier = 1.15;
        }
        // else if
        // (CombatPrayer.isPrayerActivated(player,CombatPrayer.CHIVALRY)) {
        // prayerMultiplier = 1.18;
        // } else if
        // (CombatPrayer.isPrayerActivated(player,CombatPrayer.PIETY)) {
        // prayerMultiplier = 1.23;
        // }
        switch (player.getFightType().getStyle()) {
            case AGGRESSIVE:
                combatStyleBonus = 3;
                break;
            case CONTROLLED:
                combatStyleBonus = 1;
                break;
            default:
                combatStyleBonus = 0;
                break;
        }

        // if (CombatFactory.fullVoid(player)) {
        // otherBonusMultiplier = 1.1;
        // }

        if (strengthLevel <= 10 || attackLevel <= 10) {
            otherBonusMultiplier = 1.8;
        }

        int effectiveStrengthDamage = (int) ((strengthLevel *
                prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
        double baseDamage = 1.3 + (effectiveStrengthDamage / 10) + (player.getBonus()[Combat.BONUS_STRENGTH] / 80) + ((effectiveStrengthDamage * player.getBonus()[Combat.BONUS_STRENGTH]) / 640);

        if (player.isSpecialActivated()) {
            specialMultiplier = player.getCombatSpecial().getStrength();
        }

        maxHit = (int) (baseDamage * specialMultiplier);

        if (Combat.isFullDharoks(player)) {
            maxHit += (player.getSkills()[Skills.HITPOINTS].getRealLevel() - player.getSkills()[Skills.HITPOINTS].getLevel()) * 0.35;
        }
        if (Settings.DEBUG)
            player.getEncoder().sendMessage("[DEBUG]: Maximum hit this turn " +
                    "is [" + maxHit + "].");
        return maxHit;

    }

    /**
     * Calculates the maximum hit that can be dealt using ranged for
     * {@code character}.
     *
     * @param character
     *         the character to calculate the max hit for.
     * @param victim
     *         the victim being attacked.
     * @return the maximum hit this character can deal.
     */
    private static int calculateMaxRangedHit(CharacterNode character, CharacterNode victim) {
        int maxHit = 0;
        if (character.getType() == NodeType.NPC) {
            Npc npc = (Npc) character;
            maxHit = npc.getDefinition().getMaxHit();
            if (npc.getWeakenedBy() == CombatWeaken.STRENGTH_LOW || npc.getWeakenedBy() == CombatWeaken.STRENGTH_HIGH)
                maxHit -= (int) ((npc.getWeakenedBy().getRate()) * (maxHit));
            return maxHit;
        }

        Player player = (Player) character;

        double specialMultiplier = 1;
        double prayerMultiplier = 1;
        double otherBonusMultiplier = 1;
        int rangedStrength = player.getRangedAmmo().getStrength();
        int rangeLevel = player.getSkills()[Skills.RANGED].getLevel();
        int combatStyleBonus = 0;

        switch (player.getFightType().getStyle()) {
            case ACCURATE:
                combatStyleBonus = 3;
                break;
            default:
                break;
        }

        // if (fullVoidRange(character)) {
        // otherBonusMultiplier = 1.1;
        // }

        int effectiveRangeDamage = (int) ((rangeLevel * prayerMultiplier *
                otherBonusMultiplier) + combatStyleBonus);
        double baseDamage = 1.3 + (effectiveRangeDamage / 10) +
                (rangedStrength / 80) + ((effectiveRangeDamage * rangedStrength) / 640);

        if (player.isSpecialActivated()) {
            specialMultiplier = player.getCombatSpecial().getStrength();
        }

        maxHit = (int) (baseDamage * specialMultiplier);

        if (Settings.DEBUG)
            player.getEncoder().sendMessage("[DEBUG]: Maximum hit this turn " +
                    "is [" + maxHit + "].");
        return maxHit;
    }

    /**
     * Determines if the character within {@code builder} is close enough to
     * it's victim to attack.
     *
     * @param builder
     *         the builder that will be checked.
     * @return {@code true} if the character is close enough, {@code false}
     * otherwise.
     */
    public static boolean checkAttackDistance(CombatBuilder builder) {
        Position attacker = builder.getCharacter().getPosition();
        Position victim = builder.getVictim().getPosition();
        int distance = builder.getStrategy().attackDistance(builder.getCharacter());
        MovementQueue movement = builder.getCharacter().getMovementQueue();
        MovementQueue otherMovement = builder.getVictim().getMovementQueue();

        if (!movement.isMovementDone() && !otherMovement.isMovementDone() &&
                !movement.isLockMovement() && !builder.getCharacter().isFrozen()) {
            distance += 1;

            // XXX: Might have to change this back to 1 or even remove it, not
            // sure what it's like on actual runescape. Are you allowed to
            // attack when the character is trying to run away from you?
            if (movement.isRunning()) {
                distance += 2;
            }
        }
        return attacker.withinDistance(victim, distance);
    }

    /**
     * A static factory method that constructs the default melee combat
     * strategy
     * implementation.
     *
     * @return the default melee combat strategy implementation.
     */
    public static CombatStrategy newDefaultMeleeStrategy() {
        return new DefaultMeleeCombatStrategy();
    }

    /**
     * A static factory method that constructs the default magic combat
     * strategy
     * implementation.
     *
     * @return the default magic combat strategy implementation.
     */
    public static CombatStrategy newDefaultMagicStrategy() {
        return new DefaultMagicCombatStrategy();
    }

    /**
     * A static factory method that constructs the default ranged combat
     * strategy implementation.
     *
     * @return the default ranged combat strategy implementation.
     */
    public static CombatStrategy newDefaultRangedStrategy() {
        return new DefaultRangedCombatStrategy();
    }
}
