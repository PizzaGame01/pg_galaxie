package pg_galaxie.pg_galaxie.blocks.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EnergyCable extends Block {
    public EnergyCable(Properties properties) {
        super(properties);
        this.INPUTS = new HashMap<>();
    }
    protected HashMap<BlockPos,List<BlockPos>> INPUTS;

    public void generateNetwork(BlockPos engine, HashMap<BlockPos, List<BlockPos>> machines, List<BlockPos> blocked, BlockPos checkedblock, ServerWorld world){
        blocked.add(checkedblock);
        if(!INPUTS.containsKey(checkedblock)){
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
                    if(this.MachineCanRecieve(world,c)) {
                        if(!machines.containsKey(c)) {
                            System.out.println("machine"+engine.getCoordinatesAsString()+" find at: "+c.getCoordinatesAsString());
                            blocked.add(c);
                            List<BlockPos> pos = machines.get(engine);
                            pos.add(c);
                            machines.put(engine, pos);
                        }
                    }else if(this.MachineCanExtract(world,c)) {
                        if(!machines.containsKey(c)) {
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
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);


        if(!worldIn.isRemote()){
            //System.out.println("update");
            if(!INPUTS.containsKey(pos)){
                INPUTS.put(pos,new ArrayList<BlockPos>());
            }

            List<BlockPos> BLOCKS = INPUTS.get(pos);
            INPUTS.clear();
            for (BlockPos bp : BLOCKS) {
                if (worldIn.getBlockState(bp).getBlock() instanceof MachineBlock) {
                    ((MachineBlock)worldIn.getBlockState(bp).getBlock()).INPUTS.remove(bp);
                    ((MachineBlock)worldIn.getBlockState(bp).getBlock()).networkUpdate(pos,((World)worldIn));
                }
            }
        }
    }


    public boolean MachineCanRecieve(World world,BlockPos pos){
        return Optional.ofNullable(world.getTileEntity(pos))
                .flatMap(te -> te.getCapability(CapabilityEnergy.ENERGY).resolve())
                .map(cap -> cap.canReceive())
                .orElse(false);
    }

    public boolean MachineCanExtract(World world, BlockPos pos){
        return Optional.ofNullable(world.getTileEntity(pos))
                .flatMap(te -> te.getCapability(CapabilityEnergy.ENERGY).resolve())
                .map(cap -> cap.canExtract())
                .orElse(false);
    }
}
