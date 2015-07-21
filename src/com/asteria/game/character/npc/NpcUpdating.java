package com.asteria.game.character.npc;

import java.util.Iterator;

import com.asteria.game.World;
import com.asteria.game.character.Flag;
import com.asteria.game.character.player.Player;
import com.asteria.game.location.Position;
import com.asteria.net.ByteOrder;
import com.asteria.net.ValueType;
import com.asteria.net.message.MessageBuilder;
import com.asteria.utility.BitMask;

/**
 * The class that provides static utility methods for updating NPCs.
 *
 * @author blakeman8192
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcUpdating {

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private NpcUpdating() {
        throw new UnsupportedOperationException("This class cannot be " + "instantiated!");
    }

    /**
     * The method that performs updating on NPCs for {@code player}.
     *
     * @param player
     *            the player NPCs are being updated for.
     * @throws Exception
     *             if any errors occur while updating NPCs for the player.
     */
    public static void update(Player player) throws Exception {
        MessageBuilder out = MessageBuilder.create(2048);
        MessageBuilder block = MessageBuilder.create(1024);
        out.newVarShortMessage(65);
        out.startBitAccess();
        out.putBits(8, player.getLocalNpcs().size());
        for (Iterator<Npc> i = player.getLocalNpcs().iterator(); i.hasNext();) {
            Npc npc = i.next();
            if (npc.getPosition().isViewableFrom(player.getPosition()) && npc.isVisible()) {
                NpcUpdating.updateNpcMovement(out, npc);
                if (npc.getFlags().needsUpdate()) {
                    NpcUpdating.updateState(block, npc);
                }
            } else {
                out.putBit(true);
                out.putBits(2, 3);
                i.remove();
            }
        }
        int added = 0;
        for (Npc npc : World.getNpcs()) {
            if (added == 15 || player.getLocalNpcs().size() >= 255)
                break;
            if (npc == null)
                continue;
            if (npc.getPosition().isViewableFrom(player.getPosition()) && npc.isVisible()) {
                if (player.getLocalNpcs().add(npc)) {
                    npc.getFlags().set(Flag.APPEARANCE);
                    addNpc(out, player, npc);
                    if (npc.getFlags().needsUpdate()) {
                        NpcUpdating.updateState(block, npc);
                    }
                    added++;
                }
            }
        }
        if (block.buffer().writerIndex() > 0) {
            out.putBits(14, 16383);
            out.endBitAccess();
            out.putBytes(block.buffer());
        } else {
            out.endBitAccess();
        }
        out.endVarShortMessage();
        player.getSession().queue(out);
    }

    /**
     * Puts {@code npc} in the client local list of {@code player}.
     *
     * @param out
     *            the buffer to write the data to.
     * @param player
     *            the player who's list will be modified.
     * @param npc
     *            the NPC who will be added to the player's list.
     */
    private static void addNpc(MessageBuilder out, Player player, Npc npc) {
        out.putBits(14, npc.getSlot());
        Position delta = Position.delta(player.getPosition(), npc.getPosition());
        out.putBits(5, delta.getY());
        out.putBits(5, delta.getX());
        out.putBit(npc.getFlags().needsUpdate());
        out.putBits(12, npc.getId());
        out.putBit(true);
    }

    /**
     * Updates the movement of a NPC for this sequence.
     *
     * @param out
     *            the buffer that the data will be written to.
     * @param npc
     *            the NPC's movement that will be updated.
     */
    private static void updateNpcMovement(MessageBuilder out, Npc npc) {
        if (npc.getPrimaryDirection() == -1) {
            if (npc.getFlags().needsUpdate()) {
                out.putBit(true);
                out.putBits(2, 0);
            } else {
                out.putBit(false);
            }
        } else {
            out.putBit(true);
            out.putBits(2, 1);
            out.putBits(3, npc.getPrimaryDirection());
            if (npc.getFlags().needsUpdate()) {
                out.putBit(true);
            } else {
                out.putBit(false);
            }
        }
    }

    /**
     * Updates the state of {@code npc}.
     *
     * @param block
     *            the buffer that the data will be written to.
     * @param npc
     *            the NPC that will have it's state updated.
     * @throws Exception
     *             if any errors occur while updating the state.
     */
    private static void updateState(MessageBuilder block, Npc npc) throws Exception {
        BitMask mask = new BitMask();
        if (npc.getFlags().get(Flag.ANIMATION)) {
            mask.set(0x10);
        }
        if (npc.getFlags().get(Flag.HIT_2)) {
            mask.set(8);
        }
        if (npc.getFlags().get(Flag.GRAPHICS)) {
            mask.set(0x80);
        }
        if (npc.getFlags().get(Flag.FACE_CHARACTER)) {
            mask.set(0x20);
        }
        if (npc.getFlags().get(Flag.FORCED_CHAT)) {
            mask.set(1);
        }
        if (npc.getFlags().get(Flag.HIT)) {
            mask.set(0x40);
        }
        if (npc.getFlags().get(Flag.TRANSFORM)) {
            mask.set(2);
        }
        if (npc.getFlags().get(Flag.FACE_COORDINATE)) {
            mask.set(4);
        }
        if (mask.get() >= 0x100) {
            mask.set(0x40);
            block.putShort(mask.get(), com.asteria.net.ByteOrder.LITTLE);
        } else {
            block.put(mask.get());
        }
        if (npc.getFlags().get(Flag.ANIMATION)) {
            appendAnimation(block, npc);
        }
        if (npc.getFlags().get(Flag.HIT_2)) {
            appendSecondaryHit(block, npc);
        }
        if (npc.getFlags().get(Flag.GRAPHICS)) {
            appendGraphic(block, npc);
        }
        if (npc.getFlags().get(Flag.FACE_CHARACTER)) {
            appendFaceCharacter(block, npc);
        }
        if (npc.getFlags().get(Flag.FORCED_CHAT)) {
            appendForcedChat(block, npc);
        }
        if (npc.getFlags().get(Flag.HIT)) {
            appendPrimaryHit(block, npc);
        }
        if (npc.getFlags().get(Flag.TRANSFORM)) {
            appendTransformation(block, npc);
        }
        if (npc.getFlags().get(Flag.FACE_COORDINATE)) {
            appendFaceCoordinates(block, npc);
        }
    }

    /**
     * Appends the state of a graphic to {@code out} for {@code npc}.
     *
     * @param npc
     *            the npc to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendGraphic(MessageBuilder out, Npc npc) {
        out.putShort(npc.getGraphic().getId());
        out.putInt(npc.getGraphic().getHeight());
    }

    /**
     * Appends the state of a transformation to {@code out} for {@code npc}.
     *
     * @param npc
     *            the npc to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendTransformation(MessageBuilder out, Npc npc) {
        out.putShort(npc.getTransform(), ValueType.A, ByteOrder.LITTLE);
    }

    /**
     * Appends the state of a secondary hit to {@code out} for {@code npc}.
     *
     * @param npc
     *            the npc to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendSecondaryHit(MessageBuilder out, Npc npc) throws Exception {
        if (!npc.isDead()) {
            if (npc.getCurrentHealth() <= 0) {
                npc.setCurrentHealth(0);
                World.submit(new NpcDeath(npc));
            }
        }
        out.put(npc.getSecondaryHit().getDamage(), ValueType.A);
        out.put(npc.getSecondaryHit().getType().getId(), ValueType.C);
        out.put(npc.getCurrentHealth(), ValueType.A);
        out.put(npc.getMaxHealth());
    }

    /**
     * Appends the state of facing a character to {@code out} for {@code npc}.
     *
     * @param npc
     *            the npc to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendFaceCharacter(MessageBuilder out, Npc npc) {
        out.putShort(npc.getFaceIndex());
    }

    /**
     * Appends the state of forced chat to {@code out} for {@code npc}.
     *
     * @param npc
     *            the npc to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendForcedChat(MessageBuilder out, Npc npc) {
        out.putString(npc.getForcedText());
    }

    /**
     * Appends the state of a primary hit to {@code out} for {@code npc}.
     *
     * @param npc
     *            the npc to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendPrimaryHit(MessageBuilder out, Npc npc) {
        if (!npc.isDead()) {
            if (npc.getCurrentHealth() <= 0) {
                npc.setCurrentHealth(0);
                World.submit(new NpcDeath(npc));
            }
        }
        out.put(npc.getPrimaryHit().getDamage(), ValueType.C);
        out.put(npc.getPrimaryHit().getType().getId(), ValueType.S);
        out.put(npc.getCurrentHealth(), ValueType.S);
        out.put(npc.getMaxHealth(), ValueType.C);
    }

    /**
     * Appends the state of facing a set of coordinates to {@code out} for
     * {@code npc}.
     *
     * @param npc
     *            the npc to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendFaceCoordinates(MessageBuilder out, Npc npc) {
        out.putShort(npc.getFacePosition().getX(), ByteOrder.LITTLE);
        out.putShort(npc.getFacePosition().getY(), ByteOrder.LITTLE);
    }

    /**
     * Appends the state of an animation to {@code out} for {@code npc}.
     *
     * @param npc
     *            the npc to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendAnimation(MessageBuilder out, Npc npc) {
        out.putShort(npc.getAnimation().getId(), ByteOrder.LITTLE);
        out.put(npc.getAnimation().getDelay());
    }
}
