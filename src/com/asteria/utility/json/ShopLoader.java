package com.asteria.utility.json;

import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalInt;

import com.asteria.game.item.Item;
import com.asteria.game.item.ItemDefinition;
import com.asteria.game.shop.Currency;
import com.asteria.game.shop.Shop;
import com.asteria.utility.JsonLoader;
import com.asteria.utility.Settings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all shops.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ShopLoader extends JsonLoader {

    /**
     * Creates a new {@link ShopLoader}.
     */
    public ShopLoader() {
        super("./data/json/shops/shops.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        String name = Objects.requireNonNull(reader.get("name").getAsString());
        Item[] items = Objects.requireNonNull(builder.fromJson(reader.get
                ("items").getAsJsonArray(), Item[].class));
        boolean restock = reader.get("restock").getAsBoolean();
        boolean sellItems = reader.get("can-sell-items").getAsBoolean();
        Currency currency = Objects.requireNonNull(Currency.valueOf(reader
                .get("currency").getAsString()));

        Shop shop = new Shop(name, items, restock, sellItems, currency);
        OptionalInt op = Arrays.stream(Settings.BANNED_SHOP_ITEMS).filter
                (shop.getContainer()::contains).findFirst();

        if (op.isPresent())
            throw new IllegalStateException("Item not allowed in shops: " +
                    ItemDefinition.DEFINITIONS[op.getAsInt()].getName());
        if (Shop.SHOPS.containsKey(name))
            throw new IllegalStateException("Duplicate shop name: " + name);
        Shop.SHOPS.put(name, shop);
    }
}
