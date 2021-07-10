package pg_galaxie.pg_galaxie.entitys.alien;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.util.ResourceLocation;

public class AlienRenderer extends MobRenderer<AlienEntity, AlienModel<AlienEntity>> {
    public AlienRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new AlienModel<>(), 0.5f);
        this.addLayer(new CrossedArmsItemLayer<>(this));
    }

    //protected static final ResourceLocation TEXTURE = new ResourceLocation(Space_traveler.ID,"textures/entity/alien.png");

    @Override
    public ResourceLocation getEntityTexture(AlienEntity entity) {
        return entity.getTexture();
    }
}
