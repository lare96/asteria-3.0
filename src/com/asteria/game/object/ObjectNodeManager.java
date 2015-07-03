package com.asteria.game.object;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import com.asteria.game.World;
import com.asteria.game.character.player.Player;
import com.asteria.game.location.Position;
import com.asteria.task.Task;

/**
 * The node manager that manages all registered object nodes.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ObjectNodeManager {

    /**
     * The hash collection of registered objects.
     */
    public static final Set<ObjectNode> OBJECTS = new HashSet<>();

    /**
     * The hash collection of objects that will be removed.
     */
    public static final Set<Position> REMOVE_OBJECTS = new HashSet<>();

    /**
     * The method that attempts to register {@code object}.
     *
     * @param object
     *            the object to attempt to register.
     * @return {@code true} if the object was registered, {@code false}
     *         otherwise.
     */
    public static boolean register(ObjectNode object) {
        if (object.isRegistered())
            return false;
        unregister(object.getPosition());
        if (OBJECTS.add(object)) {
            object.setRegistered(true);
            object.create();
            return true;
        }
        return false;
    }

    /**
     * The method that attempts to register {@code object} and then execute
     * {@code action} after specified amount of ticks.
     *
     * @param object
     *            the object to attempt to register.
     * @param ticks
     *            the amount of ticks to unregister this object after.
     * @return {@code true} if the object was registered, {@code false}
     *         otherwise.
     */
    public static boolean register(ObjectNode object, int ticks, Consumer<ObjectNode> action) {
        if (register(object)) {
            World.submit(new Task(ticks, false) {
                @Override
                public void execute() {
                    action.accept(object);
                }
            });
            return true;
        }
        return false;
    }

    /**
     * The method that attempts to register {@code object} for the specified
     * amount of ticks.
     *
     * @param object
     *            the object to attempt to register.
     * @param ticks
     *            the amount of ticks to unregister this object after.
     * @return {@code true} if the object was registered, {@code false}
     *         otherwise.
     */
    public static boolean register(ObjectNode object, int ticks) {
        return register(object, ticks, n -> {
            if (!unregister(n))
                throw new IllegalStateException(n + " could not be removed " + "after " + ticks + " ticks!");
        });
    }

    /**
     * The method that attempts to unregister {@code object}.
     *
     * @param object
     *            the object to attempt to unregister.
     * @return {@code true} if the object was unregistered, {@code false}
     *         otherwise.
     */
    public static boolean unregister(ObjectNode object) {
        if (!object.isRegistered())
            return false;
        return unregister(object.getPosition());
    }

    /**
     * The method that attempts to unregister the object on {@code position}.
     *
     * @param object
     *            the object to attempt to unregister.
     * @return {@code true} if the object was unregistered, {@code false}
     *         otherwise.
     */
    public static boolean unregister(Position position) {
        for (Iterator<ObjectNode> iter = OBJECTS.iterator(); iter.hasNext();) {
            ObjectNode next = iter.next();
            if (next.getPosition().equals(position)) {
                next.setRegistered(false);
                next.dispose();
                iter.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * The method that retrieves the object on {@code position}.
     *
     * @param position
     *            the position to retrieve the object on.
     * @return the object instance wrapped in an optional, or an empty optional
     *         if no object is found.
     */
    public static Optional<ObjectNode> getObject(Position position) {
        return OBJECTS.stream().filter(obj -> obj.getPosition().equals(position)).findFirst();
    }

    /**
     * The method that updates all objects in the region for {@code player}.
     *
     * @param player
     *            the player to update objects for.
     */
    public static void updateRegion(Player player) {
        OBJECTS.forEach(obj -> {
            player.getMessages().sendRemoveObject(obj.getPosition());
            if (obj.getPosition().withinDistance(player.getPosition(), 60)) {
                player.getMessages().sendObject(obj);
            }
        });
        REMOVE_OBJECTS.forEach(obj -> {
            player.getMessages().sendRemoveObject(obj);
        });
    }
}
