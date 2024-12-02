package planetsaturn.industry.Item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import planetsaturn.industry.SaturnIndustry;
import planetsaturn.industry.block.ModBlocks;

public class ModItemGroup {
    public static final ItemGroup INDUSTRY = FabricItemGroupBuilder.build(new Identifier(SaturnIndustry.MOD_ID, "industry_group"), () -> new ItemStack(ModItems.PLASTEEL));
    public static final ItemGroup INDUSTRYBLOCKS = FabricItemGroupBuilder.build(new Identifier(SaturnIndustry.MOD_ID, "industry_block_group"), () -> new ItemStack(ModBlocks.BLANK_MECHANISM));

}
