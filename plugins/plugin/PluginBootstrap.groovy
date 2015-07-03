package plugin

import java.lang.reflect.Modifier
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.logging.Logger

import com.asteria.game.World
import com.asteria.game.character.player.minigame.MinigameHandler
import com.asteria.game.plugin.PluginListener
import com.asteria.game.plugin.PluginSignature
import com.asteria.game.plugin.PluginSignatureException

final class PluginBootstrap {

    private static int count

    PluginBootstrap(Logger logger) {
        clear()
        provide()
        logger.info "The Bootstrap has been initialized with ${count} plugins!"
    }

    static def clear() {
        count = 0
        World.plugins.clear()
        MinigameHandler.MINIGAMES.clear()
    }

    static def provide() {
        List<Class<?>> listeners = init "./bin/plugin/"
        listeners.each { World.plugins.submit it }
    }

    static List<Class<?>> init(dir) {
        List<Class<?>> listeners = new LinkedList<>()
        Files.walkFileTree(Paths.get(dir),
                new SimpleFileVisitor<Path>() {
                    @Override
                    FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                        File file = path.toFile()
                        String name = file.name
                        if(!name.endsWith(".class") || name.contains("\$"))
                            return FileVisitResult.CONTINUE
                        String formatted = file.path.replace("\\", ".").substring(0, file.path.lastIndexOf(".")).replace("bin.", "").replace("..", "")
                        Class<?> c = Class.forName(formatted)
                        c.interfaces.each {
                            if(it == PluginListener.class && c.getAnnotation(PluginSignature.class) == null)
                                throw new PluginSignatureException(c)
                        }
                        if (!Modifier.isAbstract(c.modifiers) && !Modifier.isInterface(c.modifiers) && c.getAnnotations().length > 0) {
                            listeners.add(c)
                            count++
                        }
                        return FileVisitResult.CONTINUE
                    }
                })
        return listeners
    }
}