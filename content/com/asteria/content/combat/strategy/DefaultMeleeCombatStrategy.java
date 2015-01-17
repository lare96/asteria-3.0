package com.asteria.content.combat.strategy;

import com.asteria.game.NodeType;
import com.asteria.game.character.Animation;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.combat.CombatSessionData;
import com.asteria.game.character.combat.CombatStrategy;
import com.asteria.game.character.combat.CombatType;
import com.asteria.game.character.combat.weapon.FightType;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.content.WeaponInterface;
import com.asteria.game.item.Item;
import com.asteria.game.item.container.Equipment;

/**
 * The default combat strategy implementation for all characters that attack
 * with melee.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public class DefaultMeleeCombatStrategy implements CombatStrategy {

    @Override
    public boolean canAttack(CharacterNode character, CharacterNode victim) {
        return true;
    }

    @Override
    public CombatSessionData attack(CharacterNode character, CharacterNode victim) {
        startAnimation(character);
        return new CombatSessionData(character, victim, 1, CombatType.MELEE, true);
    }

    @Override
    public int attackDelay(CharacterNode character) {
        return character.getAttackSpeed();
    }

    @Override
    public int attackDistance(CharacterNode character) {
        if (character.getType() == NodeType.NPC)
            return 1;
        if (((Player) character).getWeapon() == WeaponInterface.HALBERD)
            return 2;
        return 1;
    }

    /**
     * Starts the melee animation for {@code character}.
     * 
     * @param character
     *            the character to start the animation for.
     */
    private void startAnimation(CharacterNode character) {
        if (character.getType() == NodeType.NPC) {
            Npc npc = (Npc) character;
            npc.animation(new Animation(npc.getDefinition().getAttackAnimation()));
        } else if (character.getType() == NodeType.PLAYER) {
            Player player = (Player) character;
            Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);

            if (!player.isSpecialActivated() && item != null) {
                if (item.getDefinition().getName().startsWith("Dragon dagger")) {
                    player.animation(new Animation(402));
                } else if (item.getDefinition().getName().startsWith("Dharoks")) {
                    if (player.getFightType() == FightType.BATTLEAXE_SMASH) {
                        player.animation(new Animation(2067));
                    } else {
                        player.animation(new Animation(2066));
                    }
                } else if (item.getDefinition().getName().equals("Granite maul")) {
                    player.animation(new Animation(1665));
                } else if (item.getDefinition().getName().equals("Tzhaar-ket-om")) {
                    player.animation(new Animation(2661));
                } else if (item.getDefinition().getName().endsWith("wand")) {
                    player.animation(new Animation(FightType.UNARMED_KICK.getAnimation()));
                } else if (item.getDefinition().getName().startsWith("Torags")) {
                    player.animation(new Animation(2068));
                } else if (item.getDefinition().getName().startsWith("Veracs")) {
                    player.animation(new Animation(2062));
                } else {
                    player.animation(new Animation(player.getFightType().getAnimation()));
                }
            } else {
                player.animation(new Animation(player.getFightType().getAnimation()));
            }
        }
    }
}
