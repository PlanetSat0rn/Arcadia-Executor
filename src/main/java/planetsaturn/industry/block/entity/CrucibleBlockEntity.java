package planetsaturn.industry.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeMatcher;
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

import static net.minecraft.block.entity.AbstractFurnaceBlockEntity.createFuelTimeMap;

public class CrucibleBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> Inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 200;
    private int fuelTime = 0;
    private int speedup = 0;
    private int speedupProgress = 0;
    private int maxSpeedupProgress = 100;
    private int maxFuelTime = 1200;
    private int maxSpeedup = 20;

    private HashMap<Item, Item> RecipeMap() {
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

    protected int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            return (Integer)createFuelTimeMap().getOrDefault(item, 0);
        }
    }

    public static boolean canUseAsFuel(ItemStack stack) {
        return createFuelTimeMap().containsKey(stack.getItem());
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
        nbt.putInt("crucible.maxfueltime", maxFuelTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, Inventory);
        progress = nbt.getInt("crucible.progress");
        fuelTime = nbt.getInt("crucible.fueltime");
        maxFuelTime = nbt.getInt("crucible.maxfueltime");
        speedup = nbt.getInt("crucible.speedup");
        speedupProgress = nbt.getInt("crucible.speedupprogress");
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, CrucibleBlockEntity entity) {
        if (world.isClient) {
            return;
        }
        world.getServer().sendMessage(Text.of(String.valueOf(entity.speedupProgress) + " " + String.valueOf(entity.speedup) + " " + String.valueOf(entity.progress) + " " + String.valueOf(entity.fuelTime)));

        if (entity.fuelTime >= 1) {
            entity.getWorld().setBlockState(blockPos,state.with(Properties.LIT, true));
            entity.fuelTime -= 1;
            entity.speedupProgress += 1;
            if (entity.speedupProgress >= entity.maxSpeedupProgress) {
                entity.speedupProgress = 0;
                if (entity.speedup < entity.maxSpeedup) {
                    entity.speedup += 1;
                }
            }

            } else {
            if (canUseAsFuel(entity.getStack(2))) {
                entity.fuelTime = entity.getFuelTime((entity.getStack(2)));
                entity.maxFuelTime = entity.getFuelTime((entity.getStack(2)));
                if (entity.getStack(2).getItem() == Items.LAVA_BUCKET) {
                    entity.setStack(2,new ItemStack(Items.BUCKET));
                } else {
                    entity.removeStack(2,1);
                }

            }
            entity.getWorld().setBlockState(blockPos,state.with(Properties.LIT, false));
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
                entity.resetProgress(entity);
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
            entity.setStack(1,new ItemStack(entity.RecipeMap().get(item), count+1));
            entity.resetProgress(entity);
        }
    }

    private static boolean hasRecipe(CrucibleBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        boolean isInRecipe = entity.RecipeMap().containsKey(entity.getStack(0).getItem());

        return isInRecipe && canInsertAmountIntoOutputSlot(inventory, 1) && canInsertItemIntoOutputSlot(inventory,entity.getStack(1).getItem());
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory, Integer count) {
        return inventory.getStack(1).getMaxCount() > inventory.getStack(1).getCount();
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item item) {
        return inventory.getStack(1).getItem() == item || inventory.getStack(1).isEmpty();
    }


}