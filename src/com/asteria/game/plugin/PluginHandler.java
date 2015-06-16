package com.asteria.game.plugin;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.combat.CombatStrategy;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.Minigame;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.character.player.skill.action.SkillAction;
import com.asteria.utility.LoggerUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * The plugin handler that contains data structures to hold and functions to
 * manage the held plugin listeners. Plugin listeners can be initialized,
 * submitted, and executed from this class.
 * <p>
 * <p>
 * The data structures that hold the plugin listeners are not thread safe which
 * means that plugin listeners should only be executed on the main game thread.
 *
 * @author lare96 <http://github.com/lare96>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class PluginHandler {

    /**
     * The map of {@link PluginContext}s mapped to their collection of
     * {@link PluginListener}s.
     */
    private final Multimap<Class<? extends PluginContext>, PluginListener> plugins = ArrayListMultimap.create();

    /**
     * The logger that will print important information.
     */
    private final Logger logger = LoggerUtils.getLogger(PluginHandler.class);

    /**
     * Initializes the bootstrap, effectively loading all plugin listeners in
     * the {@code ./plugins/} directory.
     */
    public void init() {
        try {
            Class<?> c = Class.forName("plugin.Bootstrap");
            Constructor<?> bootstrap = c.getConstructor(Logger.class);
            bootstrap.newInstance(logger);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error has occured while initializing the Bootstrap!", e);
        }
    }

    /**
     * Executes all of the {@link PluginListener}s for {@code type} in
     * {@code context}. This function will throw a {@link NullPointerException}
     * if no plugin listeners are available for the specified plugin context
     * type.
     *
     * @param player
     *            the player that these plugin listeners will be executed for.
     * @param type
     *            the context type to grab the plugins listeners for.
     * @param context
     *            the plugin context that will supply data to the plugin
     *            listeners.
     */
    public void execute(Player player, Class<? extends PluginContext> type, PluginContext context) {
        Collection<PluginListener> collection = plugins.get(type);
        if (collection == null)
            throw new NullPointerException("No plugin listeners exist for this plugin signature!");
        collection.forEach(c -> c.run(player, context));
    }

    /**
     * Submits a new plugin listener into the multimap of plugins. This function
     * will throw a {@link PluginSignatureException} if there is no plugin
     * signature for {@code clazz}.
     *
     * @param clazz
     *            the class of the plugin listener that will be submitted.
     */
    public void submit(Class<?> clazz) {
        try {
            PluginSignature type = clazz.getAnnotation(PluginSignature.class);
            if (type == null)
                throw new PluginSignatureException(clazz);
            for (Class<? extends PluginContext> c : type.value()) {
                if (c == Minigame.class) {
                    MinigameHandler.MINIGAMES.add((Minigame) clazz.newInstance());
                    return;
                } else if (c == SkillAction.class) {
                    return; // We don't need to cache skills.
                } else if (c == CombatStrategy.class) {
                    CombatStrategy combat = (CombatStrategy) clazz.newInstance();
                    for (int npc : combat.getNpcs())
                        Combat.STRATEGIES.put(npc, combat);
                    return;
                }
                plugins.put(c, (PluginListener<?>) clazz.newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the backing map of all its plugin entries. When this method
     * returns there will be no active plugin listeners.
     */
    public void clear() {
        plugins.clear();
    }
}
