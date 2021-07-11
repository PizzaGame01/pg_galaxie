package pg_galaxie.pg_galaxie;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.fluid.Fluid;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseRenderer;
import pg_galaxie.pg_galaxie.deferreds.*;
import pg_galaxie.pg_galaxie.entitys.alien.AlienEntity;
import pg_galaxie.pg_galaxie.entitys.rocket.RocketEntity;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("pg_galaxie")
public class Pg_galaxie {

    public static final String MODID = "pg_galaxie";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Pg_galaxie() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PGFluids.FLUIDS.register(bus);
        PGBlocks.BLOCKS.register(bus);
        //PGFluids.FLUIDS.register(bus);
        PGItems.ITEMS.register(bus);
        PGTileEntitys.TILE_ENTITYS.register(bus);
        PGContainerType.CONTAINER_TYPES.register(bus);
        PGEntitys.ENTITYS.register(bus);
        // Register the setup method for modloading
        bus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        bus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        bus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        bus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {

        event.enqueueWork(() -> {
        });

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put((EntityType<? extends AgeableEntity>) PGEntitys.ALIEN.get(), AlienEntity.setCustomAttributes().create());
            GlobalEntityTypeAttributes.put((EntityType<? extends CreatureEntity>) PGEntitys.ROCKET.get(), RocketEntity.setCustomAttributes().create());
        });

        ClientRegistry.bindTileEntityRenderer((TileEntityType)PGTileEntitys.DISPLAYCASETILE.get(), DisplayCaseRenderer::new);

        RenderTypeLookup.setRenderLayer(PGBlocks.FUEL.get(), RenderType.getCutout());

        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("pg_galaxie", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {



        @SubscribeEvent
        public static void onFluidRegistry(final RegistryEvent.Register<Fluid> event){

        }

    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void CameraPos(EntityViewRenderEvent.CameraSetup event) {
        if (event.getInfo().getRenderViewEntity().getRidingEntity() instanceof RocketEntity) {
            if (Minecraft.getInstance().gameSettings.getPointOfView().equals(PointOfView.THIRD_PERSON_FRONT)) {
                event.getInfo().movePosition(-event.getInfo().calcCameraDistance(8d), 0d, 0);
            }
            if (Minecraft.getInstance().gameSettings.getPointOfView().equals(PointOfView.THIRD_PERSON_BACK)) {
                event.getInfo().movePosition(-event.getInfo().calcCameraDistance(8d), 0d, 0);
            }
        }
    }
}
