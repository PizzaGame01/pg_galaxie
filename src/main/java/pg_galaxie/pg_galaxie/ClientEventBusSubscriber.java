package pg_galaxie.pg_galaxie;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.EntityType;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import pg_galaxie.pg_galaxie.blocks.PGChestType;
import pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseScreen;
import pg_galaxie.pg_galaxie.blocks.fuel_refinery.FuelRefineryScreen;
import pg_galaxie.pg_galaxie.deferreds.PGBlocks;
import pg_galaxie.pg_galaxie.deferreds.PGContainerType;
import pg_galaxie.pg_galaxie.deferreds.PGEntitys;
import pg_galaxie.pg_galaxie.entitys.alien.AlienRenderer;
import pg_galaxie.pg_galaxie.entitys.rocket.RocketRenderer;
import pg_galaxie.pg_galaxie.items.PGSpawnEggItem;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = Pg_galaxie.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){

        RenderingRegistry.registerEntityRenderingHandler(PGEntitys.ALIEN.get(), ((IRenderFactory) AlienRenderer::new));
        RenderingRegistry.registerEntityRenderingHandler(PGEntitys.ROCKET.get(), ((IRenderFactory) RocketRenderer::new));

        ScreenManager.registerFactory(PGContainerType.DISPLAY_CASE_CONTAINER.get(), (ScreenManager.IScreenFactory) DisplayCaseScreen::new);
        ScreenManager.registerFactory(PGContainerType.FUEL_REFINERY_CONTAINER.get(), (ScreenManager.IScreenFactory) FuelRefineryScreen::new);
    }

    @SubscribeEvent
    public static void onRegistrerEntities(final RegistryEvent.Register<EntityType<?>> event){
        PGSpawnEggItem.initSpawnEggs();
    }

    @SubscribeEvent
    public static void onStitch(final TextureStitchEvent.Pre event) {
        if (!event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            return;
        }
        Arrays.stream(PGChestType.values()).forEach(c -> {
            System.out.println(c.getTexture().getPath()+" added");
            event.addSprite(c.getTexture());
        });
        //System.out.println("registry chest");
        //event.addSprite(DisplayCaseRenderer.TEST_CHEST);
    }

    //
}
