package com.asteria.game.character.player.serialize;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.asteria.game.GameService;
import com.asteria.utility.LoggerUtils;

/**
 * An extremely important task that will "clean up" the player cache every
 * {@code 5} minutes. When the cache is cleaned, it will discard entries in the
 * cache that have been present for the previously specified time or longer.
 * Character file data can consume lots of memory, so this class ensures that
 * memory is being freed at a consistent rate.
 * <p>
 * <p>
 * Caches are thread safe, so this task may be ran sequentially on one other
 * thread or concurrently on other threads.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class PlayerCacheCleanUpTask implements Runnable {

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(PlayerCacheCleanUpTask.class);

    /**
     * The private constructor that creates {@link PlayerCacheCleanUpTask}. We
     * keep this private so that we can only create new instances of this class
     * inside this class.
     */
    private PlayerCacheCleanUpTask() {

    }

    @Override
    public void run() {
        try {
            PlayerSerialization.PLAYER_CACHE.cleanUp();
        } catch (Throwable t) {

            // Clean up fail, discard all entries.
            logger.log(Level.SEVERE, "The player cache failed to clean up!", t);
            PlayerSerialization.PLAYER_CACHE.invalidateAll();
        }
    }

    /**
     * Initializes this {@link PlayerCacheCleanUpTask} to be ran every {@code 5}
     * minutes by the logic service.
     */
    public static void init() {
        GameService.getLogicService().scheduleAtFixedRate(new PlayerCacheCleanUpTask(), 5, 5, TimeUnit.MINUTES);
    }
}
