package com.asteria.game.object;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import com.asteria.game.character.player.Player;
import com.asteria.game.location.Position;

/**
 * The node manager that manages all registered object nodes.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class ObjectNodeManager {

    /**
     * The hash collection of registered objects.
     */
    private static final Set<ObjectNode> OBJECTS = new HashSet<>();

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
        OBJECTS.stream().forEach(obj -> {
            player.getEncoder().sendRemoveObject(obj);
            if (obj.getPosition().withinDistance(player.getPosition(), 60)) {
                player.getEncoder().sendObject(obj);
            }
        });
    }
}
