package pg_galaxie.pg_galaxie.blocks.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import pg_galaxie.pg_galaxie.blocks.fuel_refinery.FuelRefineryBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EnergyCable extends Block {
    public EnergyCable(Properties properties) {
        super(properties);
        this.INPUTS = new HashMap<>();
    }
    protected HashMap<BlockPos,List<BlockPos>> INPUTS;

    public void generateNetwork(BlockPos engine, HashMap<BlockPos, List<BlockPos>> machines, List<BlockPos> blocked, BlockPos checkedblock, ServerWorld world){
        blocked.add(checkedblock);
        if(INPUTS.get(checkedblock) == null){
            INPUTS.put(checkedblock,new ArrayList<BlockPos>());
        }
        List<BlockPos> bp = INPUTS.get(checkedblock);
        bp.add(engine);
        INPUTS.put(checkedblock,bp);

        List<BlockPos> bl = new ArrayList<BlockPos>();
        bl.add(checkedblock.up());
        bl.add(checkedblock.down());
        bl.add(checkedblock.south());
        bl.add(checkedblock.north());
        bl.add(checkedblock.west());
        bl.add(checkedblock.east());

        for (BlockPos c : bl) {
            if(!blocked.contains(c)) {
                if (world.getBlockState(c).getBlock() instanceof EnergyCable) {
                    ((EnergyCable) world.getBlockState(c).getBlock()).generateNetwork(engine, machines, blocked, c, world);
                }else {
                    if(world.getBlockState(c).getBlock() instanceof InputMachine) {
                        System.out.println("machine"+engine.getCoordinatesAsString()+" find at: "+c.getCoordinatesAsString());
                        blocked.add(c);
                        List<BlockPos> pos = machines.get(engine);
                        pos.add(c);
                        machines.put(engine, pos);
                    }
                }
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if(!worldIn.isRemote()){
            System.out.println("update");
            if(INPUTS.get(pos) == null){
                INPUTS.put(pos,new ArrayList<BlockPos>());
            }
            for (BlockPos bp : INPUTS.get(pos)) {
                if (worldIn.getBlockState(bp).getBlock() instanceof InputMachine) {
                    ((InputMachine)worldIn.getBlockState(bp).getBlock()).INPUTS.remove(bp);
                    ((InputMachine)worldIn.getBlockState(bp).getBlock()).networkUpdate(pos,((World)worldIn));
                }
            }
        }
    }
}
