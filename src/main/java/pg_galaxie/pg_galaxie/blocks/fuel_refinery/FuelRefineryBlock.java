package pg_galaxie.pg_galaxie.blocks.fuel_refinery;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkHooks;
import pg_galaxie.pg_galaxie.blocks.machine.InputMachine;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class FuelRefineryBlock extends InputMachine {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final IntegerProperty BUCKETS = IntegerProperty.create("buckets",0,3);


    public FuelRefineryBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(BUCKETS,0).with(FACING, Direction.NORTH));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        super.onBlockActivated(state,worldIn,pos,player,handIn,hit);
        TileEntity te = worldIn.getTileEntity(pos);
        if (worldIn.isRemote) {
            if (te != null) {
                if(((FuelRefineryTileEntity) te).buckets < ((FuelRefineryTileEntity) te).maxbuckets) {
                    if (player.getHeldItem(handIn).getItem() == Items.WATER_BUCKET) {
                        worldIn.playSound(player, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1f, 1f);
                    }
                }
            }
            return ActionResultType.SUCCESS;
        } else {
            if(!(player.getHeldItem(handIn).getItem() instanceof BucketItem)) {
                if (te instanceof FuelRefineryTileEntity) {
                    Consumer<PacketBuffer> packetBufferConsumer = pb -> pb.writeBlockPos(pos).writeInt(((FuelRefineryTileEntity) te).buckets);

                    NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, packetBufferConsumer);
                }
            }else {
                if(player.getHeldItem(handIn).getItem() == Items.WATER_BUCKET){
                    if (te instanceof FuelRefineryTileEntity) {
                        if(((FuelRefineryTileEntity) te).buckets < ((FuelRefineryTileEntity) te).maxbuckets){
                            if(!player.isCreative()){
                                player.inventory.setInventorySlotContents(player.inventory.currentItem,new ItemStack(Items.BUCKET,1));
                            }

                            AtomicInteger _retval = new AtomicInteger(0);
                            te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                                    .ifPresent(capability -> _retval.set(capability.getFluidInTank(3000).getAmount()));
                            int amount = (int) 1000+_retval.get();
                            te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(capability -> capability.fill(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE));

                            ((World)worldIn).setBlockState(pos, state.with(BUCKETS, ((FuelRefineryTileEntity)te).buckets+1), 2);
                            ((World)worldIn).updateComparatorOutputLevel(pos, this);

                            ((FuelRefineryTileEntity) te).buckets++;
                        }
                    }
                }
            }
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FuelRefineryTileEntity();
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), this, 1);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);


        if(!worldIn.isRemote()) {

            FuelRefineryTileEntity fr = (FuelRefineryTileEntity)worldIn.getTileEntity(pos);
            ItemStack y = fr.inventory.get(0);

            if (y.getItem() == Items.WATER_BUCKET) {

                if(fr.buckets < fr.maxbuckets) {


                    AtomicInteger _retval = new AtomicInteger(0);
                    fr.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                            .ifPresent(capability -> _retval.set(capability.getFluidInTank(3000).getAmount()));
                    int amount = (int) 1000+_retval.get();
                    fr.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(capability -> capability.fill(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE));


                    fr.buckets++;
                    ((World)worldIn).setBlockState(pos, state.with(BUCKETS, fr.buckets), 2);
                    ((World)worldIn).updateComparatorOutputLevel(pos, this);
                    fr.inventory.set(0, ItemStack.EMPTY);
                    fr.getUpdatePacket();

                }
                fr.inventory.set(0, new ItemStack(Items.BUCKET, 1));

            }

            worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), this, 1);
        }else {
            //worldIn.getClosestPlayer()
            //worldIn.playSound(player, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1f, 1f);
        }
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING,BUCKETS);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    public void setupEnergy(World world,BlockPos pos){
        {
            TileEntity _ent = world.getTileEntity(pos);
            int _amount = (int) 1000;
            if (_ent != null)
                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.receiveEnergy(_amount, false));
        }
    }
}
