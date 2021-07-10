package pg_galaxie.pg_galaxie.deferreds;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import pg_galaxie.pg_galaxie.Pg_galaxie;
import pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseTileEntity;
import pg_galaxie.pg_galaxie.blocks.fuel_refinery.FuelRefineryTileEntity;

public class PGTileEntitys {
    public static DeferredRegister<TileEntityType<?>> TILE_ENTITYS = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Pg_galaxie.MODID);
    public static final RegistryObject<TileEntityType<?>> DISPLAYCASETILE = TILE_ENTITYS.register("dispalycasetile", () -> TileEntityType.Builder.create(DisplayCaseTileEntity::new,PGBlocks.CHEST.get(),PGBlocks.OAK_CHEST.get(),PGBlocks.WARPED_CHEST.get(),PGBlocks.JUNGLE_CHEST.get(),PGBlocks.DARK_OAK_CHEST.get(),PGBlocks.BIRCH_CHEST.get(), PGBlocks.CRIMSON_CHEST.get(),PGBlocks.SPRUCE_CHEST.get(),PGBlocks.ACACIA_CHEST.get()).build(null));//TileEntityType.Builder.create(TrappedChestTileEntity::new, Blocks.TRAPPED_CHEST));
    public static final RegistryObject<TileEntityType<?>> FUELREFINERYTILE = TILE_ENTITYS.register("fuelrefinerytile", () -> TileEntityType.Builder.create(FuelRefineryTileEntity::new,PGBlocks.FUEL_REFINERY_BLOCK.get()).build(null));
}
