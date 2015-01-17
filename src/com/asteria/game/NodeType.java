package com.asteria.game;

import com.asteria.game.character.npc.Npc;
import com.asteria.game.character.player.Player;
import com.asteria.game.item.ItemNode;
import com.asteria.game.object.ObjectNode;

/**
 * The enumerated type whose elements represent the different types of
 * {@link Node} implementations.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public enum NodeType {

    /**
     * The element used to represent the {@link ItemNode} implementation.
     */
    ITEM,

    /**
     * The element used to represent the {@link ObjectNode} implementation.
     */
    OBJECT,

    /**
     * The element used to represent the {@link Player} implementation.
     */
    PLAYER,

    /**
     * The element used to represent the {@link Npc} implementation.
     */
    NPC
}