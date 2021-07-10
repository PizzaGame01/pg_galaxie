package pg_galaxie.pg_galaxie.blocks;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LaunchpadBlock extends Block {

    public static final IntegerProperty STATE = IntegerProperty.create("state", 0, 1);

    public LaunchpadBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(STATE, Integer.valueOf(0)));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
            VoxelShape CUBE;
            if(state.get(STATE) == 0) {
                CUBE = makeCuboidShape(1D, 1D, 1D, 16.0D, 3D, 16.0D);
            }else {
                CUBE = makeCuboidShape(1D, 1D, 1D, 16.0D, 5D, 16.0D);
            }
            return CUBE;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {

    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        //worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), this, 1);
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), this, 1);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);
        List<Integer[]> x = new ArrayList<Integer[]>();
        x.add(new Integer[]{0,1});
        x.add(new Integer[]{1,1});
        x.add(new Integer[]{1,0});
        x.add(new Integer[]{-1,0});
        x.add(new Integer[]{-1,-1});
        x.add(new Integer[]{0,-1});
        x.add(new Integer[]{1,-1});
        x.add(new Integer[]{-1,1});

        List<Integer[]> y = new ArrayList<Integer[]>();
        y.add(new Integer[]{0,2});
        y.add(new Integer[]{0,-2});
        y.add(new Integer[]{2,0});
        y.add(new Integer[]{-2,0});
        y.add(new Integer[]{-2,1});
        y.add(new Integer[]{-2,-1});
        y.add(new Integer[]{2,1});
        y.add(new Integer[]{1,-2});
        y.add(new Integer[]{2,-2});
        y.add(new Integer[]{-2,-2});
        y.add(new Integer[]{-1,-1});
        y.add(new Integer[]{3,1});
        y.add(new Integer[]{1,2});
        y.add(new Integer[]{2,2});

        boolean canEdit = true;

        for (Integer[] value : x) {
            BlockPos bp = new BlockPos(pos.getX()+value[0],pos.getY(),pos.getZ()+value[1]);

            if(!(worldIn.getBlockState(bp).getBlock() instanceof LaunchpadBlock && worldIn.getBlockState(bp).get(STATE) == 0)){
                canEdit = false;
                break;
            }
        }

        for (Integer[] val : y) {
            BlockPos bp = new BlockPos(pos.getX()+val[0],pos.getY(),pos.getZ()+val[1]);
            if(!((!(worldIn.getBlockState(bp).getBlock() instanceof LaunchpadBlock))  || (worldIn.getBlockState(bp).getBlock() instanceof LaunchpadBlock && worldIn.getBlockState(bp).get(STATE) == 0))){
                canEdit = false;
                break;
            }
        }

        if(canEdit){
            ((World)worldIn).setBlockState(pos, state.with(STATE, Integer.valueOf(1)), 2);
            ((World)worldIn).updateComparatorOutputLevel(pos, this);
        }else if(state.get(STATE) == 1){
            ((World)worldIn).setBlockState(pos, state.with(STATE, Integer.valueOf(0)), 2);
            ((World)worldIn).updateComparatorOutputLevel(pos, this);
        }

        worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), this, 1);
    }

    //public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
    //    return false;
    //}


    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Block b = worldIn.getBlockState(pos.down()).getBlock();
        return !(b.matchesBlock(Blocks.COBWEB)||b instanceof FlowingFluidBlock || b instanceof SaplingBlock||b instanceof LeavesBlock||b instanceof LaunchpadBlock||b instanceof CropsBlock||b instanceof LadderBlock||b instanceof AbstractRailBlock|| b instanceof FlowerBlock);
    }
}
