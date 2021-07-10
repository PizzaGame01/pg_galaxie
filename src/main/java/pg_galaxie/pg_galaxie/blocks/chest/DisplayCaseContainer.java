package pg_galaxie.pg_galaxie.blocks.chest;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import pg_galaxie.pg_galaxie.deferreds.PGBlocks;
import pg_galaxie.pg_galaxie.deferreds.PGContainerType;
import pg_galaxie.pg_galaxie.blocks.PGChestType;

import java.util.Objects;

public class DisplayCaseContainer extends Container {

    public final DisplayCaseTileEntity te;
    private final IWorldPosCallable canInteractWithCallable;

    public int i;
    public PGChestType type;

    public DisplayCaseContainer(final int windowId, final PlayerInventory playerInv, final DisplayCaseTileEntity te) {
        super(PGContainerType.DISPLAY_CASE_CONTAINER.get(),windowId);
        this.te = te;
        this.canInteractWithCallable = IWorldPosCallable.of(te.getWorld(),te.getPos());

        te.openInventory(playerInv.player);

        this.type = te.type;

        this.i = 0;
        te.type.slots.forEach(x -> {
            this.addSlot(new Slot(te, this.i, x[0]*18-11+this.type.extrax, x[1]*18-11+this.type.extray));
            this.i++;
        });
        //this.addSlot(new Slot((IInventory) te,0,80,35));

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInv, j1 + l * 9 + 9,8 + j1 * 18, 166 - (4 - l) * 18 - 10));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInv, i1, 8 + i1 * 18, 142));
        }
    }

    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        te.closeInventory(playerIn);
    }

    //protected DisplayCaseContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
    //    this(windowId,playerInv,getTileEntity(playerInv,data));
    //}

    public DisplayCaseContainer(int i, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        this(i,playerInventory,DisplayCaseContainer.getTileEntity(playerInventory,packetBuffer));
    }

    protected static DisplayCaseTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data){
        Objects.requireNonNull(playerInv,"Player Inventory Cannot be null");
        Objects.requireNonNull(data,"Packet Buffer Cannot be null");
        final TileEntity te = playerInv.player.world.getTileEntity(data.readBlockPos());
        if(te instanceof DisplayCaseTileEntity){
            return (DisplayCaseTileEntity) te;
        }
        throw new IllegalStateException("Tile Entity Is Not correct");
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {

        return isWithinUsableDistance(canInteractWithCallable,playerIn, PGBlocks.OAK_CHEST.get());

    }

    protected static boolean isWithinUsableDistance(IWorldPosCallable worldPos, PlayerEntity playerIn, Block targetBlock) {
        return worldPos.applyOrElse((p_216960_2_, p_216960_3_) -> {
            return p_216960_2_.getBlockState(p_216960_3_).getBlock() instanceof DisplayCaseBlock && playerIn.getDistanceSq((double) p_216960_3_.getX() + 0.5D, (double) p_216960_3_.getY() + 0.5D, (double) p_216960_3_.getZ() + 0.5D) <= 64.0D;
        }, true);
    }

/*    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack()){
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
            if(index < 36 && !this.mergeItemStack(stack1, DisplayCaseTileEntity.slots,this.inventorySlots.size(),false)){
                return ItemStack.EMPTY;
            }
            if(!this.mergeItemStack(stack1, DisplayCaseTileEntity.slots,0,false)){
                return ItemStack.EMPTY;
            }

            if(stack1.isEmpty()){
                slot.putStack(ItemStack.EMPTY);
            }else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }*/
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
}
