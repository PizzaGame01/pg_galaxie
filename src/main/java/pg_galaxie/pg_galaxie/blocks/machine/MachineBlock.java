package pg_galaxie.pg_galaxie.blocks.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.lwjgl.system.CallbackI;
import pg_galaxie.pg_galaxie.deferreds.PGFluids;

import java.util.*;

public abstract class MachineBlock extends Block {
    public MachineBlock(Properties properties) {
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
            //System.out.println("update");
            INPUTS.clear();
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
                    ((EnergyCable) worldIn.getBlockState(c).getBlock()).neighborChanged(state,worldIn, pos,blockIn,fromPos,isMoving);
                    break;
                }
            }
            this.networkUpdate(pos,((World)worldIn));
        }
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);

        if(!INPUTS.containsKey(pos)){
            INPUTS.put(pos,new ArrayList<BlockPos>());
        }




        for (BlockPos pb : INPUTS.get(pos)) {

            TileEntity fr = worldIn.getTileEntity(pb);
            if(fr == null){
                System.out.println("contiune");
                continue;
            }

            for (BlockPos bp : INPUTS.get(pos)) {
                if(bp.getCoordinatesAsString().equals(pb.getCoordinatesAsString())){ continue;}

                TileEntity te = worldIn.getTileEntity(bp);
                if (te == null) {
                    System.out.println("contiune");
                    continue;
                }
                if (this.MachineCanRecieve(worldIn,pb)||this.MachineCanExtract(worldIn, bp)) {
                    if (this.MachineEnergyAmount(worldIn, bp) > 0 && this.MachineEnergyAmount(worldIn, pb) < this.MachineEnergyMaxAmount(worldIn, pb))
                        te.getCapability(CapabilityEnergy.ENERGY, Direction.NORTH).ifPresent(capability -> capability.extractEnergy(1, false));
                        fr.getCapability(CapabilityEnergy.ENERGY, Direction.NORTH).ifPresent(capability -> capability.receiveEnergy(1, false));
                        System.out.println("send");
                }else {
                    for (ServerPlayerEntity spe : worldIn.getPlayers()) {
                        spe.sendMessage(ITextComponent.getTextComponentOrEmpty("this want not to work"), null);
                    }
                }
            }
        }

    }

    public abstract int EnergySpeed();

    public abstract boolean canRecieve();
    public abstract boolean canExtract();

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

    public int MachineEnergyAmount(World world,BlockPos pos){
        return Optional.ofNullable(world.getTileEntity(pos))
                .flatMap(te -> te.getCapability(CapabilityEnergy.ENERGY).resolve())
                .map(cap -> cap.getEnergyStored())
                .orElse(0);
    }

    public int MachineEnergyMaxAmount(World world,BlockPos pos){
        return Optional.ofNullable(world.getTileEntity(pos))
                .flatMap(te -> te.getCapability(CapabilityEnergy.ENERGY).resolve())
                .map(cap -> cap.getMaxEnergyStored())
                .orElse(0);
    }
}