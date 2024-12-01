package planetsaturn.industry.Item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import planetsaturn.industry.SaturnIndustry;

public class ModItemGroup {
    public static final ItemGroup INDUSTRY = FabricItemGroupBuilder.build(new Identifier(SaturnIndustry.MOD_ID, "industrygroup"), () -> new ItemStack(ModItems.PLASTEEL));
}
