package org.shrigorevich.ml.common;

import org.bukkit.Location;
import org.bukkit.Material;

public class MaterialHelper {

    public static boolean isCrop(Material material) {
        switch (material) {
            case WHEAT, CARROTS, POTATOES, BEETROOTS, MELON_STEM, PUMPKIN_STEM -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public static boolean isCrop(Location l) {
        return isCrop(l.getBlock().getType());
    }
}
