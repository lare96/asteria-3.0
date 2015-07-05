package com.asteria.game.character.npc.drop;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemNode;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.game.item.ItemNodeStatic;
import com.asteria.utility.Chance;

/**
 * The static-utility class that manages all of the {@link NpcDropTable}s
 * including the process of dropping the items when an {@link Npc} is killed.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class NpcDropManager {

    /**
     * The {@link EnumMap} consisting of the cached common {@link NpcDrop}s used
     * across many {@link NpcDropTable}s.
     */
    public static final EnumMap<NpcDropCache, NpcDrop[]> COMMON = new EnumMap<>(NpcDropCache.class);

    /**
     * The {@link HashMap} that consists of the drops for {@link Npc}s.
     */
    public static final Map<Integer, NpcDropTable> TABLES = new HashMap<>();

    /**
     * The default drop table for all {@link Npc}s.
     */
    private static final NpcDropTable DEFAULT = new NpcDropTable(new NpcDrop[] { new NpcDrop(526, 1, 1, Chance.ALWAYS) },
        new NpcDropCache[] { NpcDropCache.LOW_RUNES });

    /**
     * Drops the items in {@code victim}s drop table for {@code killer}. If the
     * killer doesn't exist, the items are dropped for everyone to see.
     * 
     * @param killer
     *            the killer, may or may not exist.
     * @param victim
     *            the victim that was killed.
     */
    public static void dropItems(Optional<Player> killer, Npc victim) {
        NpcDropTable table = TABLES.getOrDefault(victim.getId(), DEFAULT);
        List<Item> dropItems = table.toItems(killer.orElse(null));
        for (Item drop : dropItems) {
            if (drop == null)
                continue;
            ItemNodeManager.register(!killer.isPresent() ? new ItemNodeStatic(drop, victim.getPosition()) : new ItemNode(drop, victim
                .getPosition(), killer.get()));
        }
        killer.ifPresent(k -> MinigameHandler.search(k).ifPresent(m -> m.onKill(k, victim)));
    }
}
