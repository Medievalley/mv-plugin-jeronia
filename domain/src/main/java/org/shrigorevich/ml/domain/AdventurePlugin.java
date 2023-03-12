package org.shrigorevich.ml.domain;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.jetbrains.annotations.NotNull;

public interface AdventurePlugin {
    @NotNull
    BukkitAudiences adventure();
    void showTitle(String title, String subTitle);
    void showTitle(String title, String subTitle, int color);
    void showTitle(String title, String subTitle, int titleColor, int subTitleColor);
}
