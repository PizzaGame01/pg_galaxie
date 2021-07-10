package pg_galaxie.pg_galaxie.blocks.chest;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import pg_galaxie.pg_galaxie.Pg_galaxie;
import pg_galaxie.pg_galaxie.blocks.PGChestType;

@OnlyIn(Dist.CLIENT)
public class DisplayCaseScreen extends ContainerScreen<DisplayCaseContainer> {

    private static final ResourceLocation DISPLAY_CASE_GUI = new ResourceLocation(Pg_galaxie.MODID,"textures/gui/display_case.png");
    private static final ResourceLocation SLOT = new ResourceLocation(Pg_galaxie.MODID,"textures/gui/slot.png");

    public PGChestType type;

    public DisplayCaseScreen(DisplayCaseContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        this.type = screenContainer.type;

        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 175;
        this.xSize = 201;
    }

    public DisplayCaseScreen(Container container, PlayerInventory playerInventory, ITextComponent iTextComponent) {
        this((DisplayCaseContainer) container,playerInventory,iTextComponent);
        //super(container, playerInventory, iTextComponent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        //this.renderHoveredTooltip(matrixStack,mouseX,mouseY);

        /*List<ITextComponent> x = new ArrayList<ITextComponent>();
        x.add(ITextComponent.getTextComponentOrEmpty("test"));

        this.func_243308_b(matrixStack, x,mouseX, mouseY);*/
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.drawString(matrixStack, String.valueOf(this.playerInventory.getDisplayName().getString()),this.playerInventoryTitleX,this.playerInventoryTitleY,4210752);
        this.font.drawString(matrixStack, String.valueOf(title.getString()),this.playerInventoryTitleX,this.playerInventoryTitleY-65,4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mx, int my) {
        RenderSystem.color4f(1f,1f,1f,1f);

        matrixStack.push();
        //matrixStack.translate(1,10,0);
        //matrixStack.translate(this.guiLeft/5,this.guiTop/5,0);
        //matrixStack.rotate(new Quaternion(0,0f,1,false));
        this.minecraft.textureManager.bindTexture(DISPLAY_CASE_GUI);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(matrixStack,x,y,0,0,this.xSize,this.ySize);

        this.minecraft.textureManager.bindTexture(SLOT);
        this.type.slots.forEach(s -> {
            this.blit(matrixStack, (s[0]*18)+x-12+this.type.extrax, (s[1]*18)+y-12+this.type.extray,0,0,18,18,18,18);
        });
        matrixStack.pop();
    }
}