package pg_galaxie.pg_galaxie.entitys.alien;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.village.GossipManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import pg_galaxie.pg_galaxie.deferreds.PGEntitys;
import pg_galaxie.pg_galaxie.entitys.alien.goals.FollowGoal;
import pg_galaxie.pg_galaxie.entitys.alien.goals.TradeGoal;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class AlienEntity extends AgeableEntity implements IMerchant, INPC {

    @Nullable
    private PlayerEntity customer;
    private Set<UUID> tradedCustomers = new HashSet<>();
    @Nullable
    private MerchantOffers offers;
    private final GossipManager gossip = new GossipManager();

    public int getAlienType() {
        return this.dataManager.get(ALIEN_TYPE);
    }
    public void setAlienType(int type) {
        this.dataManager.set(ALIEN_TYPE, type);
    }

    private static final DataParameter<Integer> ALIEN_TYPE = EntityDataManager.createKey(AlienEntity.class, DataSerializers.VARINT);

    private static final Ingredient TEMPTATION_ITEMS = Ingredient.fromItems(Items.EMERALD);

    public AlienEntity(EntityType<? extends AlienEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH,10.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED,0.25D);
    }

    @Nullable
    @Override
    public AlienEntity createChild(ServerWorld world, AgeableEntity mate) {
        double d0 = this.rand.nextDouble();
        AlienEntity villagerentity = new AlienEntity((EntityType<? extends AlienEntity>) PGEntitys.ALIEN.get(), world);
        villagerentity.onInitialSpawn(world, world.getDifficultyForLocation(villagerentity.getPosition()), SpawnReason.BREEDING, (ILivingEntityData)null, (CompoundNBT)null);
        return villagerentity;
    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setAlienType(this.rand.nextInt(AlienJobs.values().length));
        this.getOffers();
        return spawnDataIn;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ALIEN_TYPE, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        //this.eatGrassGoal = new EatGrassGoal(this);
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.25D));
        //this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, TEMPTATION_ITEMS, false));
        //this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(9, new TradeGoal(this));
        this.goalSelector.addGoal(1,new FollowGoal(this,1.1D,false));
    }

    protected void resetCustomer() {
        this.setCustomer((PlayerEntity)null);
    }
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        this.resetCustomer();
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.ENTITY_VILLAGER_AMBIENT; }

    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_VILLAGER_DEATH; }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity player) {
        this.customer = player;
    }

    @Nullable
    @Override
    public PlayerEntity getCustomer() {
        return this.customer;
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();

            int max = AlienTrades.TRADES.get(AlienJobs.values()[this.getAlienType()]).size();

            int task = new Random().nextInt(max)+1;

            for (int i = 0; i < task; i++){
                System.out.println("add "+i);
                this.populateTradeData(i+1);
            }
        }

        return this.offers;
    }



    @Override
    public void setClientSideOffers(@Nullable MerchantOffers offers) {

    }

    @Override
    public void onTrade(MerchantOffer offer) {
        offer.increaseUses();
        if(this.customer != null)
        {
            this.tradedCustomers.add(this.customer.getUniqueID());
        }
    }

    @Override
    public void verifySellingItem(ItemStack stack) {

    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public int getXp() {
        return 0;
    }

    @Override
    public void setXP(int xpIn) {

    }

    @Override
    public boolean hasXPBar() {
        return false;
    }

    public boolean isPreviousCustomer(PlayerEntity player)
    {
        return this.tradedCustomers.contains(player.getUniqueID());
    }

    @Override
    public SoundEvent getYesSound() {
        return null;
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setAlienType(compound.getInt("JobId"));
        if (compound.contains("Offers", 10)) {
            this.offers = new MerchantOffers(compound.getCompound("Offers"));
        }
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("JobId", this.getAlienType());
        MerchantOffers merchantoffers = this.getOffers();
        compound.put("Offers", merchantoffers.write());
    }

    protected void addTrades(MerchantOffers givenMerchantOffers, VillagerTrades.ITrade[] newTrades, int maxNumbers) {
        Set<Integer> set = Sets.newHashSet();
        if (newTrades.length > maxNumbers) {
            while(set.size() < maxNumbers) {
                set.add(this.rand.nextInt(newTrades.length));
            }
        } else {
            for(int i = 0; i < newTrades.length; ++i) {
                set.add(i);
            }
        }

        for(Integer integer : set) {
            VillagerTrades.ITrade villagertrades$itrade = newTrades[integer];
            MerchantOffer merchantoffer = villagertrades$itrade.getOffer(this, this.rand);
            if (merchantoffer != null) {
                givenMerchantOffers.add(merchantoffer);
            }
        }

    }

    protected void populateTradeData(int i)
    {
        Int2ObjectMap<VillagerTrades.ITrade[]> int2objectmap = AlienTrades.TRADES.get(AlienJobs.values()[this.getAlienType()]);
        if (int2objectmap != null && !int2objectmap.isEmpty()) {
            VillagerTrades.ITrade[] avillagertrades$itrade = int2objectmap.get(i);
            if (avillagertrades$itrade != null) {
                System.out.println("game add it");
                MerchantOffers merchantoffers = this.getOffers();
                this.addTrades(merchantoffers, avillagertrades$itrade, rand.nextInt(2)+1);
            }
        }
    }

    public boolean hasCustomer() {
        return this.customer != null;
    }

    public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        if (itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.hasCustomer() && !this.isSleeping() && !playerIn.isSecondaryUseActive()) {
            if (this.isChild()) {
                return ActionResultType.func_233537_a_(this.world.isRemote);
            } else {
                if (hand == Hand.MAIN_HAND) {
                    if (!this.world.isRemote) {

                    }

                    playerIn.addStat(Stats.TALKED_TO_VILLAGER);
                }
                    if (!this.world.isRemote) {
                        this.displayMerchantGui(playerIn);
                    }

                    return ActionResultType.func_233537_a_(this.world.isRemote);
                //}
                //}
            }
        } else {
            return super.getEntityInteractionResult(playerIn, hand);
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    private void displayMerchantGui(PlayerEntity player) {
        this.recalculateSpecialPricesFor(player);
        this.setCustomer(player);

        AlienJobs j = AlienJobs.values()[getAlienType()];
        this.openMerchantContainer(player, new TranslationTextComponent(this.getDisplayName().getString() + " - " +  j.getJobDisplayname().getString()),1);
    }

    private void recalculateSpecialPricesFor(PlayerEntity playerIn) {
        int i = this.getPlayerReputation(playerIn);
        if (i != 0) {
            for(MerchantOffer merchantoffer : this.getOffers()) {
                merchantoffer.increaseSpecialPrice(-MathHelper.floor((float)i * merchantoffer.getPriceMultiplier()));
            }
        }

        if (playerIn.isPotionActive(Effects.HERO_OF_THE_VILLAGE)) {
            EffectInstance effectinstance = playerIn.getActivePotionEffect(Effects.HERO_OF_THE_VILLAGE);
            int k = effectinstance.getAmplifier();

            for(MerchantOffer merchantoffer1 : this.getOffers()) {
                double d0 = 0.3D + 0.0625D * (double)k;
                int j = (int)Math.floor(d0 * (double)merchantoffer1.getBuyingStackFirst().getCount());
                merchantoffer1.increaseSpecialPrice(-Math.max(j, 1));
            }
        }

    }

    public int getPlayerReputation(PlayerEntity player) {
        return this.gossip.getReputation(player.getUniqueID(), (gossipType) -> {
            return true;
        });
    }

    public ResourceLocation getTexture() {
        return AlienJobs.values()[this.getAlienType()].TEXTURE;
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        System.out.println("hit");
        for(MerchantOffer merchantoffer : this.getOffers()) {
            merchantoffer.resetUses();
        }
        //}

        int a;

        System.out.println(amount);
        if(amount <= 0){
            a = 1;
        }else {
            a = (int)Math.floor(amount);
        }

        //for(int j = 0; j < i; ++j) {
        for(MerchantOffer merchantoffer : this.getOffers()) {
            int max = 3;
            int min = 1;
            merchantoffer.increaseSpecialPrice(a*(new Random().nextInt((max+1)-min)+min));
        }
        return super.attackEntityFrom(source,amount);
    }
}
