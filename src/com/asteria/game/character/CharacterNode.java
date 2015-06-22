package com.asteria.game.character;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.asteria.game.Node;
import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.combat.CombatBuilder;
import com.asteria.game.character.combat.CombatStrategy;
import com.asteria.game.character.combat.CombatType;
import com.asteria.game.character.combat.effect.CombatEffectType;
import com.asteria.game.character.combat.magic.CombatSpell;
import com.asteria.game.character.combat.magic.CombatWeaken;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.location.Position;
import com.asteria.task.Task;
import com.asteria.utility.MutableNumber;
import com.asteria.utility.Stopwatch;
import com.google.common.base.Preconditions;

/**
 * The {@link Node} implementation representing a node that is mobile. This
 * includes {@link Player}s and {@link Npc}s.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CharacterNode extends Node {

    /**
     * The combat builder that will handle all combat operations for this
     * character.
     */
    private final CombatBuilder combatBuilder = new CombatBuilder(this);

    /**
     * The movement queue that will handle all movement processing for this
     * character.
     */
    private final MovementQueue movementQueue = new MovementQueue(this);

    /**
     * The movement queue listener that will allow for actions to be appended to
     * the end of the movement queue.
     */
    private final MovementQueueListener movementListener = new MovementQueueListener(this);

    /**
     * The update flags used for signifying that this character needs something
     * updated.
     */
    private final UpdateFlags flags = new UpdateFlags();

    /**
     * The collection of stopwatches used for various timing operations.
     */
    private final Stopwatch lastCombat = new Stopwatch(), freezeTimer = new Stopwatch();

    /**
     * The slot this character has been assigned to.
     */
    private int slot = -1;

    /**
     * The flag that determines if this character is visible or not.
     */
    private boolean visible = true;

    /**
     * The amount of poison damage this character has.
     */
    private final MutableNumber poisonDamage = new MutableNumber();

    /**
     * The type of poison that was previously applied.
     */
    private PoisonType poisonType;

    /**
     * The primary movement direction of this character.
     */
    private int primaryDirection = -1;

    /**
     * The secondary movement direction of this character.
     */
    private int secondaryDirection = -1;

    /**
     * The last movement direction made by this character.
     */
    private int lastDirection = 0;

    /**
     * The flag that determines if this character needs placement.
     */
    private boolean needsPlacement;

    /**
     * The flag that determines if this character's movement queue needs to be
     * reset.
     */
    private boolean resetMovementQueue;

    /**
     * The combat spell currently being casted by this character.
     */
    private CombatSpell currentlyCasting;

    /**
     * The current animation being performed by this character.
     */
    private Animation animation;

    /**
     * The current graphic being performed by this character.
     */
    private Graphic graphic;

    /**
     * The current text being forced by this character.
     */
    private String forcedText;

    /**
     * The current index being faced by this character.
     */
    private int faceIndex;

    /**
     * The current coordinates being face by this character.
     */
    private Position facePosition;

    /**
     * The current primary hit being dealt on this character.
     */
    private Hit primaryHit;

    /**
     * The current secondary hit being dealt on this character.
     */
    private Hit secondaryHit;

    /**
     * The current region this character is in.
     */
    private Position currentRegion;

    /**
     * The delay representing how long this character is frozen for.
     */
    private long freezeDelay;

    /**
     * The flag determining if this character should fight back when attacked.
     */
    private boolean autoRetaliate;

    /**
     * The flag determining if this character is following someone.
     */
    private boolean following;

    /**
     * The character this character is currently following.
     */
    private CharacterNode followCharacter;

    /**
     * The flag determining if this character is dead.
     */
    private boolean dead;

    /**
     * The last position this character was in.
     */
    private Position lastPosition;

    /**
     * Creates a new {@link CharacterNode}.
     *
     * @param position
     *            the position of this character in the world.
     * @param type
     *            the type of node that this character is.
     */
    public CharacterNode(Position position, NodeType type) {
        super(position, type);
        this.autoRetaliate = (type == NodeType.NPC);
        Preconditions.checkArgument(type == NodeType.PLAYER || type == NodeType.NPC);
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + super.getType().hashCode();
        result = prime * result + slot;
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof CharacterNode))
            return false;
        CharacterNode other = (CharacterNode) obj;
        if (super.getType() != other.getType())
            return false;
        if (slot != other.slot)
            return false;
        return true;
    }

    @Override
    public String toString() {
        if (super.getType() == NodeType.PLAYER)
            return World.getPlayers().get(slot).toString();
        else if (super.getType() == NodeType.NPC)
            return World.getNpcs().get(slot).toString();
        throw new IllegalStateException("Invalid character node type!");
    }

    /**
     * The code executed by the {@link CharacterLogicSequencer} during game
     * logic processing.
     *
     * @throws Exception
     *             if any errors occur while executing code.
     */
    public abstract void sequence() throws Exception;

    /**
     * Weakens this character using {@code effect}.
     *
     * @param effect
     *            the effect to use to weaken this character.
     * @return {@code true} if the character was weakened, {@code false}
     *         otherwise.
     */
    public abstract boolean weaken(CombatWeaken effect);

    /**
     * Gets the attack speed for this character.
     *
     * @return the attack speed.
     */
    public abstract int getAttackSpeed();

    /**
     * Gets this character's current health.
     *
     * @return this charater's health.
     */
    public abstract int getCurrentHealth();

    /**
     * Decrements this character's health based on {@code hit}.
     *
     * @param hit
     *            the hit to decrement this character's health by.
     * @return the modified hit after the health was decremented.
     */
    public abstract Hit decrementHealth(Hit hit);

    /**
     * Calculates and retrieves the combat strategy for this character.
     *
     * @return the combat strategy.
     */
    public abstract CombatStrategy determineStrategy();

    /**
     * Gets the base attack level for this character based on {@code type}, used
     * for combat calculations.
     *
     * @param type
     *            the combat type.
     * @return the base attack level.
     */
    public abstract int getBaseAttack(CombatType type);

    /**
     * Gets the base defence level for this character based on {@code type},
     * used for combat calculations.
     *
     * @param type
     *            the combat type.
     * @return the base defence level.
     */
    public abstract int getBaseDefence(CombatType type);

    /**
     * Executed on a successful hit, used primarily for poison effects.
     *
     * @param character
     *            the victim in this successful hit.
     * @param type
     *            the combat type currently being used.
     */
    public abstract void onSuccessfulHit(CharacterNode character, CombatType type);

    /**
     * Restores this character's health level by {@code amount}.
     *
     * @param amount
     *            the amount to restore this health level by.
     */
    public abstract void healCharacter(int amount);

    /**
     * Applies poison with an intensity of {@code type} to the character.
     * 
     * @param type
     *            the poison type to apply.
     */
    public void poison(PoisonType type) {
        poisonType = type;
        Combat.effect(this, CombatEffectType.POISON);
    }

    /**
     * Calculates and returns the size of this character.
     *
     * @return the size of this character.
     */
    public final int size() {
        if (super.getType() == NodeType.PLAYER)
            return 1;
        return ((Npc) this).getDefinition().getSize();
    }

    /**
     * Resets the prepares this character for the next update sequence.
     */
    public final void reset() {
        primaryDirection = -1;
        secondaryDirection = -1;
        flags.reset();
        resetMovementQueue = false;
        needsPlacement = false;
        animation = null;
    }

    /**
     * Executes {@code animation} for this character.
     *
     * @param animation
     *            the animation to execute, or {@code null} to reset the current
     *            animation.
     */
    public final void animation(Animation animation) {
        if (animation == null)
            animation = new Animation(65535, AnimationPriority.HIGH);
        if (this.animation == null || this.animation.getPriority().getValue() <= animation.getPriority().getValue()) {
            this.animation = animation.copy();
            flags.set(Flag.ANIMATION);
        }
    }

    /**
     * Executes {@code graphic} for this character.
     *
     * @param graphic
     *            the graphic to execute.
     */
    public final void graphic(Graphic graphic) {
        this.graphic = graphic.copy();
        flags.set(Flag.GRAPHICS);
    }

    /**
     * Executes {@code graphic} for this character at a higher height level.
     *
     * @param graphic
     *            the graphic to execute.
     */
    public final void highGraphic(Graphic graphic) {
        this.graphic = new Graphic(graphic.getId(), 6553600);
        flags.set(Flag.GRAPHICS);
    }

    /**
     * Executes {@code forcedText} for this character.
     *
     * @param forcedText
     *            the forced text to execute.
     */
    public final void forceChat(String forcedText) {
        this.forcedText = forcedText;
        flags.set(Flag.FORCED_CHAT);
    }

    /**
     * Prompts this character to face {@code character}.
     *
     * @param character
     *            the character to face, or {@code null} to reset the face.
     */
    public final void faceCharacter(CharacterNode character) {
        this.faceIndex = character == null ? 65535 : character.getType() == NodeType.PLAYER ? character.slot + 32768 : character.slot;
        flags.set(Flag.FACE_CHARACTER);
    }

    /**
     * Prompts this character to face {@code position}.
     *
     * @param position
     *            the position to face.
     */
    public final void facePosition(Position position) {
        facePosition = new Position(2 * position.getX() + 1, 2 * position.getY() + 1);
        flags.set(Flag.FACE_COORDINATE);
    }

    /**
     * Deals {@code hit} on this character as a primary hitmark.
     *
     * @param hit
     *            the hit to deal on this character.
     */
    private final void primaryDamage(Hit hit) {
        primaryHit = decrementHealth(Objects.requireNonNull(hit));
        flags.set(Flag.HIT);
    }

    /**
     * Deals {@code hit} on this character as a secondary hitmark.
     *
     * @param hit
     *            the hit to deal on this character.
     */
    private final void secondaryDamage(Hit hit) {
        secondaryHit = decrementHealth(Objects.requireNonNull(hit));
        flags.set(Flag.HIT_2);
    }

    /**
     * Deals a series of hits to this character.
     *
     * @param hits
     *            the hits to deal to this character.
     */
    public final void damage(Hit... hits) {
        Preconditions.checkArgument(hits.length >= 1 && hits.length <= 4);

        switch (hits.length) {
        case 1:
            sendDamage(hits[0]);
            break;
        case 2:
            sendDamage(hits[0], hits[1]);
            break;
        case 3:
            sendDamage(hits[0], hits[1], hits[2]);
            break;
        case 4:
            sendDamage(hits[0], hits[1], hits[2], hits[3]);
            break;
        }
    }

    /**
     * Deals {@code hit} to this character.
     *
     * @param hit
     *            the hit to deal to this character.
     */
    private final void sendDamage(Hit hit) {
        if (flags.get(Flag.HIT)) {
            secondaryDamage(hit);
            return;
        }
        primaryDamage(hit);
    }

    /**
     * Deals {@code hit} and {@code hit2} to this character.
     *
     * @param hit
     *            the first hit to deal to this character.
     * @param hit2
     *            the second hit to deal to this character.
     */
    private final void sendDamage(Hit hit, Hit hit2) {
        sendDamage(hit);
        secondaryDamage(hit2);
    }

    /**
     * Deals {@code hit}, {@code hit2}, and {@code hit3} to this character.
     *
     * @param hit
     *            the first hit to deal to this character.
     * @param hit2
     *            the second hit to deal to this character.
     * @param hit3
     *            the third hit to deal to this character.
     */
    private final void sendDamage(Hit hit, Hit hit2, Hit hit3) {
        sendDamage(hit, hit2);

        World.submit(new Task(1, false) {
            @Override
            public void execute() {
                this.cancel();
                if (!CharacterNode.super.isRegistered()) {
                    return;
                }
                sendDamage(hit3);
            }
        });
    }

    /**
     * Deals {@code hit}, {@code hit2}, {@code hit3}, and {@code hit4} to this
     * character.
     *
     * @param hit
     *            the first hit to deal to this character.
     * @param hit2
     *            the second hit to deal to this character.
     * @param hit3
     *            the third hit to deal to this character.
     * @param hit4
     *            the fourth hit to deal to this character.
     */
    private final void sendDamage(Hit hit, Hit hit2, Hit hit3, Hit hit4) {
        sendDamage(hit, hit2);

        World.submit(new Task(1, false) {
            @Override
            public void execute() {
                this.cancel();
                if (!CharacterNode.super.isRegistered()) {
                    return;
                }
                sendDamage(hit3, hit4);
            }
        });
    }

    /**
     * Prepares to cast the {@code spell} on {@code victim}.
     *
     * @param spell
     *            the spell to cast on the victim.
     * @param victim
     *            the victim that the spell will be cast on.
     */
    public final void prepareSpell(CombatSpell spell, CharacterNode victim) {
        currentlyCasting = spell;
        currentlyCasting.startCast(this, victim);
    }

    /**
     * Freezes this character for the desired time in {@code SECONDS}.
     *
     * @param time
     *            the time to freeze this character for.
     */
    public final void freeze(long time) {
        if (isFrozen())
            return;
        freezeDelay = time;
        freezeTimer.reset();
        movementQueue.reset();
    }

    /**
     * Unfreezes this character completely allowing them to reestablish
     * movement.
     */
    public final void unfreeze() {
        freezeDelay = 0;
        freezeTimer.stop();
    }

    /**
     * Determines if this character is poisoned.
     *
     * @return {@code true} if this character is poisoned, {@code false}
     *         otherwise.
     */
    public final boolean isPoisoned() {
        return poisonDamage.get() > 0;
    }

    /**
     * Determines if this character is frozen.
     *
     * @return {@code true} if this character is frozen, {@code false}
     *         otherwise.
     */
    public final boolean isFrozen() {
        return !freezeTimer.elapsed(freezeDelay, TimeUnit.SECONDS);
    }

    /**
     * Gets the slot this character has been assigned to.
     *
     * @return the slot of this character.
     */
    public final int getSlot() {
        return slot;
    }

    /**
     * Sets the value for {@link CharacterNode#slot}.
     *
     * @param slot
     *            the new value to set.
     */
    public final void setSlot(int slot) {
        this.slot = slot;
    }

    /**
     * Gets the amount of poison damage this character has.
     *
     * @return the amount of poison damage.
     */
    public final MutableNumber getPoisonDamage() {
        return poisonDamage;
    }

    /**
     * Gets the primary direction this character is facing.
     *
     * @return the primary direction.
     */
    public final int getPrimaryDirection() {
        return primaryDirection;
    }

    /**
     * Sets the value for {@link CharacterNode#primaryDirection}.
     *
     * @param primaryDirection
     *            the new value to set.
     */
    public final void setPrimaryDirection(int primaryDirection) {
        this.primaryDirection = primaryDirection;
    }

    /**
     * Gets the secondary direction this character is facing.
     *
     * @return the secondary direction.
     */
    public final int getSecondaryDirection() {
        return secondaryDirection;
    }

    /**
     * Sets the value for {@link CharacterNode#secondaryDirection}.
     *
     * @param secondaryDirection
     *            the new value to set.
     */
    public final void setSecondaryDirection(int secondaryDirection) {
        this.secondaryDirection = secondaryDirection;
    }

    /**
     * Gets the last direction this character was facing.
     *
     * @return the last direction.
     */
    public final int getLastDirection() {
        return lastDirection;
    }

    /**
     * Sets the value for {@link CharacterNode#lastDirection}.
     *
     * @param lastDirection
     *            the new value to set.
     */
    public final void setLastDirection(int lastDirection) {
        this.lastDirection = lastDirection;
    }

    /**
     * Determines if this character needs placement.
     *
     * @return {@code true} if this character needs placement, {@code false}
     *         otherwise.
     */
    public final boolean isNeedsPlacement() {
        return needsPlacement;
    }

    /**
     * Sets the value for {@link CharacterNode#needsPlacement}.
     *
     * @param needsPlacement
     *            the new value to set.
     */
    public final void setNeedsPlacement(boolean needsPlacement) {
        this.needsPlacement = needsPlacement;
    }

    /**
     * Determines if this character needs to reset their movement queue.
     *
     * @return {@code true} if this character needs to reset their movement
     *         queue, {@code false} otherwise.
     */
    public final boolean isResetMovementQueue() {
        return resetMovementQueue;
    }

    /**
     * Sets the value for {@link CharacterNode#resetMovementQueue}.
     *
     * @param resetMovementQueue
     *            the new value to set.
     */
    public final void setResetMovementQueue(boolean resetMovementQueue) {
        this.resetMovementQueue = resetMovementQueue;
    }

    /**
     * Gets the spell that this character is currently casting.
     *
     * @return the spell currently being casted.
     */
    public final CombatSpell getCurrentlyCasting() {
        return currentlyCasting;
    }

    /**
     * Sets the value for {@link CharacterNode#currentlyCasting}.
     *
     * @param currentlyCasting
     *            the new value to set.
     */
    public final void setCurrentlyCasting(CombatSpell currentlyCasting) {
        this.currentlyCasting = currentlyCasting;
    }

    /**
     * Gets the current region this character is in.
     *
     * @return the current region.
     */
    public final Position getCurrentRegion() {
        return currentRegion;
    }

    /**
     * Sets the value for {@link CharacterNode#currentRegion}.
     *
     * @param currentRegion
     *            the new value to set.
     */
    public final void setCurrentRegion(Position currentRegion) {
        this.currentRegion = currentRegion;
    }

    /**
     * Determines if this character is auto-retaliating.
     *
     * @return {@code true} if this character is auto-retaliating, {@code false}
     *         otherwise.
     */
    public final boolean isAutoRetaliate() {
        return autoRetaliate;
    }

    /**
     * Sets the value for {@link CharacterNode#autoRetaliate}.
     *
     * @param autoRetaliate
     *            the new value to set.
     */
    public final void setAutoRetaliate(boolean autoRetaliate) {
        this.autoRetaliate = autoRetaliate;
    }

    /**
     * Determines if this character is following someone.
     *
     * @return {@code true} if this character is following someone,
     *         {@code false} otherwise.
     */
    public final boolean isFollowing() {
        return following;
    }

    /**
     * Sets the value for {@link CharacterNode#following}.
     *
     * @param following
     *            the new value to set.
     */
    public final void setFollowing(boolean following) {
        this.following = following;
    }

    /**
     * Gets the character that is currently being followed.
     *
     * @return the character being followed.
     */
    public final CharacterNode getFollowCharacter() {
        return followCharacter;
    }

    /**
     * Sets the value for {@link CharacterNode#followCharacter}.
     *
     * @param followCharacter
     *            the new value to set.
     */
    public final void setFollowCharacter(CharacterNode followCharacter) {
        this.followCharacter = followCharacter;
    }

    /**
     * Determines if this character is dead or not.
     *
     * @return {@code true} if this character is dead, {@code false} otherwise.
     */
    public final boolean isDead() {
        return dead;
    }

    /**
     * Sets the value for {@link CharacterNode#dead}.
     *
     * @param dead
     *            the new value to set.
     */
    public final void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * Gets the last position this character was on.
     *
     * @return the last position.
     */
    public final Position getLastPosition() {
        return lastPosition;
    }

    /**
     * Sets the value for {@link CharacterNode#lastPosition}.
     *
     * @param lastPosition
     *            the new value to set.
     */
    public final void setLastPosition(Position lastPosition) {
        this.lastPosition = lastPosition;
    }

    /**
     * Determines if this character is visible or not.
     *
     * @return {@code true} if this character is visible, {@code false}
     *         otherwise.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the value for {@link CharacterNode#visible}.
     *
     * @param visible
     *            the new value to set.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Gets the combat builder that will handle all combat operations for this
     * character.
     *
     * @return the combat builder.
     */
    public final CombatBuilder getCombatBuilder() {
        return combatBuilder;
    }

    /**
     * Gets the movement queue that will handle all movement processing for this
     * character.
     *
     * @return the movement queue.
     */
    public final MovementQueue getMovementQueue() {
        return movementQueue;
    }

    /**
     * Gets the movement queue listener that will allow for actions to be
     * appended to the end of the movement queue.
     *
     * @return the movement queue listener.
     */
    public final MovementQueueListener getMovementListener() {
        return movementListener;
    }

    /**
     * Gets the update flags used for signifying that this character needs
     * something updated.
     *
     * @return the update flags.
     */
    public final UpdateFlags getFlags() {
        return flags;
    }

    /**
     * Gets the timer that records the difference in time between now and the
     * last time the player was in combat.
     *
     * @return the timer that determines when the player was last in combat.
     */
    public final Stopwatch getLastCombat() {
        return lastCombat;
    }

    /**
     * Gets the animation update block value.
     *
     * @return the animation update block value.
     */
    public final Animation getAnimation() {
        return animation;
    }

    /**
     * Gets the graphic update block value.
     *
     * @return the graphic update block value.
     */
    public final Graphic getGraphic() {
        return graphic;
    }

    /**
     * Gets the forced text update block value.
     *
     * @return the forced text update block value.
     */
    public final String getForcedText() {
        return forcedText;
    }

    /**
     * Gets the face index update block value.
     *
     * @return the face index update block value.
     */
    public final int getFaceIndex() {
        return faceIndex;
    }

    /**
     * Gets the face position update block value.
     *
     * @return the face position update block value.
     */
    public final Position getFacePosition() {
        return facePosition;
    }

    /**
     * Gets the primary hit update block value.
     *
     * @return the primary hit update block value.
     */
    public final Hit getPrimaryHit() {
        return primaryHit;
    }

    /**
     * Gets the secondary hit update block value.
     *
     * @return the secondary hit update block value.
     */
    public final Hit getSecondaryHit() {
        return secondaryHit;
    }

    /**
     * Gets the type of poison that was previously applied.
     * 
     * @return the type of poison.
     */
    public PoisonType getPoisonType() {
        return poisonType;
    }

    /**
     * Sets the value for {@link CharacterNode#poisonType}.
     * 
     * @param poisonType
     *            the new value to set.
     */
    public void setPoisonType(PoisonType poisonType) {
        this.poisonType = poisonType;
    }
}
