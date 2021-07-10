package pg_galaxie.pg_galaxie;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class PacketManager {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Pg_galaxie.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void handle(int msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Work that needs to be thread-safe (most work)
            ServerPlayerEntity sender = ctx.get().getSender(); // the client that sent this packet
            // Do stuff
        });
        ctx.get().setPacketHandled(true);
    }

    public class GuiPacket{
        public int rs,rss;
        public GuiPacket(int rs, int rss){
            this.rs = rs;
            this.rss = rss;
        }
    }
}
