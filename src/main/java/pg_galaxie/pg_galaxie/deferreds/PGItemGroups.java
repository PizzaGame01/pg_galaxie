package pg_galaxie.pg_galaxie.deferreds;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import pg_galaxie.pg_galaxie.Pg_galaxie;

public class PGItemGroups {
    public static ItemGroup item = new ItemGroup("items") {
        @Override
        public ItemStack createIcon() {
            return PGItems.OIL_BUCKET.get().getDefaultInstance();
        }
    };
}
