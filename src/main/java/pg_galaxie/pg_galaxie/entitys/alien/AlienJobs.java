package pg_galaxie.pg_galaxie.entitys.alien;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import pg_galaxie.pg_galaxie.Pg_galaxie;

public enum AlienJobs {

    JOB1(
            "job.pg_galaxie.job1",
            new ResourceLocation(Pg_galaxie.MODID,"textures/entity/alien/alien_2.png"),
            0
    ),
    JOB2(
            "job.pg_galaxie.job2",
            new ResourceLocation(Pg_galaxie.MODID,"textures/entity/alien/alien.png"),
            1
    );

    public ResourceLocation TEXTURE;
    private String TTC;
    public int id;

    private AlienJobs(String TTC,ResourceLocation texture,int i){
        this.TEXTURE = texture;
        this.TTC = TTC;
        this.id = i;
    }

    public TranslationTextComponent getJobDisplayname() {
        return new TranslationTextComponent(TTC);
    }
}