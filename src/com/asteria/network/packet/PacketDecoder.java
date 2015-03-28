package com.asteria.network.packet;

import com.asteria.game.character.player.Player;
import com.asteria.network.DataBuffer;

/**
 * The class that provides functionality for decoding incoming packets.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class PacketDecoder {

    /**
     * An array of the packet opcodes mapped to their respective decoders.
     */
    public static final PacketDecoder[] PACKETS = new PacketDecoder[257];

    /**
     * An array of packet opcodes mapped to their respective sizes.
     */
    public static final int PACKET_SIZES[] = new int[257];

    /**
     * The method executed when a packet is successfully decoded by the
     * reactor.
     *
     * @param player
     *         the player that was decoding the packet.
     * @param opcode
     *         the opcode of the packet that was decoded.
     * @param size
     *         the size of the packet that was decoded.
     * @param buf
     *         the buffer containing the decoded packet data.
     */
    public abstract void decode(Player player, int opcode, int size,
                                DataBuffer buf);

    /**
     * Determines if this packet should be counted.
     *
     * @param player
     *         the player this packet will be counted for.
     * @return {@code true} if this packet should be counted, {@code false}
     * otherwise.
     */
    public boolean countPacket(Player player) {
        return true;
    }
}
