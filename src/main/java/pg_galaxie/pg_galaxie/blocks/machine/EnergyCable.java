package pg_galaxie.pg_galaxie.blocks.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.energy.CapabilityEnergy;
import pg_galaxie.pg_galaxie.blocks.fuel_refinery.FuelRefineryTileEntity;

import javax.annotation.Nullable;
import java.util.*;


public class EnergyCable extends Block {

    public EnergyCable(Properties properties) {
        super(properties);
    }
    public static HashMap<BlockPos,List<BlockPos>> CABLENETWORKS = new HashMap<>();
    public static HashMap<BlockPos,List<BlockPos>> CABLES;

    public void networkUpdate(BlockPos pos,World worldIn){
        if(!worldIn.isRemote()) {
            //if(!CABLENETWORKS.containsKey(pos)){
            //    CABLENETWORKS.put(pos,new ArrayList<BlockPos>());
            //}
            /*List<BlockPos> bl = new ArrayList<BlockPos>();
            bl.add(pos.up());
            bl.add(pos.down());
            bl.add(pos.south());
            bl.add(pos.north());
            bl.add(pos.west());
            bl.add(pos.east());*/

            HashMap<BlockPos, List<BlockPos>> map = new HashMap<>();
            HashMap<BlockPos, List<BlockPos>> map2 = new HashMap<>();

            List<BlockPos> blocked = new ArrayList<BlockPos>();
            blocked.add(pos);
            this.generateNetwork(pos, map, blocked, pos, (ServerWorld) worldIn);
            //for (BlockPos c : bl) {
            //    if (worldIn.getBlockState(c).getBlock() instanceof EnergyCable) {
            //        ((EnergyCable) worldIn.getBlockState(c).getBlock()).generateNetwork(pos, map, blocked, c, ((ServerWorld) worldIn));
            //    }
            //}

            if (map.get(pos) == null) {
                map.put(pos, new ArrayList<>());
            }

            boolean ALREADY_EXIST = true;

            if (CABLENETWORKS.entrySet().size() == 0) {
                System.out.println("System Added");
                CABLENETWORKS.put(pos, map.get(pos));
            } else {
                for (Map.Entry<BlockPos, List<BlockPos>> x : CABLENETWORKS.entrySet()) {
                    ALREADY_EXIST = true;
                    System.out.println("####TASK####");
                    for (BlockPos p : map.get(pos)) {
                        if (!(x.getValue().contains(p))) {
                            ALREADY_EXIST = false;
                            System.out.println("exist");
                        } else {
                            System.out.println("don't exist");
                        }
                    }
                    if (ALREADY_EXIST) {
                        break;
                    }
                }

                if ((!ALREADY_EXIST) && (map.get(pos).size() > 0)) {
                    System.out.println("System Added");
                    CABLENETWORKS.put(pos, map.get(pos));
                } else {
                    System.out.println("already exist");
                }
            }
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        System.out.println("x");
        this.networkUpdate(pos,worldIn);
    }

    public void generateNetwork(BlockPos engine, HashMap<BlockPos, List<BlockPos>> machines, List<BlockPos> blocked, BlockPos checkedblock, ServerWorld world){
        blocked.add(checkedblock);
        /*if(!INPUTS.containsKey(checkedblock)){
            INPUTS.put(checkedblock,new ArrayList<BlockPos>());
        }
        List<BlockPos> bp = INPUTS.get(checkedblock);
        bp.add(engine);
        INPUTS.put(checkedblock,bp);*/

        if(machines.get(engine) == null){
            machines.put(engine,new ArrayList<>());
        }

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
                    if(this.MachineCanRecieve(world,c)||this.MachineCanExtract(world,c)) {
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
            //if(!CABLENETWORKS.containsKey(pos)){
            //    CABLENETWORKS.put(pos,new ArrayList<BlockPos>());
            //}

            //List<BlockPos> BLOCKS = CABLENETWORKS.get(pos);
            //INPUTS.clear();
            //for (BlockPos bp : BLOCKS) {
                //System.out.println("update");
                //if (worldIn.getBlockState(bp).getBlock() instanceof MachineBlock) {
                    //((MachineBlock)worldIn.getBlockState(bp).getBlock()).INPUTS.remove(bp);
            try {
                for (Map.Entry<BlockPos,List<BlockPos>>x :CABLENETWORKS.entrySet()) {
                    if(x.getValue().contains(pos)){
                        CABLENETWORKS.remove(x.getKey());
                    }
                }
            }catch (ConcurrentModificationException cme){

            }

            this.networkUpdate(pos,worldIn);
                    //((MachineBlock)worldIn.getBlockState(bp).getBlock()).networkUpdate(pos,((World)worldIn));
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

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnegryCableTileEntity();
    }
}