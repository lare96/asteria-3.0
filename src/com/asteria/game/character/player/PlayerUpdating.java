package com.asteria.game.character.player;

import java.util.Iterator;

import com.asteria.game.World;
import com.asteria.game.character.Flag;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.container.Equipment;
import com.asteria.game.location.Position;
import com.asteria.network.ByteOrder;
import com.asteria.network.DataBuffer;
import com.asteria.network.ValueType;
import com.asteria.task.TaskHandler;
import com.asteria.utility.BitMask;

/**
 * The class that provides static utility methods for updating players.
 *
 * @author blakeman8192
 * @author lare96 <http://github.com/lare96>
 */
public final class PlayerUpdating {

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private PlayerUpdating() {
        throw new UnsupportedOperationException("This class cannot be " + "instantiated!");
    }

    /**
     * The method that performs updating on {@code player}.
     *
     * @param player
     *            the player being updated.
     * @throws Exception
     *             if any errors occur while updating the player.
     */
    public static void update(Player player) throws Exception {
        DataBuffer out = DataBuffer.create(16384);
        DataBuffer block = DataBuffer.create(8192);
        out.newVarShortPacket(81, player.getSession().getEncryptor());
        out.startBitAccess();
        PlayerUpdating.updateLocalPlayerMovement(player, out);
        if (player.getFlags().needsUpdate())
            PlayerUpdating.updateState(player, player, block, false, true);
        out.putBits(8, player.getLocalPlayers().size());
        for (Iterator<Player> i = player.getLocalPlayers().iterator(); i.hasNext();) {
            Player other = i.next();
            if (other.getPosition().isViewableFrom(player.getPosition()) && other.getSession().getState() == IOState.LOGGED_IN && !other
                .isNeedsPlacement() && other.isVisible()) {
                PlayerUpdating.updateOtherPlayerMovement(other, out);
                if (other.getFlags().needsUpdate()) {
                    PlayerUpdating.updateState(other, player, block, false, false);
                }
            } else {
                out.putBit(true);
                out.putBits(2, 3);
                i.remove();
            }
        }
        int added = 0;
        for (Player other : World.getPlayers()) {
            if (added == 15 || player.getLocalPlayers().size() >= 255)
                break;
            if (other == null || other.equals(player) || other.getSession().getState() != IOState.LOGGED_IN)
                continue;
            if (other.getPosition().isViewableFrom(player.getPosition()) && other.isVisible()) {
                if (player.getLocalPlayers().add(other)) {
                    added++;
                    PlayerUpdating.addPlayer(out, player, other);
                    PlayerUpdating.updateState(other, player, block, true, false);
                }
            }
        }
        if (block.buffer().position() > 0) {
            out.putBits(11, 2047);
            out.endBitAccess();
            out.putBytes(block.buffer());
        } else {
            out.endBitAccess();
        }
        out.endVarShortPacket();
        player.getSession().send(out);
    }

    /**
     * Appends the state of chat to {@code out} for {@code player}.
     *
     * @param player
     *            the player to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendChat(Player player, DataBuffer out) {
        out.putShort(((player.getChatColor() & 0xff) << 8) + (player.getChatEffects() & 0xff), ByteOrder.LITTLE);
        out.put(player.getRights().getProtocolValue());
        out.put(player.getChatText().length, ValueType.C);
        out.putBytesReverse(player.getChatText());
    }

    /**
     * Appends the state of appearance to {@code out} for {@code player}.
     *
     * @param player
     *            the player to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendAppearance(Player player, DataBuffer out) {
        Appearance appearance = player.getAppearance();
        DataBuffer block = DataBuffer.create(128);
        block.put(appearance.getGender());
        block.put(player.getHeadIcon());
        block.put(player.getSkullIcon());
        if (player.getPlayerNpc() == -1) {
            if (player.getEquipment().getId(Equipment.HEAD_SLOT) > 1) {
                block.putShort(0x200 + player.getEquipment().getId(Equipment.HEAD_SLOT));
            } else {
                block.put(0);
            }
            if (player.getEquipment().getId(Equipment.CAPE_SLOT) > 1) {
                block.putShort(0x200 + player.getEquipment().getId(Equipment.CAPE_SLOT));
            } else {
                block.put(0);
            }
            if (player.getEquipment().getId(Equipment.AMULET_SLOT) > 1) {
                block.putShort(0x200 + player.getEquipment().getId(Equipment.AMULET_SLOT));
            } else {
                block.put(0);
            }
            if (player.getEquipment().getId(Equipment.WEAPON_SLOT) > 1) {
                block.putShort(0x200 + player.getEquipment().getId(Equipment.WEAPON_SLOT));
            } else {
                block.put(0);
            }
            if (player.getEquipment().getId(Equipment.CHEST_SLOT) > 1) {
                block.putShort(0x200 + player.getEquipment().getId(Equipment.CHEST_SLOT));
            } else {
                block.putShort(0x100 + appearance.getChest());
            }
            if (player.getEquipment().getId(Equipment.SHIELD_SLOT) > 1) {
                block.putShort(0x200 + player.getEquipment().getId(Equipment.SHIELD_SLOT));
            } else {
                block.put(0);
            }
            if (player.getEquipment().getId(Equipment.CHEST_SLOT) > 1) {
                if (!player.getEquipment().get(Equipment.CHEST_SLOT).getDefinition().isPlatebody()) {
                    block.putShort(0x100 + appearance.getArms());
                } else {
                    block.put(0);
                }
            } else {
                block.putShort(0x100 + appearance.getArms());
            }
            if (player.getEquipment().getId(Equipment.LEGS_SLOT) > 1) {
                block.putShort(0x200 + player.getEquipment().getId(Equipment.LEGS_SLOT));
            } else {
                block.putShort(0x100 + appearance.getLegs());
            }
            if (player.getEquipment().getId(Equipment.HEAD_SLOT) > 1 && player.getEquipment().get(Equipment.HEAD_SLOT).getDefinition()
                .isFullHelm()) {
                block.put(0);
            } else {
                block.putShort(0x100 + appearance.getHead());
            }
            if (player.getEquipment().getId(Equipment.HANDS_SLOT) > 1) {
                block.putShort(0x200 + player.getEquipment().getId(Equipment.HANDS_SLOT));
            } else {
                block.putShort(0x100 + appearance.getHands());
            }
            if (player.getEquipment().getId(Equipment.FEET_SLOT) > 1) {
                block.putShort(0x200 + player.getEquipment().getId(Equipment.FEET_SLOT));
            } else {
                block.putShort(0x100 + appearance.getFeet());
            }
            if (appearance.isMale()) {
                if (player.getEquipment().getId(Equipment.HEAD_SLOT) > 1 && !player.getEquipment().get(Equipment.HEAD_SLOT).getDefinition()
                    .isFullHelm() || player.getEquipment().free(Equipment.HEAD_SLOT)) {
                    block.putShort(0x100 + appearance.getBeard());
                } else {
                    block.put(0);
                }
            } else {
                block.put(0);
            }
        } else {
            block.putShort(-1);
            block.putShort(player.getPlayerNpc());
        }
        block.put(appearance.getHairColor());
        block.put(appearance.getTorsoColor());
        block.put(appearance.getLegColor());
        block.put(appearance.getFeetColor());
        block.put(appearance.getSkinColor());

        block.putShort(player.getWeaponAnimation() == null || player.getWeaponAnimation().getStanding() == -1 ? 0x328 : player
            .getWeaponAnimation().getStanding());
        block.putShort(0x337);
        block.putShort(player.getWeaponAnimation() == null || player.getWeaponAnimation().getWalking() == -1 ? 0x333 : player
            .getWeaponAnimation().getWalking());
        block.putShort(0x334);
        block.putShort(0x335);
        block.putShort(0x336);
        block.putShort(player.getWeaponAnimation() == null || player.getWeaponAnimation().getRunning() == -1 ? 0x338 : player
            .getWeaponAnimation().getRunning());

        block.putLong(player.getUsernameHash());
        block.put(player.determineCombatLevel());
        block.putShort(0);

        out.put(block.buffer().position(), com.asteria.network.ValueType.C);
        out.putBytes(block.buffer());
    }

    /**
     * Puts {@code player} in the client local list of {@code other}.
     *
     * @param out
     *            the buffer to write the data to.
     * @param player
     *            the player to add to the other player's list.
     * @param other
     *            the player who's list will be modified.
     */
    private static void addPlayer(DataBuffer out, Player player, Player other) {
        out.putBits(11, other.getSlot());
        out.putBit(true);
        out.putBit(true);
        Position delta = Position.delta(player.getPosition(), other.getPosition());
        out.putBits(5, delta.getY());
        out.putBits(5, delta.getX());
    }

    /**
     * Updates movement for this local player. The difference between this
     * method and the other player method is that this will make use of sector
     * 2,3 to place the player in a specific position while sector 2,3 is not
     * present in updating of other players (it simply flags local list removal
     * instead).
     *
     * @param player
     *            the player to update movement for.
     * @param out
     *            the buffer to write to data to.
     */
    private static void updateLocalPlayerMovement(Player player, DataBuffer out) {
        boolean updateRequired = player.getFlags().needsUpdate();
        if (player.isNeedsPlacement()) {
            out.putBit(true);
            int posX = player.getPosition().getLocalX(player.getCurrentRegion());
            int posY = player.getPosition().getLocalY(player.getCurrentRegion());
            appendPlacement(out, posX, posY, player.getPosition().getZ(), player.isResetMovementQueue(), updateRequired);
        } else {
            int pDir = player.getPrimaryDirection();
            int sDir = player.getSecondaryDirection();
            if (pDir != -1) {
                out.putBit(true);
                if (sDir != -1) {
                    appendRun(out, pDir, sDir, updateRequired);
                } else {
                    appendWalk(out, pDir, updateRequired);
                }
            } else {
                if (updateRequired) {
                    out.putBit(true);
                    appendStand(out);
                } else {
                    out.putBit(false);
                }
            }
        }
    }

    /**
     * Updates the movement of a player for another player (does not make use of
     * sector 2,3).
     *
     * @param player
     *            the player to update movement for.
     * @param out
     *            the buffer to write the data to.
     */
    private static void updateOtherPlayerMovement(Player player, DataBuffer out) {
        boolean updateRequired = player.getFlags().needsUpdate();
        int pDir = player.getPrimaryDirection();
        int sDir = player.getSecondaryDirection();
        if (pDir != -1) {
            out.putBit(true);
            if (sDir != -1) {
                appendRun(out, pDir, sDir, updateRequired);
            } else {
                appendWalk(out, pDir, updateRequired);
            }
        } else {
            if (updateRequired) {
                out.putBit(true);
                appendStand(out);
            } else {
                out.putBit(false);
            }
        }
    }

    /**
     * Updates the state of {@code thisPlayer} for {@code player}.
     *
     * @param player
     *            the player to update the state for.
     * @param thisPlayer
     *            the player who's state is being updated.
     * @param block
     *            the buffer that the data will be written to.
     * @param forceAppearance
     *            if the appearance block is being forced.
     * @param noChat
     *            if the chat block is being disabled.
     * @throws Exception
     *             if any errors occur while updating the state.
     */
    private static void updateState(Player player, Player thisPlayer, DataBuffer block, boolean forceAppearance, boolean noChat) throws Exception {
        if (!player.getFlags().needsUpdate() && !forceAppearance)
            return;
        if (player.getCachedUpdateBlock() != null && !player.equals(thisPlayer) && !forceAppearance && !noChat) {
            block.putBytes(player.getCachedUpdateBlock());
            return;
        }
        DataBuffer cachedBuffer = DataBuffer.create(300);
        BitMask mask = new BitMask();

        if (player.getFlags().get(Flag.FORCED_MOVEMENT)) {
            mask.set(0x400);
        }
        if (player.getFlags().get(Flag.GRAPHICS)) {
            mask.set(0x100);
        }
        if (player.getFlags().get(Flag.ANIMATION)) {
            mask.set(8);
        }
        if (player.getFlags().get(Flag.FORCED_CHAT)) {
            mask.set(4);
        }
        if (player.getFlags().get(Flag.CHAT) && !noChat) {
            mask.set(0x80);
        }
        if (player.getFlags().get(Flag.APPEARANCE) || forceAppearance) {
            mask.set(0x10);
        }
        if (player.getFlags().get(Flag.FACE_CHARACTER)) {
            mask.set(1);
        }
        if (player.getFlags().get(Flag.FACE_COORDINATE)) {
            mask.set(2);
        }
        if (player.getFlags().get(Flag.HIT)) {
            mask.set(0x20);
        }
        if (player.getFlags().get(Flag.HIT_2)) {
            mask.set(0x200);
        }
        if (mask.get() >= 0x100) {
            mask.set(0x40);
            cachedBuffer.putShort(mask.get(), com.asteria.network.ByteOrder.LITTLE);
        } else {
            cachedBuffer.put(mask.get());
        }

        if (player.getFlags().get(Flag.FORCED_MOVEMENT)) {
            appendForcedMovement(player, cachedBuffer);
        }
        if (player.getFlags().get(Flag.GRAPHICS)) {
            appendGraphic(player, cachedBuffer);
        }
        if (player.getFlags().get(Flag.ANIMATION)) {
            appendAnimation(player, cachedBuffer);
        }
        if (player.getFlags().get(Flag.FORCED_CHAT)) {
            appendForcedChat(player, cachedBuffer);
        }
        if (player.getFlags().get(Flag.CHAT) && !noChat) {
            appendChat(player, cachedBuffer);
        }
        if (player.getFlags().get(Flag.FACE_CHARACTER)) {
            appendFaceCharacter(player, cachedBuffer);
        }
        if (player.getFlags().get(Flag.APPEARANCE) || forceAppearance) {
            appendAppearance(player, cachedBuffer);
        }
        if (player.getFlags().get(Flag.FACE_COORDINATE)) {
            appendFaceCoordinates(player, cachedBuffer);
        }
        if (player.getFlags().get(Flag.HIT)) {
            appendPrimaryHit(player, cachedBuffer);
        }
        if (player.getFlags().get(Flag.HIT_2)) {
            appendSecondaryHit(player, cachedBuffer);
        }
        if (!player.equals(thisPlayer) && !forceAppearance && !noChat) {
            player.setCachedUpdateBlock(cachedBuffer.buffer());
        }
        block.putBytes(cachedBuffer.buffer());
    }

    /**
     * Appends the state of forced movement to {@code out} for {@code player}.
     *
     * @param player
     *            the player to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendForcedMovement(Player player, DataBuffer out) {
        int localX = player.getPosition().getLocalX();
        int localY = player.getPosition().getLocalY();
        out.put(localX, ValueType.S);
        out.put(localY, ValueType.S);
        out.put(localX + player.getForcedMovement().getAmountX(), ValueType.S);
        out.put(localY + player.getForcedMovement().getAmountY(), ValueType.S);
        out.putShort(1, ValueType.A, ByteOrder.LITTLE);
        out.putShort(0, ValueType.A);
        out.put(player.getForcedMovement().getDirection(), ValueType.S);
    }

    /**
     * Appends the state of forced chat to {@code out} for {@code player}.
     *
     * @param player
     *            the player to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendForcedChat(Player player, DataBuffer out) {
        out.putString(player.getForcedText());
    }

    /**
     * Appends the state of facing another character to {@code out} for
     * {@code player}.
     *
     * @param player
     *            the player to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendFaceCharacter(Player player, DataBuffer out) {
        out.putShort(player.getFaceIndex(), ByteOrder.LITTLE);
    }

    /**
     * Appends the state of facing a set of coordinates to {@code out} for
     * {@code player}.
     *
     * @param player
     *            the player to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendFaceCoordinates(Player player, DataBuffer out) {
        out.putShort(player.getFacePosition().getX(), ValueType.A, ByteOrder.LITTLE);
        out.putShort(player.getFacePosition().getY(), ByteOrder.LITTLE);
    }

    /**
     * Appends the state of animation to {@code out} for {@code player}.
     *
     * @param player
     *            the player to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendAnimation(Player player, DataBuffer out) {
        out.putShort(player.getAnimation().getId(), ByteOrder.LITTLE);
        out.put(player.getAnimation().getDelay(), ValueType.C);
    }

    /**
     * Appends the state of a primary hit to {@code out} for {@code player}.
     *
     * @param player
     *            the player to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendPrimaryHit(Player player, DataBuffer out) throws Exception {
        out.put(player.getPrimaryHit().getDamage());
        out.put(player.getPrimaryHit().getType().getId(), ValueType.A);
        if (!player.isDead()) {
            if (player.getSkills()[Skills.HITPOINTS].getLevel() <= 0) {
                player.getSkills()[Skills.HITPOINTS].setLevel(0, true);
                player.setDead(true);
                TaskHandler.submit(new PlayerDeath(player));
            }
        }
        out.put(player.getSkills()[Skills.HITPOINTS].getLevel(), ValueType.C);
        out.put(player.getSkills()[Skills.HITPOINTS].getRealLevel());
    }

    /**
     * Appends the state of a secondary hit to {@code out} for {@code player}.
     *
     * @param player
     *            the player to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendSecondaryHit(Player player, DataBuffer out) throws Exception {
        out.put(player.getSecondaryHit().getDamage());
        out.put(player.getSecondaryHit().getType().getId(), ValueType.S);

        if (!player.isDead()) {
            if (player.getSkills()[Skills.HITPOINTS].getLevel() <= 0) {
                player.getSkills()[Skills.HITPOINTS].setLevel(0, true);
                player.setDead(true);
                TaskHandler.submit(new PlayerDeath(player));
            }
        }

        out.put(player.getSkills()[Skills.HITPOINTS].getLevel());
        out.put(player.getSkills()[Skills.HITPOINTS].getRealLevel(), ValueType.C);
    }

    /**
     * Appends the state of a graphic to {@code out} for {@code player}.
     *
     * @param player
     *            the player to append the state for.
     * @param out
     *            the buffer to append it to.
     */
    private static void appendGraphic(Player player, DataBuffer out) {
        out.putShort(player.getGraphic().getId(), ByteOrder.LITTLE);
        out.putInt(player.getGraphic().getHeight());
    }

    /**
     * Appends the stand version of the movement section of the update packet
     * (sector 2,0). Appending this (instead of just a zero bit) automatically
     * assumes that there is a required attribute update afterwards.
     *
     * @param out
     *            the buffer to write the data to.
     */
    private static void appendStand(DataBuffer out) {
        out.putBits(2, 0);
    }

    /**
     * Appends the walk version of the movement section of the update packet
     * (sector 2,1).
     *
     * @param out
     *            the buffer to write the data to.
     * @param direction
     *            the walking direction to append.
     * @param attributesUpdate
     *            whether or not a player attributes update is required.
     */
    private static void appendWalk(DataBuffer out, int direction, boolean attributesUpdate) {
        out.putBits(2, 1);
        out.putBits(3, direction);
        out.putBit(attributesUpdate);
    }

    /**
     * Appends the walk version of the movement section of the update packet
     * (sector 2,2).
     *
     * @param out
     *            the buffer to write the data to.
     * @param direction
     *            the walking direction to append.
     * @param direction2
     *            the running direction to append.
     * @param attributesUpdate
     *            whether or not a player attributes update is required.
     */
    private static void appendRun(DataBuffer out, int direction, int direction2, boolean attributesUpdate) {
        out.putBits(2, 2);
        out.putBits(3, direction);
        out.putBits(3, direction2);
        out.putBit(attributesUpdate);
    }

    /**
     * Appends the player placement version of the movement section of the
     * update packet (sector 2,3). Note that by others this was previously
     * called the "teleport update".
     *
     * @param out
     *            the buffer to write the data to.
     * @param localX
     *            the local {@code X} coordinate.
     * @param localY
     *            the local {@code Y} coordinate.
     * @param z
     *            the {@code Z} coordinate.
     * @param discardMovementQueue
     *            whether or not the client should discard the movement queue.
     * @param attributesUpdate
     *            whether or not a plater attributes update is required.
     */
    private static void appendPlacement(DataBuffer out, int localX, int localY, int z, boolean discardMovementQueue, boolean attributesUpdate) {
        out.putBits(2, 3);
        out.putBits(2, z);
        out.putBit(discardMovementQueue);
        out.putBit(attributesUpdate);
        out.putBits(7, localY);
        out.putBits(7, localX);
    }
}
