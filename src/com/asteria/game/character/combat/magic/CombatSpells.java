package com.asteria.game.character.combat.magic;

import java.util.Arrays;
import java.util.Optional;

import com.asteria.game.NodeType;
import com.asteria.game.character.Animation;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.Graphic;
import com.asteria.game.character.PoisonType;
import com.asteria.game.character.Projectile;
import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.combat.effect.CombatPoisonEffect;
import com.asteria.game.character.combat.effect.CombatTeleblockEffect;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;

/**
 * The enumerated type whose elements represent the combat spells that can be
 * cast.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum CombatSpells {
    WIND_STRIKE(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 91, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(92));
        }

        @Override
        public int maximumHit() {
            return 2;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(90, 6553600));
        }

        @Override
        public double baseExperience() {
            return 5;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556), new Item(558) });
        }

        @Override
        public int levelRequired() {
            return 1;
        }

        @Override
        public int spellId() {
            return 1152;
        }
    }),
    CONFUSE(new CombatEffectSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(716));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 103, 44, 3, 43, 31, 0));
        }

        @Override
        public void effect(CharacterNode cast, CharacterNode castOn) {
            if (!castOn.weaken(CombatWeaken.ATTACK_LOW) && cast.getType() == NodeType.PLAYER) {
                Player player = (Player) cast;
                String s = castOn.getType().name().toLowerCase();
                player.getEncoder().sendMessage("The spell has no effect " + "because the " + s + " has already been weakened.");
            }
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(104));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(102, 6553600));
        }

        @Override
        public double baseExperience() {
            return 13;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(555, 3), new Item(557, 2), new Item(559) });
        }

        @Override
        public int levelRequired() {
            return 3;
        }

        @Override
        public int spellId() {
            return 1153;
        }
    }),
    WATER_STRIKE(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 94, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(95));
        }

        @Override
        public int maximumHit() {
            return 4;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(93, 6553600));
        }

        @Override
        public double baseExperience() {
            return 7;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(555), new Item(556), new Item(558) });
        }

        @Override
        public int levelRequired() {
            return 5;
        }

        @Override
        public int spellId() {
            return 1154;
        }
    }),
    EARTH_STRIKE(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 97, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(98));
        }

        @Override
        public int maximumHit() {
            return 6;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(96, 6553600));
        }

        @Override
        public double baseExperience() {
            return 9;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 1), new Item(558, 1), new Item(557, 2) });
        }

        @Override
        public int levelRequired() {
            return 9;
        }

        @Override
        public int spellId() {
            return 1156;
        }
    }),
    WEAKEN(new CombatEffectSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(716));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 106, 44, 3, 43, 31, 0));
        }

        @Override
        public void effect(CharacterNode cast, CharacterNode castOn) {
            if (!castOn.weaken(CombatWeaken.STRENGTH_LOW) && cast.getType() == NodeType.PLAYER) {
                Player player = (Player) cast;
                String s = castOn.getType().name().toLowerCase();
                player.getEncoder().sendMessage("The spell has no effect because the " + s + " has already been weakened.");
            }
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(107));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(105, 6553600));
        }

        @Override
        public double baseExperience() {
            return 21;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(555, 3), new Item(557, 2), new Item(559, 1) });
        }

        @Override
        public int levelRequired() {
            return 11;
        }

        @Override
        public int spellId() {
            return 1157;
        }
    }),
    FIRE_STRIKE(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 100, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(101));
        }

        @Override
        public int maximumHit() {
            return 8;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(99, 6553600));
        }

        @Override
        public double baseExperience() {
            return 11;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 1), new Item(558, 1), new Item(554, 3) });
        }

        @Override
        public int levelRequired() {
            return 13;
        }

        @Override
        public int spellId() {
            return 1158;
        }
    }),
    WIND_BOLT(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 118, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(119));
        }

        @Override
        public int maximumHit() {
            return 9;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(117, 6553600));
        }

        @Override
        public double baseExperience() {
            return 13;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 2), new Item(562, 1) });
        }

        @Override
        public int levelRequired() {
            return 17;
        }

        @Override
        public int spellId() {
            return 1160;
        }
    }),
    CURSE(new CombatEffectSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(710));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 109, 44, 3, 43, 31, 0));
        }

        @Override
        public void effect(CharacterNode cast, CharacterNode castOn) {
            if (!castOn.weaken(CombatWeaken.DEFENCE_LOW) && cast.getType() == NodeType.PLAYER) {
                Player player = (Player) cast;
                String s = castOn.getType().name().toLowerCase();
                player.getEncoder().sendMessage("The spell has no effect " + "because the " + s + " has already been weakened.");
            }
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(110));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(108, 6553600));
        }

        @Override
        public double baseExperience() {
            return 29;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(555, 2), new Item(557, 3), new Item(559, 1) });
        }

        @Override
        public int levelRequired() {
            return 19;
        }

        @Override
        public int spellId() {
            return 1161;
        }
    }),
    BIND(new CombatEffectSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(710));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 178, 44, 3, 43, 31, 0));
        }

        @Override
        public void effect(CharacterNode cast, CharacterNode castOn) {
            if (castOn.getMovementQueue().isLockMovement()) {
                if (cast.getType() == NodeType.PLAYER)
                    ((Player) cast).getEncoder().sendMessage("The spell has " + "no effect because they are already frozen.");
                return;
            }
            if (castOn.getType() == NodeType.PLAYER) {
                Player player = (Player) castOn;
                player.getEncoder().sendMessage("You have been frozen by " + "magic!");
            }
            castOn.freeze(5);
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(181, 6553600));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(177, 6553600));
        }

        @Override
        public double baseExperience() {
            return 30;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(555, 3), new Item(557, 3), new Item(561, 2) });
        }

        @Override
        public int levelRequired() {
            return 20;
        }

        @Override
        public int spellId() {
            return 1572;
        }
    }),
    WATER_BOLT(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 121, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(122));
        }

        @Override
        public int maximumHit() {
            return 10;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(120, 6553600));
        }

        @Override
        public double baseExperience() {
            return 16;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 2), new Item(562, 1), new Item(555, 2) });
        }

        @Override
        public int levelRequired() {
            return 23;
        }

        @Override
        public int spellId() {
            return 1163;
        }
    }),
    EARTH_BOLT(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 124, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(125));
        }

        @Override
        public int maximumHit() {
            return 11;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(123, 6553600));
        }

        @Override
        public double baseExperience() {
            return 19;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 2), new Item(562, 1), new Item(557, 3) });
        }

        @Override
        public int levelRequired() {
            return 29;
        }

        @Override
        public int spellId() {
            return 1166;
        }
    }),
    FIRE_BOLT(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 127, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(128));
        }

        @Override
        public int maximumHit() {
            return 12;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(126, 6553600));
        }

        @Override
        public double baseExperience() {
            return 22;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 3), new Item(562, 1), new Item(554, 4) });
        }

        @Override
        public int levelRequired() {
            return 35;
        }

        @Override
        public int spellId() {
            return 1169;
        }
    }),
    CRUMBLE_UNDEAD(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(724));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 146, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(147));
        }

        @Override
        public int maximumHit() {
            return 15;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(145, 6553600));
        }

        @Override
        public double baseExperience() {
            return 24;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 2), new Item(562, 1), new Item(557, 2) });
        }

        @Override
        public int levelRequired() {
            return 39;
        }

        @Override
        public int spellId() {
            return 1171;
        }
    }),
    WIND_BLAST(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 133, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(134));
        }

        @Override
        public int maximumHit() {
            return 13;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(132, 6553600));
        }

        @Override
        public double baseExperience() {
            return 25;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 3), new Item(560, 1) });
        }

        @Override
        public int levelRequired() {
            return 41;
        }

        @Override
        public int spellId() {
            return 1172;
        }
    }),
    WATER_BLAST(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 136, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(137));
        }

        @Override
        public int maximumHit() {
            return 14;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(135, 6553600));
        }

        @Override
        public double baseExperience() {
            return 28;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(555, 3), new Item(556, 3), new Item(560, 1) });
        }

        @Override
        public int levelRequired() {
            return 47;
        }

        @Override
        public int spellId() {
            return 1175;
        }
    }),
    IBAN_BLAST(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(708));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 88, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(89));
        }

        @Override
        public int maximumHit() {
            return 25;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(87, 6553600));
        }

        @Override
        public double baseExperience() {
            return 30;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.of(new Item[] { new Item(1409) });
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(560, 1), new Item(554, 5) });
        }

        @Override
        public int levelRequired() {
            return 50;
        }

        @Override
        public int spellId() {
            return 1539;
        }
    }),
    SNARE(new CombatEffectSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(710));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 178, 44, 3, 43, 31, 0));
        }

        @Override
        public void effect(CharacterNode cast, CharacterNode castOn) {
            if (castOn.getMovementQueue().isLockMovement()) {
                if (cast.getType() == NodeType.PLAYER)
                    ((Player) cast).getEncoder().sendMessage("The spell has " + "no effect because they are already frozen.");
                return;
            }
            if (castOn.getType() == NodeType.PLAYER) {
                Player player = (Player) castOn;
                player.getEncoder().sendMessage("You have been frozen by " + "magic!");
            }
            castOn.freeze(10);
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(180, 6553600));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(177, 6553600));
        }

        @Override
        public double baseExperience() {
            return 60;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(555, 3), new Item(557, 4), new Item(561, 3) });
        }

        @Override
        public int levelRequired() {
            return 50;
        }

        @Override
        public int spellId() {
            return 1582;
        }
    }),
    MAGIC_DART(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1576));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 328, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(329));
        }

        @Override
        public int maximumHit() {
            return 19;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(327, 6553600));
        }

        @Override
        public double baseExperience() {
            return 30;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.of(new Item[] { new Item(4170) });
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(558, 4), new Item(560, 1) });
        }

        @Override
        public int levelRequired() {
            return 50;
        }

        @Override
        public int spellId() {
            return 12037;
        }
    }),
    EARTH_BLAST(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 139, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(140));
        }

        @Override
        public int maximumHit() {
            return 15;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(138, 6553600));
        }

        @Override
        public double baseExperience() {
            return 31;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 3), new Item(560, 1), new Item(557, 4) });
        }

        @Override
        public int levelRequired() {
            return 53;
        }

        @Override
        public int spellId() {
            return 1177;
        }
    }),
    FIRE_BLAST(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 130, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(131));
        }

        @Override
        public int maximumHit() {
            return 16;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(129, 6553600));
        }

        @Override
        public double baseExperience() {
            return 34;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 4), new Item(560, 1), new Item(554, 5) });
        }

        @Override
        public int levelRequired() {
            return 59;
        }

        @Override
        public int spellId() {
            return 1181;
        }
    }),
    SARADOMIN_STRIKE(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(811));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(76));
        }

        @Override
        public int maximumHit() {
            return 20;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 35;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.of(new Item[] { new Item(2415) });
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 4), new Item(565, 2), new Item(554, 2) });
        }

        @Override
        public int levelRequired() {
            return 60;
        }

        @Override
        public int spellId() {
            return 1190;
        }
    }),
    CLAWS_OF_GUTHIX(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(811));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(77));
        }

        @Override
        public int maximumHit() {
            return 20;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 35;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.of(new Item[] { new Item(2416) });
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 4), new Item(565, 2), new Item(554, 2) });
        }

        @Override
        public int levelRequired() {
            return 60;
        }

        @Override
        public int spellId() {
            return 1191;
        }
    }),
    FLAMES_OF_ZAMORAK(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(811));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(78));
        }

        @Override
        public int maximumHit() {
            return 20;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 35;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.of(new Item[] { new Item(2417) });
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 4), new Item(565, 2), new Item(554, 2) });
        }

        @Override
        public int levelRequired() {
            return 60;
        }

        @Override
        public int spellId() {
            return 1192;
        }
    }),
    WIND_WAVE(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 159, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(160));
        }

        @Override
        public int maximumHit() {
            return 17;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(158, 6553600));
        }

        @Override
        public double baseExperience() {
            return 36;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 5), new Item(565, 1) });
        }

        @Override
        public int levelRequired() {
            return 62;
        }

        @Override
        public int spellId() {
            return 1183;
        }
    }),
    WATER_WAVE(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 162, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(163));
        }

        @Override
        public int maximumHit() {
            return 18;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(161, 6553600));
        }

        @Override
        public double baseExperience() {
            return 37;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 5), new Item(565, 1), new Item(555, 7) });
        }

        @Override
        public int levelRequired() {
            return 65;
        }

        @Override
        public int spellId() {
            return 1185;
        }
    }),
    VULNERABILITY(new CombatEffectSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(729));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 168, 44, 3, 43, 31, 0));
        }

        @Override
        public void effect(CharacterNode cast, CharacterNode castOn) {
            if (!castOn.weaken(CombatWeaken.DEFENCE_HIGH) && cast.getType() == NodeType.PLAYER) {
                Player player = (Player) cast;
                String s = castOn.getType().name().toLowerCase();
                player.getEncoder().sendMessage("The spell has no effect " + "because the " + s + " has already been weakened.");
            }
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(169));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(167, 6553600));
        }

        @Override
        public double baseExperience() {
            return 76;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(557, 5), new Item(555, 5), new Item(566, 1) });
        }

        @Override
        public int levelRequired() {
            return 66;
        }

        @Override
        public int spellId() {
            return 1542;
        }
    }),
    EARTH_WAVE(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 165, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(166));
        }

        @Override
        public int maximumHit() {
            return 19;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(164, 6553600));
        }

        @Override
        public double baseExperience() {
            return 40;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 5), new Item(565, 1), new Item(557, 7) });
        }

        @Override
        public int levelRequired() {
            return 70;
        }

        @Override
        public int spellId() {
            return 1188;
        }
    }),
    ENFEEBLE(new CombatEffectSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(729));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 171, 44, 3, 43, 31, 0));
        }

        @Override
        public void effect(CharacterNode cast, CharacterNode castOn) {
            if (!castOn.weaken(CombatWeaken.STRENGTH_HIGH) && cast.getType() == NodeType.PLAYER) {
                Player player = (Player) cast;
                String s = castOn.getType().name().toLowerCase();
                player.getEncoder().sendMessage("The spell has no effect " + "because the " + s + " has already been weakened.");
            }
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(172));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(170, 6553600));
        }

        @Override
        public double baseExperience() {
            return 83;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(557, 8), new Item(555, 8), new Item(566, 1) });
        }

        @Override
        public int levelRequired() {
            return 73;
        }

        @Override
        public int spellId() {
            return 1543;
        }
    }),
    FIRE_WAVE(new CombatNormalSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(711));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 156, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(157));
        }

        @Override
        public int maximumHit() {
            return 20;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(155, 6553600));
        }

        @Override
        public double baseExperience() {
            return 42;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 5), new Item(565, 1), new Item(554, 7) });
        }

        @Override
        public int levelRequired() {
            return 75;
        }

        @Override
        public int spellId() {
            return 1189;
        }
    }),
    ENTANGLE(new CombatEffectSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(710));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 178, 44, 3, 43, 31, 0));
        }

        @Override
        public void effect(CharacterNode cast, CharacterNode castOn) {
            if (castOn.getMovementQueue().isLockMovement()) {
                if (cast.getType() == NodeType.PLAYER)
                    ((Player) cast).getEncoder().sendMessage("The spell has " + "no effect because they are already frozen.");
                return;
            }
            if (castOn.getType() == NodeType.PLAYER) {
                Player player = (Player) castOn;
                player.getEncoder().sendMessage("You have been frozen by " + "magic!");
            }
            castOn.freeze(15);
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(179, 6553600));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(177, 6553600));
        }

        @Override
        public double baseExperience() {
            return 91;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(555, 5), new Item(557, 5), new Item(561, 4) });
        }

        @Override
        public int levelRequired() {
            return 79;
        }

        @Override
        public int spellId() {
            return 1592;
        }
    }),
    STUN(new CombatEffectSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(729));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 174, 44, 3, 43, 31, 0));
        }

        @Override
        public void effect(CharacterNode cast, CharacterNode castOn) {
            if (!castOn.weaken(CombatWeaken.ATTACK_HIGH) && cast.getType() == NodeType.PLAYER) {
                Player player = (Player) cast;
                String s = castOn.getType().name().toLowerCase();
                player.getEncoder().sendMessage("The spell has no effect " + "because the " + s + " has already been weakened.");
            }
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(107));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(173, 6553600));
        }

        @Override
        public double baseExperience() {
            return 90;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(557, 12), new Item(555, 12), new Item(556, 1) });
        }

        @Override
        public int levelRequired() {
            return 80;
        }

        @Override
        public int spellId() {
            return 1562;
        }
    }),
    TELEBLOCK(new CombatEffectSpell() {
        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1819));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 344, 44, 3, 43, 31, 0));
        }

        @Override
        public void effect(CharacterNode cast, CharacterNode castOn) {
            if (castOn.getType() == NodeType.PLAYER)
                Combat.effect(new CombatTeleblockEffect((Player) castOn));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(345));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 65;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(563, 1), new Item(562, 1), new Item(560, 1) });
        }

        @Override
        public int levelRequired() {
            return 85;
        }

        @Override
        public int spellId() {
            return 12445;
        }
    }),
    SMOKE_RUSH(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            Combat.effect(new CombatPoisonEffect(castOn, PoisonType.DEFAULT_RANGED));
        }

        @Override
        public int radius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 384, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(385));
        }

        @Override
        public int maximumHit() {
            return 13;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 30;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 1), new Item(554, 1), new Item(562, 2), new Item(560, 2) });
        }

        @Override
        public int levelRequired() {
            return 50;
        }

        @Override
        public int spellId() {
            return 12939;
        }
    }),
    SHADOW_RUSH(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            castOn.weaken(CombatWeaken.ATTACK_LOW);
        }

        @Override
        public int radius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 378, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(379));
        }

        @Override
        public int maximumHit() {
            return 14;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 31;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 1), new Item(566, 1), new Item(562, 2), new Item(560, 2) });
        }

        @Override
        public int levelRequired() {
            return 52;
        }

        @Override
        public int spellId() {
            return 12987;
        }
    }),
    BLOOD_RUSH(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            cast.healCharacter((int) (damage * 0.25));
        }

        @Override
        public int radius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 372, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(373));
        }

        @Override
        public int maximumHit() {
            return 15;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 33;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(565, 1), new Item(562, 2), new Item(560, 2) });
        }

        @Override
        public int levelRequired() {
            return 56;
        }

        @Override
        public int spellId() {
            return 12901;
        }
    }),
    ICE_RUSH(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            castOn.freeze(7);
        }

        @Override
        public int radius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 360, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(361));
        }

        @Override
        public int maximumHit() {
            return 18;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 34;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(555, 2), new Item(562, 2), new Item(560, 2) });
        }

        @Override
        public int levelRequired() {
            return 58;
        }

        @Override
        public int spellId() {
            return 12861;
        }
    }),
    SMOKE_BURST(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            Combat.effect(new CombatPoisonEffect(castOn, PoisonType.DEFAULT_RANGED));
        }

        @Override
        public int radius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(389));
        }

        @Override
        public int maximumHit() {
            return 13;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 36;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 2), new Item(554, 2), new Item(562, 4), new Item(560, 2) });
        }

        @Override
        public int levelRequired() {
            return 62;
        }

        @Override
        public int spellId() {
            return 12963;
        }
    }),
    SHADOW_BURST(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            castOn.weaken(CombatWeaken.ATTACK_LOW);
        }

        @Override
        public int radius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(382));
        }

        @Override
        public int maximumHit() {
            return 18;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 37;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 1), new Item(566, 2), new Item(562, 4), new Item(560, 2) });
        }

        @Override
        public int levelRequired() {
            return 64;
        }

        @Override
        public int spellId() {
            return 13011;
        }
    }),
    BLOOD_BURST(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            cast.healCharacter((int) (damage * 0.25));
        }

        @Override
        public int radius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(376));
        }

        @Override
        public int maximumHit() {
            return 21;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 39;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(565, 2), new Item(562, 4), new Item(560, 2) });
        }

        @Override
        public int levelRequired() {
            return 68;
        }

        @Override
        public int spellId() {
            return 12919;
        }
    }),
    ICE_BURST(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            castOn.freeze(10);
        }

        @Override
        public int radius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(363));
        }

        @Override
        public int maximumHit() {
            return 22;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 40;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(555, 4), new Item(562, 4), new Item(560, 2) });
        }

        @Override
        public int levelRequired() {
            return 70;
        }

        @Override
        public int spellId() {
            return 12881;
        }
    }),
    SMOKE_BLITZ(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            Combat.effect(new CombatPoisonEffect(castOn, PoisonType.DEFAULT_RANGED));
        }

        @Override
        public int radius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 386, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(387));
        }

        @Override
        public int maximumHit() {
            return 23;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 42;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 2), new Item(554, 2), new Item(565, 2), new Item(560, 2) });
        }

        @Override
        public int levelRequired() {
            return 74;
        }

        @Override
        public int spellId() {
            return 12951;
        }
    }),
    SHADOW_BLITZ(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            castOn.weaken(CombatWeaken.ATTACK_HIGH);
        }

        @Override
        public int radius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 380, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(381));
        }

        @Override
        public int maximumHit() {
            return 24;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 43;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 2), new Item(566, 2), new Item(565, 2), new Item(560, 2) });
        }

        @Override
        public int levelRequired() {
            return 76;
        }

        @Override
        public int spellId() {
            return 12999;
        }
    }),
    BLOOD_BLITZ(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            cast.healCharacter((int) (damage * 0.25));
        }

        @Override
        public int radius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 374, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(375));
        }

        @Override
        public int maximumHit() {
            return 25;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 45;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(565, 4), new Item(560, 2) });
        }

        @Override
        public int levelRequired() {
            return 80;
        }

        @Override
        public int spellId() {
            return 12911;
        }
    }),
    ICE_BLITZ(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            castOn.freeze(10);
        }

        @Override
        public int radius() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1978));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(367));
        }

        @Override
        public int maximumHit() {
            return 26;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(366, 6553600));
        }

        @Override
        public double baseExperience() {
            return 46;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(555, 3), new Item(565, 2), new Item(560, 2) });
        }

        @Override
        public int levelRequired() {
            return 82;
        }

        @Override
        public int spellId() {
            return 12871;
        }
    }),
    SMOKE_BARRAGE(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            Combat.effect(new CombatPoisonEffect(castOn, PoisonType.SUPER_RANGED));
        }

        @Override
        public int radius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(391));
        }

        @Override
        public int maximumHit() {
            return 27;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 48;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 4), new Item(554, 4), new Item(565, 2), new Item(560, 4) });
        }

        @Override
        public int levelRequired() {
            return 86;
        }

        @Override
        public int spellId() {
            return 12975;
        }
    }),
    SHADOW_BARRAGE(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            castOn.weaken(CombatWeaken.ATTACK_HIGH);
        }

        @Override
        public int radius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(383));
        }

        @Override
        public int maximumHit() {
            return 28;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 49;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(556, 4), new Item(566, 3), new Item(565, 2), new Item(560, 4) });
        }

        @Override
        public int levelRequired() {
            return 88;
        }

        @Override
        public int spellId() {
            return 13023;
        }
    }),
    BLOOD_BARRAGE(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            cast.healCharacter((int) (damage * 0.25));
        }

        @Override
        public int radius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(377));
        }

        @Override
        public int maximumHit() {
            return 29;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 51;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(560, 4), new Item(566, 1), new Item(565, 4) });
        }

        @Override
        public int levelRequired() {
            return 92;
        }

        @Override
        public int spellId() {
            return 12929;
        }
    }),
    ICE_BARRAGE(new CombatAncientSpell() {
        @Override
        public void effect(CharacterNode cast, CharacterNode castOn, int damage) {
            if (damage < 1)
                return;
            castOn.freeze(15);
        }

        @Override
        public int radius() {
            return 1;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.of(new Animation(1979));
        }

        @Override
        public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(369));
        }

        @Override
        public int maximumHit() {
            return 30;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return 52;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.of(new Item[] { new Item(555, 6), new Item(565, 2), new Item(560, 4) });
        }

        @Override
        public int levelRequired() {
            return 94;
        }

        @Override
        public int spellId() {
            return 12891;
        }
    });

    /**
     * The spell attached to this element.
     */
    private final CombatSpell spell;

    /**
     * Creates a new {@link CombatSpells}.
     *
     * @param spell
     *            the spell attached to this element.
     */
    private CombatSpells(CombatSpell spell) {
        this.spell = spell;
    }

    /**
     * Gets the spell attached to this element.
     *
     * @return the spell.
     */
    public final CombatSpell getSpell() {
        return spell;
    }

    /**
     * Gets the spell with a {@link CombatSpell#spellId()} of {@code id}.
     *
     * @param id
     *            the identification of the combat spell.
     * @return the combat spell with that identification.
     */
    public static Optional<CombatSpells> getSpell(int id) {
        return Arrays.stream(CombatSpells.values()).filter(s -> s != null && s.getSpell().spellId() == id).findFirst();
    }
}
