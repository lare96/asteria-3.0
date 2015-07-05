package com.asteria.game.character.npc.drop;


/**
 * The enumerated type containing {@link NpcDrop}s common among multiple
 * {@link NpcDropTable}s.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public enum NpcDropCache {

    /**
     * Contains only the basic elemental and combat runes, air, water, earth,
     * fire, mind, body, and chaos.
     */
    LOW_RUNES,

    /**
     * Contains high quantities of basic elemental runes, astral runes, and low
     * quantities of higher tier runes such as death, blood, law, nature, and
     * soul.
     */
    MED_RUNES,

    /**
     * Contains extremely high quantities of all runes excluding low-level
     * combat spell runes such as body and mind.
     */
    HIGH_RUNES,

    /**
     * Contains only low level herbs such as Guam, Marrentill, Tarromin,
     * Harralander, and Ranarr.
     */
    LOW_HERBS,

    /**
     * Contains medium level herbs such as Ranarr, Toadflax, Irit, Avantoe, and
     * Kwuarm.
     */
    MED_HERBS,

    /**
     * Contains high level herbs such as Snapdragon, Cadantine, Lantadyme, Dwarf
     * Weed, and Torstol. Also contains the herbs from the medium tier. All of
     * the herbs in this tier are noted.
     */
    HIGH_HERBS,

    /**
     * Contains only noted sapphire and emerald gems in low quantities.
     */
    LOW_GEMS,

    /**
     * Contains the gems from the lower tier as well as ruby and diamond noted
     * gems in higher quantities.
     */
    MED_GEMS,

    /**
     * Contains the noted gems from the lower tiers as well as dragonstone and
     * onyx gems in the highest possible quantities.
     */
    HIGH_GEMS,

    /**
     * Contains lower level/useless equipment such as adamant armor, arrows, and
     * weapons.
     */
    LOW_EQUIPMENT,

    /**
     * Contains medium level equipment such as useless runite weapons, arrows,
     * and med helms.
     */
    MED_EQUIPMENT,

    /**
     * Contains higher level equipment such as rune armor, weapons, and dragon
     * weapons. Also has rune throwing knives and darts.
     */
    HIGH_EQUIPMENT,

    /**
     * Contains low level resources such as noted raw/cooked tuna, noted willow
     * logs, noted steel bars, coins, noted coal, noted iron ore, and low
     * quantities of pure essence.
     */
    LOW_RESOURCES,

    /**
     * Contains medium level resources such as noted raw/cooked lobsters and
     * swordfish, noted willow/maple logs, noted mithril bars, coins, noted
     * coal, noted mithril ore, and decent amounts of pure essence.
     */
    MED_RESOURCES,

    /**
     * Contains high level resources such as noted raw/cooked manta ray and
     * shark, noted yew and magic logs, adamant and noted rune bars, coins,
     * noted coal in high amounts, noted adamant and rune ore, very high amounts
     * of pure essence.
     */
    HIGH_RESOURCES
}
