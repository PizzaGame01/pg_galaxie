package pg_galaxie.pg_galaxie.blocks.machine;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import pg_galaxie.pg_galaxie.deferreds.PGTileEntitys;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnegryCableTileEntity extends TileEntity implements ITickableTileEntity {
    public EnegryCableTileEntity() {
        super(PGTileEntitys.CABLE.get());
    }

    @Override
    public void tick() {
        if(!this.world.isRemote()) {
            if (this.world.getBlockState(this.pos).getBlock() instanceof EnergyCable) {
                EnergyCable ec = (EnergyCable) this.world.getBlockState(this.pos).getBlock();
                if (EnergyCable.CABLENETWORKS.get(this.pos) != null) {
                    List<BlockPos> RECIEVE = new ArrayList<>();
                    List<BlockPos> EXTRACT = new ArrayList<>();

                    for (BlockPos p:EnergyCable.CABLENETWORKS.get(pos)) {
                        if(this.canExtract(world,p)){
                            EXTRACT.add(p);
                        }else if(this.canRecieve(world,p)){
                            RECIEVE.add(p);
                        }
                    }

                    for (BlockPos c : RECIEVE) {
                        for(BlockPos e : EXTRACT){
                            TileEntity et = this.world.getTileEntity(e);
                            TileEntity ct = this.world.getTileEntity(e);

                            if(et == null||ct==null){
                                continue;
                            }
                            if(this.getEnergy(world,e) >= 1&&this.getEnergy(world,c) < this.getMaxEnergy(world,c)){

                                et.getCapability(CapabilityEnergy.ENERGY, Direction.NORTH).ifPresent(capability -> capability.extractEnergy(1, false));
                                ct.getCapability(CapabilityEnergy.ENERGY, Direction.NORTH).ifPresent(capability -> capability.receiveEnergy(1, false));
                            }
                        }
                    }

                    System.out.println("handle");
                    /*for (BlockPos pb : EnergyCable.CABLENETWORKS.get(pos)) {
                        TileEntity fr = this.world.getTileEntity(pb);
                        if (fr == null) {
                            System.out.println("contiune");
                            continue;
                        }
                        for (BlockPos bp : EnergyCable.CABLENETWORKS.get(pos)) {
                            if (bp.getCoordinatesAsString().equals(pb.getCoordinatesAsString())) {
                                continue;
                            }

                            TileEntity te = this.world.getTileEntity(bp);
                            if (te == null) {
                                System.out.println("contiune");
                                continue;
                            }
                            if (this.canRecieve(world, pb) || this.canExtract(world, bp)) {
                                if (this.getEnergy(world, bp) > 0 && this.getEnergy(world, pb) < this.getMaxEnergy(world, pb)) {
                                    te.getCapability(CapabilityEnergy.ENERGY, Direction.NORTH).ifPresent(capability -> capability.extractEnergy(1, false));
                                    fr.getCapability(CapabilityEnergy.ENERGY, Direction.NORTH).ifPresent(capability -> capability.receiveEnergy(1, false));
                                    System.out.println("send");

                                    world.getPlayers().get(0).sendMessage(ITextComponent.getTextComponentOrEmpty("send"),null);
                                }else {
                                    world.getPlayers().get(0).sendMessage(ITextComponent.getTextComponentOrEmpty("its worng"),null);
                                }
                            }
                        }
                    }*/
                }
            }
        }
    }

    public boolean canRecieve(World world, BlockPos pos){
        return Optional.ofNullable(world.getTileEntity(pos))
                .flatMap(te -> te.getCapability(CapabilityEnergy.ENERGY).resolve())
                .map(cap -> cap.canReceive())
                .orElse(false);
    }

    public boolean canExtract(World world, BlockPos pos){
        return Optional.ofNullable(world.getTileEntity(pos))
                .flatMap(te -> te.getCapability(CapabilityEnergy.ENERGY).resolve())
                .map(cap -> cap.canExtract())
                .orElse(false);
    }

    public int getEnergy(World world,BlockPos pos){
        return Optional.ofNullable(world.getTileEntity(pos))
                .flatMap(te -> te.getCapability(CapabilityEnergy.ENERGY).resolve())
                .map(cap -> cap.getEnergyStored())
                .orElse(0);
    }

    public int getMaxEnergy(World world,BlockPos pos){
        return Optional.ofNullable(world.getTileEntity(pos))
                .flatMap(te -> te.getCapability(CapabilityEnergy.ENERGY).resolve())
                .map(cap -> cap.getMaxEnergyStored())
                .orElse(0);
    }
}
