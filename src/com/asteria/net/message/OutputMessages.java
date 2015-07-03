package com.asteria.net.message;

import java.util.Objects;

import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.Item;
import com.asteria.game.item.ItemNode;
import com.asteria.game.location.Palette;
import com.asteria.game.location.PaletteTile;
import com.asteria.game.location.Position;
import com.asteria.game.object.ObjectDirection;
import com.asteria.game.object.ObjectNode;
import com.asteria.game.object.ObjectType;
import com.asteria.net.ByteOrder;
import com.asteria.net.ValueType;
import com.google.common.base.Preconditions;

/**
 * The utility class used to queue {@link MessageBuilder}s to be encoded and
 * written to the Client.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class OutputMessages {

    /**
     * The player that will queue these messages.
     */
    private final Player player;

    /**
     * Creates a new {@link OutputMessages}.
     *
     * @param player
     *            the player that will queue these messages.
     */
    public OutputMessages(Player player) {
        this.player = player;
    }

    /**
     * The message that forces the player to view {@code id} tab.
     *
     * @param id
     *            the tab to force on the player.
     * @return an instance of this encoder.
     */
    public OutputMessages sendForceTab(int id) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(106);
        msg.put(id, ValueType.C);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that either shows or hides a layer on an interface.
     *
     * @param id
     *            the interface to show or hide a layer on.
     * @param hide
     *            if the layer should be hidden or shown.
     * @return an instance of this encoder.
     */
    public OutputMessages sendInterfaceLayer(int id, boolean hide) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(171);
        msg.put(hide ? 1 : 0);
        msg.putShort(id);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that updates a special bar with {@code amount} of special
     * energy.
     *
     * @param id
     *            the special bar to update with energy.
     * @param amount
     *            the amount of energy to update a special bar with.
     * @return an instance of this encoder.
     */
    public OutputMessages sendUpdateSpecial(int id, int amount) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(70);
        msg.putShort(amount);
        msg.putShort(0, com.asteria.net.ByteOrder.LITTLE);
        msg.putShort(id, com.asteria.net.ByteOrder.LITTLE);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The messages that display {@code str} on an empty chatbox.
     *
     * @param str
     *            the string to display on the chatbox.
     * @return an instance of this encoder.
     */
    public OutputMessages sendChatboxString(String str) {
        sendString(str, 357);
        sendString("Click here to continue", 358);
        sendChatInterface(356);
        return this;
    }

    /**
     * The messages that play an animation for an object that only the
     * underlying player can see.
     *
     * @param position
     *            the position the object is on.
     * @param animation
     *            the animation to play for this object.
     * @param type
     *            the object type of the object.
     * @param direction
     *            the direction this object is facing.
     * @return an instance of this encoder.
     */
    public OutputMessages sendObjectAnimation(Position position, int animation, ObjectType type, ObjectDirection direction) {
        sendCoordinates(position);
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(160);
        msg.put(((0 & 7) << 4) + (0 & 7), ValueType.S);
        msg.put((type.getId() << 2) + (direction.getId() & 3), ValueType.S);
        msg.putShort(animation, ValueType.A);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The messages that play an animation for an object that all local players
     * can see.
     *
     * @param position
     *            the position the object is on.
     * @param animation
     *            the animation to play for this object.
     * @param type
     *            the object type of the object.
     * @param direction
     *            the direction this object is facing.
     * @return an instance of this encoder.
     */
    public OutputMessages sendLocalObjectAnimation(Position position, int animation, ObjectType type, ObjectDirection direction) {
        player.getMessages().sendObjectAnimation(position, animation, type, direction);
        player.getLocalPlayers().stream().filter(Objects::nonNull).forEach(
            p -> p.getMessages().sendObjectAnimation(position, animation, type, direction));
        return this;
    }

    /**
     * The message that creates a graphic that only the underlying player can
     * see.
     *
     * @param id
     *            the id of the graphic that will be created.
     * @param position
     *            the position of the graphic that will be created.
     * @param level
     *            the height of the graphic that will be created.
     * @return an instance of this encoder.
     */
    public OutputMessages sendGraphic(int id, Position position, int level) {
        sendCoordinates(position);
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(4);
        msg.put(0);
        msg.putShort(id);
        msg.put(level);
        msg.putShort(0);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that creates a graphic that all local players can see.
     *
     * @param id
     *            the id of the graphic that will be created.
     * @param position
     *            the position of the graphic that will be created.
     * @param level
     *            the height of the graphic that will be created.
     * @return an instance of this encoder.
     */
    public OutputMessages sendLocalGraphic(int id, Position position, int level) {
        player.getMessages().sendGraphic(id, position, level);
        player.getLocalPlayers().stream().filter(Objects::nonNull).forEach(p -> p.getMessages().sendGraphic(id, position, level));
        return this;
    }

    /**
     * The message that creates a graphic that all players can see.
     *
     * @param id
     *            the id of the graphic that will be created.
     * @param position
     *            the position of the graphic that will be created.
     * @param level
     *            the height of the graphic that will be created.
     * @return an instance of this encoder.
     */
    public static void sendAllGraphic(int id, Position position, int level) {
        World.getPlayers().forEach(p -> p.getMessages().sendGraphic(id, position, level));
    }

    /**
     * The message that will play a sound for the underlying player.
     *
     * @param id
     *            the id of the sound that will be played.
     * @param type
     *            the type of sound that will be played.
     * @param delay
     *            the delay before the sound will be played.
     * @return an instance of this encoder.
     */
    public OutputMessages sendSound(int id, int type, int delay) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(174);
        msg.putShort(id);
        msg.put(type);
        msg.putShort(delay);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that will play a sound for all of the local players.
     *
     * @param id
     *            the id of the sound that will be played.
     * @param type
     *            the type of sound that will be played.
     * @param delay
     *            the delay before the sound will be played.
     * @return an instance of this encoder.
     */
    public OutputMessages sendLocalSound(int id, int type, int delay) {
        player.getMessages().sendSound(id, type, delay);
        player.getLocalPlayers().stream().filter(Objects::nonNull).forEach(p -> p.getMessages().sendSound(id, type, delay));
        return this;
    }

    /**
     * The message that allows for an interface to be animated.
     *
     * @param id
     *            the interface to animate on.
     * @param animation
     *            the animation to animate the interface with.
     * @return an instance of this encoder.
     */
    public OutputMessages sendInterfaceAnimation(int id, int animation) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(200);
        msg.putShort(id);
        msg.putShort(animation);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that updates the state of the multi-combat icon.
     *
     * @param hide
     *            determines if the icon should be turned on or off.
     * @return an instance of this encoder.
     */
    public OutputMessages sendMultiIcon(boolean hide) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(61);
        msg.put(hide ? 0 : 1);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends {@code item} on a specific interface slot.
     *
     * @param id
     *            the interface to display the item on.
     * @param item
     *            the item to display on the interface.
     * @param slot
     *            the slot on the interface to display the item on.
     * @return an instance of this encoder.
     */
    public OutputMessages sendItemOnInterfaceSlot(int id, Item item, int slot) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newVarShortMessage(34);
        msg.putShort(id);
        msg.put(slot);
        msg.putShort(item.getId() + 1);

        if (item.getAmount() > 254) {
            msg.put(255);
            msg.putShort(item.getAmount());
        } else {
            msg.put(item.getAmount());
        }
        msg.endVarShortMessage();
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends an item model on an interface.
     *
     * @param id
     *            the interface id to send the model on.
     * @param zoom
     *            the zoom of the model that will be sent.
     * @param model
     *            the item model that will be sent on the interface, or in other
     *            words the item identification.
     * @return an instance of this encoder.
     */
    public OutputMessages sendItemModelOnInterface(int id, int zoom, int model) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(246);
        msg.putShort(id, com.asteria.net.ByteOrder.LITTLE);
        msg.putShort(zoom).putShort(model);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends an array of items on an interface.
     *
     * @param id
     *            the interface that the items will be sent on.
     * @param items
     *            the items that will be sent on the interface.
     * @param length
     *            the amount of items that will be sent on the interface.
     * @return an instance of this encoder.
     */
    public OutputMessages sendItemsOnInterface(int id, Item[] items, int length) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newVarShortMessage(53).putShort(id);
        if (items == null) {
            msg.putShort(0);
            msg.put(0);
            msg.putShort(0, com.asteria.net.ValueType.A, com.asteria.net.ByteOrder.LITTLE);
            msg.endVarShortMessage();
            player.getSession().queue(msg);
            return this;
        }
        msg.putShort(length);
        for (Item item : items) {
            if (item != null) {
                if (item.getAmount() > 254) {
                    msg.put(255);
                    msg.putInt(item.getAmount(), com.asteria.net.ByteOrder.INVERSE_MIDDLE);
                } else {
                    msg.put(item.getAmount());
                }
                msg.putShort(item.getId() + 1, com.asteria.net.ValueType.A, com.asteria.net.ByteOrder.LITTLE);
            } else {
                msg.put(0);
                msg.putShort(0, com.asteria.net.ValueType.A, com.asteria.net.ByteOrder.LITTLE);
            }
        }
        msg.endVarShortMessage();
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends an array of items on an interface, with the length
     * being the capacity of {@code items}.
     *
     * @param id
     *            the interface that the items will be sent on.
     * @param items
     *            the items that will be sent on the interface.
     * @return an instance of this encoder.
     */
    public OutputMessages sendItemsOnInterface(int id, Item[] items) {
        int length = (items == null) ? 0 : items.length;
        return sendItemsOnInterface(id, items, length);
    }

    /**
     * The message that sends the head model of an NPC to an interface.
     *
     * @param id
     *            the interface to send the model on.
     * @param model
     *            the NPC model that will be sent on the interface, or in other
     *            words the NPC identification.
     * @return an instance of this encoder.
     */
    public OutputMessages sendNpcModelOnInterface(int id, int model) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(75);
        msg.putShort(model, ValueType.A, ByteOrder.LITTLE);
        msg.putShort(id, ValueType.A, ByteOrder.LITTLE);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that creates a custom map region made up tiles from other
     * regions.
     *
     * @param palette
     *            the tiles to construct into a region.
     * @return an instance of this encoder.
     */
    public OutputMessages sendCustomMapRegion(Palette palette) {
        sendMapRegion();
        MessageBuilder msg = MessageBuilder.create();
        msg.newVarShortMessage(241);
        msg.putShort(player.getPosition().getRegionY() + 6, ValueType.A);
        msg.startBitAccess();

        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 13; x++) {
                for (int y = 0; y < 13; y++) {
                    PaletteTile tile = palette.getTile(x, y, z);
                    msg.putBits(1, tile != null ? 1 : 0);
                    if (tile != null)
                        msg.putBits(26, tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() << 1);
                }
            }
        }
        msg.endBitAccess();
        msg.putShort(player.getPosition().getRegionX() + 6);
        msg.endVarShortMessage();
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends the head model of a player to an interface.
     *
     * @param id
     *            the interface to send the model on.
     * @return an instance of this encoder.
     */
    public OutputMessages sendPlayerModelOnInterface(int id) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(185);
        msg.putShort(id, ValueType.A, ByteOrder.LITTLE);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that causes a sidebar icon to start flashing.
     *
     * @param code
     *            the identification of the sidebar to flash. The code for each
     *            of the sidebar icons are as follows:
     *            <p>
     *            <p>
     *            Attack type: 0
     *            <p>
     *            Stats: -1
     *            <p>
     *            Quests: -2
     *            <p>
     *            Inventory: -3
     *            <p>
     *            Wearing: -4
     *            <p>
     *            Prayer: -5
     *            <p>
     *            Magic: -6
     *            <p>
     *            Empty: -7
     *            <p>
     *            Friends list: -8
     *            <p>
     *            Ignore list: -9
     *            <p>
     *            Log out: -10
     *            <p>
     *            Settings: -11
     *            <p>
     *            Emotes: -12
     *            <p>
     *            Music: -13
     *            <p>
     *            <p>
     * @return an instance of this encoder.
     */
    public OutputMessages sendFlashSidebar(int code) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(24);
        msg.put(code, ValueType.A);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that displays the "Enter name" interface.
     *
     * @return an instance of this encoder.
     */
    public OutputMessages sendEnterName() {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(187);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that changes the state of the minimap.
     *
     * @param code
     *            the new state of the minimap. The code for each of the minimap
     *            states are as follows:
     *            <p>
     *            <p>
     *            Normal: 0
     *            <p>
     *            Normal, but unclickable: 1
     *            <p>
     *            Blacked out: 2
     *            <p>
     *            <p>
     * @return an instance of this encoder.
     */
    public OutputMessages sendMinimapState(int code) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(99);
        msg.put(code);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that resets the camera's angle.
     *
     * @return an instance of this encoder.
     */
    public OutputMessages sendResetCameraAngle() {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(108);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends the camera angle. There isn't much documentation
     * out there on what the values actually represent.
     *
     * @param x
     *            the {@code X} coordinate within the region.
     * @param y
     *            the {@code Y} coordinate within the region.
     * @param level
     *            the level of the camera from the ground.
     * @param speed
     *            how fast the camera will turn to the angle.
     * @param angle
     *            the angle the camera will turn to.
     * @return an instance of this encoder.
     */
    public OutputMessages sendCameraAngle(int x, int y, int level, int speed, int angle) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(177);
        msg.put(x / 64);
        msg.put(y / 64);
        msg.putShort(level);
        msg.put(speed);
        msg.put(angle);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that moves the actual camera. There isn't much documentation
     * out there on what the values actually represent.
     *
     * @param x
     *            the {@code X} coordinate within the region.
     * @param y
     *            the {@code Y} coordinate within the region.
     * @param z
     *            the {@code Z} coordinate within the region.
     * @param speed
     *            how fast the camera will move.
     * @param angle
     *            the angle the camera will turn to while moving.
     * @return an instance of this encoder.
     */
    public OutputMessages sendCameraMovement(int x, int y, int z, int speed, int angle) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(166);
        msg.put(x / 64);
        msg.put(y / 64);
        msg.putShort(z);
        msg.put(speed);
        msg.put(angle);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that causes the screen and camera to shake.
     *
     * @param parameter
     *            the position parameter to oscillate. The position parameters
     *            are as follows:
     *            <p>
     *            <p>
     *            Camera location along world X axis (a horizontal axis, aligned
     *            with map grid X): 0
     *            <p>
     *            Camera location along world Z axis (vertical axis): 1
     *            <p>
     *            Camera location along world Y axis (a horizontal axis, aligned
     *            with map grid Y): 2
     *            <p>
     *            Camera orientation in world X plane w.r.t. world Z axis, i.e.
     *            yaw: 3
     *            <p>
     *            Camera orientation in world Z plane w.r.t. world X axis, i.e.
     *            pitch: 4
     *            <p>
     *            <p>
     * @param jitter
     *            the amount of randomization in the screen shake.
     * @param amplitude
     *            the maximum extent of the shake.
     * @param frequency
     *            how often the screen will shake (scaled by 100).
     * @return an instance of this encoder.
     */
    public OutputMessages sendCameraShake(int parameter, int jitter, int amplitude, int frequency) {
        Preconditions.checkArgument(parameter <= 4);
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(35);
        msg.put(parameter);
        msg.put(jitter);
        msg.put(amplitude);
        msg.put(frequency);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that resets the position of the camera.
     *
     * @return an instance of this encoder.
     */
    public OutputMessages sendResetCameraPosition() {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(107);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that plays music for the underlying player.
     *
     * @param id
     *            the identification of the music to play.
     * @return an instance of this encoder.
     */
    public OutputMessages sendMusic(int id) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(74);
        msg.putShort(id, ByteOrder.LITTLE);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends the system update timer. A timer showing how many
     * seconds until a 'System Update' will appear in the lower left hand corner
     * of the game screen. After the timer reaches 0 all players are
     * disconnected and are unable to log in again until server is restarted.
     * Players connecting will receive a message stating,
     * "The server is being updated. Please wait 1 minute and try again."
     * (unless stated otherwise).
     *
     * @param amount
     *            the amount of time until an update.
     * @return an instance of this encoder.
     */
    public OutputMessages sendSystemUpdate(int amount) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(114);
        msg.putShort(amount, ByteOrder.LITTLE);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends the underlying player's run energy percentage to
     * the correct place.
     *
     * @return an instance of this encoder.
     */
    public OutputMessages sendRunEnergy() {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(110);
        msg.put(player.getRunEnergy().get());
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that changes the color of an interface that is text.
     *
     * @param id
     *            the interface identification to send the color on.
     * @param color
     *            the new color that will be added to the interface. The color
     *            hex codes are as follows:
     *            <p>
     *            <p>
     *            Red: 0x6000
     *            <p>
     *            Yellow: 0x33FF66
     *            <p>
     *            Green: 0x3366
     *            <p>
     *            <p>
     * @return an instance of this encoder.
     */
    public OutputMessages sendInterfaceColor(int id, int color) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(122);
        msg.putShort(id, ValueType.A, ByteOrder.LITTLE);
        msg.putShort(color, ValueType.A, ByteOrder.LITTLE);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that launches a projectile that only the underlying player
     * can see.
     *
     * @param position
     *            the position of the projectile.
     * @param offset
     *            the offset position of the projectile.
     * @param angle
     *            the angle of the projectile.
     * @param speed
     *            the speed of the projectile.
     * @param gfxMoving
     *            the rate that projectile gfx moves in.
     * @param startHeight
     *            the starting height of the projectile.
     * @param endHeight
     *            the ending height of the projectile.
     * @param lockon
     *            the lockon value of this projectile.
     * @param time
     *            the time it takes for this projectile to hit its desired
     *            position.
     * @return an instance of this encoder.
     */
    public OutputMessages sendProjectile(Position position, Position offset, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) {
        sendCoordinates(position);
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(117);
        msg.put(angle);
        msg.put(offset.getY());
        msg.put(offset.getX());
        msg.putShort(lockon);
        msg.putShort(gfxMoving);
        msg.put(startHeight);
        msg.put(endHeight);
        msg.putShort(time);
        msg.putShort(speed);
        msg.put(16);
        msg.put(64);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that launches a projectile that all of the local players can
     * see.
     *
     * @param position
     *            the position of the projectile.
     * @param offset
     *            the offset position of the projectile.
     * @param angle
     *            the angle of the projectile.
     * @param speed
     *            the speed of the projectile.
     * @param gfxMoving
     *            the rate that projectile gfx moves in.
     * @param startHeight
     *            the starting height of the projectile.
     * @param endHeight
     *            the ending height of the projectile.
     * @param lockon
     *            the lockon value of this projectile.
     * @param time
     *            the time it takes for this projectile to hit its desired
     *            position.
     * @return an instance of this encoder.
     */
    public void sendAllProjectile(Position position, Position offset, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) {
        player.getLocalPlayers().stream().filter(Objects::nonNull).forEach(
            p -> p.getMessages().sendProjectile(position, offset, angle, speed, gfxMoving, startHeight, endHeight, lockon, time));
    }

    /**
     * The message that changes the configuration value for a certain client
     * setting in the form of a byte.
     *
     * @param id
     *            the setting identification number.
     * @param state
     *            the new value for the setting.
     * @return an instance of this encoder.
     */
    public OutputMessages sendByteState(int id, int state) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(36);
        msg.putShort(id, ByteOrder.LITTLE);
        msg.put(state);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that changes the configuration value for a certain client
     * setting in the form of an integer.
     *
     * @param id
     *            the setting identification number.
     * @param state
     *            the new value for the setting.
     * @return an instance of this encoder.
     */
    public OutputMessages sendIntState(int id, int state) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(87);
        msg.putShort(id, ByteOrder.LITTLE);
        msg.putInt(state, ByteOrder.MIDDLE);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that spawns an object only the underlying player can see.
     *
     * @param object
     *            the object to spawn for the player.
     * @return an instance of this encoder.
     */
    public OutputMessages sendObject(ObjectNode object) {
        sendCoordinates(object.getPosition());
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(151);
        msg.put(0, ValueType.S);
        msg.putShort(object.getId(), ByteOrder.LITTLE);
        msg.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ValueType.S);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that removes an object only the underlying player can see.
     *
     * @param position
     *            the position of the object to remove for the player.
     * @return an instance of this encoder.
     */
    public OutputMessages sendRemoveObject(Position position) {
        sendCoordinates(position);
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(101);
        msg.put((ObjectType.DEFAULT.getId() << 2) + (ObjectDirection.SOUTH.getId() & 3), ValueType.C);
        msg.put(0);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The messages that replace an existing object with a new one.
     *
     * @param position
     *            the position that the old object is in.
     * @param object
     *            the new object to take its place.
     * @return an instance of this encoder.
     */
    public OutputMessages sendReplaceObject(Position position, int object) {
        sendRemoveObject(position);
        sendObject(new ObjectNode(object, position, ObjectDirection.SOUTH));
        return this;
    }

    /**
     * The message that sends the underlying player's skill to the proper
     * interfaces.
     *
     * @param id
     *            the identification number of the skill.
     * @param level
     *            the level reached in this skill.
     * @param exp
     *            the amount of experience obtained in this skill.
     * @return an instance of this encoder.
     */
    public OutputMessages sendSkill(int id, int level, int exp) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(134).put(id).putInt(exp, ByteOrder.MIDDLE).put(level);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that closes any interfaces the underlying player has open.
     *
     * @return an instance of this encoder.
     */
    public OutputMessages sendCloseWindows() {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(219);
        player.getSession().queue(msg);
        player.getDialogueChain().interrupt();
        return this;
    }

    /**
     * The message that sends the first private messaging list load status.
     *
     * @param code
     *            the status of the friends list. The status for the friends
     *            lists are as follows:
     *            <p>
     *            <p>
     *            Loading: 0
     *            <p>
     *            Connecting: 1
     *            <p>
     *            Loaded: 2
     *            <p>
     *            <p>
     * @return an instance of this encoder.
     */
    public OutputMessages sendPrivateMessageListStatus(int code) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(221);
        msg.put(code);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends a player to the friend list.
     *
     * @param name
     *            the player's name to add to the list.
     * @param online
     *            if the player is online or not.
     * @return an instance of this encoder.
     */
    public OutputMessages sendPrivateMessageFriend(long name, boolean online) {
        int value = online ? 1 : 0;
        if (value != 0)
            value += 9;
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(50);
        msg.putLong(name);
        msg.put(value);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends a hint arrow on a position.
     *
     * @param position
     *            the position to send the arrow on.
     * @param direction
     *            the direction on the position to send the arrow on. The
     *            possible directions to put the arrow on are as follows:
     *            <p>
     *            <p>
     *            Middle: 2
     *            <p>
     *            West: 3
     *            <p>
     *            East: 4
     *            <p>
     *            South: 5
     *            <p>
     *            North: 6
     *            <p>
     *            <p>
     * @return an instance of this encoder.
     */
    public OutputMessages sendPositionHintArrow(Position position, int direction) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(254);
        msg.put(direction);
        msg.putShort(position.getX());
        msg.putShort(position.getY());
        msg.put(position.getZ());
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends a hint arrow on {@code character}.
     *
     * @param character
     *            the character to send a hint arrow on.
     * @return an instance of this encoder.
     */
    public OutputMessages sendCharacterHintArrow(CharacterNode character) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(254).put(character.getType() == NodeType.NPC ? 1 : 10);
        msg.putShort(character.getSlot());
        msg.put(0);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends a private message to another player.
     *
     * @param name
     *            the name of the player you are sending the message to.
     * @param rights
     *            the rights the player sending the message has.
     * @param message
     *            the actual message compressed into bytes.
     * @param size
     *            the size of the message being sent.
     * @return an instance of this encoder.
     */
    public OutputMessages sendPrivateMessage(long name, int rights, byte[] message, int size) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newVarMessage(196);
        msg.putLong(name);
        msg.putInt(player.getPrivateMessage().getLastMessage().getAndIncrement());
        msg.put(rights);
        msg.putBytes(message, size);
        msg.endVarMessage();
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends the players current coordinates to the client.
     *
     * @param position
     *            the coordinates to send to the client.
     * @return an instance of this encoder.
     */
    public OutputMessages sendCoordinates(Position position) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(85);
        msg.put(position.getY() - (player.getCurrentRegion().getRegionY() * 8), ValueType.C);
        msg.put(position.getX() - (player.getCurrentRegion().getRegionX() * 8), ValueType.C);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that opens a walkable interface for the underlying player.
     *
     * @param id
     *            the identification of the interface to open.
     * @return an instance of this encoder.
     */
    public OutputMessages sendWalkable(int id) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(208);
        msg.putShort(id, ByteOrder.LITTLE);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that spawns a ground item.
     *
     * @param item
     *            the ground item to spawn.
     * @return an instance of this encoder.
     */
    public OutputMessages sendGroundItem(ItemNode item) {
        sendCoordinates(item.getPosition());
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(44);
        msg.putShort(item.getItem().getId(), ValueType.A, ByteOrder.LITTLE);
        msg.putShort(item.getItem().getAmount());
        msg.put(0);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that removes a ground item.
     *
     * @param item
     *            the ground item to remove.
     * @return an instance of this encoder.
     */
    public OutputMessages sendRemoveGroundItem(ItemNode item) {
        sendCoordinates(item.getPosition());
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(156);
        msg.put(0, ValueType.S);
        msg.putShort(item.getItem().getId());
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends the player context menus.
     *
     * @param slot
     *            the slot for the option to be placed in.
     * @param option
     *            the string literal option to display.
     * @return an instance of this encoder.
     */
    public OutputMessages sendContextMenu(int slot, String option) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newVarMessage(104);
        msg.put(slot, com.asteria.net.ValueType.C);
        msg.put(0, com.asteria.net.ValueType.A);
        msg.putString(option);
        msg.endVarMessage();
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that attaches text to an interface.
     *
     * @param text
     *            the text to attach to the interface.
     * @param id
     *            the identification for the interface.
     * @return an instance of this encoder.
     */
    public OutputMessages sendString(String text, int id) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newVarShortMessage(126);
        msg.putString(text);
        msg.putShort(id, ValueType.A);
        msg.endVarShortMessage();
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that opens an interface and displays another interface over
     * the inventory area.
     *
     * @param open
     *            the interface to open.
     * @param overlay
     *            the interface to send on the inventory area.
     * @return an instance of this encoder.
     */
    public OutputMessages sendInventoryInterface(int open, int overlay) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(248);
        msg.putShort(open, ValueType.A);
        msg.putShort(overlay);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that opens an interface for underlying player.
     *
     * @param id
     *            the identification number of the interface to open.
     * @return an instance of this encoder.
     */
    public OutputMessages sendInterface(int id) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(97);
        msg.putShort(id);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends the underlying player a message to the chatbox.
     *
     * @param message
     *            the message to send.
     * @return an instance of this encoder.
     */
    public OutputMessages sendMessage(String message) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newVarMessage(253);
        msg.putString(message);
        msg.endVarMessage();
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends an interface to a certain sidebar.
     *
     * @param sidebar
     *            the sidebar to send the interface on.
     * @param id
     *            the interface to send on the sidebar.
     * @return an instance of this encoder.
     */
    public OutputMessages sendSidebarInterface(int sidebar, int id) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(71);
        msg.putShort(id);
        msg.put(sidebar, ValueType.A);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends the current map region.
     *
     * @return an instance of this encoder.
     */
    public OutputMessages sendMapRegion() {
        player.setCurrentRegion(player.getPosition().copy());
        player.setNeedsPlacement(true);
        player.setUpdateRegion(true);
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(73);
        msg.putShort(player.getPosition().getRegionX() + 6, com.asteria.net.ValueType.A);
        msg.putShort(player.getPosition().getRegionY() + 6);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that disconnects the underlying player.
     *
     * @return an instance of this encoder.
     */
    public OutputMessages sendLogout() {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(109);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that sends the slot and membership status to the client.
     *
     * @return an instance of this encoder.
     */
    public OutputMessages sendDetails() {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(249);
        msg.put(1, ValueType.A);
        msg.putShort(player.getSlot(), ValueType.A, ByteOrder.LITTLE);
        player.getSession().queue(msg);
        return this;
    }

    /**
     * The message that shows an interface in the chat box.
     *
     * @param id
     *            the identification of interface to show.
     * @return an instance of this encoder.
     */
    public OutputMessages sendChatInterface(int id) {
        MessageBuilder msg = MessageBuilder.create();
        msg.newMessage(164);
        msg.putShort(id, ByteOrder.LITTLE);
        player.getSession().queue(msg);
        return this;
    }
}
