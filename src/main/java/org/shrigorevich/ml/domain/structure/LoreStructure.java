package org.shrigorevich.ml.domain.structure;

import org.bukkit.entity.Entity;

import java.util.Optional;

public interface LoreStructure extends Structure, Nameable, Volumeable {

    long getDestructionPercent();
    void setDestroyedPercent(int destroyedPercent);
    int getFoodStock();
    /** <p>Replenish or reduce food stock by the value passed</p>
     * Negative value - reduce food stock <br>
     * Positive value - increase food stock
     * @param foodAmount - The value by which the stock will change
     */
    void updateFoodStock(int foodAmount);
    Optional<Entity> getLaborer();
    void setLaborer(Entity e);
}
