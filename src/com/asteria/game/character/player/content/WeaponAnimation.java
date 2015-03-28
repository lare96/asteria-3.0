package com.asteria.game.character.player.content;

import java.util.HashMap;
import java.util.Map;

import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;

/**
 * The container class that represents one equipment animation that is used
 * with
 * weapons.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class WeaponAnimation {

    /**
     * The hash collection of weapon animations.
     */
    public static final Map<Integer, WeaponAnimation> ANIMATIONS = new
            HashMap<>();

    /**
     * The standing animation for this weapon animation.
     */
    private final int standing;

    /**
     * The walking animation for this weapon animation.
     */
    private final int walking;

    /**
     * The running animation for this weapon animation.
     */
    private final int running;

    /**
     * Creates a new {@link WeaponAnimation}.
     *
     * @param standing
     *         the standing animation for this weapon animation.
     * @param walking
     *         the walking animation for this weapon animation.
     * @param running
     *         the running animation for this weapon animation.
     */
    public WeaponAnimation(int standing, int walking, int running) {
        this.standing = standing;
        this.walking = walking;
        this.running = running;
    }

    /**
     * A substitute for {@link Object#clone()} that creates another 'copy' of
     * this instance. The created copy is <i>safe</i> meaning it does not hold
     * <b>any</b> references to the original instance.
     *
     * @return a reference-free copy of this instance.
     */
    public WeaponAnimation copy() {
        return new WeaponAnimation(standing, walking, running);
    }

    /**
     * The method executed when weapon {@code item} is equipped that assigns a
     * weapon animation to {@code player}.
     *
     * @param player
     *         the player equipping the item.
     * @param item
     *         the item the player is equipping.
     */
    public static void execute(Player player, Item item) {
        if (item == null)
            return;
        WeaponAnimation animation = ANIMATIONS.get(item.getId());
        player.setWeaponAnimation(animation == null ? null : animation.copy());
    }

    /**
     * Gets the standing animation for this weapon animation.
     *
     * @return the standing animation.
     */
    public int getStanding() {
        return standing;
    }

    /**
     * Gets the walking animation for this weapon animation.
     *
     * @return the walking animation.
     */
    public int getWalking() {
        return walking;
    }

    /**
     * Gets the running animation for this weapon animation.
     *
     * @return the running animation.
     */
    public int getRunning() {
        return running;
    }
}