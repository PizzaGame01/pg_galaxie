package pg_galaxie.pg_galaxie.entitys.alien;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class AlienModel <T extends AlienEntity> extends EntityModel<T> {
    private final ModelRenderer head;
    private final ModelRenderer nose;
    private final ModelRenderer body;
    private final ModelRenderer arms;
    private final ModelRenderer leg0;
    private final ModelRenderer leg1;

    public AlienModel() {
        textureWidth = 90;
        textureHeight = 64;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);
        head.setTextureOffset(34, 0).addBox(-5.0F, -19.0F, -5.0F, 10.0F, 9.0F, 10.0F, 0.0F, false);

        nose = new ModelRenderer(this);
        nose.setRotationPoint(0.0F, -2.0F, 0.0F);
        head.addChild(nose);
        nose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 24.0F, 0.0F);
        body.setTextureOffset(16, 20).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);
        body.setTextureOffset(0, 38).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.5F, false);

        arms = new ModelRenderer(this);
        arms.setRotationPoint(0.0F, 2.0F, 0.0F);
        setRotationAngle(arms, -0.7854F, 0.0F, 0.0F);
        arms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
        arms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.0F, false);
        arms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

        leg0 = new ModelRenderer(this);
        leg0.setRotationPoint(-2.0F, 12.0F, 0.0F);
        leg0.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        leg1 = new ModelRenderer(this);
        leg1.setRotationPoint(2.0F, 12.0F, 0.0F);
        leg1.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {


        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);

        //this.head.rotateAngleZ = 0.3F * MathHelper.sin(0.45F * ageInTicks);
        this.head.rotateAngleX = 0F;

        this.head.rotateAngleZ = 0.0F;


        this.arms.rotationPointY = 3.0F;
        this.arms.rotationPointZ = -1.0F;
        this.arms.rotateAngleX = -0.75F;
        this.leg0.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.leg0.rotateAngleY = 0.0F;
        this.leg1.rotateAngleY = 0.0F;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){

        if(isChild){
            matrixStack.scale(0.5f,0.5f,0.5f);
            matrixStack.translate(0.0D, 1.5D, 0.0D);
        }else {
            matrixStack.scale(1,1,1);
            matrixStack.translate(0.0D, 0.0D, 0.0D);
        }
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        arms.render(matrixStack, buffer, packedLight, packedOverlay);
        leg0.render(matrixStack, buffer, packedLight, packedOverlay);
        leg1.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
