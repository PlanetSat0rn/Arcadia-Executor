package planetsaturn.industry;

import net.minecraft.item.Item;

import java.util.HashMap;

public class SmeltMap {
    /*
    public static HashMap<Item,Item> CreateRecipeDatabase() {
        return new HashMap<>();
    }

    public static void AddRecipe(HashMap<Item,Item> database, Item ingredient, Item result) {
        database.put(ingredient,result);
    }
*/
    public static Item FindResultFromIngredient(HashMap<Item,Item> database, Item ingredient) {
        return database.get(ingredient);
    }

    public static Boolean HasIngredient(HashMap<Item,Item> database, Item ingredient) {
        return database.containsKey(ingredient);
    }
}
