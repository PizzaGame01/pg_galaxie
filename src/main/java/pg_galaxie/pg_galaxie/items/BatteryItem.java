package pg_galaxie.pg_galaxie.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class BatteryItem extends Item {
    public BatteryItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }
    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return new Color(0, 133, 186, 109).getRGB();
    }
}
