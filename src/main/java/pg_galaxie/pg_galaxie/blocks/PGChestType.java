package pg_galaxie.pg_galaxie.blocks;

import net.minecraft.util.ResourceLocation;
import pg_galaxie.pg_galaxie.Pg_galaxie;
import pg_galaxie.pg_galaxie.blocks.chest.DisplayCaseBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum PGChestType {


    OAK("oak", PGChestType::DEFAULT_CHEST_COTAINER,19,18*2-8),
    TEST("test", PGChestType::DEFAULT_CHEST_COTAINER,19,18*2-8),
    BIRCH("birch", PGChestType::DEFAULT_CHEST_COTAINER,19,18*2-8),
    ACACIA("acacia", PGChestType::DEFAULT_CHEST_COTAINER,19,18*2-8),
    CRIMSON("crimson", PGChestType::DEFAULT_CHEST_COTAINER,19,18*2-8),
    DARK_OAK("dark_oak", PGChestType::DEFAULT_CHEST_COTAINER,19,18*2-8),
    JUNGLE("jungle", PGChestType::DEFAULT_CHEST_COTAINER,19,18*2-8),
    SPRUCE("spruce", PGChestType::DEFAULT_CHEST_COTAINER,19,18*2-8),
    WARPED("warped", PGChestType::DEFAULT_CHEST_COTAINER,19,18*2-8);

    public static List<Integer[]> DEFAULT_CHEST_COTAINER(){
        List<Integer[]> CHEST_SLOTS = new ArrayList<Integer[]>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 3; j++) {
                CHEST_SLOTS.add(new Integer[]{i,j});
            }
        }
        return CHEST_SLOTS;
    }

    public String chestname;
    public List<Integer[]> slots;

    public int extrax, extray;


    PGChestType(String chestname, Supplier<List<Integer[]>> slots, int extrax,int extray){
        this.chestname = chestname;
        this.slots = slots.get();
        this.extrax = extrax;
        this.extray = extray;
    };

    public ResourceLocation getTexture(){
        return new ResourceLocation(Pg_galaxie.MODID,"entity/chest/"+chestname);
    }
}
