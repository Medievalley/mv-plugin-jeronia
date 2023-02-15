package org.shrigorevich.ml.domain.mob;

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
