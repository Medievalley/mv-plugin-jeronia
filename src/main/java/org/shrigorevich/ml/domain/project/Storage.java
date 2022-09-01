package org.shrigorevich.ml.domain.project;

import org.bukkit.Material;


public interface Storage {
    int getResource(Material material);
    void addResource(Material material, int number);
}
