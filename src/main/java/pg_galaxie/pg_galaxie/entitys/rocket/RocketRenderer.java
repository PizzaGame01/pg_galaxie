package pg_galaxie.pg_galaxie.entitys.rocket;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.*;
import net.minecraft.util.ResourceLocation;
import pg_galaxie.pg_galaxie.Pg_galaxie;
import pg_galaxie.pg_galaxie.entitys.alien.AlienEntity;
import pg_galaxie.pg_galaxie.entitys.alien.AlienModel;

public class RocketRenderer extends MobRenderer<RocketEntity, RocketModel<RocketEntity>> {

    public RocketRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new RocketModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(RocketEntity entity) {
        return new ResourceLocation(Pg_galaxie.MODID,"textures/entity/rocket.png");
    }
}
