package org.shrigorevich.ml.domain.structure.contracts;

import org.bukkit.entity.Villager;
import org.shrigorevich.ml.domain.volume.Volumeable;

import java.util.Optional;

public interface LoreStructure extends Structure, Volumeable {

    String getName();
    int getPriority();
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