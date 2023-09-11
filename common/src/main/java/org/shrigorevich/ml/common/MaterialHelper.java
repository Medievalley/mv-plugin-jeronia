package org.shrigorevich.ml.common;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

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

    /**
     * Bamboo and iron door ignored
     */
    private static boolean isDoor(Material material) {
        switch (material) {
            case DARK_OAK_DOOR,
                ACACIA_DOOR,
                BIRCH_DOOR,
                CRIMSON_DOOR,
                OAK_DOOR,
                JUNGLE_DOOR,
                MANGROVE_DOOR,
                SPRUCE_DOOR,
                WARPED_DOOR -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public static boolean isDoor(Block b) {
        return isDoor(b.getType());
    }
    public static boolean isDoor(Location l) {
        return isDoor(l.getBlock());
    }
}
