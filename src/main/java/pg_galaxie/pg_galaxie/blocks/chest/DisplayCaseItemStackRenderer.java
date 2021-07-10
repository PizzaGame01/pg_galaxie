package pg_galaxie.pg_galaxie.blocks.chest;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import pg_galaxie.pg_galaxie.blocks.PGChestType;

import java.util.function.Supplier;

public class DisplayCaseItemStackRenderer <T extends TileEntity> extends ItemStackTileEntityRenderer
{
    //private final DisplayCaseTileEntity chestBasic = new DisplayCaseTileEntity();

    private final Supplier<pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseTileEntity> te;

    public DisplayCaseItemStackRenderer(final Supplier<pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseTileEntity> te) {
        this.te = te;
    }

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        //TileEntity tileentity;
        //tileentity = this.chestBasic;
        //TileEntityRendererDispatcher.instance.renderItem(tileentity, matrixStack, buffer, combinedLight, combinedOverlay);
        PGChestType c = ((DisplayCaseBlock)((BlockItem)stack.getItem()).getBlock()).type;
        DisplayCaseTileEntity t = ((pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseTileEntity)this.te.get());
        t.type = c;
        TileEntityRendererDispatcher.instance.renderItem((TileEntity)t, matrixStack, buffer, combinedLight, combinedOverlay);
    }

}
