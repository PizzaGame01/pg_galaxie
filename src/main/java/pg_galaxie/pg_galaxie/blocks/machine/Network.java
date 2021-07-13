package pg_galaxie.pg_galaxie.blocks.machine;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class Network {

    public final static List<Network> NETWORKS = new ArrayList<Network>();

    public int c;

    public final ServerWorld world;

    protected List<BlockPos> cables;
    protected List<BlockPos> machines;

    public Network(ServerWorld w){
        NETWORKS.add(this);
        this.c = NETWORKS.size()-1;

        this.world = w;

        this.cables = new ArrayList<>();
        this.machines = new ArrayList<>();
    }

    public void destroy(){
        for (Network n : NETWORKS) {
            if(n.c > c){
                n.c -= 1;
            }
        }
        NETWORKS.remove(c);
    }

    public void addCable(BlockPos b){
        if(this.world.getBlockState(b).getBlock() instanceof EnergyCable){
            this.cables.add(b);
        }
    }
}
