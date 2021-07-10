package pg_galaxie.pg_galaxie.entitys.rocket;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import pg_galaxie.pg_galaxie.blocks.LaunchpadBlock;
import pg_galaxie.pg_galaxie.deferreds.PGBlocks;
import pg_galaxie.pg_galaxie.deferreds.PGItems;

import javax.annotation.Nullable;

public class RocketEntity extends CreatureEntity implements IRideable{
    public RocketEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return super.createSpawnPacket();
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH,10.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED,0D);
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        return false;
    }

    @Override
    public void applyKnockback(float strength, double ratioX, double ratioZ) {

    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        this.remove();
        return false;
    }

    public float ar,ay,ap;

    @Override
    public void tick() {
        super.tick();
        ar += 1;
        if (ar == 1) {
            ay += 0.006;
            ap += 0.006;
        }
        if (ar == 2) {
            ar = 0;
            ay = 0;
            ap = 0;
        }

        BlockPos old = this.getPosition();
        BlockPos bp = new BlockPos(old.getX(),old.getY(),old.getZ());


        if(this.isOnGround() && !(this.world.getBlockState(bp).getBlock() instanceof LaunchpadBlock && this.world.getBlockState(bp).get(LaunchpadBlock.STATE) == 1)){
            ItemEntity item = new ItemEntity(this.world,bp.getX()+0.5f,bp.getY()+0.5f,bp.getZ()+0.5f,new ItemStack(PGItems.RocketItem.get(),1));
            this.world.addEntity(item);
            this.remove();
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();

        BlockPos old = this.getPosition();
        BlockPos bp = new BlockPos(old.getX(),old.getY(),old.getZ());
        // TODO: 05.07.2021 ask if rocket fly


        if (world instanceof ServerWorld) {
            for (ServerPlayerEntity p : ((ServerWorld) world).getPlayers()) {
                ((ServerWorld) world).spawnParticle(p, ParticleTypes.FLAME, true, this.getPosX(), this.getPosY(), this.getPosZ(), 100, 0.1, 0.1, 0.1, (double) 0.001);
                ((ServerWorld) world).spawnParticle(p, ParticleTypes.SMOKE, true, this.getPosX(), this.getPosY(), this.getPosZ(), 50, 0.1, 0.1, 0.1, (double) 0.04);
            }
        }
    }

    @Override
    protected void collideWithEntity(Entity p_82167_1_) {
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
    }

    @Override
    protected void pushOutOfBlocks(double x, double y, double z) {

    }

    @Override
    public boolean boost() {
        return false;
    }

    @Override
    public void travelTowards(Vector3d travelVec) {

    }

    @Override
    public float getMountedSpeed() {
        return 0;
    }

    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        if (!this.world.isRemote) {
            player.startRiding(this);
        }
        return super.applyPlayerInteraction(player, vec, hand);
    }

    @Override
    public void updatePassenger(Entity passenger) {
        this.positionRider(passenger, this::pos);
    }

    private void positionRider(Entity entity, Entity.IMoveCallback callback) {
        if (this.isPassenger(entity)) {
            double d0 = this.getPosY() + this.getMountedYOffset() + entity.getYOffset();
            callback.accept(entity, this.getPosX(), d0, this.getPosZ());
        }
    }

    private void pos(Entity entity, double x, double y, double z) {
        entity.setRawPosition(x, y - 0.85f, z);
        entity.setBoundingBox(this.getSize(this.getPose()).func_242285_a(x, y - 0.85f, z));
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(PGItems.RocketItem.get(),1);
    }
}
