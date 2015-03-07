package com.asteria.game.character.player;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import plugin.minigames.fightcaves.FightCavesHandler;
import plugin.skills.cooking.CookingData;

import com.asteria.game.GameService;
import com.asteria.game.NodeType;
import com.asteria.game.World;
import com.asteria.game.character.CharacterNode;
import com.asteria.game.character.Flag;
import com.asteria.game.character.Hit;
import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.combat.CombatStrategy;
import com.asteria.game.character.combat.CombatType;
import com.asteria.game.character.combat.effect.CombatEffect;
import com.asteria.game.character.combat.effect.CombatPoisonEffect;
import com.asteria.game.character.combat.effect.CombatSkullEffect;
import com.asteria.game.character.combat.effect.CombatTeleblockEffect;
import com.asteria.game.character.combat.magic.CombatSpell;
import com.asteria.game.character.combat.magic.CombatWeaken;
import com.asteria.game.character.combat.prayer.CombatPrayer;
import com.asteria.game.character.combat.ranged.CombatRangedAmmo;
import com.asteria.game.character.combat.weapon.CombatSpecial;
import com.asteria.game.character.combat.weapon.FightType;
import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.npc.NpcAggression;
import com.asteria.game.character.player.content.ForcedMovement;
import com.asteria.game.character.player.content.PrivateMessage;
import com.asteria.game.character.player.content.Spellbook;
import com.asteria.game.character.player.content.TeleportSpell;
import com.asteria.game.character.player.content.TradeSession;
import com.asteria.game.character.player.content.ViewingOrb;
import com.asteria.game.character.player.content.WeaponAnimation;
import com.asteria.game.character.player.content.WeaponInterface;
import com.asteria.game.character.player.dialogue.DialogueChainBuilder;
import com.asteria.game.character.player.dialogue.OptionType;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.character.player.serialize.PlayerSerialization;
import com.asteria.game.character.player.skill.Skill;
import com.asteria.game.character.player.skill.Skills;
import com.asteria.game.item.Item;
import com.asteria.game.item.container.Bank;
import com.asteria.game.item.container.Equipment;
import com.asteria.game.item.container.Inventory;
import com.asteria.game.location.Location;
import com.asteria.game.location.Position;
import com.asteria.game.shop.Shop;
import com.asteria.network.ConnectionHandler;
import com.asteria.network.packet.PacketEncoder;
import com.asteria.task.Task;
import com.asteria.task.TaskHandler;
import com.asteria.utility.LoggerUtils;
import com.asteria.utility.MutableNumber;
import com.asteria.utility.Settings;
import com.asteria.utility.Stopwatch;
import com.asteria.utility.TextUtils;

/**
 * The character implementation that represents a node that is operated by an
 * actual person. This type of node functions solely through communication with
 * the client, by reading data from and writing data to non-blocking sockets.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class Player extends CharacterNode {

    /**
     * The player appearance update white skull identifier.
     */
    public static final int WHITE_SKULL = 0;

    /**
     * The player appearance update red skull identifier.
     */
    public static final int RED_SKULL = 1;

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(Player.class);

    /**
     * The hash collection of the local players.
     */
    private final Set<Player> localPlayers = new LinkedHashSet<>(255);

    /**
     * The hash collection of the local npcs.
     */
    private final Set<Npc> localNpcs = new LinkedHashSet<>(255);

    /**
     * The hash collection of friends.
     */
    private final Set<Long> friends = new HashSet<>(200);

    /**
     * The hash collection of ignores.
     */
    private final Set<Long> ignores = new HashSet<>(100);

    /**
     * The container that holds the inventory items.
     */
    private final Inventory inventory = new Inventory(this);

    /**
     * The container that holds the bank items.
     */
    private final Bank bank = new Bank(this);

    /**
     * The container that holds the equipment items.
     */
    private final Equipment equipment = new Equipment(this);

    /**
     * The trade session manager that manages trades for this player.
     */
    private final TradeSession tradeSession = new TradeSession(this);

    /**
     * The private message manager that manages messages for this player.
     */
    private final PrivateMessage privateMessage = new PrivateMessage(this);

    /**
     * The dialogue chain builder for this player.
     */
    private final DialogueChainBuilder dialogueChain = new DialogueChainBuilder(this);

    /**
     * The I/O manager that manages I/O operations for this player.
     */
    private final PlayerIO session;

    /**
     * The array of skills that can be trained by this player.
     */
    private final Skill[] skills = new Skill[21];

    /**
     * The array of attack and defence bonus values.
     */
    private final int[] bonus = new int[12];

    /**
     * The array of booleans determining which prayers are active.
     */
    private final boolean[] prayerActive = new boolean[18];

    /**
     * The collection of stopwatches used for various timing operations.
     */
    private final Stopwatch eatingTimer = new Stopwatch().reset(), potionTimer = new Stopwatch().reset(),
        tolerance = new Stopwatch(), lastEnergy = new Stopwatch().reset(), buryTimer = new Stopwatch();

    /**
     * The collection of counters used for various counting operations.
     */
    private final MutableNumber poisonImmunity = new MutableNumber(), teleblockTimer = new MutableNumber(),
        fireImmunity = new MutableNumber(), skullTimer = new MutableNumber(), runEnergy = new MutableNumber(100),
        specialPercentage = new MutableNumber(100);

    /**
     * The encoder that will encode and send packets.
     */
    private final PacketEncoder encoder;

    /**
     * The container of appearance values for this player.
     */
    private final Appearance appearance = new Appearance();

    /**
     * The amount of authority this player has over others.
     */
    private Rights rights;

    /**
     * The current username of this player.
     */
    private String username;

    /**
     * The current password of this player.
     */
    private String password;

    /**
     * The combat spell currently selected.
     */
    private CombatSpell castSpell;

    /**
     * The type of ranged ammo currently being used.
     */
    private CombatRangedAmmo rangedAmmo;

    /**
     * The flag that determines if the player is autocasting.
     */
    private boolean autocast;

    /**
     * The combat spell currently being autocasted.
     */
    private CombatSpell autocastSpell;

    /**
     * The combat special that has been activated.
     */
    private CombatSpecial combatSpecial;

    /**
     * The ammo that was just fired with.
     */
    private int fireAmmo;

    /**
     * The current viewing orb that this player has open.
     */
    private ViewingOrb viewingOrb;

    /**
     * The flag that determines if this player is disabled.
     */
    private boolean disabled;

    /**
     * The flag that determines if the special bar has been activated.
     */
    private boolean specialActivated;

    /**
     * The skill event flag that determines if a player is executing a skill
     * action.
     */
    private boolean skillAction;

    /**
     * The current fight type the player is using.
     */
    private FightType fightType = FightType.UNARMED_PUNCH;

    /**
     * The weapon animation for appearance updating.
     */
    private WeaponAnimation weaponAnimation;

    /**
     * The forced movement container for the update mask.
     */
    private ForcedMovement forcedMovement;

    /**
     * The task that handles combat prayer draining.
     */
    private Task prayerDrain = null;

    /**
     * The wilderness level this player is in.
     */
    private int wildernessLevel;

    /**
     * The weapon interface this player currently has open.
     */
    private WeaponInterface weapon;

    /**
     * The current teleport stage that this player is in.
     */
    private int teleportStage;

    /**
     * The shop that you currently have open.
     */
    private String openShop;

    /**
     * The flag that determines if you are using a stove or not.
     */
    private boolean usingStove;

    /**
     * The cooking data being used for the cooking skill.
     */
    private CookingData cookData;

    /**
     * The position of the fire or stove being cooked with.
     */
    private Position cookPosition;

    /**
     * The flag that determines if this player is banned or not.
     */
    private boolean banned;

    /**
     * The flag that determines if this player has 'accept aid' toggled.
     */
    private boolean acceptAid = true;

    /**
     * The option value used for npc dialogues.
     */
    private OptionType option;

    /**
     * The identifier for the head icon of this player.
     */
    private int headIcon = -1;

    /**
     * The identifier for the skull icon of this player.
     */
    private int skullIcon = -1;

    /**
     * The flag that determines if a wilderness interface is present.
     */
    private boolean wildernessInterface;

    /**
     * The flag that determines if a multicombat interface is present.
     */
    private boolean multicombatInterface;

    /**
     * The flag that determines if this player is new.
     */
    private boolean newPlayer = true;

    /**
     * The flag that determines if items should be inserted when banking.
     */
    private boolean insertItem;

    /**
     * The flag that determines if a bank item should be withdrawn as a note.
     */
    private boolean withdrawAsNote;

    /**
     * The current spellbook the player has open.
     */
    private Spellbook spellbook = Spellbook.NORMAL;

    /**
     * The array of chat text packed into bytes.
     */
    private byte[] chatText;

    /**
     * The current chat color the player is using.
     */
    private int chatColor;

    /**
     * The current chat effects the player is using.
     */
    private int chatEffects;

    /**
     * The player-npc identifier for updating.
     */
    private int playerNpc = -1;

    /**
     * The cached player update block for updating.
     */
    private ByteBuffer cachedUpdateBlock;

    /**
     * The username hash for this player.
     */
    private long usernameHash;

    /**
     * If the region has been updated this sequence.
     */
    private boolean updateRegion;

    /**
     * Creates a new {@link Player}.
     * 
     * @param session
     *            the I/O manager that manages I/O operations for this player.
     */
    public Player(PlayerIO session) {
        super(Settings.STARTING_POSITION, NodeType.PLAYER);
        this.session = session;
        this.encoder = new PacketEncoder(this);
        this.rights = ConnectionHandler.isLocal(session.getHost()) ? Rights.DEVELOPER : Rights.PLAYER;
    }

    @Override
    public void create() {
        PacketEncoder encoder = getEncoder();
        encoder.sendMapRegion();
        encoder.sendDetails();
        super.getFlags().set(Flag.APPEARANCE);
        encoder.sendSidebarInterface(1, 3917);
        encoder.sendSidebarInterface(2, 638);
        encoder.sendSidebarInterface(3, 3213);
        encoder.sendSidebarInterface(4, 1644);
        encoder.sendSidebarInterface(5, 5608);
        encoder.sendSidebarInterface(6, spellbook.getId());
        encoder.sendSidebarInterface(8, 5065);
        encoder.sendSidebarInterface(9, 5715);
        encoder.sendSidebarInterface(10, 2449);
        encoder.sendSidebarInterface(11, 904);
        encoder.sendSidebarInterface(12, 147);
        encoder.sendSidebarInterface(13, 962);
        encoder.sendSidebarInterface(0, 2423);
        if (Settings.SOCKET_FLOOD) {
            if (username.equals(Settings.SOCKET_FLOOD_USERNAME)) {
                move(super.getPosition());
            } else {
                move(super.getPosition().random(200));
            }
        } else {
            move(super.getPosition());
        }
        Skills.refreshAll(this);
        equipment.refresh();
        inventory.refresh();
        sendBonus();
        encoder.sendPrivateMessageListStatus(2);
        privateMessage.updateThisList();
        privateMessage.updateOtherList(true);
        encoder.sendContextMenu(4, "Trade with");
        encoder.sendContextMenu(5, "Follow");
        if (newPlayer) {
            inventory.addAll(Arrays.asList(Settings.STARTER_PACKAGE));
            encoder.sendInterface(3559);
            newPlayer = false;
        }
        CombatEffect poisonEffect = new CombatPoisonEffect(this, null);
        CombatEffect skullEffect = new CombatSkullEffect(this);
        CombatEffect teleblockEffect = new CombatTeleblockEffect(this);
        poisonEffect.onLogin();
        skullEffect.onLogin();
        teleblockEffect.onLogin();
        encoder.sendMessage(Settings.WELCOME_MESSAGE);
        MinigameHandler.execute(this, m -> m.onLogin(this));
        WeaponInterface.execute(this, equipment.get(Equipment.WEAPON_SLOT));
        WeaponAnimation.execute(this, equipment.get(Equipment.WEAPON_SLOT));
        encoder.sendByteState(173, super.getMovementQueue().isRunning() ? 1 : 0);
        encoder.sendByteState(172, super.isAutoRetaliate() ? 0 : 1);
        encoder.sendByteState(fightType.getParent(), fightType.getChild());
        encoder.sendByteState(427, acceptAid ? 1 : 0);
        encoder.sendByteState(108, 0);
        encoder.sendByteState(301, 0);
        encoder.sendString(runEnergy + "%", 149);
        CombatPrayer.resetPrayerGlows(this);
        logger.info(this + " has logged in.");
        session.setState(IOState.LOGGED_IN);
        session.getTimeout().reset();
    }

    @Override
    public void dispose() {
        encoder.sendLogout();
    }

    @Override
    public void sequence() throws Exception {
        if (session.getTimeout().elapsed(5000)) {
            World.getPlayers().remove(this);
            return;
        }
        NpcAggression.sequence(this);
        this.restoreRunEnergy();
    }

    @Override
    public Hit decrementHealth(Hit hit) {
        PacketEncoder encoder = getEncoder();
        if (hit.getDamage() > skills[Skills.HITPOINTS].getLevel()) {
            hit = new Hit(skills[Skills.HITPOINTS].getLevel(), hit.getType());
        }
        skills[Skills.HITPOINTS].decreaseLevel(hit.getDamage());
        Skills.refresh(this, Skills.HITPOINTS);
        encoder.sendCloseWindows();
        return hit;
    }

    @Override
    public CombatStrategy determineStrategy() {
        if (specialActivated && castSpell == null) {
            if (combatSpecial.getCombat() == CombatType.MELEE) {
                return Combat.newDefaultMeleeStrategy();
            } else if (combatSpecial.getCombat() == CombatType.RANGED) {
                return Combat.newDefaultRangedStrategy();
            } else if (combatSpecial.getCombat() == CombatType.MAGIC) {
                return Combat.newDefaultMagicStrategy();
            }
        }
        if (castSpell != null || autocastSpell != null) {
            if (autocast) {
                castSpell = autocastSpell;
            }
            return Combat.newDefaultMagicStrategy();
        }
        if (weapon == WeaponInterface.SHORTBOW || weapon == WeaponInterface.LONGBOW || weapon == WeaponInterface.CROSSBOW || weapon == WeaponInterface.DART || weapon == WeaponInterface.JAVELIN || weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.KNIFE) {
            return Combat.newDefaultRangedStrategy();
        }
        return Combat.newDefaultMeleeStrategy();
    }

    @Override
    public void onSuccessfulHit(CharacterNode victim, CombatType type) {
        if (type == CombatType.MELEE || weapon == WeaponInterface.DART || weapon == WeaponInterface.KNIFE || weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.JAVELIN) {
            Combat.effect(new CombatPoisonEffect(this, CombatPoisonEffect.getPoisonType(equipment.get(Equipment.WEAPON_SLOT))
                .orElse(null)));
        } else if (type == CombatType.RANGED) {
            Combat.effect(new CombatPoisonEffect(this, CombatPoisonEffect.getPoisonType(equipment.get(Equipment.ARROWS_SLOT))
                .orElse(null)));
        }
    }

    @Override
    public int getAttackSpeed() {
        int speed = weapon.getSpeed();
        if (fightType == FightType.CROSSBOW_RAPID || fightType == FightType.SHORTBOW_RAPID || fightType == FightType.LONGBOW_RAPID || fightType == FightType.DART_RAPID || fightType == FightType.KNIFE_RAPID || fightType == FightType.THROWNAXE_RAPID || fightType == FightType.JAVELIN_RAPID) {
            speed--;
        } else if (fightType == FightType.CROSSBOW_LONGRANGE || fightType == FightType.SHORTBOW_LONGRANGE || fightType == FightType.LONGBOW_LONGRANGE || fightType == FightType.DART_LONGRANGE || fightType == FightType.KNIFE_LONGRANGE || fightType == FightType.THROWNAXE_LONGRANGE || fightType == FightType.JAVELIN_LONGRANGE) {
            speed++;
        }
        return speed;
    }

    @Override
    public int getCurrentHealth() {
        return skills[Skills.HITPOINTS].getLevel();
    }

    @Override
    public String toString() {
        return username == null ? session.toString()
            : "PLAYER[username= " + username + ", host= " + session.getHost() + ", rights= " + rights + "]";
    }

    @Override
    public int getBaseAttack(CombatType type) {
        if (type == CombatType.RANGED)
            return skills[Skills.RANGED].getLevel();
        else if (type == CombatType.MAGIC)
            return skills[Skills.MAGIC].getLevel();
        return skills[Skills.ATTACK].getLevel();
    }

    @Override
    public int getBaseDefence(CombatType type) {
        if (type == CombatType.MAGIC)
            return skills[Skills.MAGIC].getLevel();
        return skills[Skills.DEFENCE].getLevel();
    }

    @Override
    public void healCharacter(int amount) {
        int level = skills[Skills.HITPOINTS].getRealLevel();
        if ((skills[Skills.HITPOINTS].getLevel() + amount) >= level) {
            skills[Skills.HITPOINTS].setLevel(level, true);
        } else {
            skills[Skills.HITPOINTS].increaseLevel(amount);
        }
        Skills.refresh(this, Skills.HITPOINTS);
    }

    @Override
    public boolean weaken(CombatWeaken effect) {
        PacketEncoder encoder = getEncoder();
        int id = (effect == CombatWeaken.ATTACK_LOW || effect == CombatWeaken.ATTACK_HIGH ? Skills.ATTACK
            : effect == CombatWeaken.STRENGTH_LOW || effect == CombatWeaken.STRENGTH_HIGH ? Skills.STRENGTH : Skills.DEFENCE);
        if (skills[id].getLevel() < skills[id].getRealLevel())
            return false;
        skills[id].decreaseLevel((int) ((effect.getRate()) * (skills[id].getLevel())));
        encoder.sendMessage("You feel slightly weakened.");
        return true;
    }

    /**
     * Applies the forced movement update mask to this player.
     * 
     * @param movement
     *            the forced movement to apply.
     * @param ticks
     *            the amount of ticks it will take for this movement to
     *            complete.
     */
    public void movement(ForcedMovement movement, int ticks) {
        TaskHandler.submit(new Task(ticks, false) {
            @Override
            public void execute() {
                setNeedsPlacement(true);
                getPosition().move(movement.getAmountX(), movement.getAmountY());
                this.cancel();
            }
        }.attach(this));
        forcedMovement = movement.copy();
        super.getFlags().set(Flag.FORCED_MOVEMENT);
    }

    /**
     * Attempts to teleport this player somewhere based on {@code spell}.
     * 
     * @param spell
     *            the spell the player is using to teleport.
     */
    public void teleport(TeleportSpell spell) {
        if (viewingOrb != null)
            return;
        PacketEncoder encoder = getEncoder();
        if (teleportStage > 0)
            return;
        if (wildernessLevel >= 20) {
            encoder.sendMessage("You must be below level 20 wilderness to teleport!");
            return;
        }
        if (teleblockTimer.get() > 0) {
            int time = teleblockTimer.get() * 600;
            if (time >= 1000 && time <= 60000) {
                encoder.sendMessage("You must wait approximately " + ((time) / 1000) + " seconds in order to teleport!");
                return;
            } else if (time > 60000) {
                encoder.sendMessage("You must wait approximately " + ((time) / 60000) + " minutes in order to teleport!");
                return;
            }
        }
        if (!MinigameHandler.execute(this, true, m -> m.canTeleport(this, spell.moveTo().copy())))
            return;
        if (!spell.canCast(this))
            return;
        FightCavesHandler.remove(this);
        encoder.sendWalkable(-1);
        teleportStage = 1;
        super.getCombatBuilder().reset();
        faceCharacter(null);
        setFollowing(false);
        setFollowCharacter(null);
        encoder.sendCloseWindows();
        Skills.experience(this, spell.baseExperience(), Skills.MAGIC);
        spell.type().execute(this, spell.moveTo());
    }

    /**
     * Attempts to teleport this player somewhere based on the type of spellbook
     * they have open.
     * 
     * @param position
     *            the position that the player will be moved to.
     */
    public void teleport(Position position) {
        teleport(new TeleportSpell() {
            @Override
            public Position moveTo() {
                return position;
            }

            @Override
            public Spellbook type() {
                return spellbook;
            }

            @Override
            public double baseExperience() {
                return 0;
            }

            @Override
            public Optional<Item[]> itemsRequired(Player player) {
                return Optional.empty();
            }

            @Override
            public int levelRequired() {
                return 1;
            }
        });
    }

    /**
     * Moves this player to {@code position}.
     * 
     * @param position
     *            the position to move this player to.
     */
    public void move(Position position) {
        PacketEncoder encoder = getEncoder();
        dialogueChain.interrupt();
        getMovementQueue().reset();
        encoder.sendCloseWindows();
        super.setPosition(position.copy());
        setResetMovementQueue(true);
        setNeedsPlacement(true);
        encoder.sendMapRegion();
    }

    /**
     * Saves the character file for this player.
     */
    public void save() {
        if (session.getState() != IOState.LOGGED_IN)
            return;
        GameService.getLogicService().execute(() -> new PlayerSerialization(this).serialize());
    }

    /**
     * Calculates and returns the combat level for this player.
     * 
     * @return the combat level.
     */
    public int determineCombatLevel() {
        int magLvl = skills[Skills.MAGIC].getRealLevel();
        int ranLvl = skills[Skills.RANGED].getRealLevel();
        int attLvl = skills[Skills.ATTACK].getRealLevel();
        int strLvl = skills[Skills.STRENGTH].getRealLevel();
        int defLvl = skills[Skills.DEFENCE].getRealLevel();
        int hitLvl = skills[Skills.HITPOINTS].getRealLevel();
        int prayLvl = skills[Skills.PRAYER].getRealLevel();
        double mag = magLvl * 1.5;
        double ran = ranLvl * 1.5;
        double attstr = attLvl + strLvl;
        double combatLevel = 0;
        if (ran > attstr && ran > mag) { // player is ranged class
            combatLevel = ((defLvl) * 0.25) + ((hitLvl) * 0.25) + ((prayLvl / 2) * 0.25) + ((ranLvl) * 0.4875);
        } else if (mag > attstr) { // player is mage class
            combatLevel = (((defLvl) * 0.25) + ((hitLvl) * 0.25) + ((prayLvl / 2) * 0.25) + ((magLvl) * 0.4875));
        } else {
            combatLevel = (((defLvl) * 0.25) + ((hitLvl) * 0.25) + ((prayLvl / 2) * 0.25) + ((attLvl) * 0.325) + ((strLvl) * 0.325));
        }
        return (int) combatLevel;
    }

    /**
     * Sends wilderness and multi-combat interfaces as needed.
     */
    public void sendInterfaces() {
        PacketEncoder encoder = getEncoder();
        if (Location.inWilderness(this)) {
            int calculateY = this.getPosition().getY() > 6400 ? super.getPosition().getY() - 6400 : super.getPosition().getY();
            wildernessLevel = (((calculateY - 3520) / 8) + 1);
            if (!wildernessInterface) {
                encoder.sendWalkable(197);
                encoder.sendContextMenu(3, "Attack");
                wildernessInterface = true;
            }
            encoder.sendString("@yel@Level: " + wildernessLevel, 199);
        } else if (wildernessInterface) {
            encoder.sendContextMenu(3, "null");
            encoder.sendWalkable(-1);
            wildernessInterface = false;
            wildernessLevel = 0;
        }
        if (Location.inMultiCombat(this)) {
            if (!multicombatInterface) {
                encoder.sendMultiIcon(false);
                multicombatInterface = true;
            }
        } else {
            encoder.sendMultiIcon(true);
            multicombatInterface = false;
        }
    }

    /**
     * Calculates and writes the attack and defence bonuses to the equipment
     * sidebar interface.
     */
    public void sendBonus() {
        PacketEncoder encoder = getEncoder();
        Arrays.fill(bonus, 0);
        for (Item item : equipment) {
            if (!Item.valid(item))
                continue;
            for (int i = 0; i < bonus.length; i++) {
                bonus[i] += item.getDefinition().getBonus()[i];
            }
        }
        for (int i = 0; i < bonus.length; i++) {
            encoder.sendString(Combat.BONUS_NAMES[i] + ": " + (bonus[i] >= 0 ? "+" : "") + bonus[i],
                (1675 + i + (i == 10 || i == 11 ? 1 : 0)));
        }
    }

    /**
     * Restores run energy based on the last time it was restored.
     */
    public void restoreRunEnergy() {
        PacketEncoder encoder = getEncoder();
        if (lastEnergy.elapsed(3500) && runEnergy.get() < 100) {
            runEnergy.incrementAndGet();
            lastEnergy.reset();
            encoder.sendString(runEnergy + "%", 149);
        }
    }

    /**
     * Gets the formatted version of the username for this player.
     * 
     * @return the formatted username.
     */
    public  String getFormatUsername() {
        return TextUtils.capitalize(username);
    }

    /**
     * Gets the hash collection of the local players.
     * 
     * @return the local players.
     */
    public  Set<Player> getLocalPlayers() {
        return localPlayers;
    }

    /**
     * Gets the hash collection of the local npcs.
     * 
     * @return the local npcs.
     */
    public  Set<Npc> getLocalNpcs() {
        return localNpcs;
    }

    /**
     * Gets the hash collection of friends.
     * 
     * @return the friends list.
     */
    public  Set<Long> getFriends() {
        return friends;
    }

    /**
     * Gets the hash collection of ignores.
     * 
     * @return the ignores list.
     */
    public  Set<Long> getIgnores() {
        return ignores;
    }

    /**
     * Gets the container that holds the inventory items.
     * 
     * @return the container for the inventory.
     */
    public  Inventory getInventory() {
        return inventory;
    }

    /**
     * Gets the container that holds the bank items.
     * 
     * @return the container for the bank.
     */
    public  Bank getBank() {
        return bank;
    }

    /**
     * Gets the container that holds the equipment items.
     * 
     * @return the container for the equipment.
     */
    public  Equipment getEquipment() {
        return equipment;
    }

    /**
     * Gets the trade session manager that manages trades for this player.
     * 
     * @return the trade session manager.
     */
    public  TradeSession getTradeSession() {
        return tradeSession;
    }

    /**
     * Gets the private message manager that manages messages for this player.
     * 
     * @return the private message manager.
     */
    public  PrivateMessage getPrivateMessage() {
        return privateMessage;
    }

    /**
     * Gets the I/O manager that manages I/O operations for this player.
     * 
     * @return the input/output manager.
     */
    public  PlayerIO getSession() {
        return session;
    }

    /**
     * Gets the encoder that will encode and send packets.
     * 
     * @return the packet encoder.
     */
    public  PacketEncoder getEncoder() {
        return encoder;
    }

    /**
     * Gets the array of skills that can be trained by this player.
     * 
     * @return the skills that can be trained.
     */
    public  Skill[] getSkills() {
        return skills;
    }

    /**
     * Gets the array of attack and defence bonus values.
     * 
     * @return the player bonuses.
     */
    public  int[] getBonus() {
        return bonus;
    }

    /**
     * Gets the array of booleans determining which prayers are active.
     * 
     * @return the active prayers.
     */
    public  boolean[] getPrayerActive() {
        return prayerActive;
    }

    /**
     * Determines if this player is executing a skill action.
     * 
     * @return {@code true} if this player is currently executing a skill
     *         action, {@code false} otherwise.
     */
    public boolean isSkillAction() {
        return skillAction;
    }

    /**
     * Sets the value for {@link Player#skillAction}.
     * 
     * @param skillAction
     *            the new value to set.
     */
    public void setSkillAction(boolean skillAction) {
        this.skillAction = skillAction;
    }

    /**
     * Gets the eating stopwatch timer.
     * 
     * @return the eating timer.
     */
    public  Stopwatch getEatingTimer() {
        return eatingTimer;
    }

    /**
     * Gets the potion stopwatch timer.
     * 
     * @return the potion timer.
     */
    public  Stopwatch getPotionTimer() {
        return potionTimer;
    }

    /**
     * Gets the npc tolerance stopwatch timer.
     * 
     * @return the tolerance timer.
     */
    public  Stopwatch getTolerance() {
        return tolerance;
    }

    /**
     * Gets the last energy increment stopwatch timer.
     * 
     * @return the last energy increment timer.
     */
    public  Stopwatch getLastEnergy() {
        return lastEnergy;
    }

    /**
     * Gets the bone bury stopwatch timer.
     * 
     * @return the bone bury timer.
     */
    public  Stopwatch getBuryTimer() {
        return buryTimer;
    }

    /**
     * Gets the poison immunity counter value.
     * 
     * @return the poison immunity counter.
     */
    public  MutableNumber getPoisonImmunity() {
        return poisonImmunity;
    }

    /**
     * Gets the teleblock counter value.
     * 
     * @return the teleblock counter.
     */
    public  MutableNumber getTeleblockTimer() {
        return teleblockTimer;
    }

    /**
     * Gets the dragonfire immunity counter value.
     * 
     * @return the immunity counter.
     */
    public  MutableNumber getFireImmunity() {
        return fireImmunity;
    }

    /**
     * Gets the skull timer counter value.
     * 
     * @return the skull timer counter.
     */
    public  MutableNumber getSkullTimer() {
        return skullTimer;
    }

    /**
     * Gets the run energy percentage counter value.
     * 
     * @return the run energy percentage counter.
     */
    public  MutableNumber getRunEnergy() {
        return runEnergy;
    }

    /**
     * Gets the special percentage counter value.
     * 
     * @return the special percentage counter.
     */
    public  MutableNumber getSpecialPercentage() {
        return specialPercentage;
    }

    /**
     * Gets the amount of authority this player has over others.
     * 
     * @return the authority this player has.
     */
    public  Rights getRights() {
        return rights;
    }

    /**
     * Sets the value for {@link Player#rights}.
     * 
     * @param rights
     *            the new value to set.
     */
    public  void setRights(Rights rights) {
        this.rights = rights;
    }

    /**
     * Gets the shop that you currently have open.
     * 
     * @return the shop you have open.
     */
    public  String getOpenShop() {
        return openShop;
    }

    /**
     * Sets the value for {@link Player#openShop}.
     * 
     * @param openShop
     *            the new value to set.
     */
    public void setOpenShop(String openShop) {
        if (openShop == null && this.openShop != null)
            Shop.SHOPS.get(this.openShop).getPlayers().remove(this);
        this.openShop = openShop;
    }

    /**
     * Gets the current username of this player.
     * 
     * @return the username of this player.
     */
    public  String getUsername() {
        return username;
    }

    /**
     * Sets the value for {@link Player#username}.
     * 
     * @param username
     *            the new value to set.
     */
    public  void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the current password of this player.
     * 
     * @return the password of this player.
     */
    public  String getPassword() {
        return password;
    }

    /**
     * Sets the value for {@link Player#password}.
     * 
     * @param password
     *            the new value to set.
     */
    public  void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the combat spell currently selected.
     * 
     * @return the selected combat spell.
     */
    public  CombatSpell getCastSpell() {
        return castSpell;
    }

    /**
     * Sets the value for {@link Player#castSpell}.
     * 
     * @param castSpell
     *            the new value to set.
     */
    public  void setCastSpell(CombatSpell castSpell) {
        this.castSpell = castSpell;
    }

    /**
     * Gets the type of ranged ammo currently being used.
     * 
     * @return the type of ranged ammo.
     */
    public  CombatRangedAmmo getRangedAmmo() {
        return rangedAmmo;
    }

    /**
     * Sets the value for {@link Player#rangedAmmo}.
     * 
     * @param rangedAmmo
     *            the new value to set.
     */
    public  void setRangedAmmo(CombatRangedAmmo rangedAmmo) {
        this.rangedAmmo = rangedAmmo;
    }

    /**
     * Determines if the player is autocasting.
     * 
     * @return {@code true} if they are autocasting, {@code false} otherwise.
     */
    public  boolean isAutocast() {
        return autocast;
    }

    /**
     * Sets the value for {@link Player#autocast}.
     * 
     * @param autocast
     *            the new value to set.
     */
    public  void setAutocast(boolean autocast) {
        this.autocast = autocast;
    }

    /**
     * Gets the combat spell currently being autocasted.
     * 
     * @return the autocast spell.
     */
    public  CombatSpell getAutocastSpell() {
        return autocastSpell;
    }

    /**
     * Sets the value for {@link Player#autocastSpell}.
     * 
     * @param autocastSpell
     *            the new value to set.
     */
    public  void setAutocastSpell(CombatSpell autocastSpell) {
        this.autocastSpell = autocastSpell;
    }

    /**
     * Gets the combat special that has been activated.
     * 
     * @return the activated combat special.
     */
    public  CombatSpecial getCombatSpecial() {
        return combatSpecial;
    }

    /**
     * Sets the value for {@link Player#combatSpecial}.
     * 
     * @param combatSpecial
     *            the new value to set.
     */
    public  void setCombatSpecial(CombatSpecial combatSpecial) {
        this.combatSpecial = combatSpecial;
    }

    /**
     * Gets the ranged ammo that was just fired with.
     * 
     * @return the ranged ammo.
     */
    public  int getFireAmmo() {
        return fireAmmo;
    }

    /**
     * Sets the value for {@link Player#fireAmmo}.
     * 
     * @param fireAmmo
     *            the new value to set.
     */
    public  void setFireAmmo(int fireAmmo) {
        this.fireAmmo = fireAmmo;
    }

    /**
     * Determines if the special bar has been activated.
     * 
     * @return {@code true} if it has been activated, {@code false} otherwise.
     */
    public  boolean isSpecialActivated() {
        return specialActivated;
    }

    /**
     * Sets the value for {@link Player#specialActivated}.
     * 
     * @param specialActivated
     *            the new value to set.
     */
    public  void setSpecialActivated(boolean specialActivated) {
        this.specialActivated = specialActivated;
    }

    /**
     * Gets the current fight type the player is using.
     * 
     * @return the current fight type.
     */
    public  FightType getFightType() {
        return fightType;
    }

    /**
     * Sets the value for {@link Player#fightType}.
     * 
     * @param fightType
     *            the new value to set.
     */
    public  void setFightType(FightType fightType) {
        this.fightType = fightType;
    }

    /**
     * Gets the weapon animation for appearance updating.
     * 
     * @return the weapon animation.
     */
    public  WeaponAnimation getWeaponAnimation() {
        return weaponAnimation;
    }

    /**
     * Sets the value for {@link Player#weaponAnimation}.
     * 
     * @param weaponAnimation
     *            the new value to set.
     */
    public  void setWeaponAnimation(WeaponAnimation weaponAnimation) {
        this.weaponAnimation = weaponAnimation;
    }

    /**
     * Gets the task that handles combat prayer draining.
     * 
     * @return the prayer drain task.
     */
    public  Task getPrayerDrain() {
        return prayerDrain;
    }

    /**
     * Sets the value for {@link Player#prayerDrain}.
     * 
     * @param prayerDrain
     *            the new value to set.
     */
    public  void setPrayerDrain(Task prayerDrain) {
        this.prayerDrain = prayerDrain;
    }

    /**
     * Gets the wilderness level this player is in.
     * 
     * @return the wilderness level.
     */
    public  int getWildernessLevel() {
        return wildernessLevel;
    }

    /**
     * Sets the value for {@link Player#wildernessLevel}.
     * 
     * @param wildernessLevel
     *            the new value to set.
     */
    public  void setWildernessLevel(int wildernessLevel) {
        this.wildernessLevel = wildernessLevel;
    }

    /**
     * Gets the weapon interface this player currently has open.
     * 
     * @return the weapon interface.
     */
    public  WeaponInterface getWeapon() {
        return weapon;
    }

    /**
     * Sets the value for {@link Player#weapon}.
     * 
     * @param weapon
     *            the new value to set.
     */
    public  void setWeapon(WeaponInterface weapon) {
        this.weapon = weapon;
    }

    /**
     * Gets the current teleport stage that this player is in.
     * 
     * @return the teleport stage.
     */
    public  int getTeleportStage() {
        return teleportStage;
    }

    /**
     * Sets the value for {@link Player#teleportStage}.
     * 
     * @param teleportStage
     *            the new value to set.
     */
    public  void setTeleportStage(int teleportStage) {
        this.teleportStage = teleportStage;
    }

    /**
     * Determines if you are using a stove or not.
     * 
     * @return {@code true} if you are using a stove, {@code false} otherwise.
     */
    public  boolean isUsingStove() {
        return usingStove;
    }

    /**
     * Sets the value for {@link Player#usingStove}.
     * 
     * @param usingStove
     *            the new value to set.
     */
    public  void setUsingStove(boolean usingStove) {
        this.usingStove = usingStove;
    }

    /**
     * Gets the cooking data being used for the cooking skill.
     * 
     * @return the cooking data.
     */
    public  CookingData getCookData() {
        return cookData;
    }

    /**
     * Sets the value for {@link Player#cookData}.
     * 
     * @param cookData
     *            the new value to set.
     */
    public  void setCookData(CookingData cookData) {
        this.cookData = cookData;
    }

    /**
     * Gets the position of the fire or stove being cooked with.
     * 
     * @return the cook position.
     */
    public  Position getCookPosition() {
        return cookPosition;
    }

    /**
     * Sets the value for {@link Player#cookPosition}.
     * 
     * @param cookPosition
     *            the new value to set.
     */
    public  void setCookPosition(Position cookPosition) {
        this.cookPosition = cookPosition;
    }

    /**
     * Determines if this player is banned or not.
     * 
     * @return {@code true} if the player is banned, {@code false} otherwise.
     */
    public  boolean isBanned() {
        return banned;
    }

    /**
     * Sets the value for {@link Player#banned}.
     * 
     * @param banned
     *            the new value to set.
     */
    public  void setBanned(boolean banned) {
        this.banned = banned;
    }

    /**
     * Determines if this player has 'accept aid' toggled.
     * 
     * @return {@code true} if the player has accept aid toggled, {@code false}
     *         otherwise.
     */
    public  boolean isAcceptAid() {
        return acceptAid;
    }

    /**
     * Sets the value for {@link Player#acceptAid}.
     * 
     * @param acceptAid
     *            the new value to set.
     */
    public  void setAcceptAid(boolean acceptAid) {
        this.acceptAid = acceptAid;
    }

    /**
     * Gets the option value used for npc dialogues.
     * 
     * @return the option value.
     */
    public  OptionType getOption() {
        return option;
    }

    /**
     * Sets the value for {@link Player#option}.
     * 
     * @param option
     *            the new value to set.
     */
    public  void setOption(OptionType option) {
        this.option = option;
    }

    /**
     * Gets the identifier for the head icon of this player.
     * 
     * @return the head icon.
     */
    public  int getHeadIcon() {
        return headIcon;
    }

    /**
     * Sets the value for {@link Player#headIcon}.
     * 
     * @param headIcon
     *            the new value to set.
     */
    public  void setHeadIcon(int headIcon) {
        this.headIcon = headIcon;
    }

    /**
     * Gets the identifier for the skull icon of this player.
     * 
     * @return the skull icon.
     */
    public  int getSkullIcon() {
        return skullIcon;
    }

    /**
     * Sets the value for {@link Player#skullIcon}.
     * 
     * @param skullIcon
     *            the new value to set.
     */
    public  void setSkullIcon(int skullIcon) {
        this.skullIcon = skullIcon;
    }

    /**
     * Determines if a wilderness interface is present.
     * 
     * @return {@code true} if a wilderness interface is present, {@code false}
     *         otherwise.
     */
    public  boolean isWildernessInterface() {
        return wildernessInterface;
    }

    /**
     * Sets the value for {@link Player#wildernessInterface}.
     * 
     * @param wildernessInterface
     *            the new value to set.
     */
    public  void setWildernessInterface(boolean wildernessInterface) {
        this.wildernessInterface = wildernessInterface;
    }

    /**
     * Determines if a multicombat interface is present.
     * 
     * @return {@code true} if a multicombat interface is present, {@code false}
     *         otherwise.
     */
    public  boolean isMulticombatInterface() {
        return multicombatInterface;
    }

    /**
     * Sets the value for {@link Player#multicombatInterface}.
     * 
     * @param multicombatInterface
     *            the new value to set.
     */
    public  void setMulticombatInterface(boolean multicombatInterface) {
        this.multicombatInterface = multicombatInterface;
    }

    /**
     * Determines if this player is new.
     * 
     * @return {@code true} if this player is new, {@code false} otherwise.
     */
    public  boolean isNewPlayer() {
        return newPlayer;
    }

    /**
     * Sets the value for {@link Player#newPlayer}.
     * 
     * @param newPlayer
     *            the new value to set.
     */
    public  void setNewPlayer(boolean newPlayer) {
        this.newPlayer = newPlayer;
    }

    /**
     * Determines if items should be inserted when banking.
     * 
     * @return {@code true} if items should be inserted, {@code false}
     *         otherwise.
     */
    public  boolean isInsertItem() {
        return insertItem;
    }

    /**
     * Sets the value for {@link Player#insertItem}.
     * 
     * @param insertItem
     *            the new value to set.
     */
    public  void setInsertItem(boolean insertItem) {
        this.insertItem = insertItem;
    }

    /**
     * Determines if a bank item should be withdrawn as a note.
     * 
     * @return {@code true} if items should be withdrawn as notes, {@code false}
     *         otherwise.
     */
    public  boolean isWithdrawAsNote() {
        return withdrawAsNote;
    }

    /**
     * Sets the value for {@link Player#withdrawAsNote}.
     * 
     * @param withdrawAsNote
     *            the new value to set.
     */
    public  void setWithdrawAsNote(boolean withdrawAsNote) {
        this.withdrawAsNote = withdrawAsNote;
    }

    /**
     * Gets the current spellbook the player has open.
     * 
     * @return the spellbook open.
     */
    public  Spellbook getSpellbook() {
        return spellbook;
    }

    /**
     * Sets the value for {@link Player#spellbook}.
     * 
     * @param spellbook
     *            the new value to set.
     */
    public  void setSpellbook(Spellbook spellbook) {
        this.spellbook = spellbook;
    }

    /**
     * Gets the array of chat text packed into bytes.
     * 
     * @return the array of chat text.
     */
    public  byte[] getChatText() {
        return chatText;
    }

    /**
     * Sets the value for {@link Player#chatText}.
     * 
     * @param chatText
     *            the new value to set.
     */
    public  void setChatText(byte[] chatText) {
        this.chatText = chatText;
    }

    /**
     * Gets the current chat color the player is using.
     * 
     * @return the chat color.
     */
    public  int getChatColor() {
        return chatColor;
    }

    /**
     * Sets the value for {@link Player#chatColor}.
     * 
     * @param chatColor
     *            the new value to set.
     */
    public  void setChatColor(int chatColor) {
        this.chatColor = chatColor;
    }

    /**
     * Gets the current chat effects the player is using.
     * 
     * @return the chat effects.
     */
    public  int getChatEffects() {
        return chatEffects;
    }

    /**
     * Sets the value for {@link Player#chatEffects}.
     * 
     * @param chatEffects
     *            the new value to set.
     */
    public  void setChatEffects(int chatEffects) {
        this.chatEffects = chatEffects;
    }

    /**
     * Gets the player-npc identifier for updating.
     * 
     * @return the player npc identifier.
     */
    public  int getPlayerNpc() {
        return playerNpc;
    }

    /**
     * Sets the value for {@link Player#playerNpc}.
     * 
     * @param playerNpc
     *            the new value to set.
     */
    public  void setPlayerNpc(int playerNpc) {
        this.playerNpc = playerNpc;
    }

    /**
     * Gets the cached player update block for updating.
     * 
     * @return the cached update block.
     */
    public  ByteBuffer getCachedUpdateBlock() {
        return cachedUpdateBlock;
    }

    /**
     * Sets the value for {@link Player#cachedUpdateBlock}.
     * 
     * @param cachedUpdateBlock
     *            the new value to set.
     */
    public  void setCachedUpdateBlock(ByteBuffer cachedUpdateBlock) {
        this.cachedUpdateBlock = cachedUpdateBlock;
    }

    /**
     * Gets the username hash for this player.
     * 
     * @return the username hash.
     */
    public  long getUsernameHash() {
        return usernameHash;
    }

    /**
     * Sets the value for {@link Player#usernameHash}.
     * 
     * @param usernameHash
     *            the new value to set.
     */
    public  void setUsernameHash(long usernameHash) {
        this.usernameHash = usernameHash;
    }

    /**
     * Gets the current dialogue chain we are in.
     * 
     * @return the dialogue chain.
     */
    public  DialogueChainBuilder getDialogueChain() {
        return dialogueChain;
    }

    /**
     * Determines if the region has been updated this sequence.
     * 
     * @return {@code true} if the region has been updated, {@code false}
     *         otherwise.
     */
    public  boolean isUpdateRegion() {
        return updateRegion;
    }

    /**
     * Sets the value for {@link Player#updateRegion}.
     * 
     * @param updateRegion
     *            the new value to set.
     */
    public  void setUpdateRegion(boolean updateRegion) {
        this.updateRegion = updateRegion;
    }

    /**
     * Gets the forced movement container for the update mask.
     * 
     * @return the forced movement container.
     */
    public ForcedMovement getForcedMovement() {
        return forcedMovement;
    }

    /**
     * Sets the value for {@link Player#forcedMovement}.
     * 
     * @param forcedMovement
     *            the new value to set.
     */
    public void setForcedMovement(ForcedMovement forcedMovement) {
        this.forcedMovement = forcedMovement;
    }

    /**
     * Gets the current viewing orb that this player has open.
     * 
     * @return the current viewing orb.
     */
    public ViewingOrb getViewingOrb() {
        return viewingOrb;
    }

    /**
     * Sets the value for {@link Player#viewingOrb}.
     * 
     * @param viewingOrb
     *            the new value to set.
     */
    public void setViewingOrb(ViewingOrb viewingOrb) {
        this.viewingOrb = viewingOrb;
    }

    /**
     * Gets the container of appearance values for this player.
     *
     * @return the container of appearance values.
     */
    public Appearance getAppearance() {
        return appearance;
    }

    /**
     * Determines if this player is disabled.
     * 
     * @return {@code true} if this player is disabled, {@code false} otherwise.
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Sets the value for {@link Player#disabled}.
     * 
     * @param disabled
     *            the new value to set.
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
