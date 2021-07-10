package pg_galaxie.pg_galaxie.entitys.rocket;// Made with Blockbench 3.8.3
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;

public class RocketModel<R extends CreatureEntity> extends EntityModel<RocketEntity> {
	private final ModelRenderer Rocket;
	private final ModelRenderer fin1;
	private final ModelRenderer Rocket_r1;
	private final ModelRenderer fin2;
	private final ModelRenderer Rocket_r2;
	private final ModelRenderer fin3;
	private final ModelRenderer Rocket_r3;
	private final ModelRenderer Rocket_r4;
	private final ModelRenderer fin4;
	private final ModelRenderer Rocket_r5;
	private final ModelRenderer Rocket_r6;
	private final ModelRenderer engine;
	private final ModelRenderer body;
	private final ModelRenderer head;

	public RocketModel() {
		textureWidth = 128;
		textureHeight = 128;

		Rocket = new ModelRenderer(this);
		Rocket.setRotationPoint(0.0F, 24.0F, 0.0F);
		Rocket.setTextureOffset(77, 54).addBox(-12.0F, -16.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		Rocket.setTextureOffset(77, 54).addBox(8.0F, -16.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		fin1 = new ModelRenderer(this);
		fin1.setRotationPoint(3.0F, -1.0F, -3.0F);
		Rocket.addChild(fin1);
		setRotationAngle(fin1, 0.0F, 0.7854F, 0.0F);
		fin1.setTextureOffset(82, 109).addBox(13.0F, -13.0F, -2.0F, 2.0F, 15.0F, 4.0F, 0.0F, false);

		Rocket_r1 = new ModelRenderer(this);
		Rocket_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		fin1.addChild(Rocket_r1);
		setRotationAngle(Rocket_r1, 0.0F, 0.0F, 0.4712F);
		Rocket_r1.setTextureOffset(110, 114).addBox(9.0F, -11.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
		Rocket_r1.setTextureOffset(104, 116).addBox(10.0F, -9.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		Rocket_r1.setTextureOffset(94, 120).addBox(0.0F, -13.0F, -1.0F, 9.0F, 6.0F, 2.0F, 0.0F, false);

		fin2 = new ModelRenderer(this);
		fin2.setRotationPoint(-3.0F, -1.0F, 3.0F);
		Rocket.addChild(fin2);
		setRotationAngle(fin2, 0.0F, 0.7854F, 0.0F);
		fin2.setTextureOffset(82, 109).addBox(-15.0F, -13.0F, -2.0F, 2.0F, 15.0F, 4.0F, 0.0F, false);

		Rocket_r2 = new ModelRenderer(this);
		Rocket_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
		fin2.addChild(Rocket_r2);
		setRotationAngle(Rocket_r2, 0.0F, 0.0F, -0.4712F);
		Rocket_r2.setTextureOffset(110, 114).addBox(-10.0F, -11.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
		Rocket_r2.setTextureOffset(104, 116).addBox(-11.0F, -9.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		Rocket_r2.setTextureOffset(94, 120).addBox(-9.0F, -13.0F, -1.0F, 9.0F, 6.0F, 2.0F, 0.0F, false);

		fin3 = new ModelRenderer(this);
		fin3.setRotationPoint(3.0F, -1.0F, 3.0F);
		Rocket.addChild(fin3);
		setRotationAngle(fin3, 0.0F, -0.7854F, 0.0F);


		Rocket_r3 = new ModelRenderer(this);
		Rocket_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
		fin3.addChild(Rocket_r3);
		setRotationAngle(Rocket_r3, 0.0F, 1.5708F, 0.0F);
		Rocket_r3.setTextureOffset(116, 111).addBox(-2.0F, -13.0F, 13.0F, 4.0F, 15.0F, 2.0F, 0.0F, false);

		Rocket_r4 = new ModelRenderer(this);
		Rocket_r4.setRotationPoint(0.0F, 0.0F, 0.0F);
		fin3.addChild(Rocket_r4);
		setRotationAngle(Rocket_r4, 0.0F, 0.0F, 0.4712F);
		Rocket_r4.setTextureOffset(104, 116).addBox(10.0F, -9.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		Rocket_r4.setTextureOffset(94, 120).addBox(0.0F, -13.0F, -1.0F, 9.0F, 6.0F, 2.0F, 0.0F, false);
		Rocket_r4.setTextureOffset(110, 114).addBox(9.0F, -11.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

		fin4 = new ModelRenderer(this);
		fin4.setRotationPoint(-3.0F, -1.0F, -3.0F);
		Rocket.addChild(fin4);
		setRotationAngle(fin4, 0.0F, -0.7854F, 0.0F);


		Rocket_r5 = new ModelRenderer(this);
		Rocket_r5.setRotationPoint(0.0F, 0.0F, 0.0F);
		fin4.addChild(Rocket_r5);
		setRotationAngle(Rocket_r5, 0.0F, 0.0F, -0.4712F);
		Rocket_r5.setTextureOffset(110, 114).addBox(-10.0F, -11.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
		Rocket_r5.setTextureOffset(104, 116).addBox(-11.0F, -9.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		Rocket_r5.setTextureOffset(94, 120).addBox(-9.0F, -13.0F, -1.0F, 9.0F, 6.0F, 2.0F, 0.0F, false);

		Rocket_r6 = new ModelRenderer(this);
		Rocket_r6.setRotationPoint(0.0F, 0.0F, 0.0F);
		fin4.addChild(Rocket_r6);
		setRotationAngle(Rocket_r6, 0.0F, 1.5708F, 0.0F);
		Rocket_r6.setTextureOffset(116, 111).addBox(-2.0F, -13.0F, -15.0F, 4.0F, 15.0F, 2.0F, 0.0F, false);

		engine = new ModelRenderer(this);
		engine.setRotationPoint(0.0F, -2.0F, 0.0F);
		Rocket.addChild(engine);
		engine.setTextureOffset(106, 109).addBox(-5.0F, -1.0F, 4.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		engine.setTextureOffset(109, 100).addBox(4.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);
		engine.setTextureOffset(106, 109).addBox(-5.0F, -1.0F, -5.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		engine.setTextureOffset(109, 100).addBox(-5.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);
		engine.setTextureOffset(108, 109).addBox(-4.0F, -2.0F, 3.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
		engine.setTextureOffset(111, 102).addBox(3.0F, -2.0F, -3.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
		engine.setTextureOffset(108, 109).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
		engine.setTextureOffset(111, 102).addBox(-4.0F, -2.0F, -3.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
		engine.setTextureOffset(111, 102).addBox(-3.0F, -3.0F, -3.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
		engine.setTextureOffset(104, 93).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);
		engine.setTextureOffset(112, 109).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
		engine.setTextureOffset(111, 102).addBox(2.0F, -3.0F, -3.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
		engine.setTextureOffset(112, 109).addBox(-2.0F, -3.0F, 2.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		Rocket.addChild(body);
		body.setTextureOffset(64, 71).addBox(-8.0F, -7.0F, -8.0F, 16.0F, 1.0F, 16.0F, 0.0F, false);
		body.setTextureOffset(94, 36).addBox(-8.0F, -41.0F, 7.0F, 16.0F, 34.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(94, 52).addBox(-8.0F, -25.0F, -8.0F, 16.0F, 18.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(94, 46).addBox(-8.0F, -33.0F, -8.0F, 4.0F, 8.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(111, 47).addBox(4.0F, -33.0F, -8.0F, 4.0F, 8.0F, 1.0F, 0.0F, true);
		body.setTextureOffset(94, 36).addBox(-8.0F, -41.0F, -8.0F, 16.0F, 8.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(56, 0).addBox(-8.0F, -41.0F, -7.0F, 1.0F, 34.0F, 14.0F, 0.0F, false);
		body.setTextureOffset(66, 73).addBox(-7.0F, -41.0F, -7.0F, 14.0F, 1.0F, 14.0F, 0.0F, false);
		body.setTextureOffset(56, 0).addBox(7.0F, -41.0F, -7.0F, 1.0F, 34.0F, 14.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		Rocket.addChild(head);
		head.setTextureOffset(0, 108).addBox(-9.0F, -43.0F, -9.0F, 18.0F, 2.0F, 18.0F, 0.0F, false);
		head.setTextureOffset(4, 90).addBox(-8.0F, -45.0F, -8.0F, 16.0F, 2.0F, 16.0F, 0.0F, false);
		head.setTextureOffset(9, 74).addBox(-7.0F, -47.0F, -7.0F, 14.0F, 2.0F, 14.0F, 0.0F, false);
		head.setTextureOffset(13, 60).addBox(-6.0F, -49.0F, -6.0F, 12.0F, 2.0F, 12.0F, 0.0F, false);
		head.setTextureOffset(18, 48).addBox(-5.0F, -51.0F, -5.0F, 10.0F, 2.0F, 10.0F, 0.0F, false);
		head.setTextureOffset(26, 30).addBox(-3.0F, -55.0F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, false);
		head.setTextureOffset(22, 38).addBox(-4.0F, -53.0F, -4.0F, 8.0F, 2.0F, 8.0F, 0.0F, false);
		head.setTextureOffset(34, 14).addBox(-1.0F, -69.0F, -1.0F, 2.0F, 14.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		Rocket.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(RocketEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.Rocket.rotateAngleZ = entityIn.ay;
		this.Rocket.rotateAngleX = entityIn.ap;
	}

}