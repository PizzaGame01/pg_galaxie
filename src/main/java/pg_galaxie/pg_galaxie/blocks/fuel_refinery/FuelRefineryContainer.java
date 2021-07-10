package pg_galaxie.pg_galaxie.blocks.fuel_refinery;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import pg_galaxie.pg_galaxie.deferreds.PGBlocks;
import pg_galaxie.pg_galaxie.deferreds.PGContainerType;

import java.util.Objects;

public class FuelRefineryContainer extends Container {

    public final FuelRefineryTileEntity te;
    private final IWorldPosCallable canInteractWithCallable;

    public IIntArray refíneryData;

    public FuelRefineryContainer(final int windowId, final PlayerInventory playerInv, final FuelRefineryTileEntity te,IIntArray data) {
        super(PGContainerType.FUEL_REFINERY_CONTAINER.get(),windowId);
        this.te = te;
        this.refíneryData = data;

        this.canInteractWithCallable = IWorldPosCallable.of(te.getWorld(),te.getPos());
        trackIntArray(refíneryData);
        te.openInventory(playerInv.player);
        this.addSlot(new Slot(te,0,0,0));

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInv, j1 + l * 9 + 9,8 + j1 * 18, 166 - (4 - l) * 18 - 10));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInv, i1, 8 + i1 * 18, 142));
        }
    }

    public FuelRefineryContainer(int i, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        this(i,playerInventory,FuelRefineryContainer.getTileEntity(playerInventory,packetBuffer),new IntArray(3));
        this.te.buckets = packetBuffer.readInt();
        //this.te.rs = packetBuffer.readInt();
    }


    protected static FuelRefineryTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data){
        Objects.requireNonNull(playerInv,"Player Inventory Cannot be null");
        Objects.requireNonNull(data,"Packet Buffer Cannot be null");
        final TileEntity te = playerInv.player.world.getTileEntity(data.readBlockPos());
        if(te instanceof FuelRefineryTileEntity){
            return (FuelRefineryTileEntity) te;
        }
        throw new IllegalStateException("Tile Entity Is Not correct");
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable,playerIn, PGBlocks.FUEL_REFINERY_BLOCK.get());
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < 36) {
                if (!this.mergeItemStack(itemstack1, 36, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, te.slots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    /*public int getBuckets() {
        return refíneryData.get(1);
    }

    public int getMaxbuckets() {
        return te.maxbuckets;
    }
    public int getRs() {
        return refíneryData.get(2);
    }*/

    @Override
    public void updateProgressBar(int id, int data) {
        super.updateProgressBar(id, data);
    }
}
