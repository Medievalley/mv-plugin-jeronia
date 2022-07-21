package org.shrigorevich.ml.domain.enums;

public enum StructureType {
    PRIVATE(1), LORE(2);

    private final int typeId;

    StructureType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }
}
