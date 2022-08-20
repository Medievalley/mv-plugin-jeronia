package org.shrigorevich.ml.common;

import org.bukkit.Location;

public class Utils {

    public static boolean isLocationsEquals(Location l1, Location l2) {
        return l1.getBlockX() == l2.getBlockX() &&
                l1.getBlockY() == l2.getBlockY() &&
                l1.getBlockZ() == l2.getBlockZ();
    }
}
