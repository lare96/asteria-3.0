package com.asteria.network;

import java.nio.ByteBuffer;

/**
 * A resizable buffer implementation backed by a byte buffer, that is used for
 * reading and writing data.
 *
 * @author lare96 <http://github.com/lare96>
 * @author blakeman8192
 */
public final class DataBuffer {

    /**
     * An array of the bit masks used for writing bits.
     */
    private static final int[] BIT_MASK = {0, 0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f,
            0x7f, 0xff, 0x1ff, 0x3ff, 0x7ff, 0xfff, 0x1fff, 0x3fff, 0x7fff,
            0xffff, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff,
            0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff,
            0x1fffffff, 0x3fffffff, 0x7fffffff, -1};

    /**
     * The default capacity of this buffer.
     */
    private static final int DEFAULT_CAP = 128;

    /**
     * The backing byte buffer used to read and write data.
     */
    private ByteBuffer buf;

    /**
     * The position of the buffer when a variable length packet is created.
     */
    private int varLengthIndex = 0;

    /**
     * The current bit position when writing bits.
     */
    private int bitIndex = 0;

    /**
     * Creates a new {@link DataBuffer} with the {@code buf} backing buffer.
     *
     * @param buf
     *         the backing byte buffer used to read and write data.
     */
    private DataBuffer(ByteBuffer buf) {
        this.buf = buf;
    }

    /**
     * Creates a new {@link DataBuffer} with the {@code buf} backing buffer.
     *
     * @param buf
     *         the backing byte buffer used to read and write data.
     * @return the newly created buffer.
     */
    public static DataBuffer create(ByteBuffer buf) {
        return new DataBuffer(buf);
    }

    /**
     * Creates a new {@link DataBuffer} with the {@code cap} as the capacity.
     *
     * @param cap
     *         the capacity of the buffer.
     * @return the newly created buffer.
     */
    public static DataBuffer create(int cap) {
        return DataBuffer.create(ByteBuffer.allocate(cap));
    }

    /**
     * Creates a new {@link DataBuffer} with the default capacity.
     *
     * @return the newly created buffer.
     */
    public static DataBuffer create() {
        return DataBuffer.create(DEFAULT_CAP);
    }

    /**
     * Prepares the buffer for writing bits.
     */
    public void startBitAccess() {
        bitIndex = buf.position() * 8;
    }

    /**
     * Prepares the buffer for writing bytes.
     */
    public void endBitAccess() {
        buf.position((bitIndex + 7) / 8);
    }

    /**
     * Checks if the buffer can hold the amount of requested bytes. If the
     * buffer cannot hold the specified amount, it will double in size until it
     * is able to.
     *
     * @param requested
     *         the amount of requested bytes.
     */
    private void requestSpace(int requested) {
        if ((buf.position() + requested + 1) >= buf.capacity()) {
            int oldPosition = buf.position();
            byte[] oldBuffer = buf.array();
            int newLength = (buf.capacity() * 2);
            buf = ByteBuffer.allocate(newLength);
            buf.position(oldPosition);
            System.arraycopy(oldBuffer, 0, buf.array(), 0, oldBuffer.length);
            requestSpace(requested);
        }
    }

    /**
     * Builds a new packet header.
     *
     * @param opcode
     *         the opcode of the packet.
     * @param encryptor
     *         the encryptor for encrypting the packet header.
     * @return an instance of this data buffer.
     */
    public DataBuffer newPacket(int opcode, ISAACCipher encryptor) {
        put(opcode + encryptor.getKey());
        return this;
    }

    /**
     * Builds a new packet header for a variable length packet. Note that the
     * corresponding {@link DataBuffer#endVarPacket()} method must be called to
     * finish the packet.
     *
     * @param opcode
     *         the opcode of the packet.
     * @param encryptor
     *         the encryptor for encrypting the packet header.
     * @return an instance of this data buffer.
     */
    public DataBuffer newVarPacket(int opcode, ISAACCipher encryptor) {
        newPacket(opcode, encryptor);
        varLengthIndex = buf.position();
        put(0);
        return this;
    }

    /**
     * Builds a new packet header for a variable length packet, where the
     * length
     * is written as a short instead of a byte. Note that the corresponding
     * {@link DataBuffer#endVarShortPacket()} method must be called to finish
     * the packet.
     *
     * @param opcode
     *         the opcode of the packet.
     * @param encryptor
     *         the encryptor for encrypting the packet header.
     * @return an instance of this data buffer.
     */
    public DataBuffer newVarShortPacket(int opcode, ISAACCipher encryptor) {
        newPacket(opcode, encryptor);
        varLengthIndex = buf.position();
        putShort(0);
        return this;
    }

    /**
     * Finishes a variable packet header by writing the actual packet length at
     * the length byte's position. Call this when the construction of the
     * actual
     * variable length packet is complete.
     *
     * @return an instance of this data buffer.
     */
    public DataBuffer endVarPacket() {
        requestSpace(1);
        buf.put(varLengthIndex, (byte) (buf.position() - varLengthIndex - 1));
        return this;
    }

    /**
     * Finishes a variable packet header by writing the actual packet length at
     * the length short's position. Call this when the construction of the
     * variable short length packet is complete.
     *
     * @return an instance of this data buffer.
     */
    public DataBuffer endVarShortPacket() {
        requestSpace(2);
        buf.putShort(varLengthIndex, (short) (buf.position() - varLengthIndex - 2));
        return this;
    }

    /**
     * Writes the bytes from the argued buffer into this buffer. This method
     * does not modify the argued buffer, and please do not flip the buffer
     * beforehand.
     *
     * @param from
     *         the argued buffer that bytes will be written from.
     * @return an instance of this data buffer.
     */
    public DataBuffer putBytes(ByteBuffer from) {
        for (int i = 0; i < from.position(); i++) {
            put(from.get(i));
        }
        return this;
    }

    /**
     * Writes the bytes from the argued buffer into this buffer.
     *
     * @param from
     *         the argued buffer that bytes will be written from.
     * @return an instance of this data buffer.
     */
    public DataBuffer putBytes(byte[] from, int size) {
        requestSpace(size);
        buf.put(from, 0, size);
        return this;
    }

    /**
     * Writes the bytes from the argued byte array into this buffer, in
     * reverse.
     *
     * @param data
     *         the data to write to this buffer.
     */
    public DataBuffer putBytesReverse(byte[] data) {
        for (int i = data.length - 1; i >= 0; i--) {
            put(data[i]);
        }
        return this;
    }

    /**
     * Writes the value as a variable amount of bits.
     *
     * @param amount
     *         the amount of bits to write.
     * @param value
     *         the value of the bits.
     * @return an instance of this data buffer.
     * @throws IllegalArgumentException
     *         if the number of bits is not between {@code 1} and {@code 32}
     *         inclusive.
     */
    public DataBuffer putBits(int amount, int value) {
        if (amount < 0 || amount > 32)
            throw new IllegalArgumentException("Number of bits must be " + "between 1 and 32 inclusive.");
        int bytePos = bitIndex >> 3;
        int bitOffset = 8 - (bitIndex & 7);
        bitIndex = bitIndex + amount;
        int requiredSpace = bytePos - buf.position() + 1;
        requiredSpace += (amount + 7) / 8;
        if (buf.remaining() < requiredSpace) {
            ByteBuffer old = buf;
            buf = ByteBuffer.allocate(old.capacity() + requiredSpace);
            old.flip();
            buf.put(old);
        }
        for (; amount > bitOffset; bitOffset = 8) {
            byte tmp = buf.get(bytePos);
            tmp &= ~BIT_MASK[bitOffset];
            tmp |= (value >> (amount - bitOffset)) & BIT_MASK[bitOffset];
            buf.put(bytePos++, tmp);
            amount -= bitOffset;
        }
        if (amount == bitOffset) {
            byte tmp = buf.get(bytePos);
            tmp &= ~BIT_MASK[bitOffset];
            tmp |= value & BIT_MASK[bitOffset];
            buf.put(bytePos, tmp);
        } else {
            byte tmp = buf.get(bytePos);
            tmp &= ~(BIT_MASK[amount] << (bitOffset - amount));
            tmp |= (value & BIT_MASK[amount]) << (bitOffset - amount);
            buf.put(bytePos, tmp);
        }
        return this;
    }

    /**
     * Writes a boolean bit flag.
     *
     * @param flag
     *         the flag to write.
     * @return an instance of this data buffer.
     */
    public DataBuffer putBit(boolean flag) {
        putBits(1, flag ? 1 : 0);
        return this;
    }

    /**
     * Writes a value as a byte.
     *
     * @param value
     *         the value to write.
     * @param type
     *         the value type.
     * @return an instance of this data buffer.
     */
    public DataBuffer put(int value, ValueType type) {
        requestSpace(1);
        switch (type) {
            case A:
                value += 128;
                break;
            case C:
                value = -value;
                break;
            case S:
                value = 128 - value;
                break;
            case STANDARD:
                break;
        }
        buf.put((byte) value);
        return this;
    }

    /**
     * Writes a value as a normal byte.
     *
     * @param value
     *         the value to write.
     * @return an instance of this data buffer.
     */
    public DataBuffer put(int value) {
        put(value, ValueType.STANDARD);
        return this;
    }

    /**
     * Writes a value as a short.
     *
     * @param value
     *         the value to write.
     * @param type
     *         the value type.
     * @param order
     *         the byte order.
     * @return an instance of this data buffer.
     * @throws IllegalArgumentExcpetion
     *         if middle or inverse-middle value types are selected.
     */
    public DataBuffer putShort(int value, ValueType type, ByteOrder order) {
        switch (order) {
            case BIG:
                put(value >> 8);
                put(value, type);
                break;
            case MIDDLE:
                throw new IllegalArgumentException("Middle-endian short is " + "impossible!");
            case INVERSE_MIDDLE:
                throw new IllegalArgumentException("Inverse-middle-endian " + "short is impossible!");
            case LITTLE:
                put(value, type);
                put(value >> 8);
                break;
        }
        return this;
    }

    /**
     * Writes a value as a normal big-endian short.
     *
     * @param value
     *         the value to write.
     * @return an instance of this data buffer.
     */
    public DataBuffer putShort(int value) {
        putShort(value, ValueType.STANDARD, ByteOrder.BIG);
        return this;
    }

    /**
     * Writes a value as a big-endian short.
     *
     * @param value
     *         the value to write.
     * @param type
     *         the value type.
     * @return an instance of this data buffer.
     */
    public DataBuffer putShort(int value, ValueType type) {
        putShort(value, type, ByteOrder.BIG);
        return this;
    }

    /**
     * Writes a value as a standard short.
     *
     * @param value
     *         the value to write.
     * @param order
     *         the byte order.
     * @return an instance of this data buffer.
     */
    public DataBuffer putShort(int value, ByteOrder order) {
        putShort(value, ValueType.STANDARD, order);
        return this;
    }

    /**
     * Writes a value as an integer.
     *
     * @param value
     *         the value to write.
     * @param type
     *         the value type.
     * @param order
     *         the byte order.
     * @return an instance of this data buffer.
     */
    public DataBuffer putInt(int value, ValueType type, ByteOrder order) {
        switch (order) {
            case BIG:
                put(value >> 24);
                put(value >> 16);
                put(value >> 8);
                put(value, type);
                break;
            case MIDDLE:
                put(value >> 8);
                put(value, type);
                put(value >> 24);
                put(value >> 16);
                break;
            case INVERSE_MIDDLE:
                put(value >> 16);
                put(value >> 24);
                put(value, type);
                put(value >> 8);
                break;
            case LITTLE:
                put(value, type);
                put(value >> 8);
                put(value >> 16);
                put(value >> 24);
                break;
        }
        return this;
    }

    /**
     * Writes a value as a standard big-endian integer.
     *
     * @param value
     *         the value to write.
     * @return an instance of this data buffer.
     */
    public DataBuffer putInt(int value) {
        putInt(value, ValueType.STANDARD, ByteOrder.BIG);
        return this;
    }

    /**
     * Writes a value as a big-endian integer.
     *
     * @param value
     *         the value to write.
     * @param type
     *         the value type.
     * @return an instance of this data buffer.
     */
    public DataBuffer putInt(int value, ValueType type) {
        putInt(value, type, ByteOrder.BIG);
        return this;
    }

    /**
     * Writes a value as a standard integer.
     *
     * @param value
     *         the value to write.
     * @param order
     *         the byte order.
     * @return an instance of this data buffer.
     */
    public DataBuffer putInt(int value, ByteOrder order) {
        putInt(value, ValueType.STANDARD, order);
        return this;
    }

    /**
     * Writes a value as a long.
     *
     * @param value
     *         the value to write.
     * @param type
     *         the value type.
     * @param order
     *         the byte order.
     * @return an instance of this data buffer.
     * @throws UnsupportedOperationException
     *         if middle or inverse-middle value types are selected.
     */
    public DataBuffer putLong(long value, ValueType type, ByteOrder order) {
        switch (order) {
            case BIG:
                put((int) (value >> 56));
                put((int) (value >> 48));
                put((int) (value >> 40));
                put((int) (value >> 32));
                put((int) (value >> 24));
                put((int) (value >> 16));
                put((int) (value >> 8));
                put((int) value, type);
                break;
            case MIDDLE:
                throw new UnsupportedOperationException("Middle-endian long " + "is not implemented!");
            case INVERSE_MIDDLE:
                throw new UnsupportedOperationException("Inverse-middle-endian long is not implemented!");
            case LITTLE:
                put((int) value, type);
                put((int) (value >> 8));
                put((int) (value >> 16));
                put((int) (value >> 24));
                put((int) (value >> 32));
                put((int) (value >> 40));
                put((int) (value >> 48));
                put((int) (value >> 56));
                break;
        }
        return this;
    }

    /**
     * Writes a value as a standard big-endian long.
     *
     * @param value
     *         the value to write.
     * @return an instance of this data buffer.
     */
    public DataBuffer putLong(long value) {
        putLong(value, ValueType.STANDARD, ByteOrder.BIG);
        return this;
    }

    /**
     * Writes a value as a big-endian long.
     *
     * @param value
     *         the value to write.
     * @param type
     *         the value type.
     * @return an instance of this data buffer.
     */
    public DataBuffer putLong(long value, ValueType type) {
        putLong(value, type, ByteOrder.BIG);
        return this;
    }

    /**
     * Writes a value as a standard long.
     *
     * @param value
     *         the value to write.
     * @param order
     *         the byte order to write.
     * @return an instance of this data buffer.
     */
    public DataBuffer putLong(long value, ByteOrder order) {
        putLong(value, ValueType.STANDARD, order);
        return this;
    }

    /**
     * Writes a RuneScape string value.
     *
     * @param string
     *         the string to write.
     * @return an instance of this data buffer.
     */
    public DataBuffer putString(String string) {
        for (byte value : string.getBytes()) {
            put(value);
        }
        put(10);
        return this;
    }

    /**
     * Reads a value as a byte.
     *
     * @param signed
     *         if the byte is signed.
     * @param type
     *         the value type.
     * @return the value of the byte.
     */
    public int get(boolean signed, ValueType type) {
        int value = buf.get();
        switch (type) {
            case A:
                value = value - 128;
                break;
            case C:
                value = -value;
                break;
            case S:
                value = 128 - value;
                break;
            case STANDARD:
                break;
        }
        return signed ? value : value & 0xff;
    }

    /**
     * Reads a standard signed byte.
     *
     * @return the value of the byte.
     */
    public int get() {
        return get(true, ValueType.STANDARD);
    }

    /**
     * Reads a standard byte.
     *
     * @param signed
     *         if the byte is signed.
     * @return the value of the byte.
     */
    public int get(boolean signed) {
        return get(signed, ValueType.STANDARD);
    }

    /**
     * Reads a signed byte.
     *
     * @param type
     *         the value type.
     * @return the value of the byte.
     */
    public int get(ValueType type) {
        return get(true, type);
    }

    /**
     * Reads a short value.
     *
     * @param signed
     *         if the short is signed.
     * @param type
     *         the value type.
     * @param order
     *         the byte order.
     * @return the value of the short.
     * @throws UnsupportedOperationException
     *         if middle or inverse-middle value types are selected.
     */
    public int getShort(boolean signed, ValueType type, ByteOrder order) {
        int value = 0;
        switch (order) {
            case BIG:
                value |= get(false) << 8;
                value |= get(false, type);
                break;
            case MIDDLE:
                throw new UnsupportedOperationException("Middle-endian short " + "is impossible!");
            case INVERSE_MIDDLE:
                throw new UnsupportedOperationException("Inverse-middle-endian short is impossible!");
            case LITTLE:
                value |= get(false, type);
                value |= get(false) << 8;
                break;
        }
        return signed ? value : value & 0xffff;
    }

    /**
     * Reads a standard signed big-endian short.
     *
     * @return the value of the short.
     */
    public int getShort() {
        return getShort(true, ValueType.STANDARD, ByteOrder.BIG);
    }

    /**
     * Reads a standard big-endian short.
     *
     * @param signed
     *         if the short is signed.
     * @return the value of the short.
     */
    public int getShort(boolean signed) {
        return getShort(signed, ValueType.STANDARD, ByteOrder.BIG);
    }

    /**
     * Reads a signed big-endian short.
     *
     * @param type
     *         the value type.
     * @return the value of the short.
     */
    public int getShort(ValueType type) {
        return getShort(true, type, ByteOrder.BIG);
    }

    /**
     * Reads a big-endian short.
     *
     * @param signed
     *         if the short is signed.
     * @param type
     *         the value type.
     * @return the value of the short.
     */
    public int getShort(boolean signed, ValueType type) {
        return getShort(signed, type, ByteOrder.BIG);
    }

    /**
     * Reads a signed standard short.
     *
     * @param order
     *         the byte order.
     * @return the value of the short.
     */
    public int getShort(ByteOrder order) {
        return getShort(true, ValueType.STANDARD, order);
    }

    /**
     * Reads a standard short.
     *
     * @param signed
     *         if the short is signed.
     * @param order
     *         the byte order.
     * @return the value of the short.
     */
    public int getShort(boolean signed, ByteOrder order) {
        return getShort(signed, ValueType.STANDARD, order);
    }

    /**
     * Reads a signed short.
     *
     * @param type
     *         the value type.
     * @param order
     *         the byte order.
     * @return the value of the short.
     */
    public int getShort(ValueType type, ByteOrder order) {
        return getShort(true, type, order);
    }

    /**
     * Reads an integer.
     *
     * @param signed
     *         if the short is signed.
     * @param type
     *         the value type.
     * @param order
     *         the byte order.
     * @return the value of the integer.
     */
    public int getInt(boolean signed, ValueType type, ByteOrder order) {
        long value = 0;
        switch (order) {
            case BIG:
                value |= get(false) << 24;
                value |= get(false) << 16;
                value |= get(false) << 8;
                value |= get(false, type);
                break;
            case MIDDLE:
                value |= get(false) << 8;
                value |= get(false, type);
                value |= get(false) << 24;
                value |= get(false) << 16;
                break;
            case INVERSE_MIDDLE:
                value |= get(false) << 16;
                value |= get(false) << 24;
                value |= get(false, type);
                value |= get(false) << 8;
                break;
            case LITTLE:
                value |= get(false, type);
                value |= get(false) << 8;
                value |= get(false) << 16;
                value |= get(false) << 24;
                break;
        }
        return (int) (signed ? value : value & 0xffffffffL);
    }

    /**
     * Reads a signed standard big-endian integer.
     *
     * @return the value of the integer.
     */
    public int getInt() {
        return getInt(true, ValueType.STANDARD, ByteOrder.BIG);
    }

    /**
     * Reads a standard big-endian integer.
     *
     * @param signed
     *         if the short is signed.
     * @return the value of the integer.
     */
    public int getInt(boolean signed) {
        return getInt(signed, ValueType.STANDARD, ByteOrder.BIG);
    }

    /**
     * Reads a signed big-endian integer.
     *
     * @param type
     *         the value type.
     * @return the value of the integer.
     */
    public int getInt(ValueType type) {
        return getInt(true, type, ByteOrder.BIG);
    }

    /**
     * Reads a big-endian integer.
     *
     * @param signed
     *         if the short is signed.
     * @param type
     *         the value type.
     * @return the value of the integer.
     */
    public int getInt(boolean signed, ValueType type) {
        return getInt(signed, type, ByteOrder.BIG);
    }

    /**
     * Reads a signed standard integer.
     *
     * @param order
     *         the byte order.
     * @return the value of the integer.
     */
    public int getInt(ByteOrder order) {
        return getInt(true, ValueType.STANDARD, order);
    }

    /**
     * Reads a standard integer.
     *
     * @param signed
     *         if the short is signed.
     * @param order
     *         the byte order.
     * @return the value of the integer.
     */
    public int getInt(boolean signed, ByteOrder order) {
        return getInt(signed, ValueType.STANDARD, order);
    }

    /**
     * Reads a signed integer.
     *
     * @param type
     *         the value type.
     * @param order
     *         the byte order.
     * @return the value of the integer.
     */
    public int getInt(ValueType type, ByteOrder order) {
        return getInt(true, type, order);
    }

    /**
     * Reads a signed long value.
     *
     * @param type
     *         the value type.
     * @param order
     *         the byte order.
     * @return the value of the integer.
     * @throws UnsupportedOperationException
     *         if middle or inverse-middle value types are selected.
     */
    public long getLong(ValueType type, ByteOrder order) {
        long value = 0;
        switch (order) {
            case BIG:
                value |= (long) get(false) << 56L;
                value |= (long) get(false) << 48L;
                value |= (long) get(false) << 40L;
                value |= (long) get(false) << 32L;
                value |= (long) get(false) << 24L;
                value |= (long) get(false) << 16L;
                value |= (long) get(false) << 8L;
                value |= get(false, type);
                break;
            case INVERSE_MIDDLE:
            case MIDDLE:
                throw new UnsupportedOperationException("Middle and " + "inverse-middle value types not supported!");
            case LITTLE:
                value |= get(false, type);
                value |= (long) get(false) << 8L;
                value |= (long) get(false) << 16L;
                value |= (long) get(false) << 24L;
                value |= (long) get(false) << 32L;
                value |= (long) get(false) << 40L;
                value |= (long) get(false) << 48L;
                value |= (long) get(false) << 56L;
                break;
        }
        return value;
    }

    /**
     * Reads a signed standard big-endian long.
     *
     * @return the value of the long.
     */
    public long getLong() {
        return getLong(ValueType.STANDARD, ByteOrder.BIG);
    }

    /**
     * Reads a signed big-endian long
     *
     * @param type
     *         the value type
     * @return the value of the long.
     */
    public long getLong(ValueType type) {
        return getLong(type, ByteOrder.BIG);
    }

    /**
     * Reads a signed standard long.
     *
     * @param order
     *         the byte order
     * @return the value of the long.
     */
    public long getLong(ByteOrder order) {
        return getLong(ValueType.STANDARD, order);
    }

    /**
     * Reads a RuneScape string value.
     *
     * @return the value of the string.
     */
    public String getString() {
        byte temp;
        StringBuilder b = new StringBuilder();
        while ((temp = (byte) get()) != 10) {
            b.append((char) temp);
        }
        return b.toString();
    }

    /**
     * Reads the amount of bytes into the array, starting at the current
     * position.
     *
     * @param amount
     *         the amount to read.
     * @return a buffer filled with the data.
     */
    public byte[] getBytes(int amount) {
        return getBytes(amount, ValueType.STANDARD);
    }

    /**
     * Reads the amount of bytes into a byte array, starting at the current
     * position.
     *
     * @param amount
     *         the amount of bytes.
     * @param type
     *         the value type of each byte.
     * @return a buffer filled with the data.
     */
    public byte[] getBytes(int amount, ValueType type) {
        byte[] data = new byte[amount];
        for (int i = 0; i < amount; i++) {
            data[i] = (byte) get(type);
        }
        return data;
    }

    /**
     * Reads the amount of bytes from the buffer in reverse, starting at
     * current
     * position + amount and reading in reverse until the current position.
     *
     * @param amount
     *         the amount of bytes to read.
     * @param type
     *         the value type of each byte.
     * @return a buffer filled with the data.
     */
    public byte[] getBytesReverse(int amount, ValueType type) {
        byte[] data = new byte[amount];
        int dataPosition = 0;
        for (int i = buf.position() + amount - 1; i >= buf.position(); i--) {
            int value = buf.get(i);
            switch (type) {
                case A:
                    value -= 128;
                    break;
                case C:
                    value = -value;
                    break;
                case S:
                    value = 128 - value;
                    break;
                case STANDARD:
                    break;
            }
            data[dataPosition++] = (byte) value;
        }
        return data;
    }

    /**
     * Gets the backing byte buffer used to read and write data.
     *
     * @return the backing byte buffer used to read and write data.
     */
    public ByteBuffer buffer() {
        return buf;
    }
}
