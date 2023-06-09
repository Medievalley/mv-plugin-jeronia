package org.shrigorevich.ml.state.structures;

import org.shrigorevich.ml.domain.structures.Storage;

interface ExStorage extends Storage {
    /** <p>Replenish or reduce resources stock by the value passed</p>
     * Negative value - reduce resources stock <br>
     * Positive value - increase resources stock
     * @param amount - The value by which the stock will change
     */
    void updateResources(int amount);
}
