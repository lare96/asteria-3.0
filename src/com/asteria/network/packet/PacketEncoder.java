package com.asteria.network.packet;

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
import com.asteria.network.ByteOrder;
import com.asteria.network.DataBuffer;
import com.asteria.network.ValueType;
import com.google.common.base.Preconditions;

/**
 * The class that contains functions that encode and send data to the client.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class PacketEncoder {

    /**
     * The player that will encode these packets.
     */
    private final Player player;

    /**
     * Creates a new {@link PacketEncoder}.
     *
     * @param player
     *         the player that will encode these packets.
     */
    public PacketEncoder(Player player) {
        this.player = player;
    }

    /**
     * The packet that forces the player to view {@code id} tab.
     *
     * @param id
     *         the tab to force on the player.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendForceTab(int id) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(106, player.getSession().getEncryptor());
        out.put(id, ValueType.C);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that either shows or hides a layer on an interface.
     *
     * @param id
     *         the interface to show or hide a layer on.
     * @param hide
     *         if the layer should be hidden or shown.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendInterfaceLayer(int id, boolean hide) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(171, player.getSession().getEncryptor());
        out.put(hide ? 1 : 0);
        out.putShort(id);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that updates a special bar with {@code amount} of special
     * energy.
     *
     * @param id
     *         the special bar to update with energy.
     * @param amount
     *         the amount of energy to update a special bar with.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendUpdateSpecial(int id, int amount) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(70, player.getSession().getEncryptor());
        out.putShort(amount);
        out.putShort(0, com.asteria.network.ByteOrder.LITTLE);
        out.putShort(id, com.asteria.network.ByteOrder.LITTLE);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packets that display {@code str} on an empty chatbox.
     *
     * @param str
     *         the string to display on the chatbox.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendChatboxString(String str) {
        sendString(str, 357);
        sendString("Click here to continue", 358);
        sendChatInterface(356);
        return this;
    }

    /**
     * The packets that play an animation for an object that only the
     * underlying
     * player can see.
     *
     * @param position
     *         the position the object is on.
     * @param animation
     *         the animation to play for this object.
     * @param type
     *         the object type of the object.
     * @param direction
     *         the direction this object is facing.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendObjectAnimation(Position position, int animation, ObjectType type, ObjectDirection direction) {
        sendCoordinates(position);
        DataBuffer out = DataBuffer.create();
        out.newPacket(160, player.getSession().getEncryptor());
        out.put(((0 & 7) << 4) + (0 & 7), ValueType.S);
        out.put((type.getId() << 2) + (direction.getId() & 3), ValueType.S);
        out.putShort(animation, ValueType.A);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packets that play an animation for an object that all local players
     * can see.
     *
     * @param position
     *         the position the object is on.
     * @param animation
     *         the animation to play for this object.
     * @param type
     *         the object type of the object.
     * @param direction
     *         the direction this object is facing.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendLocalObjectAnimation(Position position, int animation, ObjectType type, ObjectDirection direction) {
        player.getEncoder().sendObjectAnimation(position, animation, type, direction);
        player.getLocalPlayers().stream().filter(Objects::nonNull).forEach(p -> p.getEncoder().sendObjectAnimation(position, animation, type, direction));
        return this;
    }

    /**
     * The packet that creates a graphic that only the underlying player can
     * see.
     *
     * @param id
     *         the id of the graphic that will be created.
     * @param position
     *         the position of the graphic that will be created.
     * @param level
     *         the height of the graphic that will be created.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendGraphic(int id, Position position, int level) {
        sendCoordinates(position);
        DataBuffer out = DataBuffer.create();
        out.newPacket(4, player.getSession().getEncryptor());
        out.put(0);
        out.putShort(id);
        out.put(level);
        out.putShort(0);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that creates a graphic that all local players can see.
     *
     * @param id
     *         the id of the graphic that will be created.
     * @param position
     *         the position of the graphic that will be created.
     * @param level
     *         the height of the graphic that will be created.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendLocalGraphic(int id, Position position, int level) {
        player.getEncoder().sendGraphic(id, position, level);
        player.getLocalPlayers().stream().filter(Objects::nonNull).forEach(p -> p.getEncoder().sendGraphic(id, position, level));
        return this;
    }

    /**
     * The packet that creates a graphic that all players can see.
     *
     * @param id
     *         the id of the graphic that will be created.
     * @param position
     *         the position of the graphic that will be created.
     * @param level
     *         the height of the graphic that will be created.
     * @return an instance of this encoder.
     */
    public static void sendAllGraphic(int id, Position position, int level) {
        World.getPlayers().forEach(p -> p.getEncoder().sendGraphic(id, position, level));
    }

    /**
     * The packet that will play a sound for the underlying player.
     *
     * @param id
     *         the id of the sound that will be played.
     * @param type
     *         the type of sound that will be played.
     * @param delay
     *         the delay before the sound will be played.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendSound(int id, int type, int delay) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(174, player.getSession().getEncryptor());
        out.putShort(id);
        out.put(type);
        out.putShort(delay);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that will play a sound for all of the local players.
     *
     * @param id
     *         the id of the sound that will be played.
     * @param type
     *         the type of sound that will be played.
     * @param delay
     *         the delay before the sound will be played.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendLocalSound(int id, int type, int delay) {
        player.getEncoder().sendSound(id, type, delay);
        player.getLocalPlayers().stream().filter(Objects::nonNull).forEach(p -> p.getEncoder().sendSound(id, type, delay));
        return this;
    }

    /**
     * The packet that allows for an interface to be animated.
     *
     * @param id
     *         the interface to animate on.
     * @param animation
     *         the animation to animate the interface with.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendInterfaceAnimation(int id, int animation) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(200, player.getSession().getEncryptor());
        out.putShort(id);
        out.putShort(animation);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that updates the state of the multi-combat icon.
     *
     * @param hide
     *         determines if the icon should be turned on or off.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendMultiIcon(boolean hide) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(61, player.getSession().getEncryptor());
        out.put(hide ? 0 : 1);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends {@code item} on a specific interface slot.
     *
     * @param id
     *         the interface to display the item on.
     * @param item
     *         the item to display on the interface.
     * @param slot
     *         the slot on the interface to display the item on.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendItemOnInterfaceSlot(int id, Item item, int slot) {
        DataBuffer out = DataBuffer.create();
        out.newVarShortPacket(34, player.getSession().getEncryptor());
        out.putShort(id);
        out.put(slot);
        out.putShort(item.getId() + 1);

        if (item.getAmount() > 254) {
            out.put(255);
            out.putShort(item.getAmount());
        } else {
            out.put(item.getAmount());
        }
        out.endVarShortPacket();
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends an item model on an interface.
     *
     * @param id
     *         the interface id to send the model on.
     * @param zoom
     *         the zoom of the model that will be sent.
     * @param model
     *         the item model that will be sent on the interface, or in other
     *         words the item identification.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendItemModelOnInterface(int id, int zoom, int model) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(246, player.getSession().getEncryptor());
        out.putShort(id, com.asteria.network.ByteOrder.LITTLE);
        out.putShort(zoom).putShort(model);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends an array of items on an interface.
     *
     * @param id
     *         the interface that the items will be sent on.
     * @param items
     *         the items that will be sent on the interface.
     * @param length
     *         the amount of items that will be sent on the interface.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendItemsOnInterface(int id, Item[] items, int length) {
        DataBuffer out = DataBuffer.create();
        out.newVarShortPacket(53, player.getSession().getEncryptor()).putShort(id);
        if (items == null) {
            out.putShort(0);
            out.put(0);
            out.putShort(0, com.asteria.network.ValueType.A, com.asteria.network.ByteOrder.LITTLE);
            out.endVarShortPacket();
            player.getSession().send(out);
            return this;
        }
        out.putShort(length);
        for (Item item : items) {
            if (item != null) {
                if (item.getAmount() > 254) {
                    out.put(255);
                    out.putInt(item.getAmount(), com.asteria.network.ByteOrder.INVERSE_MIDDLE);
                } else {
                    out.put(item.getAmount());
                }
                out.putShort(item.getId() + 1, com.asteria.network.ValueType.A, com.asteria.network.ByteOrder.LITTLE);
            } else {
                out.put(0);
                out.putShort(0, com.asteria.network.ValueType.A, com.asteria.network.ByteOrder.LITTLE);
            }
        }
        out.endVarShortPacket();
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends an array of items on an interface, with the length
     * being the capacity of {@code items}.
     *
     * @param id
     *         the interface that the items will be sent on.
     * @param items
     *         the items that will be sent on the interface.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendItemsOnInterface(int id, Item[] items) {
        int length = (items == null) ? 0 : items.length;
        return sendItemsOnInterface(id, items, length);
    }

    /**
     * The packet that sends the head model of an NPC to an interface.
     *
     * @param id
     *         the interface to send the model on.
     * @param model
     *         the NPC model that will be sent on the interface, or in other
     *         words the NPC identification.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendNpcModelOnInterface(int id, int model) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(75, player.getSession().getEncryptor());
        out.putShort(model, ValueType.A, ByteOrder.LITTLE);
        out.putShort(id, ValueType.A, ByteOrder.LITTLE);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that creates a custom map region made up tiles from other
     * regions.
     *
     * @param palette
     *         the tiles to construct into a region.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendCustomMapRegion(Palette palette) {
        sendMapRegion();
        DataBuffer out = DataBuffer.create();
        out.newVarShortPacket(241, player.getSession().getEncryptor());
        out.putShort(player.getPosition().getRegionY() + 6, ValueType.A);
        out.startBitAccess();

        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 13; x++) {
                for (int y = 0; y < 13; y++) {
                    PaletteTile tile = palette.getTile(x, y, z);
                    out.putBits(1, tile != null ? 1 : 0);
                    if (tile != null)
                        out.putBits(26, tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() << 1);
                }
            }
        }
        out.endBitAccess();
        out.putShort(player.getPosition().getRegionX() + 6);
        out.endVarShortPacket();
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends the head model of a player to an interface.
     *
     * @param id
     *         the interface to send the model on.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendPlayerModelOnInterface(int id) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(185, player.getSession().getEncryptor());
        out.putShort(id, ValueType.A, ByteOrder.LITTLE);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that causes a sidebar icon to start flashing.
     *
     * @param code
     *         the identification of the sidebar to flash. The code for each
     *         of the sidebar icons are as follows:
     *         <p>
     *         <p>
     *         Attack type: 0
     *         <p>
     *         Stats: -1
     *         <p>
     *         Quests: -2
     *         <p>
     *         Inventory: -3
     *         <p>
     *         Wearing: -4
     *         <p>
     *         Prayer: -5
     *         <p>
     *         Magic: -6
     *         <p>
     *         Empty: -7
     *         <p>
     *         Friends list: -8
     *         <p>
     *         Ignore list: -9
     *         <p>
     *         Log out: -10
     *         <p>
     *         Settings: -11
     *         <p>
     *         Emotes: -12
     *         <p>
     *         Music: -13
     *         <p>
     *         <p>
     * @return an instance of this encoder.
     */
    public PacketEncoder sendFlashSidebar(int code) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(24, player.getSession().getEncryptor());
        out.put(code, ValueType.A);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that displays the "Enter name" interface.
     *
     * @return an instance of this encoder.
     */
    public PacketEncoder sendEnterName() {
        DataBuffer out = DataBuffer.create();
        out.newPacket(187, player.getSession().getEncryptor());
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that changes the state of the minimap.
     *
     * @param code
     *         the new state of the minimap. The code for each of the minimap
     *         states are as follows:
     *         <p>
     *         <p>
     *         Normal: 0
     *         <p>
     *         Normal, but unclickable: 1
     *         <p>
     *         Blacked out: 2
     *         <p>
     *         <p>
     * @return an instance of this encoder.
     */
    public PacketEncoder sendMinimapState(int code) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(99, player.getSession().getEncryptor());
        out.put(code);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that resets the camera's angle.
     *
     * @return an instance of this encoder.
     */
    public PacketEncoder sendResetCameraAngle() {
        DataBuffer out = DataBuffer.create();
        out.newPacket(108, player.getSession().getEncryptor());
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends the camera angle. There isn't much documentation
     * out there on what the values actually represent.
     *
     * @param x
     *         the {@code X} coordinate within the region.
     * @param y
     *         the {@code Y} coordinate within the region.
     * @param level
     *         the level of the camera from the ground.
     * @param speed
     *         how fast the camera will turn to the angle.
     * @param angle
     *         the angle the camera will turn to.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendCameraAngle(int x, int y, int level, int speed, int angle) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(177, player.getSession().getEncryptor());
        out.put(x / 64);
        out.put(y / 64);
        out.putShort(level);
        out.put(speed);
        out.put(angle);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that moves the actual camera. There isn't much documentation
     * out there on what the values actually represent.
     *
     * @param x
     *         the {@code X} coordinate within the region.
     * @param y
     *         the {@code Y} coordinate within the region.
     * @param z
     *         the {@code Z} coordinate within the region.
     * @param speed
     *         how fast the camera will move.
     * @param angle
     *         the angle the camera will turn to while moving.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendCameraMovement(int x, int y, int z, int speed, int angle) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(166, player.getSession().getEncryptor());
        out.put(x / 64);
        out.put(y / 64);
        out.putShort(z);
        out.put(speed);
        out.put(angle);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that causes the screen and camera to shake.
     *
     * @param parameter
     *         the position parameter to oscillate. The position parameters
     *         are as follows:
     *         <p>
     *         <p>
     *         Camera location along world X axis (a horizontal axis, aligned
     *         with map grid X): 0
     *         <p>
     *         Camera location along world Z axis (vertical axis): 1
     *         <p>
     *         Camera location along world Y axis (a horizontal axis, aligned
     *         with map grid Y): 2
     *         <p>
     *         Camera orientation in world X plane w.r.t. world Z axis, i.e.
     *         yaw: 3
     *         <p>
     *         Camera orientation in world Z plane w.r.t. world X axis, i.e.
     *         pitch: 4
     *         <p>
     *         <p>
     * @param jitter
     *         the amount of randomization in the screen shake.
     * @param amplitude
     *         the maximum extent of the shake.
     * @param frequency
     *         how often the screen will shake (scaled by 100).
     * @return an instance of this encoder.
     */
    public PacketEncoder sendCameraShake(int parameter, int jitter, int amplitude, int frequency) {
        Preconditions.checkArgument(parameter <= 4);
        DataBuffer out = DataBuffer.create();
        out.newPacket(35, player.getSession().getEncryptor());
        out.put(parameter);
        out.put(jitter);
        out.put(amplitude);
        out.put(frequency);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that resets the position of the camera.
     *
     * @return an instance of this encoder.
     */
    public PacketEncoder sendResetCameraPosition() {
        DataBuffer out = DataBuffer.create();
        out.newPacket(107, player.getSession().getEncryptor());
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that plays music for the underlying player.
     *
     * @param id
     *         the identification of the music to play.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendMusic(int id) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(74, player.getSession().getEncryptor());
        out.putShort(id, ByteOrder.LITTLE);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends the system update timer. A timer showing how many
     * seconds until a 'System Update' will appear in the lower left hand
     * corner
     * of the game screen. After the timer reaches 0 all players are
     * disconnected and are unable to log in again until server is restarted.
     * Players connecting will receive a message stating,
     * "The server is being updated. Please wait 1 minute and try again."
     * (unless stated otherwise).
     *
     * @param amount
     *         the amount of time until an update.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendSystemUpdate(int amount) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(114, player.getSession().getEncryptor());
        out.putShort(amount, ByteOrder.LITTLE);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends the underlying player's run energy percentage to
     * the correct place.
     *
     * @return an instance of this encoder.
     */
    public PacketEncoder sendRunEnergy() {
        DataBuffer out = DataBuffer.create();
        out.newPacket(110, player.getSession().getEncryptor());
        out.put(player.getRunEnergy().get());
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that changes the color of an interface that is text.
     *
     * @param id
     *         the interface identification to send the color on.
     * @param color
     *         the new color that will be added to the interface. The color
     *         hex codes are as follows:
     *         <p>
     *         <p>
     *         Red: 0x6000
     *         <p>
     *         Yellow: 0x33FF66
     *         <p>
     *         Green: 0x3366
     *         <p>
     *         <p>
     * @return an instance of this encoder.
     */
    public PacketEncoder sendInterfaceColor(int id, int color) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(122, player.getSession().getEncryptor());
        out.putShort(id, ValueType.A, ByteOrder.LITTLE);
        out.putShort(color, ValueType.A, ByteOrder.LITTLE);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that launches a projectile that only the underlying player
     * can
     * see.
     *
     * @param position
     *         the position of the projectile.
     * @param offset
     *         the offset position of the projectile.
     * @param angle
     *         the angle of the projectile.
     * @param speed
     *         the speed of the projectile.
     * @param gfxMoving
     *         the rate that projectile gfx moves in.
     * @param startHeight
     *         the starting height of the projectile.
     * @param endHeight
     *         the ending height of the projectile.
     * @param lockon
     *         the lockon value of this projectile.
     * @param time
     *         the time it takes for this projectile to hit its desired
     *         position.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendProjectile(Position position, Position offset, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) {
        sendCoordinates(position);
        DataBuffer out = DataBuffer.create();
        out.newPacket(117, player.getSession().getEncryptor());
        out.put(angle);
        out.put(offset.getY());
        out.put(offset.getX());
        out.putShort(lockon);
        out.putShort(gfxMoving);
        out.put(startHeight);
        out.put(endHeight);
        out.putShort(time);
        out.putShort(speed);
        out.put(16);
        out.put(64);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that launches a projectile that all of the local players can
     * see.
     *
     * @param position
     *         the position of the projectile.
     * @param offset
     *         the offset position of the projectile.
     * @param angle
     *         the angle of the projectile.
     * @param speed
     *         the speed of the projectile.
     * @param gfxMoving
     *         the rate that projectile gfx moves in.
     * @param startHeight
     *         the starting height of the projectile.
     * @param endHeight
     *         the ending height of the projectile.
     * @param lockon
     *         the lockon value of this projectile.
     * @param time
     *         the time it takes for this projectile to hit its desired
     *         position.
     * @return an instance of this encoder.
     */
    public void sendAllProjectile(Position position, Position offset, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) {
        player.getLocalPlayers().stream().filter(Objects::nonNull).forEach(p -> p.getEncoder().sendProjectile(position, offset, angle, speed, gfxMoving, startHeight, endHeight, lockon, time));
    }

    /**
     * The packet that changes the configuration value for a certain client
     * setting in the form of a byte.
     *
     * @param id
     *         the setting identification number.
     * @param state
     *         the new value for the setting.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendByteState(int id, int state) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(36, player.getSession().getEncryptor());
        out.putShort(id, ByteOrder.LITTLE);
        out.put(state);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that changes the configuration value for a certain client
     * setting in the form of an integer.
     *
     * @param id
     *         the setting identification number.
     * @param state
     *         the new value for the setting.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendIntState(int id, int state) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(87, player.getSession().getEncryptor());
        out.putShort(id, ByteOrder.LITTLE);
        out.putInt(state, ByteOrder.MIDDLE);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that spawns an object only the underlying player can see.
     *
     * @param object
     *         the object to spawn for the player.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendObject(ObjectNode object) {
        sendCoordinates(object.getPosition());
        DataBuffer out = DataBuffer.create();
        out.newPacket(151, player.getSession().getEncryptor());
        out.put(0, ValueType.S);
        out.putShort(object.getId(), ByteOrder.LITTLE);
        out.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ValueType.S);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that removes an object only the underlying player can see.
     *
     * @param position
     *         the position of the object to remove for the player.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendRemoveObject(Position position) {
        sendCoordinates(position);
        DataBuffer out = DataBuffer.create();
        out.newPacket(101, player.getSession().getEncryptor());
        out.put((ObjectType.DEFAULT.getId() << 2) + (ObjectDirection.SOUTH.getId() & 3), ValueType.C);
        out.put(0);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packets that replace an existing object with a new one.
     *
     * @param position
     *         the position that the old object is in.
     * @param object
     *         the new object to take its place.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendReplaceObject(Position position, int object) {
        sendRemoveObject(position);
        sendObject(new ObjectNode(object, position, ObjectDirection.SOUTH));
        return this;
    }

    /**
     * The packet that sends the underlying player's skill to the proper
     * interfaces.
     *
     * @param id
     *         the identification number of the skill.
     * @param level
     *         the level reached in this skill.
     * @param exp
     *         the amount of experience obtained in this skill.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendSkill(int id, int level, int exp) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(134, player.getSession().getEncryptor()).put(id).putInt(exp, ByteOrder.MIDDLE).put(level);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that closes any interfaces the underlying player has open.
     *
     * @return an instance of this encoder.
     */
    public PacketEncoder sendCloseWindows() {
        DataBuffer out = DataBuffer.create();
        out.newPacket(219, player.getSession().getEncryptor());
        player.getSession().send(out);
        player.getDialogueChain().interrupt();
        return this;
    }

    /**
     * The packet that sends the first private messaging list load status.
     *
     * @param code
     *         the status of the friends list. The status for the friends
     *         lists are as follows:
     *         <p>
     *         <p>
     *         Loading: 0
     *         <p>
     *         Connecting: 1
     *         <p>
     *         Loaded: 2
     *         <p>
     *         <p>
     * @return an instance of this encoder.
     */
    public PacketEncoder sendPrivateMessageListStatus(int code) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(221, player.getSession().getEncryptor());
        out.put(code);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends a player to the friend list.
     *
     * @param name
     *         the player's name to add to the list.
     * @param online
     *         if the player is online or not.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendPrivateMessageFriend(long name, boolean online) {
        int value = online ? 1 : 0;
        if (value != 0)
            value += 9;
        DataBuffer out = DataBuffer.create();
        out.newPacket(50, player.getSession().getEncryptor());
        out.putLong(name);
        out.put(value);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends a hint arrow on a position.
     *
     * @param position
     *         the position to send the arrow on.
     * @param direction
     *         the direction on the position to send the arrow on. The
     *         possible directions to put the arrow on are as follows:
     *         <p>
     *         <p>
     *         Middle: 2
     *         <p>
     *         West: 3
     *         <p>
     *         East: 4
     *         <p>
     *         South: 5
     *         <p>
     *         North: 6
     *         <p>
     *         <p>
     * @return an instance of this encoder.
     */
    public PacketEncoder sendPositionHintArrow(Position position, int direction) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(254, player.getSession().getEncryptor());
        out.put(direction);
        out.putShort(position.getX());
        out.putShort(position.getY());
        out.put(position.getZ());
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends a hint arrow on {@code character}.
     *
     * @param character
     *         the character to send a hint arrow on.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendCharacterHintArrow(CharacterNode character) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(254, player.getSession().getEncryptor()).put(character.getType() == NodeType.NPC ? 1 : 10);
        out.putShort(character.getSlot());
        out.put(0);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends a private message to another player.
     *
     * @param name
     *         the name of the player you are sending the message to.
     * @param rights
     *         the rights the player sending the message has.
     * @param message
     *         the actual message compressed into bytes.
     * @param size
     *         the size of the message being sent.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendPrivateMessage(long name, int rights, byte[] message, int size) {
        DataBuffer out = DataBuffer.create();
        out.newVarPacket(196, player.getSession().getEncryptor());
        out.putLong(name);
        out.putInt(player.getPrivateMessage().getLastMessage().getAndIncrement());
        out.put(rights);
        out.putBytes(message, size);
        out.endVarPacket();
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends the players current coordinates to the client.
     *
     * @param position
     *         the coordinates to send to the client.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendCoordinates(Position position) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(85, player.getSession().getEncryptor());
        out.put(position.getY() - (player.getCurrentRegion().getRegionY() * 8), ValueType.C);
        out.put(position.getX() - (player.getCurrentRegion().getRegionX() * 8), ValueType.C);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that opens a walkable interface for the underlying player.
     *
     * @param id
     *         the identification of the interface to open.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendWalkable(int id) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(208, player.getSession().getEncryptor());
        out.putShort(id, ByteOrder.LITTLE);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that spawns a ground item.
     *
     * @param item
     *         the ground item to spawn.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendGroundItem(ItemNode item) {
        sendCoordinates(item.getPosition());
        DataBuffer out = DataBuffer.create();
        out.newPacket(44, player.getSession().getEncryptor());
        out.putShort(item.getItem().getId(), ValueType.A, ByteOrder.LITTLE);
        out.putShort(item.getItem().getAmount());
        out.put(0);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that removes a ground item.
     *
     * @param item
     *         the ground item to remove.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendRemoveGroundItem(ItemNode item) {
        sendCoordinates(item.getPosition());
        DataBuffer out = DataBuffer.create();
        out.newPacket(156, player.getSession().getEncryptor());
        out.put(0, ValueType.S);
        out.putShort(item.getItem().getId());
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends the player context menus.
     *
     * @param slot
     *         the slot for the option to be placed in.
     * @param option
     *         the string literal option to display.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendContextMenu(int slot, String option) {
        DataBuffer out = DataBuffer.create();
        out.newVarPacket(104, player.getSession().getEncryptor());
        out.put(slot, com.asteria.network.ValueType.C);
        out.put(0, com.asteria.network.ValueType.A);
        out.putString(option);
        out.endVarPacket();
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that attaches text to an interface.
     *
     * @param text
     *         the text to attach to the interface.
     * @param id
     *         the identification for the interface.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendString(String text, int id) {
        DataBuffer out = DataBuffer.create();
        out.newVarShortPacket(126, player.getSession().getEncryptor());
        out.putString(text);
        out.putShort(id, ValueType.A);
        out.endVarShortPacket();
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that opens an interface and displays another interface over
     * the inventory area.
     *
     * @param open
     *         the interface to open.
     * @param overlay
     *         the interface to send on the inventory area.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendInventoryInterface(int open, int overlay) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(248, player.getSession().getEncryptor());
        out.putShort(open, com.asteria.network.ValueType.A);
        out.putShort(overlay);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that opens an interface for underlying player.
     *
     * @param id
     *         the identification number of the interface to open.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendInterface(int id) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(97, player.getSession().getEncryptor());
        out.putShort(id);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends the underlying player a message to the chatbox.
     *
     * @param message
     *         the message to send.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendMessage(String message) {
        DataBuffer out = DataBuffer.create();
        out.newVarPacket(253, player.getSession().getEncryptor());
        out.putString(message);
        out.endVarPacket();
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends an interface to a certain sidebar.
     *
     * @param sidebar
     *         the sidebar to send the interface on.
     * @param id
     *         the interface to send on the sidebar.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendSidebarInterface(int sidebar, int id) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(71, player.getSession().getEncryptor());
        out.putShort(id);
        out.put(sidebar, com.asteria.network.ValueType.A);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends the current map region.
     *
     * @return an instance of this encoder.
     */
    public PacketEncoder sendMapRegion() {
        player.setCurrentRegion(player.getPosition().copy());
        player.setNeedsPlacement(true);
        player.setUpdateRegion(true);
        DataBuffer out = DataBuffer.create();
        out.newPacket(73, player.getSession().getEncryptor());
        out.putShort(player.getPosition().getRegionX() + 6, com.asteria.network.ValueType.A);
        out.putShort(player.getPosition().getRegionY() + 6);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that disconnects the underlying player.
     *
     * @return an instance of this encoder.
     */
    public PacketEncoder sendLogout() {
        DataBuffer out = DataBuffer.create();
        out.newPacket(109, player.getSession().getEncryptor());
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that sends the slot and membership status to the client.
     *
     * @return an instance of this encoder.
     */
    public PacketEncoder sendDetails() {
        DataBuffer out = DataBuffer.create();
        out.newPacket(249, player.getSession().getEncryptor());
        out.put(1, ValueType.A);
        out.putShort(player.getSlot(), ValueType.A, ByteOrder.LITTLE);
        player.getSession().send(out);
        return this;
    }

    /**
     * The packet that shows an interface in the chat box.
     *
     * @param id
     *         the identification of interface to show.
     * @return an instance of this encoder.
     */
    public PacketEncoder sendChatInterface(int id) {
        DataBuffer out = DataBuffer.create();
        out.newPacket(164, player.getSession().getEncryptor());
        out.putShort(id, ByteOrder.LITTLE);
        player.getSession().send(out);
        return this;
    }
}
