package org.shrigorevich.ml.state.mobs;

import org.shrigorevich.ml.domain.mobs.PresetUnit;

class PresetUnitImpl implements PresetUnit {

    private final double powerPercent;
    private final double qtyPercent;

    public PresetUnitImpl(double powerPercent, double qtyPercent) {
        this.powerPercent = powerPercent;
        this.qtyPercent = qtyPercent;
    }


    @Override
    public double getPowerPercent() {
        return powerPercent;
    }

    @Override
    public double getQtyPercent() {
        return qtyPercent;
    }
}
