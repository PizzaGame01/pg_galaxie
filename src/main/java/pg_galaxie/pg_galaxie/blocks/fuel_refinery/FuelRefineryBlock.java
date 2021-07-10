package pg_galaxie.pg_galaxie.blocks.fuel_refinery;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Consumer;

public class FuelRefineryBlock extends Block {
    public FuelRefineryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof FuelRefineryTileEntity) {
                Consumer<PacketBuffer> packetBufferConsumer = pb -> pb.writeBlockPos(pos).writeInt(((FuelRefineryTileEntity) te).buckets);

                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te,packetBufferConsumer);
            }
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FuelRefineryTileEntity();
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), this, 1);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);


        if(!worldIn.isRemote()) {

            FuelRefineryTileEntity fr = (FuelRefineryTileEntity)worldIn.getTileEntity(pos);
            ItemStack y = fr.inventory.get(0);

            if (y.getItem() == Items.WATER_BUCKET) {

                    if(fr.buckets < fr.maxbuckets) {

                        fr.buckets++;
                        fr.inventory.set(0, ItemStack.EMPTY);
                        fr.getUpdatePacket();

                }
                fr.inventory.set(0, new ItemStack(Items.BUCKET, 1));

            }

            worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), this, 1);
        }
    }
}
