package com.asteria.net.message.impl;

import java.util.Optional;

import com.asteria.game.World;
import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.combat.effect.CombatEffectType;
import com.asteria.game.character.combat.magic.CombatSpell;
import com.asteria.game.character.combat.magic.CombatSpells;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.Minigame;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.location.Location;
import com.asteria.net.ByteOrder;
import com.asteria.net.ValueType;
import com.asteria.net.message.InputMessageListener;
import com.asteria.net.message.MessageBuilder;

/**
 * The message sent from the client when a player attacks another player.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class AttackPlayerMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        switch (opcode) {
        case 249:
            attackMagic(player, payload);
            break;
        case 73:
            attackOther(player, payload);
            break;
        }
    }

    /**
     * Attempts to attack a player with a magic spell.
     *
     * @param player
     *            the player to attempt to attack.
     * @param payload
     *            the payloadfer for reading the sent data.
     */
    private void attackMagic(Player player, MessageBuilder payload) {
        int index = payload.getShort(true, ValueType.A);
        int spellId = payload.getShort(true, ByteOrder.LITTLE);
        Player victim = World.getPlayers().get(index);
        CombatSpell spell = CombatSpells.getSpell(spellId).get().getSpell();

        if (index < 0 || index > World.getPlayers().capacity() || spellId < 0 || !checkAttack(player, victim))
            return;
        player.setCastSpell(spell);
        player.getCombatBuilder().attack(victim);
    }

    /**
     * Attempts to attack a player with any other form of combat such as melee
     * or ranged.
     *
     * @param player
     *            the player to attempt to attack.
     * @param payload
     *            the payloadfer for reading the sent data.
     */
    private void attackOther(Player player, MessageBuilder payload) {
        int index = payload.getShort(true, ByteOrder.LITTLE);
        Player victim = World.getPlayers().get(index);

        if (index < 0 || index > World.getPlayers().capacity() || !checkAttack(player, victim))
            return;
        player.getCombatBuilder().attack(victim);
    }

    /**
     * Determines if an attack can be made by the {@code attacker} on
     * {@code victim}.
     *
     * @param attacker
     *            the player that is trying to attack.
     * @param victim
     *            the player that is being targeted.
     * @return {@code true} if an attack can be made, {@code false} otherwise.
     */
    private boolean checkAttack(Player attacker, Player victim) {
        if (victim == null || victim.equals(attacker))
            return false;
        if (!Location.inMultiCombat(attacker) && attacker.getCombatBuilder().isBeingAttacked() && attacker.getCombatBuilder()
            .getLastAttacker() != victim) {
            attacker.getMessages().sendMessage("You are already under attack!");
            return false;
        }
        Optional<Minigame> optional = MinigameHandler.search(attacker);
        if (!optional.isPresent()) {
            if (!Location.inWilderness(attacker) || !Location.inWilderness(victim)) {
                attacker.getMessages().sendMessage(
                    "Both you and " + victim.getFormatUsername() + " need to be in the wilderness" + " to fight!");
                return false;
            }
            int combatDifference = Combat.combatLevelDifference(attacker.determineCombatLevel(), victim.determineCombatLevel());
            if (combatDifference > attacker.getWildernessLevel() || combatDifference > victim.getWildernessLevel()) {
                attacker.getMessages().sendMessage("Your combat level " + "difference is too great to attack that player here.");
                return false;
            }
            if (!attacker.getCombatBuilder().isBeingAttacked() || attacker.getCombatBuilder().isBeingAttacked() && attacker
                .getCombatBuilder().getLastAttacker() != victim && Location.inMultiCombat(attacker)) {
                Combat.effect(attacker, CombatEffectType.SKULL);
            }
        } else {
            if (!optional.get().canHit(attacker, victim))
                return false;
        }
        return true;
    }
}
