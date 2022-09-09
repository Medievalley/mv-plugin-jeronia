package org.shrigorevich.ml.common;

import org.bukkit.Location;
import org.bukkit.Material;

public class Utils {

    public static boolean isLocationsEquals(Location l1, Location l2) {
        return l1.distanceSquared(l2) < 1;
    }

    public static boolean isStructPlant(Material material) {
        switch (material) {
            case WHEAT:
            case POTATOES:
            case CARROTS:
                return true;
            default:
                return false;
        }
    }
}
