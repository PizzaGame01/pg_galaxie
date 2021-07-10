package pg_galaxie.pg_galaxie.blocks.chest;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import pg_galaxie.pg_galaxie.deferreds.PGTileEntitys;
import pg_galaxie.pg_galaxie.blocks.PGChestType;

import javax.annotation.Nullable;
import java.util.List;

public class DisplayCaseBlock extends AbstractChestBlock<DisplayCaseTileEntity> implements IWaterLoggable {


    protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public PGChestType type;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public DisplayCaseBlock(PGChestType type, Properties builder) {
        super(builder, () -> {
            return (TileEntityType<? extends DisplayCaseTileEntity>) PGTileEntitys.DISPLAYCASETILE.get();
        });

        this.type = type;
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DisplayCaseTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof DisplayCaseTileEntity){
                NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider) te,pos);
                //player.openContainer(te);
                //player.openContainer(this.TYPE_NAME)
                player.addStat(this.getOpenStat());
            }
        }
        return ActionResultType.CONSUME;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new DisplayCaseTileEntity();
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
    public static Direction getDirectionToAttached(BlockState state) {
        Direction direction = state.get(FACING);
        return  direction.rotateYCCW();
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Nullable
    private Direction getDirectionToAttach(BlockItemUseContext context, Direction direction) {
        BlockState blockstate = context.getWorld().getBlockState(context.getPos().offset(direction));
        if(blockstate.hasProperty(FACING)){
            return blockstate.get(FACING);
        }else {
            return null;
        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER));
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof DisplayCaseTileEntity) {
                ((DisplayCaseTileEntity)tileentity).setCustomName(stack.getDisplayName());
            }
        }

    }

    @OnlyIn(Dist.CLIENT)
    public TileEntityMerger.ICallbackWrapper<? extends ChestTileEntity> combine(BlockState state, World world, BlockPos pos, boolean override) {
        return TileEntityMerger.ICallback::func_225537_b_;
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    protected Stat<ResourceLocation> getOpenStat() {
        return Stats.CUSTOM.get(Stats.OPEN_CHEST);
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.matchesBlock(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof IInventory) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static TileEntityMerger.ICallback<ChestTileEntity, Float2FloatFunction> getLidRotationCallback(final IChestLid lid) {
        return new TileEntityMerger.ICallback<ChestTileEntity, Float2FloatFunction>() {
            public Float2FloatFunction func_225539_a_(ChestTileEntity p_225539_1_, ChestTileEntity p_225539_2_) {
                return (angle) -> {
                    return Math.max(p_225539_1_.getLidAngle(angle), p_225539_2_.getLidAngle(angle));
                };
            }

            public Float2FloatFunction func_225538_a_(ChestTileEntity p_225538_1_) {
                return p_225538_1_::getLidAngle;
            }

            public Float2FloatFunction func_225537_b_() {
                return lid::getLidAngle;
            }
        };
    }

    public static boolean isBlocked(IWorld world, BlockPos pos) {
        return isBelowSolidBlock(world, pos) || isCatSittingOn(world, pos);
    }


    private static boolean isBelowSolidBlock(IBlockReader reader, BlockPos worldIn) {
        BlockPos blockpos = worldIn.up();
        return reader.getBlockState(blockpos).isNormalCube(reader, blockpos);
    }

    private static boolean isCatSittingOn(IWorld world, BlockPos pos) {
        List<CatEntity> list = world.getEntitiesWithinAABB(CatEntity.class, new AxisAlignedBB((double)pos.getX(), (double)(pos.getY() + 1), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 2), (double)(pos.getZ() + 1)));
        if (!list.isEmpty()) {
            for(CatEntity catentity : list) {
                if (catentity.isEntitySleeping()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }
}
