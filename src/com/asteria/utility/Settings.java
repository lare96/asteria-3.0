package com.asteria.utility;

import java.math.BigInteger;

import com.asteria.game.item.Item;
import com.asteria.game.location.Position;

/**
 * The static utility class that holds various constants used throughout the
 * server.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class Settings {

    /**
     * The name of this server.
     */
    public static final String NAME = "Asteria 3.0";

    /**
     * The port this server will bind to.
     */
    public static final int PORT = 43594;

    /**
     * Determines if debugging messages should be printed or not.
     */
    public static final boolean DEBUG = true;

    /**
     * Players that don't have a username equal to {@code SOCKET_FLOOD_USERNAME}
     * are moved within 200 squares of the home area on login.
     */
    public static final boolean SOCKET_FLOOD = false;

    /**
     * Players that don't have this username are moved within 200 squares of the
     * home area on login if {@code SOCKET_FLOOD} is flagged.
     */
    public static final String SOCKET_FLOOD_USERNAME = "lare96";

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
     * The position new players will be moved to.
     */
    public static final Position STARTING_POSITION = new Position(3093, 3244);

    /**
     * The maximum amount of packets that can be decoded in one sequence.
     */
    public static final int PACKET_LIMIT = 15;

    /**
     * The items that are not allowed to be bought or sold in shops.
     */
    public static final int[] BANNED_SHOP_ITEMS = { 995 };

    /**
     * The items that are not allowed to be traded.
     */
    public static final int[] ITEM_UNTRADEABLE = {};

    /**
     * The items that are obtained through caskets.
     */
    public static final Item[] CASKET_ITEMS = { new Item(1061), new Item(592), new Item(1059), new Item(995, 100000),
            new Item(4212), new Item(995, 50000), new Item(401), new Item(995, 150000), new Item(407) };

    /**
     * The message that will be sent on every login.
     */
    public static final String WELCOME_MESSAGE = "Welcome to Asteria 3.0!";

    /**
     * The items received when a player logs in for the first time.
     */
    public static final Item[] STARTER_PACKAGE = { new Item(995, 10000) };

    /**
     * Messages chosen a random to be sent to a player that has killed another
     * player. {@code -victim-} is replaced with the player's name that was
     * killed, while {@code -killer-} is replaced with the killer's name.
     */
    public static final String[] DEATH_MESSAGES = { "You have just killed -victim-!",
            "You have completely slaughtered -victim-!", "I bet -victim- will think twice before messing with you again!",
            "Your killing style is impeccable, -victim- didn't stand a chance!" };

    /**
     * The items that will be kept on death regardless of if a player is skulled
     * or not.
     */
    public static final int[] KEEP_ON_DEATH = { 6570 };

    /**
     * The default constructor.
     * 
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private Settings() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }
}
