package planetsaturn.industry.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import planetsaturn.industry.Item.ModItemGroup;
import planetsaturn.industry.SaturnIndustry;

public class ModBlocks {

    public static final Block PLASTEEL_BLOCK = registerBlock("plasteel_block",
            new Block(FabricBlockSettings.of(Material.METAL).strength(4f).requiresTool()), ModItemGroup.INDUSTRY);

    public static final Block BLANK_MECHANISM = registerBlock("blank_mechanism",
            new Block(FabricBlockSettings.of(Material.METAL).strength(4f).requiresTool()), ModItemGroup.INDUSTRY);

    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name,block,tab);
        return Registry.register(Registry.BLOCK, new Identifier(SaturnIndustry.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        return Registry.register(Registry.ITEM, new Identifier(SaturnIndustry.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlocks() {
        SaturnIndustry.LOGGER.debug("Registering ModBlocks for" + SaturnIndustry.MOD_ID);
    }
}
