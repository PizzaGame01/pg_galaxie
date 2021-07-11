package pg_galaxie.pg_galaxie.blocks.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class InputMachine extends Block {
    public InputMachine(Properties properties) {
        super(properties);
        this.INPUTS = new HashMap<>();
    }

    public HashMap<BlockPos, List<BlockPos>> INPUTS;

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        this.networkUpdate(pos,worldIn);
    }

    public void networkUpdate(BlockPos pos,World worldIn){
        if(!worldIn.isRemote()){
            if(!INPUTS.containsKey(pos)){
                INPUTS.put(pos,new ArrayList<BlockPos>());
            }
            List<BlockPos> bl = new ArrayList<BlockPos>();
            bl.add(pos.up());
            bl.add(pos.down());
            bl.add(pos.south());
            bl.add(pos.north());
            bl.add(pos.west());
            bl.add(pos.east());

            List<BlockPos> blocked = new ArrayList<BlockPos>();
            blocked.add(pos);
            for (BlockPos c : bl) {
                if (worldIn.getBlockState(c).getBlock() instanceof EnergyCable) {
                    ((EnergyCable) worldIn.getBlockState(c).getBlock()).generateNetwork(pos, INPUTS, blocked, c, ((ServerWorld) worldIn));
                }
            }
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!INPUTS.containsKey(pos)){
            INPUTS.put(pos,new ArrayList<BlockPos>());
        }
        /*for (BlockPos c : INPUTS.get(pos)) {
            System.out.println(c.getCoordinatesAsString());
        }*/
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if(!worldIn.isRemote()){
            System.out.println("update");
            INPUTS.clear();
            this.networkUpdate(pos,((World)worldIn));
        }
    }
}
