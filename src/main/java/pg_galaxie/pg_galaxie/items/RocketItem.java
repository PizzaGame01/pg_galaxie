package pg_galaxie.pg_galaxie.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.model.b3d.B3DModel;
import pg_galaxie.pg_galaxie.blocks.LaunchpadBlock;
import pg_galaxie.pg_galaxie.deferreds.PGEntitys;
import pg_galaxie.pg_galaxie.entitys.rocket.RocketEntity;

import java.util.List;

public class RocketItem extends Item {
    public RocketItem(Properties properties) {
        super(properties);
    }


    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockState state = context.getWorld().getBlockState(context.getPos());

        int x = context.getPos().getX(),y = context.getPos().getY(),z = context.getPos().getZ();

        AxisAlignedBB scanAbove = new AxisAlignedBB(x+1, y+1, z+1, x-1, y-1, z-1);
        List<Entity> entities = context.getPlayer().getEntityWorld().getEntitiesWithinAABB(RocketEntity.class, scanAbove);

        if(entities.size() < 1) {
            if (state.getBlock() instanceof LaunchpadBlock && state.get(LaunchpadBlock.STATE) == 1) {
                //if (context.getWorld() instanceof ServerWorld && !context.getWorld().isRemote) {
                    RocketEntity r = new RocketEntity((EntityType<? extends CreatureEntity>) PGEntitys.ROCKET.get(), context.getWorld());

                    //((ServerWorld) context.getWorld()).func_242417_l(r);
                    float f = (float) MathHelper.floor((MathHelper.wrapDegrees(context.getPlacementYaw() - 180.0F) + 22.5F) / 90.0F) * 90.0F;

                    context.getWorld().addEntity(r);
                    r.setHeadRotation(f,0);
                    r.setPositionAndRotationDirect(x,y,z,f,0f,0,false);
                    r.setLocationAndAngles(x, y, z, f, 0.0F);

                    r.setLocationAndAngles((x + 0.5), (y + 1), (z + 0.5), (float) 0, (float) 0);
                    r.setRenderYawOffset((float) f);
                    r.rotationYaw = (float) (f);
                    r.setRenderYawOffset(r.rotationYaw);
                    r.prevRotationYaw = r.rotationYaw;
                    r.prevRenderYawOffset = r.rotationYaw;
                    r.rotationYawHead = r.rotationYaw;
                    r.prevRotationYawHead = r.rotationYaw;
                //}
                /*context.getWorld().addEntity(r);
                r.rotationYaw = yaw;
                r.setHeadRotation(yaw,0);
                r.rotationYawHead = yaw;
                r.renderYawOffset = yaw;
                r.prevRotationYaw = yaw;
                r.prevRenderYawOffset = yaw;*/
                //((MobEntity) entityToSpawn).rotationYawHead = entityToSpawn.rotationYaw;
                //((MobEntity) entityToSpawn).prevRotationYawHead = entityToSpawn.rotationYaw;
                //RocketEntity r = new RocketEntity()//create entity
            }
        }
        return super.onItemUse(context);
    }
}
