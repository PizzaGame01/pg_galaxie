package pg_galaxie.pg_galaxie.deferreds;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.WallOrFloorItem;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import pg_galaxie.pg_galaxie.Pg_galaxie;
import pg_galaxie.pg_galaxie.blocks.LaunchpadBlock;
import pg_galaxie.pg_galaxie.blocks.PGChestType;
import pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseBlock;
import pg_galaxie.pg_galaxie.blocks.fuel_refinery.FuelRefineryBlock;
import pg_galaxie.pg_galaxie.blocks.machine.EnergyCable;

public class PGBlocks {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Pg_galaxie.MODID);
    public static RegistryObject<Block> CHEST = BLOCKS.register("chest",() -> new DisplayCaseBlock(PGChestType.TEST, AbstractBlock.Properties.create(Material.WOOD)));

    public static RegistryObject<Block> OAK_CHEST = BLOCKS.register("oak_chest",() -> new DisplayCaseBlock(PGChestType.OAK, AbstractBlock.Properties.create(Material.WOOD)));
    public static RegistryObject<Block> BIRCH_CHEST = BLOCKS.register("birch_chest",() -> new DisplayCaseBlock(PGChestType.BIRCH, AbstractBlock.Properties.create(Material.WOOD)));
    public static RegistryObject<Block> ACACIA_CHEST = BLOCKS.register("acacia_chest",() -> new DisplayCaseBlock(PGChestType.ACACIA, AbstractBlock.Properties.create(Material.WOOD)));
    public static RegistryObject<Block> CRIMSON_CHEST = BLOCKS.register("crimson_chest",() -> new DisplayCaseBlock(PGChestType.CRIMSON, AbstractBlock.Properties.create(Material.WOOD)));
    public static RegistryObject<Block> DARK_OAK_CHEST = BLOCKS.register("dark_oak_chest",() -> new DisplayCaseBlock(PGChestType.DARK_OAK, AbstractBlock.Properties.create(Material.WOOD)));
    public static RegistryObject<Block> SPRUCE_CHEST = BLOCKS.register("spruce_chest",() -> new DisplayCaseBlock(PGChestType.SPRUCE, AbstractBlock.Properties.create(Material.WOOD)));
    public static RegistryObject<Block> JUNGLE_CHEST = BLOCKS.register("jungle_chest",() -> new DisplayCaseBlock(PGChestType.JUNGLE, AbstractBlock.Properties.create(Material.WOOD)));
    public static RegistryObject<Block> WARPED_CHEST = BLOCKS.register("warped_chest",() -> new DisplayCaseBlock(PGChestType.WARPED, AbstractBlock.Properties.create(Material.WOOD)));

    public static RegistryObject<LaunchpadBlock> LAUNCHPAD = BLOCKS.register("launchpad",() -> new LaunchpadBlock(AbstractBlock.Properties.create(Material.ROCK)));
    public static RegistryObject<Block> FUEL_REFINERY_BLOCK = BLOCKS.register("fuel_refinery",() -> new FuelRefineryBlock(AbstractBlock.Properties.create(Material.WOOD)));
    public static RegistryObject<EnergyCable> CABLE = BLOCKS.register("cable",() -> new EnergyCable(AbstractBlock.Properties.create(Material.WOOD)));
}
