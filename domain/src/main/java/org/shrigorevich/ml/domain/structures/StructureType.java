package org.shrigorevich.ml.domain.structures;

public enum StructureType {
    MAIN(1),
    AGRONOMIC(2),
    PRIVATE(3),
    ABODE(4),
    PRESSURE(5),
    FARM_GUILD(6),
    CRAFT_GUILD(7),
    DEFENCE_GUILD(8),
    WIZARD_GUILD(9);

    private final int typeId;

    StructureType(int typeId) {
        this.typeId = typeId;
    }

    public int getId() {
        return typeId;
    }

    public static StructureType valueOf(int id) {
        for(StructureType st : StructureType.values()) {
            if (st.getId() == id) {
                return st;
            }
        }
        return null;
    }
}
