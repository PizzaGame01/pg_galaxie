package pg_galaxie.pg_galaxie.blocks.fuel_refinery;

import com.google.common.base.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import pg_galaxie.pg_galaxie.deferreds.PGTileEntitys;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FuelRefineryTileEntity extends LockableLootTileEntity {

    public static int slots = 1;
    public NonNullList<ItemStack> inventory;
    protected int numPlayersUsing;

    public final FluidTank fluidTank= new FluidTank(3000, fs -> {
        if (fs.getFluid() == Fluids.WATER)
            return true;
        if (fs.getFluid() == Fluids.FLOWING_WATER)
            return true;
        return false;
    }) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            try {
                world.getTileEntity(pos).markDirty();
            }catch (NullPointerException npe){

            }
            //markDirty();
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
        }
    };
    public final EnergyStorage energyStorage = new EnergyStorage(9000, 200, 200, 0) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int retval = super.receiveEnergy(maxReceive, simulate);
            if (!simulate) {
                world.getTileEntity(pos).markDirty();
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
            }
            return retval;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int retval = super.extractEnergy(maxExtract, simulate);
            if (!simulate) {
                world.getTileEntity(pos).markDirty();
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
            }
            return retval;
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return true;
        }

        @Override
        public int getMaxEnergyStored() {
            return 1000;
        }
    };


    public int maxbuckets, buckets;

    protected final IIntArray refineryData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return FuelRefineryTileEntity.this.maxbuckets;
                case 1:
                    return FuelRefineryTileEntity.this.fluidTank.getFluidAmount()/1000;//FuelRefineryTileEntity.this.buckets;
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

        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(capability -> capability.fill(new FluidStack(Fluids.WATER, 0), IFluidHandler.FluidAction.EXECUTE));
        this.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.getEnergyStored());
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

        this.buckets = nbt.getInt("buckets");
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.inventory);
        }
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("buckets",this.buckets);
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if(cap == CapabilityEnergy.ENERGY){
            return LazyOptional.of(() -> this.energyStorage).cast();
        }else if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return LazyOptional.of(() -> this.fluidTank).cast();
        }
        return null;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return this.getCapability(cap);
    }
}
