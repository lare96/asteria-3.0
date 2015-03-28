package com.asteria.game.service;

import com.asteria.game.World;
import com.asteria.game.character.npc.NpcUpdating;
import com.asteria.game.character.player.PlayerUpdating;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

/**
 * The concurrent update service that will execute the update sequence in
 * parallel using {@link Runtime#availableProcessors()} threads. If the hosting
 * computer has more than one core, is it guaranteed that this update service
 * will perform better than {@link SequentialUpdateService}.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ConcurrentUpdateService implements Runnable {

    /**
     * The phaser keeps the entire update sequence in proper synchronization
     * with the main game thread.
     */
    private static Phaser synchronizer = new Phaser(1);

    /**
     * The executor that allows us to utilize multiple threads to update in
     * parallel.
     */
    private static ExecutorService updateService = ConcurrentUpdateService
            .create();

    @Override
    public void run() {

        // Sequence movement and perform sequential processing for players.
        World.getPlayers().forEach(player -> {
            try {
                player.sequence();
                player.getMovementQueue().sequence();
            } catch (Exception e) {
                e.printStackTrace();
                World.getPlayers().remove(player);
            }
        });

        // Sequence movement and perform sequential processing for npcs.
        World.getNpcs().forEach(npc -> {
            try {
                npc.sequence();
                npc.getMovementQueue().sequence();
            } catch (Exception e) {
                e.printStackTrace();
                World.getNpcs().remove(npc);
            }
        });

        // Update players for players, and npcs for players.
        synchronizer.bulkRegister(World.getPlayers().size());
        World.getPlayers().forEach(player -> updateService.execute(() -> {
            synchronized (player) {
                try {
                    PlayerUpdating.update(player);
                    NpcUpdating.update(player);
                } catch (Exception e) {
                    e.printStackTrace();
                    World.getPlayers().remove(player);
                } finally {
                    synchronizer.arriveAndDeregister();
                }
            }
        }));
        synchronizer.arriveAndAwaitAdvance();

        // Prepare players for the next update sequence.
        synchronizer.bulkRegister(World.getPlayers().size());
        World.getPlayers().forEach(player -> updateService.execute(() -> {
            synchronized (player) {
                try {
                    player.reset();
                    player.setCachedUpdateBlock(null);
                    player.getSession().getPacketCount().set(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    World.getPlayers().remove(player);
                } finally {
                    synchronizer.arriveAndDeregister();
                }
            }
        }));
        synchronizer.arriveAndAwaitAdvance();

        // Prepare npcs for the next update sequence.
        synchronizer.bulkRegister(World.getNpcs().size());
        World.getNpcs().forEach(npc -> updateService.execute(() -> {
            synchronized (npc) {
                try {
                    npc.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                    World.getNpcs().remove(npc);
                } finally {
                    synchronizer.arriveAndDeregister();
                }
            }
        }));
        synchronizer.arriveAndAwaitAdvance();
    }

    /**
     * Creates and configures the update service that will execute updating in
     * parallel for characters. The returned executor is <b>unconfigurable</b>
     * meaning it's configuration can no longer be modified.
     *
     * @return the newly created and configured update service.
     */
    private static ExecutorService create() {
        int nThreads = Runtime.getRuntime().availableProcessors();
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor)
                Executors.newScheduledThreadPool(nThreads);
        executor.setRejectedExecutionHandler(new CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat
                ("UpdateServiceThread").build());
        return Executors.unconfigurableScheduledExecutorService(executor);
    }
}