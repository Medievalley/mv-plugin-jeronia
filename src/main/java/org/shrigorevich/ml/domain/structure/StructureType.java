package org.shrigorevich.ml.domain.structure;

public enum StructureType {
    PRIVATE(1), AGRONOMIC(2), MOB_ABODE(3);

    private final int typeId;

    StructureType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public static StructureType valueOf(int id) {
        for(StructureType st : StructureType.values()) {
            if (st.getTypeId() == id) {
                return st;
            }
        }
        return null;
    }
}
