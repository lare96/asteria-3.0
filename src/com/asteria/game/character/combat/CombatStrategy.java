package com.asteria.game.character.combat;

import com.asteria.game.character.CharacterNode;
import com.asteria.game.plugin.PluginContext;

/**
 * The blueprint of a combat session that determines how a character will act in
 * combat.
 *
 * @author lare96 <http://github.com/lare96>
 */
public interface CombatStrategy extends PluginContext {

    /**
     * Determines if {@code character} is able to make an attack on
     * {@code victim}.
     *
     * @param character
     *            the character to has if able.
     * @param victim
     *            the character being attacked.
     * @return {@code true} if an attack can be made, {@code false} otherwise.
     */
    public boolean canAttack(CharacterNode character, CharacterNode victim);

    /**
     * Executed when {@code character} has passed the initial {@code canAttack}
     * check and is about to attack {@code victim}.
     *
     * @param character
     *            the character that is attacking.
     * @param victim
     *            the character being attacked.
     * @return a container holding the data for the attack.
     */
    public CombatSessionData attack(CharacterNode character, CharacterNode victim);

    /**
     * Determines the delay for when {@code character} will attack.
     *
     * @param character
     *            the character waiting to attack.
     * @return the value that the attack timer should be reset to.
     */
    public int attackDelay(CharacterNode character);

    /**
     * Determines how close {@code character} must be to attack.
     *
     * @param character
     *            the character that is attacking.
     * @return the radius that the character must be in to attack.
     */
    public int attackDistance(CharacterNode character);

    /**
     * The NPCs that will be assigned this combat strategy.
     *
     * @return the array of assigned NPCs.
     */
    public int[] getNpcs();
}
