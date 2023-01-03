package org.shrigorevich.ml.domain.structure;

public enum StructureType {
    PRIVATE(1), LORE(2), MOB_ABODE(3);

    private final int typeId;

    StructureType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }
}
