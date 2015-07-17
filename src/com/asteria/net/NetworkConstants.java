package com.asteria.net;

import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector.Level;

import java.math.BigInteger;

import com.asteria.net.message.InputMessageListener;
import com.google.common.collect.ImmutableList;

/**
 * The class that contains a collection of constants related to the network.
 * This class serves no other purpose than to hold constants.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class NetworkConstants {

    /**
     * The port that this server will bind to.
     */
    public static final int PORT = 43594;

    /**
     * The input timeout value that determines how long a session can go without
     * reading data from the client in {@code SECONDS}.
     */
    public static final int INPUT_TIMEOUT = 5;

    /**
     * The size of a variable sized packet.
     */
    public static final int VAR_SIZE = -1;

    /**
     * The size of a variable sized short packet.
     */
    public static final int VAR_SIZE_SHORT = -2;

    /**
     * The resource leak detection level when not running the server in debug
     * mode.
     */
    public static final Level RESOURCE_DETECTION = Level.DISABLED;

    /**
     * An array of the message opcodes mapped to their respective listeners.
     */
    public static final InputMessageListener[] MESSAGES = new InputMessageListener[257];

    /**
     * An array of message opcodes mapped to their respective sizes.
     */
    public static final int MESSAGE_SIZES[] = new int[257];

    /**
     * The {@link AttributeKey} value that is used to retrieve the session
     * instance from the attribute map of a {@link Channel}.
     */
    public static final AttributeKey<PlayerIO> SESSION_KEY = AttributeKey.valueOf("session.KEY");

    /**
     * The throttle interval for incoming connections accepted by the
     * {@link ConnectionHandler}.
     */
    public static final long CONNECTION_INTERVAL = 1000;

    /**
     * The maximum amount of connections that can be active at a time, or in
     * other words how many clients can be logged in at once per connection.
     */
    public static final int CONNECTION_AMOUNT = 1;

    /**
     * Determines if RSA should be decoded in the login block.
     */
    public static final boolean DECODE_RSA = true;

    /**
     * The private RSA modulus and exponent values.
     */
    public static final BigInteger RSA_MODULUS = new BigInteger(
        "94306533927366675756465748344550949689550982334568289470527341681445613288505954291473168510012417401156971344988779343797488043615702971738296505168869556915772193568338164756326915583511871429998053169912492097791139829802309908513249248934714848531624001166946082342750924060600795950241816621880914628143"),
        RSA_EXPONENT = new BigInteger(
            "58942123322685908809689084302625256728774551587748168286651364002223076520293763732441711633712538400732268844501356343764421742749024359146319836858905124072353297696448255112361453630421295623429362610999525258756790291981270575779800669035081348981858658116089267888135561190976376091835832053427710797233");

    /**
     * The maximum amount of messages that can be decoded in one sequence.
     */
    public static final int DECODE_LIMIT = 15;

    /**
     * The list of exceptions that are ignored and discarded by the
     * {@link NetworkChannelHandler}.
     */
    public static final ImmutableList<String> IGNORED_EXCEPTIONS = ImmutableList.of(
        "An existing connection was forcibly closed by the remote host",
        "An established connection was aborted by the software in your host machine");

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private NetworkConstants() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }
}
