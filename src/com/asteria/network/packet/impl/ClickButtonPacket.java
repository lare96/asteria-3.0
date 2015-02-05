package com.asteria.network.packet.impl;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.asteria.content.skills.cooking.Cooking;
import com.asteria.game.World;
import com.asteria.game.character.combat.magic.CombatSpells;
import com.asteria.game.character.combat.prayer.CombatPrayer;
import com.asteria.game.character.combat.weapon.FightType;
import com.asteria.game.character.player.Player;
import com.asteria.game.character.player.content.Spellbook;
import com.asteria.game.character.player.content.TradeStage;
import com.asteria.game.character.player.content.WeaponInterface;
import com.asteria.game.character.player.dialogue.OptionType;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.item.container.Equipment;
import com.asteria.game.location.Position;
import com.asteria.network.DataBuffer;
import com.asteria.network.packet.PacketDecoder;
import com.asteria.task.Task;
import com.asteria.task.TaskManager;
import com.asteria.utility.BufferUtils;
import com.asteria.utility.Settings;

/**
 * The packet sent from the client when the player clicks some sort of button or
 * module.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class ClickButtonPacket extends PacketDecoder {

    // TODO: Convert all buttons to ones that have been read properly.

    /**
     * The flag that determines if this packet should be read properly.
     */
    private static final boolean PROPER_READ = false;

    /**
     * The logger that will print important information.
     */
    private static Logger logger = Logger.getLogger(ClickButtonPacket.class.getSimpleName());

    @Override
    public void decode(Player player, int opcode, int size, DataBuffer buf) {
        int button = PROPER_READ ? buf.getShort() : BufferUtils.hexToInt(buf.getBytes(2));

        switch (button) {
        case 53152:
            if (player.getCookData() != null) {
                Cooking cooking = new Cooking(player, player.getCookPosition(), player.getCookData(), player.isUsingStove(), 1);
                cooking.start();
            }
            break;
        case 53151:
            if (player.getCookData() != null) {
                Cooking cooking = new Cooking(player, player.getCookPosition(), player.getCookData(), player.isUsingStove(), 5);
                cooking.start();
            }
            break;
        case 53149:
            if (player.getCookData() != null) {
                int amount = player.getInventory().amount(player.getCookData().getRawId());
                Cooking cooking = new Cooking(player, player.getCookPosition(), player.getCookData(), player.isUsingStove(),
                    amount);
                cooking.start();
            }
            break;
        case 50235:
        case 4140:
            player.teleport(new Position(3094, 3243));
            break;
        // case 4146: // falador
        // player.teleport(new Position(2964, 3378));
        // break;
        // case 4150: // camelot
        // player.teleport(new Position(2757, 3477));
        // break;
        // case 6004: // ardougne
        // player.teleport(new Position(2529, 3307));
        // break;
        // case 6005: // watchtower
        // player.teleport(new Position(2951, 3087));
        // break;
        // case 29031: // trollheim
        // player.teleport(new Position(2892, 3670));
        // break;
        // case 72038: // ape atoll
        // player.teleport(new Position(2755, 2784));
        // break;
        // case 50253: // kharyrll
        //
        // break;
        // case 51005: // lassar
        //
        // break;
        // case 51013: // dareeyak
        //
        // break;
        // case 51023: // carrallangar
        //
        // break;
        // case 51031: // annakarl
        //
        // break;
        // case 51039: // ghorrock
        //
        // break;
        case 21233:
            CombatPrayer.THICK_SKIN.activateOrDeactivate(player);
            break;
        case 21234:
            CombatPrayer.BURST_OF_STRENGTH.activateOrDeactivate(player);
            break;
        case 21235:
            CombatPrayer.CLARITY_OF_THOUGHT.activateOrDeactivate(player);
            break;
        case 21236:
            CombatPrayer.ROCK_SKIN.activateOrDeactivate(player);
            break;
        case 21237:
            CombatPrayer.SUPERHUMAN_STRENGTH.activateOrDeactivate(player);
            break;
        case 21238:
            CombatPrayer.IMPROVED_REFLEXES.activateOrDeactivate(player);
            break;
        case 21239:
            CombatPrayer.RAPID_RESTORE.activateOrDeactivate(player);
            break;
        case 21240:
            CombatPrayer.RAPID_HEAL.activateOrDeactivate(player);
            break;
        case 21241:
            CombatPrayer.PROTECT_ITEM.activateOrDeactivate(player);
            break;
        case 21242:
            CombatPrayer.STEEL_SKIN.activateOrDeactivate(player);
            break;
        case 21243:
            CombatPrayer.ULTIMATE_STRENGTH.activateOrDeactivate(player);
            break;
        case 21244:
            CombatPrayer.INCREDIBLE_REFLEXES.activateOrDeactivate(player);
            break;
        case 21245:
            CombatPrayer.PROTECT_FROM_MAGIC.activateOrDeactivate(player);
            break;
        case 21246:
            CombatPrayer.PROTECT_FROM_MISSILES.activateOrDeactivate(player);
            break;
        case 21247:
            CombatPrayer.PROTECT_FROM_MELEE.activateOrDeactivate(player);
            break;
        case 2171:
            CombatPrayer.RETRIBUTION.activateOrDeactivate(player);
            break;
        case 2172:
            CombatPrayer.REDEMPTION.activateOrDeactivate(player);
            break;
        case 2173:
            CombatPrayer.SMITE.activateOrDeactivate(player);
            break;
        case 48177:
            if (player.isAcceptAid()) {
                player.getEncoder().sendMessage("Accept aid has been turned off.");
                player.setAcceptAid(false);
            }
            break;
        case 48176:
            if (!player.isAcceptAid()) {
                player.getEncoder().sendMessage("Accept aid has been turned on.");
                player.setAcceptAid(true);
            }
            break;
        case 150:
            if (!player.isAutoRetaliate()) {
                player.setAutoRetaliate(true);
                player.getEncoder().sendMessage("Auto retaliate has been turned on!");
            }
            break;
        case 151:
            if (player.isAutoRetaliate()) {
                player.setAutoRetaliate(false);
                player.getEncoder().sendMessage("Auto retaliate has been turned off!");
            }
            break;
        case 56109:
            if (player.getDialogueChain().executeOptions(OptionType.FIRST_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;
        case 56110:
            if (player.getDialogueChain().executeOptions(OptionType.SECOND_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;

        case 9167:
            if (player.getDialogueChain().executeOptions(OptionType.FIRST_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;
        case 9168:
            if (player.getDialogueChain().executeOptions(OptionType.SECOND_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;
        case 9169:
            if (player.getDialogueChain().executeOptions(OptionType.THIRD_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;

        case 32017:
            if (player.getDialogueChain().executeOptions(OptionType.FIRST_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;
        case 32018:
            if (player.getDialogueChain().executeOptions(OptionType.SECOND_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;
        case 32019:
            if (player.getDialogueChain().executeOptions(OptionType.THIRD_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;
        case 32020:
            if (player.getDialogueChain().executeOptions(OptionType.FOURTH_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;

        case 32029:
            if (player.getDialogueChain().executeOptions(OptionType.FIRST_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;
        case 32030:
            if (player.getDialogueChain().executeOptions(OptionType.SECOND_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;
        case 32031:
            if (player.getDialogueChain().executeOptions(OptionType.THIRD_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;
        case 32032:
            if (player.getDialogueChain().executeOptions(OptionType.FOURTH_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;
        case 32033:
            if (player.getDialogueChain().executeOptions(OptionType.FIFTH_OPTION))
                return;
            switch (player.getOption()) {

            }
            break;

        case 9154:
            if (!MinigameHandler.execute(player, true, m -> m.canLogout(player)))
                return;
            if (!player.getLastCombat().elapsed(10, TimeUnit.SECONDS)) {
                player.getEncoder().sendMessage("You must wait 10 seconds after combat before logging out.");
                return;
            }
            World.getPlayers().remove(player);
            break;
        case 153:
            if (player.getRunEnergy().get() == 0)
                return;
            player.getMovementQueue().setRunning(true);
            player.getEncoder().sendConfig(173, 1);
            break;
        case 152:
            player.getMovementQueue().setRunning(false);
            player.getEncoder().sendConfig(173, 0);
            break;
        case 21011:
            player.setWithdrawAsNote(false);
            break;
        case 21010:
            player.setWithdrawAsNote(true);
            break;
        case 31195:
            player.setInsertItem(true);
            break;
        case 31194:
            player.setInsertItem(false);
            break;
        case 13092:
            if (player.getTradeSession().inTradeSession()) {
                Player partner = player.getTradeSession().getOther();

                if (!partner.getTradeSession().inTradeSession())
                    return;
                if (partner.getInventory().remaining() < player.getTradeSession().getContainer().size()) {
                    player.getEncoder().sendMessage(
                        partner.getFormatUsername() + " does not have enough free slots for this many items.");
                    return;
                }
                player.getTradeSession().setStage(TradeStage.FIRST_ACCEPT);
                player.getEncoder().sendString("Waiting for other player...", 3431);
                partner.getEncoder().sendString("Other player has accepted", 3431);
                if (player.getTradeSession().getStage() == TradeStage.FIRST_ACCEPT && partner.getTradeSession().getStage() == TradeStage.FIRST_ACCEPT) {
                    partner.getTradeSession().execute(TradeStage.FIRST_ACCEPT);
                    player.getTradeSession().execute(TradeStage.FIRST_ACCEPT);
                }
            }
            break;
        case 13218:
            if (player.getTradeSession().inTradeSession()) {
                Player partner = player.getTradeSession().getOther();
                if (!partner.getTradeSession().inTradeSession())
                    return;
                player.getTradeSession().setStage(TradeStage.FINAL_ACCEPT);
                partner.getEncoder().sendString("Other player has accepted.", 3535);
                player.getEncoder().sendString("Waiting for other player...", 3535);

                if (player.getTradeSession().getStage() == TradeStage.FINAL_ACCEPT && partner.getTradeSession().getStage() == TradeStage.FINAL_ACCEPT) {
                    player.getTradeSession().execute(TradeStage.FINAL_ACCEPT);
                    partner.getTradeSession().execute(TradeStage.FINAL_ACCEPT);
                }
            }
            break;

        case 1080: // staff
            player.setFightType(FightType.STAFF_BASH);
            break;
        case 1079:
            player.setFightType(FightType.STAFF_POUND);
            break;
        case 1078:
            player.setFightType(FightType.STAFF_FOCUS);
            break;
        case 1177: // warhammer
            player.setFightType(FightType.WARHAMMER_POUND);
            break;
        case 1176:
            player.setFightType(FightType.WARHAMMER_PUMMEL);
            break;
        case 1175:
            player.setFightType(FightType.WARHAMMER_BLOCK);
            break;
        case 3014: // scythe
            player.setFightType(FightType.SCYTHE_REAP);
            break;
        case 3017:
            player.setFightType(FightType.SCYTHE_CHOP);
            break;
        case 3016:
            player.setFightType(FightType.SCYTHE_JAB);
            break;
        case 3015:
            player.setFightType(FightType.SCYTHE_BLOCK);
            break;
        case 6168: // battle axe
            player.setFightType(FightType.BATTLEAXE_CHOP);
            break;
        case 6171:
            player.setFightType(FightType.BATTLEAXE_HACK);
            break;
        case 6170:
            player.setFightType(FightType.BATTLEAXE_SMASH);
            break;
        case 6169:
            player.setFightType(FightType.BATTLEAXE_BLOCK);
            break;
        case 6221: // crossbow
            player.setFightType(FightType.CROSSBOW_ACCURATE);
            break;
        case 6220:
            player.setFightType(FightType.CROSSBOW_RAPID);
            break;
        case 6219:
            player.setFightType(FightType.CROSSBOW_LONGRANGE);
            break;
        case 6236: // shortbow & longbow
            if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                player.setFightType(FightType.SHORTBOW_ACCURATE);
            } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                player.setFightType(FightType.LONGBOW_ACCURATE);
            }
            break;
        case 6235:
            if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                player.setFightType(FightType.SHORTBOW_RAPID);
            } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                player.setFightType(FightType.LONGBOW_RAPID);
            }
            break;
        case 6234:
            if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                player.setFightType(FightType.SHORTBOW_LONGRANGE);
            } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                player.setFightType(FightType.LONGBOW_LONGRANGE);
            }
            break;
        case 8234: // dagger & sword
            if (player.getWeapon() == WeaponInterface.DAGGER) {
                player.setFightType(FightType.DAGGER_STAB);
            } else if (player.getWeapon() == WeaponInterface.SWORD) {
                player.setFightType(FightType.SWORD_STAB);
            }
            break;
        case 8237:
            if (player.getWeapon() == WeaponInterface.DAGGER) {
                player.setFightType(FightType.DAGGER_LUNGE);
            } else if (player.getWeapon() == WeaponInterface.SWORD) {
                player.setFightType(FightType.SWORD_LUNGE);
            }
            break;
        case 8236:
            if (player.getWeapon() == WeaponInterface.DAGGER) {
                player.setFightType(FightType.DAGGER_SLASH);
            } else if (player.getWeapon() == WeaponInterface.SWORD) {
                player.setFightType(FightType.SWORD_SLASH);
            }
            break;
        case 8235:
            if (player.getWeapon() == WeaponInterface.DAGGER) {
                player.setFightType(FightType.DAGGER_BLOCK);
            } else if (player.getWeapon() == WeaponInterface.SWORD) {
                player.setFightType(FightType.SWORD_BLOCK);
            }
            break;
        case 9125: // scimitar & longsword
            if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                player.setFightType(FightType.SCIMITAR_CHOP);
            } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                player.setFightType(FightType.LONGSWORD_CHOP);
            }
            break;
        case 9128:
            if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                player.setFightType(FightType.SCIMITAR_SLASH);
            } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                player.setFightType(FightType.LONGSWORD_SLASH);
            }
            break;
        case 9127:
            if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                player.setFightType(FightType.SCIMITAR_LUNGE);
            } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                player.setFightType(FightType.LONGSWORD_LUNGE);
            }
            break;
        case 9126:
            if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                player.setFightType(FightType.SCIMITAR_BLOCK);
            } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                player.setFightType(FightType.LONGSWORD_BLOCK);
            }
            break;
        case 14218: // mace
            player.setFightType(FightType.MACE_POUND);
            break;
        case 14221:
            player.setFightType(FightType.MACE_PUMMEL);
            break;
        case 14220:
            player.setFightType(FightType.MACE_SPIKE);
            break;
        case 14219:
            player.setFightType(FightType.MACE_BLOCK);
            break;
        case 17102: // knife, thrownaxe, dart & javelin
            if (player.getWeapon() == WeaponInterface.KNIFE) {
                player.setFightType(FightType.KNIFE_ACCURATE);
            } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                player.setFightType(FightType.THROWNAXE_ACCURATE);
            } else if (player.getWeapon() == WeaponInterface.DART) {
                player.setFightType(FightType.DART_ACCURATE);
            } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                player.setFightType(FightType.JAVELIN_ACCURATE);
            }
            break;
        case 17101:
            if (player.getWeapon() == WeaponInterface.KNIFE) {
                player.setFightType(FightType.KNIFE_RAPID);
            } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                player.setFightType(FightType.THROWNAXE_RAPID);
            } else if (player.getWeapon() == WeaponInterface.DART) {
                player.setFightType(FightType.DART_RAPID);
            } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                player.setFightType(FightType.JAVELIN_RAPID);
            }
            break;
        case 17100:
            if (player.getWeapon() == WeaponInterface.KNIFE) {
                player.setFightType(FightType.KNIFE_LONGRANGE);
            } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                player.setFightType(FightType.THROWNAXE_LONGRANGE);
            } else if (player.getWeapon() == WeaponInterface.DART) {
                player.setFightType(FightType.DART_LONGRANGE);
            } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                player.setFightType(FightType.JAVELIN_LONGRANGE);
            }
            break;
        case 18077: // spear
            player.setFightType(FightType.SPEAR_LUNGE);
            break;
        case 18080:
            player.setFightType(FightType.SPEAR_SWIPE);
            break;
        case 18079:
            player.setFightType(FightType.SPEAR_POUND);
            break;
        case 18078:
            player.setFightType(FightType.SPEAR_BLOCK);
            break;
        case 18103: // 2h sword
            player.setFightType(FightType.TWOHANDEDSWORD_CHOP);
            break;
        case 15106:
            player.setFightType(FightType.TWOHANDEDSWORD_SLASH);
            break;
        case 18105:
            player.setFightType(FightType.TWOHANDEDSWORD_SMASH);
            break;
        case 18104:
            player.setFightType(FightType.TWOHANDEDSWORD_BLOCK);
            break;
        case 21200: // pickaxe
            player.setFightType(FightType.PICKAXE_SPIKE);
            break;
        case 21203:
            player.setFightType(FightType.PICKAXE_IMPALE);
            break;
        case 21202:
            player.setFightType(FightType.PICKAXE_SMASH);
            break;
        case 21201:
            player.setFightType(FightType.PICKAXE_BLOCK);
            break;
        case 30088: // claws
            player.setFightType(FightType.CLAWS_CHOP);
            break;
        case 30091:
            player.setFightType(FightType.CLAWS_SLASH);
            break;
        case 30090:
            player.setFightType(FightType.CLAWS_LUNGE);
            break;
        case 30089:
            player.setFightType(FightType.CLAWS_BLOCK);
            break;
        case 33018: // halberd
            player.setFightType(FightType.HALBERD_JAB);
            break;
        case 33020:
            player.setFightType(FightType.HALBERD_SWIPE);
            break;
        case 33016:
            player.setFightType(FightType.HALBERD_FEND);
            break;
        case 22228: // unarmed
            player.setFightType(FightType.UNARMED_PUNCH);
            break;
        case 22230:
            player.setFightType(FightType.UNARMED_KICK);
            break;
        case 22229:
            player.setFightType(FightType.UNARMED_BLOCK);
            break;
        case 48010: // whip
            player.setFightType(FightType.WHIP_FLICK);
            break;
        case 48009:
            player.setFightType(FightType.WHIP_LASH);
            break;
        case 48008:
            player.setFightType(FightType.WHIP_DEFLECT);
            break;

        case 24017:
        case 7212:
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            break;

        case 1093:
        case 1094:
        case 1097:
            if (player.isAutocast()) {
                player.setCastSpell(null);
                player.setAutocastSpell(null);
                player.setAutocast(false);
                player.getEncoder().sendConfig(108, 0);
            } else if (!player.isAutocast()) {
                if (player.getEquipment().getId(Equipment.WEAPON_SLOT) == 4675) {
                    if (player.getSpellbook() != Spellbook.ANCIENT) {
                        player.getEncoder().sendMessage("You can only autocast ancient magics with this staff.");
                        return;
                    }

                    player.getEncoder().sendSidebarInterface(0, 1689);
                } else {
                    if (player.getSpellbook() != Spellbook.NORMAL) {
                        player.getEncoder().sendMessage("You can only autocast standard magics with this staff.");
                        return;
                    }

                    player.getEncoder().sendSidebarInterface(0, 1829);
                }
            }
            break;

        case 51133:
            player.setAutocastSpell(CombatSpells.SMOKE_RUSH.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51185:
            player.setAutocastSpell(CombatSpells.SHADOW_RUSH.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51091:
            player.setAutocastSpell(CombatSpells.BLOOD_RUSH.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 24018:
            player.setAutocastSpell(CombatSpells.ICE_RUSH.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51159:
            player.setAutocastSpell(CombatSpells.SMOKE_BURST.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51211:
            player.setAutocastSpell(CombatSpells.SHADOW_BURST.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51111:
            player.setAutocastSpell(CombatSpells.BLOOD_BURST.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51069:
            player.setAutocastSpell(CombatSpells.ICE_BURST.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51146:
            player.setAutocastSpell(CombatSpells.SMOKE_BLITZ.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51198:
            player.setAutocastSpell(CombatSpells.SHADOW_BLITZ.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51102:
            player.setAutocastSpell(CombatSpells.BLOOD_BLITZ.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51058:
            player.setAutocastSpell(CombatSpells.ICE_BLITZ.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51172:
            player.setAutocastSpell(CombatSpells.SMOKE_BARRAGE.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51224:
            player.setAutocastSpell(CombatSpells.SHADOW_BARRAGE.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51122:
            player.setAutocastSpell(CombatSpells.BLOOD_BARRAGE.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 51080:
            player.setAutocastSpell(CombatSpells.ICE_BARRAGE.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7038:
            player.setAutocastSpell(CombatSpells.WIND_STRIKE.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7039:
            player.setAutocastSpell(CombatSpells.WATER_STRIKE.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7040:
            player.setAutocastSpell(CombatSpells.EARTH_STRIKE.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7041:
            player.setAutocastSpell(CombatSpells.FIRE_STRIKE.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7042:
            player.setAutocastSpell(CombatSpells.WIND_BOLT.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7043:
            player.setAutocastSpell(CombatSpells.WATER_BOLT.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7044:
            player.setAutocastSpell(CombatSpells.EARTH_BOLT.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7045:
            player.setAutocastSpell(CombatSpells.FIRE_BOLT.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7046:
            player.setAutocastSpell(CombatSpells.WIND_BLAST.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7047:
            player.setAutocastSpell(CombatSpells.WATER_BLAST.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7048:
            player.setAutocastSpell(CombatSpells.EARTH_BLAST.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7049:
            player.setAutocastSpell(CombatSpells.FIRE_BLAST.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7050:
            player.setAutocastSpell(CombatSpells.WIND_WAVE.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7051:
            player.setAutocastSpell(CombatSpells.WATER_WAVE.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7052:
            player.setAutocastSpell(CombatSpells.EARTH_WAVE.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 7053:
            player.setAutocastSpell(CombatSpells.FIRE_WAVE.getSpell());
            player.setAutocast(true);
            player.getEncoder().sendSidebarInterface(0, player.getWeapon().getId());
            player.getEncoder().sendConfig(108, 3);
            break;
        case 29138:
        case 29038:
        case 29063:
        case 29113:
        case 29163:
        case 29188:
        case 29213:
        case 29238:
        case 30007:
        case 48023:
        case 33033:
        case 30108:
            if (player.getCombatSpecial() == null) {
                return;
            }

            if (player.isSpecialActivated()) {
                player.getEncoder().sendConfig(301, 0);
                player.setSpecialActivated(false);
            } else {
                if (player.getSpecialPercentage().get() < player.getCombatSpecial().getAmount()) {
                    player.getEncoder().sendMessage("You do not have enough special energy left!");
                    return;
                }

                player.getEncoder().sendConfig(301, 1);
                player.setSpecialActivated(true);

                TaskManager.submit(new Task(1, false) {
                    @Override
                    public void execute() {
                        if (!player.isSpecialActivated()) {
                            this.cancel();
                            return;
                        }

                        player.getCombatSpecial().onActivation(player, player.getCombatBuilder().getVictim());
                        this.cancel();
                    }
                }.attach(player));
            }
            break;
        default:
            if (Settings.DEBUG)
                logger.info("Unhandled button: " + button);
            break;
        }
    }
}
