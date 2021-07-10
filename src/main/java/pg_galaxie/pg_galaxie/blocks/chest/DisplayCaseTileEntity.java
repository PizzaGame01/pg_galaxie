package pg_galaxie.pg_galaxie.blocks.chest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import pg_galaxie.pg_galaxie.deferreds.PGTileEntitys;
import pg_galaxie.pg_galaxie.blocks.PGChestType;

public class DisplayCaseTileEntity extends LockableLootTileEntity implements IChestLid, ITickableTileEntity {
    private NonNullList<ItemStack> chestContents;
    /** The current angle of the lid (between 0 and 1) */
    public int slots;
    protected float lidAngle;
    /** The angle of the lid last tick */
    protected float prevLidAngle;
    /** The number of players currently using this chest */
    protected int numPlayersUsing;

    public PGChestType type;
    /**
     * A counter that is incremented once each tick. Used to determine when to recompute " this is done every 200 ticks
     * (but staggered between different chests). However, the new value isn't actually sent to clients when it is
     * changed.
     */
    private int ticksSinceSync;
    private net.minecraftforge.common.util.LazyOptional<net.minecraftforge.items.IItemHandlerModifiable> chestHandler;

    protected DisplayCaseTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public DisplayCaseTileEntity() {
        this(PGTileEntitys.DISPLAYCASETILE.get());
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.type.slots.size();
    }

    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.pg_galaxie."+type.chestname+"_chest");
    }

    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);

        String x = nbt.getString("chesttype");
        for (PGChestType ct: PGChestType.values()) {
            if(ct.chestname.equals(x)){
                this.type = ct;
                break;
            }
        }

        this.chestContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.chestContents);
        }
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putString("chesttype",type.chestname);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.chestContents);
        }

        return compound;
    }

    public void tick() {
        if(type == null){
            PGChestType c = ((DisplayCaseBlock)getBlockState().getBlock()).type;
            this.type = c;
            this.chestContents = NonNullList.withSize(c.slots.size(), ItemStack.EMPTY);
        }
        if (++this.ticksSinceSync % 20 * 4 == 0) {
            Block b = this.world.getBlockState(this.pos).getBlock();
            if(b instanceof DisplayCaseBlock) {
                this.world.addBlockEvent(this.pos, b, 1, this.numPlayersUsing);
            }
        }

        this.prevLidAngle = this.lidAngle;
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        float f = 0.1F;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            double d0 = (double)i + 0.5D;
            double d1 = (double)k + 0.5D;
            this.playSound(SoundEvents.BLOCK_CHEST_OPEN);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f2 = this.lidAngle;
            if (this.numPlayersUsing > 0) {
                this.lidAngle += 0.1F;
            } else {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            float f1 = 0.5F;
            if (this.lidAngle < 0.5F && f2 >= 0.5F) {
                double d3 = (double)i + 0.5D;
                double d2 = (double)k + 0.5D;
                this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }

    }

    private void playSound(SoundEvent soundIn) {
        //ChestType chesttype = this.getBlockState().get(ChestBlock.TYPE);
        //if (chesttype != ChestType.LEFT) {
            double d0 = (double)this.pos.getX() + 0.5D;
            double d1 = (double)this.pos.getY() + 0.5D;
            double d2 = (double)this.pos.getZ() + 0.5D;
            //if (chesttype == ChestType.RIGHT) {
            //    Direction direction = ChestBlock.getDirectionToAttached(this.getBlockState());
            //    d0 += (double)direction.getXOffset() * 0.5D;
            //    d2 += (double)direction.getZOffset() * 0.5D;
            //}

            this.world.playSound((PlayerEntity)null, d0, d1, d2, soundIn, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        //}
    }

    /**
     * See {@link Block#eventReceived} for more information. This must return true serverside before it is called
     * clientside.
     */

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
        if (block instanceof DisplayCaseBlock) {
            this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, block);
        }

    }

    protected NonNullList<ItemStack> getItems() {
        return this.chestContents;
    }

    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.chestContents = itemsIn;
    }

    public void addItem(ItemStack IS){
        this.chestContents.add(IS);
    }

    public void addItem(int count, ItemStack IS){
        this.chestContents.add(count,IS);
    }

    @OnlyIn(Dist.CLIENT)
    public float getLidAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }

    public static int getPlayersUsing(IBlockReader reader, BlockPos posIn) {
        BlockState blockstate = reader.getBlockState(posIn);
        if (blockstate.hasTileEntity()) {
            TileEntity tileentity = reader.getTileEntity(posIn);
            if (tileentity instanceof DisplayCaseTileEntity) {
                return ((DisplayCaseTileEntity)tileentity).numPlayersUsing;
            }
        }

        return 0;
    }

    public static void swapContents(DisplayCaseTileEntity chest, DisplayCaseTileEntity otherChest) {
        NonNullList<ItemStack> nonnulllist = chest.getItems();
        chest.setItems(otherChest.getItems());
        otherChest.setItems(nonnulllist);
    }

    protected Container createMenu(int id, PlayerInventory player) {
        return new DisplayCaseContainer(id,player,this);
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        if (this.chestHandler != null) {
            net.minecraftforge.common.util.LazyOptional<?> oldHandler = this.chestHandler;
            this.chestHandler = null;
            oldHandler.invalidate();
        }
    }

    /*@Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> cap, Direction side) {
        if (!this.removed && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (this.chestHandler == null)
                this.chestHandler = net.minecraftforge.common.util.LazyOptional.of(this::createHandler);
            return this.chestHandler.cast();
        }
        return super.getCapability(cap, side);
    }*/

    /*private net.minecraftforge.items.IItemHandlerModifiable createHandler() {
        BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof DisplayCaseBlock)) {
            return new net.minecraftforge.items.wrapper.InvWrapper(this);
        }
        IInventory inv = DisplayCaseBlock.getChestInventory((DisplayCaseBlock) state.getBlock(), state, getWorld(), getPos(), true);
        return new net.minecraftforge.items.wrapper.InvWrapper(inv == null ? this : inv);
    }*/

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        if (chestHandler != null)
            chestHandler.invalidate();
    }
}
