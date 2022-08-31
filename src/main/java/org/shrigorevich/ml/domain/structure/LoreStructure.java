package org.shrigorevich.ml.domain.structure;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

import java.util.Optional;

public interface LoreStructure extends Structure, Volumeable, Comparable<LoreStructure> {

    String getName();
    int getPriority();
    long getDestructionPercent();
    void setDestroyedPercent(int destroyedPercent);
    int getFoodStock();
    /** <p>Replenish or reduce food stock by the value passed</p>
     * Negative value - reduce food stock <br>
     * Positive value - increase food stock
     * @param foodAmount - The value by which the stock will change
     */
    void updateFoodStock(int foodAmount);
    Optional<Villager> getLaborer();
    void setLaborer(Villager e);
}
