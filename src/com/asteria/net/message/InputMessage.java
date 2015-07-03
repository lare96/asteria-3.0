package com.asteria.net.message;

import com.asteria.net.codec.MessageDecoder;

/**
 * The {@link Message} implementation that is a message that has been sent from
 * the client and decoded by the {@link MessageDecoder}.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class InputMessage implements Message {

    /**
     * The opcode of this message.
     */
    private final int opcode;

    /**
     * The size of this message.
     */
    private final int size;

    /**
     * The payload of this message.
     */
    private final MessageBuilder payload;

    /**
     * Creates a new {@link InputMessage}.
     *
     * @param opcode
     *            the opcode of this message.
     * @param size
     *            the size of this message.
     * @param payload
     *            the payload of this message.
     */
    public InputMessage(int opcode, int size, MessageBuilder payload) {
        this.opcode = opcode;
        this.size = size;
        this.payload = payload;
    }

    /**
     * Gets the opcode of this message.
     * 
     * @return the opcode.
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * Gets the size of this message.
     * 
     * @return the size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the payload of this message.
     * 
     * @return the payload.
     */
    public MessageBuilder getPayload() {
        return payload;
    }
}
