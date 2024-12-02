package planetsaturn.industry.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import planetsaturn.industry.Item.ModItems;
import planetsaturn.industry.SaturnIndustry;
import planetsaturn.industry.screen.CrucibleScreenHandler;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Hashtable;

public class CrucibleBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> Inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 200;
    private int fuelTime = 0;
    private int speedup = 0;
    private int speedupProgress = 0;
    private int maxSpeedupProgress = 100;
    private int maxFuelTime = 0;
    private int maxSpeedup = 20;

    private HashMap<Item, Item> getRecipeMap() {
        return new HashMap<Item, Item>() {
            {
                put(Items.RAW_IRON, Items.IRON_INGOT);
                put(Items.RAW_COPPER, Items.COPPER_INGOT);
                put(Items.RAW_GOLD, Items.GOLD_INGOT);
                put(Items.ANCIENT_DEBRIS, Items.NETHERITE_SCRAP);
                put(ModItems.PLASTEEL_MIXTURE, ModItems.PLASTEEL);
            }
        };
    }



    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRUCIBLE, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0: return CrucibleBlockEntity.this.progress;
                    case 1: return CrucibleBlockEntity.this.maxProgress;
                    case 2: return CrucibleBlockEntity.this.fuelTime;
                    case 3: return CrucibleBlockEntity.this.speedup;
                    case 4: return CrucibleBlockEntity.this.maxFuelTime;
                    case 5: return CrucibleBlockEntity.this.maxSpeedup;
                    case 6: return CrucibleBlockEntity.this.speedupProgress;
                    case 7: return CrucibleBlockEntity.this.maxSpeedupProgress;
                    default: return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0: CrucibleBlockEntity.this.progress = value; break;
                    case 1: CrucibleBlockEntity.this.maxProgress = value; break;
                    case 2: CrucibleBlockEntity.this.fuelTime = value; break;
                    case 3: CrucibleBlockEntity.this.speedup = value; break;
                    case 4: CrucibleBlockEntity.this.maxFuelTime = value; break;
                    case 5: CrucibleBlockEntity.this.maxSpeedup = value; break;
                    case 6: CrucibleBlockEntity.this.speedupProgress = value; break;
                    case 7: CrucibleBlockEntity.this.maxSpeedupProgress = value; break;
                }
            }

            public int size() {
                return 8;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.Inventory;
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new CrucibleScreenHandler(syncId,inv,this,this.propertyDelegate);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.saturnindustry.crucible");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, Inventory);
        nbt.putInt("crucible.progress", progress);
        nbt.putInt("crucible.fueltime", fuelTime);
        nbt.putInt("crucible.speedup", speedup);
        nbt.putInt("crucible.speedupprogress", speedupProgress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, Inventory);
        progress = nbt.getInt("crucible.progress");
        fuelTime = nbt.getInt("crucible.fueltime");
        speedup = nbt.getInt("crucible.speedup");
        speedupProgress = nbt.getInt("crucible.speedupprogress");
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, CrucibleBlockEntity entity) {
        if (world.isClient) {
            return;
        }
        world.getServer().sendMessage(Text.of(String.valueOf(entity.speedupProgress) + " " + String.valueOf(entity.speedup) + " " + String.valueOf(entity.progress) + " " + String.valueOf(entity.fuelTime)));

        if (entity.fuelTime >= 1) {
            entity.fuelTime -= 1;
            entity.speedupProgress += 1;
            if (entity.speedupProgress >= entity.maxSpeedupProgress) {
                entity.speedupProgress = 0;
                if (entity.speedup < entity.maxSpeedup) {
                    entity.speedup += 1;
                }
            }

            } else {
            if (entity.getStack(2).getItem() == Items.LAVA_BUCKET) {
                entity.fuelTime = 1200;
                entity.setStack(2,new ItemStack(Items.BUCKET));
            }
            entity.speedupProgress -= 1;
                if (entity.speedupProgress <= 0) {
                    entity.speedupProgress = entity.maxSpeedupProgress;
                    if (entity.speedup > 0) {
                        entity.speedup -= 1;
                    }
                }
            }

        if(hasRecipe(entity)) {
            entity.progress += entity.speedup;
            markDirty(world,blockPos,state);
            if (entity.fuelTime == 0) {
                entity.getWorld().setBlockState(entity.pos,entity.getWorld().getBlockState(entity.pos).with(Properties.LIT, false));
                entity.resetProgress(entity);
            } else {
                entity.getWorld().setBlockState(entity.pos,entity.getWorld().getBlockState(entity.pos).with(Properties.LIT, true));
            }
            if (entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
            } else {
                entity.resetProgress(entity);
                markDirty(world,blockPos,state);
            }

    }

    private void resetProgress(CrucibleBlockEntity entity) {
        this.progress = 0;
    }

    private static void craftItem(CrucibleBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        if(hasRecipe(entity)) {
            int count = entity.getStack(1).getCount();
            Item item = entity.getStack(0).getItem();
            entity.removeStack(0,1);
            entity.setStack(1,new ItemStack(entity.getRecipeMap().get(item), count+1));
            entity.resetProgress(entity);
        }
    }

    private static boolean hasRecipe(CrucibleBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        boolean isInRecipe = entity.getRecipeMap().containsKey(entity.getStack(0).getItem());

        return isInRecipe && canInsertAmountIntoOutputSlot(inventory, 1) && canInsertItemIntoOutputSlot(inventory,entity.getStack(1).getItem());
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory, Integer count) {
        return inventory.getStack(1).getMaxCount() > inventory.getStack(1).getCount();
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item item) {
        return inventory.getStack(1).getItem() == item || inventory.getStack(1).isEmpty();
    }


}