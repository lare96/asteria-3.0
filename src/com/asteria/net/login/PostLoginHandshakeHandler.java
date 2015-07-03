package com.asteria.net.login;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.math.BigInteger;
import java.util.List;

import com.asteria.net.ISAACCipher;
import com.asteria.net.NetworkConstants;
import com.asteria.net.message.LoginDetailsMessage;
import com.asteria.net.message.MessageBuilder;

/**
 * The {@link ByteToMessageDecoder} implementation that will manage the
 * post-handshake section of the login protocol.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class PostLoginHandshakeHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // Read the login type, validate it.
        if (in.readableBytes() < 2)
            throw new InvalidLoginException(ctx.channel(), "Not enough bytes to read.");
        int type = in.readByte();
        if (type != 16 && type != 18)
            throw new InvalidLoginException(ctx.channel(), "Invalid login type [" + type + "]");

        // Decode the block length, validate RSA block size.
        int blockLength = in.readUnsignedByte();
        int loginEncryptPacketSize = blockLength - (36 + 1 + 1 + 2);
        if (loginEncryptPacketSize <= 0)
            throw new InvalidLoginException(ctx.channel(), "Invalid RSA packet size [" + loginEncryptPacketSize + "]");
        if (in.readableBytes() < blockLength)
            throw new InvalidLoginException(ctx.channel(), "Not enough bytes to read for this block.");

        // Read the client version, validate it.
        in.readByte();
        int version = in.readShort();
        if (version != 317)
            throw new InvalidLoginException(ctx.channel(), "Invalid client version [" + version + "]");

        // Read and ignore the data for CRC keys.
        in.readByte();
        for (int i = 0; i < 9; i++)
            in.readInt();

        // Either decode RSA, or proceed normally depending on the network
        // settings.
        loginEncryptPacketSize--;
        in.readByte();
        String username = null;
        String password = null;
        ISAACCipher encryptor = null;
        ISAACCipher decryptor = null;
        if (NetworkConstants.DECODE_RSA) {
            byte[] encryptionBytes = new byte[loginEncryptPacketSize];
            in.readBytes(encryptionBytes);
            ByteBuf rsaBuffer = Unpooled.wrappedBuffer(new BigInteger(encryptionBytes).modPow(NetworkConstants.RSA_EXPONENT,
                NetworkConstants.RSA_MODULUS).toByteArray());
            int rsaOpcode = rsaBuffer.readByte();
            if (rsaOpcode != 10)
                throw new InvalidLoginException(ctx.channel(), "Invalid RSA opcode [" + rsaOpcode + "]");
            long clientHalf = rsaBuffer.readLong();
            long serverHalf = rsaBuffer.readLong();
            int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
            decryptor = new ISAACCipher(isaacSeed);
            for (int i = 0; i < isaacSeed.length; i++)
                isaacSeed[i] += 50;
            encryptor = new ISAACCipher(isaacSeed);
            rsaBuffer.readInt();
            MessageBuilder db = MessageBuilder.create(rsaBuffer);
            username = db.getString();
            password = db.getString();
        } else {
            in.readByte();
            long clientHalf = in.readLong();
            long serverHalf = in.readLong();
            int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
            decryptor = new ISAACCipher(isaacSeed);
            for (int i = 0; i < isaacSeed.length; i++)
                isaacSeed[i] += 50;
            encryptor = new ISAACCipher(isaacSeed);
            in.readInt();
            MessageBuilder db = MessageBuilder.create(in);
            username = db.getString().toLowerCase().replaceAll("_", " ").trim();
            password = db.getString().toLowerCase();
        }

        // Finally, we've decoded all the data we need for the final response of
        // the login protocol. Here we send it as an upstream Netty message to
        // be handled in the PlayerIO class.
        out.add(new LoginDetailsMessage(ctx, username, password, encryptor, decryptor));
    }
}
