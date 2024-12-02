package planetsaturn.industry.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import planetsaturn.industry.SaturnIndustry;
import planetsaturn.industry.block.ModBlocks;
import planetsaturn.industry.block.custom.CrucibleBlock;

public class ModBlockEntities {
    public static BlockEntityType<CrucibleBlockEntity> CRUCIBLE;




    public static void registerModBlockEntities() {
        SaturnIndustry.LOGGER.debug("Registering Mod Block Entities for" + SaturnIndustry.MOD_ID);

        CRUCIBLE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(SaturnIndustry.MOD_ID, "crucible"),
                FabricBlockEntityTypeBuilder.create(CrucibleBlockEntity::new,
                        ModBlocks.CRUCIBLE).build(null));
    }
}
