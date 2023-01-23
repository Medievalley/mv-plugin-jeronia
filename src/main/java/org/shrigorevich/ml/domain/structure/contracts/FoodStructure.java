package org.shrigorevich.ml.domain.structure.contracts;

public interface FoodStructure extends Structure, TownInfrastructure, WorkPlace {

    String getName();
    int getFoodStock();
    /** <p>Replenish or reduce food stock by the value passed</p>
     * Negative value - reduce food stock <br>
     * Positive value - increase food stock
     * @param foodAmount - The value by which the stock will change
     */
    void updateFoodStock(int foodAmount);
}
