package pg_galaxie.pg_galaxie.deferreds;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import pg_galaxie.pg_galaxie.Pg_galaxie;
import pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseItemStackRenderer;
import pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseTileEntity;
import pg_galaxie.pg_galaxie.items.PGSpawnEggItem;
import pg_galaxie.pg_galaxie.items.RocketItem;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class PGItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Pg_galaxie.MODID);
    public static RegistryObject<BlockItem> DISPLAY_CASE = ITEMS.register("display_case",() -> new BlockItem(PGBlocks.CHEST.get(),new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(() -> (Callable<ItemStackTileEntityRenderer>)(() -> new DisplayCaseItemStackRenderer<pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseTileEntity>((Supplier<pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseTileEntity>) DisplayCaseTileEntity::new)))));

    public static RegistryObject<BlockItem> OAK_CHEST= ITEMS.register("oak_chest",() -> new BlockItem(PGBlocks.OAK_CHEST.get(),new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(() -> (Callable<ItemStackTileEntityRenderer>)(() -> new DisplayCaseItemStackRenderer<DisplayCaseTileEntity>((Supplier<DisplayCaseTileEntity>) DisplayCaseTileEntity::new)))));
    public static RegistryObject<BlockItem> BIRCH_CHEST= ITEMS.register("birch_chest",() -> new BlockItem(PGBlocks.BIRCH_CHEST.get(),new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(() -> (Callable<ItemStackTileEntityRenderer>)(() -> new DisplayCaseItemStackRenderer<DisplayCaseTileEntity>((Supplier<DisplayCaseTileEntity>) DisplayCaseTileEntity::new)))));
    public static RegistryObject<BlockItem> ACACIA_CHEST= ITEMS.register("acacia_chest",() -> new BlockItem(PGBlocks.ACACIA_CHEST.get(),new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(() -> (Callable<ItemStackTileEntityRenderer>)(() -> new DisplayCaseItemStackRenderer<DisplayCaseTileEntity>((Supplier<DisplayCaseTileEntity>) DisplayCaseTileEntity::new)))));
    public static RegistryObject<BlockItem> CRIMSON_CHEST= ITEMS.register("crimson_chest",() -> new BlockItem(PGBlocks.CRIMSON_CHEST.get(),new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(() -> (Callable<ItemStackTileEntityRenderer>)(() -> new DisplayCaseItemStackRenderer<DisplayCaseTileEntity>((Supplier<DisplayCaseTileEntity>) DisplayCaseTileEntity::new)))));
    public static RegistryObject<BlockItem> DARK_OAK_CHEST= ITEMS.register("dark_oak_chest",() -> new BlockItem(PGBlocks.DARK_OAK_CHEST.get(),new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(() -> (Callable<ItemStackTileEntityRenderer>)(() -> new DisplayCaseItemStackRenderer<DisplayCaseTileEntity>((Supplier<DisplayCaseTileEntity>) DisplayCaseTileEntity::new)))));
    public static RegistryObject<BlockItem> SPRUCE_CHEST= ITEMS.register("spruce_chest",() -> new BlockItem(PGBlocks.SPRUCE_CHEST.get(),new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(() -> (Callable<ItemStackTileEntityRenderer>)(() -> new DisplayCaseItemStackRenderer<DisplayCaseTileEntity>((Supplier<DisplayCaseTileEntity>) DisplayCaseTileEntity::new)))));
    public static RegistryObject<BlockItem> JUNGLE_CHEST= ITEMS.register("jungle_chest",() -> new BlockItem(PGBlocks.JUNGLE_CHEST.get(),new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(() -> (Callable<ItemStackTileEntityRenderer>)(() -> new DisplayCaseItemStackRenderer<DisplayCaseTileEntity>((Supplier<DisplayCaseTileEntity>) DisplayCaseTileEntity::new)))));
    public static RegistryObject<BlockItem> WARPED_CHEST= ITEMS.register("warped_chest",() -> new BlockItem(PGBlocks.WARPED_CHEST.get(),new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(() -> (Callable<ItemStackTileEntityRenderer>)(() -> new DisplayCaseItemStackRenderer<DisplayCaseTileEntity>((Supplier<DisplayCaseTileEntity>) DisplayCaseTileEntity::new)))));

    public static final RegistryObject<PGSpawnEggItem> ALIEN_SPAWN_EGG = ITEMS.register("alien_spawn_egg",() -> new PGSpawnEggItem(PGEntitys.ALIEN,0x322E94,0x105A93,new Item.Properties().group(ItemGroup.MISC)));

    public static RegistryObject<BlockItem> LAUNCHPAD = ITEMS.register("launchpad",() -> new BlockItem(PGBlocks.LAUNCHPAD.get(),new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

    public static RegistryObject<Item> RocketItem = ITEMS.register("rocket_item", () -> new RocketItem(new Item.Properties().group(ItemGroup.MISC)));
    public static RegistryObject<Item> FUEL_REFINERY = ITEMS.register("fuel_refinery",() -> new BlockItem(PGBlocks.FUEL_REFINERY_BLOCK.get(),new Item.Properties().group(ItemGroup.DECORATIONS)));
    public static RegistryObject<Item> CABLE = ITEMS.register("cable",() -> new BlockItem(PGBlocks.CABLE.get(),new Item.Properties().group(ItemGroup.DECORATIONS)));
 //   public static RegistryObject<Item> FUEL_BUCKET = ITEMS.register("fuel_bucket",() -> new BucketItem(PGFluids.FUEL.get(), (new Item.Properties()).containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.MISC)));

    public static final RegistryObject<Item> FUEL_BUCKET = ITEMS.register("fuel_bucket", () -> new BucketItem(PGFluids.FUEL, (new Item.Properties()).containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.MISC)));
    public static final RegistryObject<Item> OIL_BUCKET = ITEMS.register("oil_bucket", () -> new BucketItem(PGFluids.OIL, (new Item.Properties()).containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.MISC)));

}
