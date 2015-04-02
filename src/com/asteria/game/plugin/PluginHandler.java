package com.asteria.game.plugin;

import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.combat.CombatStrategy;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.minigame.Minigame;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.character.player.skill.action.SkillAction;
import com.asteria.utility.LoggerUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The plugin handler that contains data structures to hold and functions to
 * manage the held plugin listeners. Plugin listeners can be initialized,
 * submitted, and executed from this class.
 * <p>
 * <p>
 * The data structures that hold the plugin listeners are not thread safe
 * which means that plugin listeners should only be executed on the main game
 * thread.
 *
 * @author lare96 <http://github.com/lare96>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class PluginHandler {

    /**
     * The map of {@link PluginContext}s mapped to their collection of
     * {@link PluginListener}s.
     */
    public static final Multimap<Class<? extends PluginContext>,
            PluginListener> PLUGINS = ArrayListMultimap.create();

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(PluginHandler.class);

    /**
     * Initializes the bootstrap, effectively loading all plugin listeners in
     * the {@code ./plugins/} directory.
     */
    public static void init() {
        try {
            Class<?> c = Class.forName("plugin.Bootstrap");
            Constructor<?> bootstrap = c.getConstructor(Logger.class);
            bootstrap.newInstance(logger);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error has occured while initializing" +
                    " the Bootstrap!", e);
        }
    }

    /**
     * Executes all of the {@link PluginListener}s for {@code type} in
     * {@code context}. This function will throw a {@link NullPointerException}
     * if no plugin listeners are available for the specified plugin context
     * type.
     *
     * @param player
     *         the player that these plugin listeners will be executed for.
     * @param type
     *         the context type to grab the plugins listeners for.
     * @param context
     *         the plugin context that will supply data to the plugin
     *         listeners.
     */
    public static void execute(Player player, Class<? extends PluginContext>
            type, PluginContext context) {
        Collection<PluginListener> collection = PLUGINS.get(type);
        if (collection == null)
            throw new NullPointerException("No plugin listeners exist for " +
                    "this plugin signature!");
        collection.forEach(c -> c.run(player, context));
    }

    /**
     * Submits a new plugin listener into the multimap of plugins. This
     * function will throw a {@link PluginSignatureException} if there is no
     * plugin signature for {@code clazz}.
     *
     * @param clazz
     *         the class of the plugin listener that will be submitted.
     */
    public static void submit(Class<?> clazz) {
        try {
            PluginSignature type = clazz.getAnnotation(PluginSignature.class);
            if (type == null) {
                throw new PluginSignatureException(clazz);
            }
            else if (type.value() == Minigame.class) {
                MinigameHandler.MINIGAMES.add((Minigame) clazz.newInstance());
                return;
            }
            else if (type.value() == SkillAction.class) {
                return; // We don't need to cache skills.
            }
            else if (type.value() == CombatStrategy.class) {
                CombatStrategy combat = (CombatStrategy) clazz.newInstance();
                for (int npc : combat.getNpcs())
                    Combat.STRATEGIES.put(npc, combat);
                return;
            }
            PLUGINS.put(type.value(), (PluginListener<?>) clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
