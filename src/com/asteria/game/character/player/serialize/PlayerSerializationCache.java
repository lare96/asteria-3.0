package com.asteria.game.character.player.serialize;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.asteria.game.GameService;
import com.asteria.utility.LoggerUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonObject;

/**
 * The wrapper for the cache that will store character files on logout for later
 * use. We cache character files on logout for {@code 15} minutes so that if the
 * player logs out within that time period, we don't have to reload the
 * character file. Many benefits can be seen with this on larger servers
 * especially, where players are constantly logging in and out within short
 * periods of time.
 * <p>
 * <p>
 * Caches are thread safe, so functions within this class can be executed within
 * multiple threads.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class PlayerSerializationCache implements Runnable {

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(PlayerSerializationCache.class);

    /**
     * The collection character files that have been cached by the
     * {@link PlayerSerializer}. These character files will be removed from the
     * cache {@code 15} minutes after they've been added to free up memory.
     */
    private final Cache<Long, JsonObject> cache = CacheBuilder.newBuilder().initialCapacity(100).expireAfterWrite(15,
        TimeUnit.MINUTES).concurrencyLevel(2).build();

    /**
     * The flag that determines if entries should be automatically invalidated.
     */
    private final boolean automatic;

    /**
     * Creates a new {@link PlayerSerializationCache} with {@code automatic}
     * determining the invalidation policy.
     * 
     * @param automatic
     *            determines if entries should be automatically invalidated with
     *            the logic service, or if the cache should manually invalidate
     *            entries upon reads and writes.
     */
    public PlayerSerializationCache(boolean automatic) {
        this.automatic = automatic;
    }

    @Override
    public void run() {
        try {
            cache.cleanUp();
        } catch (Throwable t) {

            // Clean up fail, discard all entries.
            logger.log(Level.SEVERE, "The player cache failed to clean up!", t);
            cache.invalidateAll();
        }
    }

    /**
     * Initializes this {@link PlayerSerializationCache} task to be ran every
     * {@code 15} minutes by the logic service, only if automatic invalidation
     * is enabled.
     */
    public void init() {
        if (automatic)
            GameService.getLogicService().scheduleAtFixedRate(this, 15, 15, TimeUnit.MINUTES);
    }

    /**
     * Inserts an entry for {@code value} with {@code data} into this player
     * serialization cache.
     * 
     * @param value
     *            the username hash of the player.
     * @param data
     *            the data for the character file.
     */
    public void add(long value, JsonObject data) {
        cache.put(value, data);
    }

    /**
     * Retrieves the character file data for {@code value} wrapped in an
     * optional.
     * 
     * @param value
     *            the username hash of the player.
     * @return the data wrapped in an optional if present, or an empty optional
     *         if not present.
     */
    public Optional<JsonObject> get(long value) {
        return Optional.ofNullable(cache.getIfPresent(value));
    }

    /**
     * Determines if entries should be automatically invalidated.
     * 
     * @return {@code true} if there is automatic invalidation, {@code false}
     *         otherwise.
     */
    public boolean isAutomatic() {
        return automatic;
    }
}
