package com.asteria.game;

import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.serialize.PlayerSerializationCache;
import com.asteria.game.item.Item;
import com.asteria.game.location.Location;
import com.asteria.game.location.Position;
import com.asteria.game.location.SquareLocation;
import com.google.common.collect.ImmutableList;

/**
 * The class that contains a collection of constants related to the game. This
 * class serves no other purpose than to hold constants.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class GameConstants {

    /**
     * The flag that determines if the {@link PlayerSerializationCache} should
     * be periodically forced to clean itself rather than doing it manually on
     * read and writes. Making this value {@code true} will keep memory usage
     * lower, at the cost of increased CPU usage (and vice-versa).
     */
    public static final boolean CLEAN_CACHE = false;

    /**
     * The default time that all utility threads will go idle on after not
     * receiving any tasks.
     */
    public static final int THREAD_TIMEOUT = 45;

    /**
     * The cycle rate of the {@link GameService}.
     */
    public static final int CYCLE_RATE = 600;

    /**
     * How long the player will stay logged in for after they have x-logged
     * during combat.
     */
    public static final int INVALID_LOGOUT_SECONDS = 90;

    /**
     * The flag that determines if processing should be parallelized, improving
     * the performance of the server times {@code n} (where
     * {@code n = Runtime.getRuntime().availableProcessors()}) at the cost of
     * substantially more CPU usage.
     */
    public static final boolean CONCURRENCY = (Runtime.getRuntime().availableProcessors() > 1);

    /**
     * The maximum amount of players that can be logged in on a single game
     * sequence.
     */
    public static final int LOGIN_THRESHOLD = 50;

    /**
     * The maximum amount of players that can be logged in on a single game
     * sequence.
     */
    public static final int LOGOUT_THRESHOLD = 50;

    /**
     * The absolute distance that players must be within to be targeted by
     * aggressive {@link Npc}s.
     */
    public static final int TARGET_DISTANCE = 6;

    /**
     * The maximum amount of drops that can be rolled from the unique table.
     */
    public static final int DROP_THRESHOLD = 2;

    /**
     * The time in seconds that has to be spent in a region before {@link Npc}s
     * stop acting aggressive towards a specific {@link Player}.
     */
    public static final int TOLERANCE_SECONDS = 600;

    /**
     * The collection of locations representing the wilderness areas.
     */
    public static final ImmutableList<Location> WILDERNESS = ImmutableList.of(new SquareLocation(2941, 3518, 3392, 3966, 0));

    /**
     * The collection of locations representing multi-combat areas.
     */
    public static final ImmutableList<Location> MULTIPLE_COMBAT = ImmutableList.of();

    /**
     * The position new players will be moved to.
     */
    public static final Position STARTING_POSITION = new Position(3093, 3244);

    /**
     * The items that are not allowed to be bought or sold in shops.
     */
    public static final int[] BANNED_SHOP_ITEMS = { 995 };

    /**
     * The items that are not allowed to be traded.
     */
    public static final int[] ITEM_UNTRADEABLE = { 6570 };

    /**
     * The items that are obtained through caskets.
     */
    public static final Item[] CASKET_ITEMS = { new Item(1061), new Item(592), new Item(1059), new Item(995, 100000), new Item(4212),
            new Item(995, 50000), new Item(401), new Item(995, 150000), new Item(407) };

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
    public static final String[] DEATH_MESSAGES = { "You have just killed -victim-!", "You have completely slaughtered -victim-!",
            "I bet -victim- will think twice before messing with you again!",
            "Your killing style is impeccable, -victim- didn't stand a chance!" };

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private GameConstants() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }
}
