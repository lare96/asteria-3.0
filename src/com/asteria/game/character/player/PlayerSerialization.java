package com.asteria.game.character.player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.asteria.game.character.MovementQueue;
import com.asteria.game.character.combat.weapon.FightType;
import com.asteria.game.character.player.content.Spellbook;
import com.asteria.game.character.player.login.LoginResponse;
import com.asteria.game.character.player.skill.Skill;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;
import com.asteria.game.item.container.Bank;
import com.asteria.game.item.container.Equipment;
import com.asteria.game.item.container.Inventory;
import com.asteria.game.location.Position;
import com.asteria.utility.Counter;
import com.asteria.utility.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The serializer that will serialize and deserialize character files for
 * players.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class PlayerSerialization {

    /**
     * The linked hash collection of tokens that will be serialized and
     * deserialized. A linked hash set is used here to ensure that there is only
     * one of each token, and to preserve order.
     */
    private final Set<TokenSerializer> tokens = new LinkedHashSet<>();

    /**
     * The player this serializer is dedicated to.
     */
    private final Player player;

    /**
     * The character file that corresponds to this player.
     */
    private final File cf;

    /**
     * Creates a new {@link PlayerSerialization}.
     * 
     * @param player
     *            the player this serializer is dedicated to.
     */
    public PlayerSerialization(Player player) {
        this.player = player;
        this.cf = Paths.get("./data/players/" + player.getUsername() + ".json").toFile();
        createTokens();
    }

    /**
     * The function where all of the tokens are added to the linked hash
     * collection. Add as many tokens here as needed but keep in mind tokens
     * cannot have the same name.<br>
     * The token serialization format is as follows:<br>
     * 
     * <pre>
     * tokens.add(new TokenSerializer(NAME_OF_TOKEN, SERIALIZATION, DESERIALIZATION));
     * </pre>
     * 
     * For those who are still confused, here is an example. Lets say we want
     * "deathCount" to be saved to and loaded from the character file:<br>
     * 
     * <pre>
     * private int deathCount;
     * 
     * public void setDeathCount(int deathCount) {
     *     this.deathCount = deathCount;
     * }
     * 
     * public int getDeathCount() {
     *     return deathCount;
     * }
     * </pre>
     * 
     * We would be able to do it like this:<br>
     * 
     * <pre>
     * tokens.add(new TokenSerializer(&quot;death-count&quot;, player.getDeathCount(), n -&gt; player.setDeathCount(n.getAsInt())));
     * </pre>
     */
    private void createTokens() {
        Gson b = new GsonBuilder().create();
        Player p = player;
        tokens.add(new TokenSerializer("username", p.getUsername(), n -> p.setUsername(n.getAsString())));
        tokens.add(new TokenSerializer("password", p.getPassword(), n -> p.setPassword(n.getAsString())));
        tokens.add(new TokenSerializer("position", p.getPosition(), n -> p.setPosition(b.fromJson(n, Position.class))));
        tokens.add(new TokenSerializer("rights", p.getRights(), n -> p.setRights(Rights.valueOf(n.getAsString()))));
        tokens.add(new TokenSerializer("gender", p.getGender(), n -> p.setGender(n.getAsInt())));
        int[] appearance = p.getAppearance();
        tokens.add(new TokenSerializer("appearance", appearance, n -> Utility.dump(b.fromJson(n, int[].class), appearance)));
        tokens.add(new TokenSerializer("colors", p.getColors(), n -> Utility.dump(b.fromJson(n, int[].class), p.getColors())));
        MovementQueue movement = p.getMovementQueue();
        tokens.add(new TokenSerializer("running", movement.isRunning(), n -> movement.setRunning(n.getAsBoolean())));
        tokens.add(new TokenSerializer("new-player", p.isNewPlayer(), n -> p.setNewPlayer(n.getAsBoolean())));
        Inventory inventory = p.getInventory();
        tokens.add(new TokenSerializer("inventory", inventory.container(), n -> inventory.setItems(b.fromJson(n, Item[].class))));
        Bank bank = p.getBank();
        tokens.add(new TokenSerializer("bank", bank.container(), n -> bank.setItems(b.fromJson(n, Item[].class))));
        Equipment equipment = p.getEquipment();
        tokens.add(new TokenSerializer("equipment", equipment.container(), n -> equipment.setItems(b.fromJson(n, Item[].class))));
        Set<Long> f = p.getFriends();
        tokens.add(new TokenSerializer("friends", f.toArray(), n -> Collections.addAll(f, b.fromJson(n, Long[].class))));
        Set<Long> i = p.getIgnores();
        tokens.add(new TokenSerializer("ignores", i.toArray(), n -> Collections.addAll(i, b.fromJson(n, Long[].class))));
        Counter energy = p.getRunEnergy();
        tokens.add(new TokenSerializer("run-energy", energy.get(), n -> energy.set(n.getAsInt())));
        Spellbook book = p.getSpellbook();
        tokens.add(new TokenSerializer("spellbook", book.name(), n -> p.setSpellbook(Spellbook.valueOf(n.getAsString()))));
        tokens.add(new TokenSerializer("account-banned", p.isBanned(), n -> p.setBanned(n.getAsBoolean())));
        tokens.add(new TokenSerializer("auto-retaliate", p.isAutoRetaliate(), n -> p.setAutoRetaliate(n.getAsBoolean())));
        FightType type = p.getFightType();
        tokens.add(new TokenSerializer("fight-type", type.name(), n -> p.setFightType(FightType.valueOf(n.getAsString()))));
        Counter skulled = p.getSkullTimer();
        tokens.add(new TokenSerializer("skull-timer", skulled.get(), n -> skulled.set(n.getAsInt())));
        tokens.add(new TokenSerializer("accept-aid", p.isAcceptAid(), n -> p.setAcceptAid(n.getAsBoolean())));
        tokens.add(new TokenSerializer("poison-damage", p.getPoisonDamage(), n -> p.setPoisonDamage(n.getAsInt())));
        Counter teleblocked = p.getTeleblockTimer();
        tokens.add(new TokenSerializer("teleblock-timer", teleblocked.get(), n -> teleblocked.set(n.getAsInt())));
        Counter percentage = p.getSpecialPercentage();
        tokens.add(new TokenSerializer("special-amount", percentage.get(), n -> percentage.set(n.getAsInt())));
        tokens.add(new TokenSerializer("skills", p.getSkills(), n -> Utility.dump(b.fromJson(n, Skill[].class), p.getSkills())));
    }

    /**
     * Serializes the dedicated player into a {@code JSON} file.
     */
    public void serialize() {
        try {
            cf.getParentFile().setWritable(true);
            if (!cf.getParentFile().exists()) {
                try {
                    cf.getParentFile().mkdirs();
                } catch (SecurityException e) {
                    throw new IllegalStateException("Unable to create directory for character files!");
                }
            }
            try (FileWriter out = new FileWriter(cf)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject obj = new JsonObject();
                tokens.stream().forEach(t -> obj.add(t.getName(), gson.toJsonTree(t.getToJson())));
                out.write(gson.toJson(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserializes the dedicated player from a {@code JSON} file.
     * 
     * @param password
     *            the password that will be used to validate if the player has
     *            the right credentials.
     * @return the login response determined by what happened before, during,
     *         and after deserialization.
     */
    public LoginResponse deserialize(String password) {
        try {
            cf.setReadable(true);
            if (!cf.exists()) {
                Skills.create(player);
                return LoginResponse.NORMAL;
            }
            try (FileReader in = new FileReader(cf)) {
                JsonObject reader = (JsonObject) new JsonParser().parse(in);
                tokens.stream().filter(t -> reader.has(t.getName())).forEach(t -> t.getFromJson().accept(reader.get(t.getName())));
            }
            if (!password.equals(player.getPassword()))
                return LoginResponse.INVALID_CREDENTIALS;
            if (player.isBanned())
                return LoginResponse.ACCOUNT_DISABLED;
        } catch (Exception e) {
            e.printStackTrace();
            return LoginResponse.COULD_NOT_COMPLETE_LOGIN;
        }
        return LoginResponse.NORMAL;
    }

    /**
     * The container that represents a token that can be both serialized and
     * deserialized.
     * 
     * @author lare96 <http://www.rune-server.org/members/lare96/>
     */
    private static final class TokenSerializer {

        /**
         * The name of this serializable token.
         */
        private final String name;

        /**
         * The {@code Object} being serialized by this token.
         */
        private final Object toJson;

        /**
         * The deserialization consumer for this token.
         */
        private final Consumer<JsonElement> fromJson;

        /**
         * Creates a new {@link TokenSerializer}.
         * 
         * @param name
         *            the name of this serializable token.
         * @param toJson
         *            the {@code Object} being serialized by this token.
         * @param fromJson
         *            the deserialization consumer for this token.
         */
        public TokenSerializer(String name, Object toJson, Consumer<JsonElement> fromJson) {
            this.name = name;
            this.toJson = toJson;
            this.fromJson = fromJson;
        }

        @Override
        public String toString() {
            return "TOKEN[name= " + name + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (!(obj instanceof TokenSerializer))
                return false;
            TokenSerializer other = (TokenSerializer) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

        /**
         * Gets the name of this serializable token.
         * 
         * @return the name of this token.
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the {@code Object} being serialized by this token.
         * 
         * @return the serializable object.
         */
        public Object getToJson() {
            return toJson;
        }

        /**
         * Gets the deserialization consumer for this token.
         * 
         * @return the deserialization consumer.
         */
        public Consumer<JsonElement> getFromJson() {
            return fromJson;
        }
    }
}
