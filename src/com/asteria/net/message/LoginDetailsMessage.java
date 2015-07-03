package com.asteria.net.message;

import io.netty.channel.ChannelHandlerContext;

import com.asteria.net.ISAACCipher;

/**
 * The {@link Message} implementation that contains data used for the final
 * portion of the login protocol.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class LoginDetailsMessage implements Message {

    /**
     * The {@link ChannelHandlerContext} that holds our {@link Channel}
     * instance.
     */
    private final ChannelHandlerContext ctx;

    /**
     * The username of the player.
     */
    private final String username;

    /**
     * The password of the player.
     */
    private final String password;

    /**
     * The encryptor for encrypting messages.
     */
    private final ISAACCipher encryptor;

    /**
     * The decryptor for decrypting messages.
     */
    private final ISAACCipher decryptor;

    /**
     * Creates a new {@link LoginDetailsMessage}.
     *
     * @param ctx
     *            the {@link ChannelHandlerContext} that holds our
     *            {@link Channel} instance.
     * @param username
     *            the username of the player.
     * @param password
     *            the password of the player.
     * @param encryptor
     *            the encryptor for encrypting messages.
     * @param decryptor
     *            the decryptor for decrypting messages.
     */
    public LoginDetailsMessage(ChannelHandlerContext ctx, String username, String password, ISAACCipher encryptor, ISAACCipher decryptor) {
        this.ctx = ctx;
        this.username = username;
        this.password = password;
        this.encryptor = encryptor;
        this.decryptor = decryptor;
    }

    /**
     * Gets the {@link ChannelHandlerContext} that holds our {@link Channel}
     * instance.
     * 
     * @return the channel handler context.
     */
    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    /**
     * Gets the username of the player.
     * 
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the player.
     * 
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the encryptor for encrypting messages.
     * 
     * @return the encryptor.
     */
    public ISAACCipher getEncryptor() {
        return encryptor;
    }

    /**
     * Gets the decryptor for decrypting messages.
     * 
     * @return the decryptor.
     */
    public ISAACCipher getDecryptor() {
        return decryptor;
    }
}
