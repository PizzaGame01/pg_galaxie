package pg_galaxie.pg_galaxie.blocks.fuel_refinery;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import pg_galaxie.pg_galaxie.Pg_galaxie;
import pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseContainer;

public class FuelRefineryScreen extends ContainerScreen<FuelRefineryContainer> {

    private static final ResourceLocation DISPLAY_CASE_GUI = new ResourceLocation(Pg_galaxie.MODID,"textures/gui/display_case.png");
    private static final ResourceLocation[] fluids = new ResourceLocation[]{new ResourceLocation(Pg_galaxie.MODID,"textures/gui/3mb_empty.png"),new ResourceLocation(Pg_galaxie.MODID,"textures/gui/3mb_water.png"),new ResourceLocation(Pg_galaxie.MODID,"textures/gui/3mb_lava.png")};


    public int rs,oldBuckets,oldRs;

    public FuelRefineryScreen(FuelRefineryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        //this.rss = 53;
        this.oldBuckets = this.container.te.buckets;
        this.rs = getRsForBucekts(this.oldBuckets);//53;
        // TODO: 09.07.2021 save rs in the tile entity
    }

    public FuelRefineryScreen(Container container, PlayerInventory playerInventory, ITextComponent iTextComponent) {
        this((FuelRefineryContainer) container,playerInventory,iTextComponent);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mx, int my) {
        this.minecraft.textureManager.bindTexture(DISPLAY_CASE_GUI);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(matrixStack,x,y,0,0,this.xSize,this.ySize);

        this.minecraft.textureManager.bindTexture(fluids[0]);
        this.blit(matrixStack,x,y,0,0,19,53,19,53);

        matrixStack.push();

        this.minecraft.textureManager.bindTexture(fluids[1]);
        //this.blit(matrixStack,0,0,0,-2,0,0,19,53,19,53);


        int interpolatedRs = (int) (this.oldRs + (this.rs - this.oldRs) * partialTicks);
        this.blit(matrixStack, x, y+53-(interpolatedRs+1), 0, -interpolatedRs-1, 19, interpolatedRs, 19, 53);//+((53/maxbuckets)*(maxbuckets-buckets))
        matrixStack.pop();
    }

    @Override
    public void tick() {
        super.tick();

        this.oldRs = this.rs;

        /*if (s > this.rs) {
            this.rs++;
        } else if (s < this.rs) {
            this.rs--;
        }*/
        //interpolatedRs = this.rs + partialTicks
        if (this.oldBuckets != this.container.te.buckets) {
            int targetRs = this.getRsForBucekts(this.container.te.buckets);
            if (targetRs != this.rs) {
                this.rs += Integer.signum(targetRs - this.rs);
            } else {
                this.oldBuckets = this.container.te.buckets;
            }
        }

        System.out.println(this.container.te.buckets);
    }

    private int getRsForBucekts(int buckets) {
        return (53 / this.container.te.maxbuckets) * buckets;
    }
}
