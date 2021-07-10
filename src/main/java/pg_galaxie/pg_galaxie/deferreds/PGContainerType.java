package pg_galaxie.pg_galaxie.deferreds;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import pg_galaxie.pg_galaxie.Pg_galaxie;
import pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseContainer;
import pg_galaxie.pg_galaxie.blocks.fuel_refinery.FuelRefineryContainer;

public class PGContainerType {
    public static DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Pg_galaxie.MODID);
    public static final RegistryObject<ContainerType<?>> DISPLAY_CASE_CONTAINER = CONTAINER_TYPES.register("display_case",() -> IForgeContainerType.create((IContainerFactory) DisplayCaseContainer::new));
    public static final RegistryObject<ContainerType<?>> FUEL_REFINERY_CONTAINER = CONTAINER_TYPES.register("fuel_refinery_container",() -> IForgeContainerType.create((IContainerFactory) FuelRefineryContainer::new));

}
