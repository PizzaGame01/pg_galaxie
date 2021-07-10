package pg_galaxie.pg_galaxie.deferreds;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import pg_galaxie.pg_galaxie.Pg_galaxie;
import pg_galaxie.pg_galaxie.entitys.alien.AlienEntity;
import pg_galaxie.pg_galaxie.entitys.rocket.RocketEntity;

public class PGEntitys {
    public static final DeferredRegister<EntityType<?>> ENTITYS = DeferredRegister.create(ForgeRegistries.ENTITIES, Pg_galaxie.MODID);

    public static RegistryObject<EntityType<?>> ALIEN = ENTITYS.register("alien", () -> EntityType.Builder.create(AlienEntity::new, EntityClassification.CREATURE).size(0.75f,2f).build(new ResourceLocation(Pg_galaxie.MODID,"alien").toString()));
    public static RegistryObject<EntityType<?>> ROCKET = ENTITYS.register("rocket", () -> EntityType.Builder.create(RocketEntity::new, EntityClassification.CREATURE).size(0.75f,2f).build(new ResourceLocation(Pg_galaxie.MODID,"rocket").toString()));

}
