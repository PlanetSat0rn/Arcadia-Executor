package planetsaturn.industry.Item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Objects;

public class WrenchItem extends Item {

    public WrenchItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        boolean success = false;
        if(!context.getWorld().isClient && context.getHand() == Hand.MAIN_HAND) {

            BlockPos pos = context.getBlockPos();
            World world = context.getWorld();
            BlockState blockstate = world.getBlockState(pos);
            Direction direction = context.getSide();
            if (blockstate.contains(Properties.HOPPER_FACING)) {
                BlockState otherblockstate = blockstate.with(Properties.HOPPER_FACING, direction);
                world.setBlockState(pos, otherblockstate);
                success = true;
            }
            else if (blockstate.contains(Properties.FACING)) {
                BlockState otherblockstate = blockstate.with(Properties.FACING, direction);
                world.setBlockState(pos, otherblockstate);
                success = true;
            }
            else if (blockstate.contains(Properties.HORIZONTAL_FACING)) {
                BlockState otherblockstate = blockstate.with(Properties.HORIZONTAL_FACING, direction);
                world.setBlockState(pos, otherblockstate);
                success = true;
            }
            else if (blockstate.contains(Properties.AXIS)) {
                BlockState otherblockstate = blockstate.with(Properties.AXIS, direction.getAxis());
                world.setBlockState(pos, otherblockstate);
                success = true;
            }
        }
        if (success) {
            context.getStack().damage(1, context.getPlayer(), p -> p.sendToolBreakStatus(context.getHand()));
            return ActionResult.SUCCESS;
        }
        else {
            return ActionResult.FAIL;
        }
    }
}

