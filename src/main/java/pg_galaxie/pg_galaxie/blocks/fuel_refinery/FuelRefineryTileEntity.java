package pg_galaxie.pg_galaxie.blocks.fuel_refinery;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import pg_galaxie.pg_galaxie.deferreds.PGTileEntitys;

public class FuelRefineryTileEntity extends LockableLootTileEntity {

    public static int slots = 1;
    public NonNullList<ItemStack> inventory;
    protected int numPlayersUsing;

    public int maxbuckets, buckets;

    protected final IIntArray refineryData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return FuelRefineryTileEntity.this.maxbuckets;
                case 1:
                    return FuelRefineryTileEntity.this.buckets;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    FuelRefineryTileEntity.this.maxbuckets = value;
                    break;
                case 1:
                    FuelRefineryTileEntity.this.buckets = value;
                    break;
            }

        }

        public int size() {
            return 4;
        }
    };

    public IIntArray getRefineryData() {
        return refineryData;
    }

    protected FuelRefineryTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        this.maxbuckets = 3;
        this.buckets = 0;
    }



    public FuelRefineryTileEntity() {
        this(PGTileEntitys.FUELREFINERYTILE.get());
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.inventory = itemsIn;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return ITextComponent.getTextComponentOrEmpty("Fuel Refinery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new FuelRefineryContainer(id,player,this, refineryData);
    }

    @Override
    public int getSizeInventory() {
        return slots;
    }

    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.inventory);
        }
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.inventory);
        }

        return compound;
    }

    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    public void openInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.onOpenOrClose();
        }

    }

    public void closeInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.onOpenOrClose();
        }

    }

    protected void onOpenOrClose() {
        Block block = this.getBlockState().getBlock();
        if (block instanceof FuelRefineryBlock) {
            this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, block);
        }

    }

    public void addBucket() {

        this.buckets += 1;
        System.out.println(this.buckets);
    }
}
