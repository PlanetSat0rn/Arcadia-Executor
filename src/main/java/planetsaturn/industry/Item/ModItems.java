package planetsaturn.industry.Item;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import planetsaturn.industry.Item.custom.WrenchItem;
import planetsaturn.industry.SaturnIndustry;

public class ModItems {

    public static final Item PLASTEEL_MIXTURE = registerItem("plasteel_mixture",
            new Item(new FabricItemSettings().group(ModItemGroup.INDUSTRY)));
    public static final Item PLASTEEL = registerItem("plasteel",
            new Item(new FabricItemSettings().group(ModItemGroup.INDUSTRY)));
    public static final Item LITHIUM = registerItem("lithium",
            new Item(new FabricItemSettings().group(ModItemGroup.INDUSTRY)));
    public static final Item WRENCH = registerItem("wrench",
            new WrenchItem(new FabricItemSettings().group(ModItemGroup.INDUSTRY).maxCount(1).maxDamage(128)));

    private static Item registerItem(String name,Item item) {
        return Registry.register(Registry.ITEM, new Identifier(SaturnIndustry.MOD_ID, name), item);
    }

    public static void registerModItems() {
SaturnIndustry.LOGGER.debug("Registering Mod Items for " + SaturnIndustry.MOD_ID);
    }
}
