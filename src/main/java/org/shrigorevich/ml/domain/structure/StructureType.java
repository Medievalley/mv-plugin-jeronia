package org.shrigorevich.ml.domain.structure;

public enum StructureType {
    MAIN(1, "main"),
    AGRONOMIC(2, "agronomic"),
    PRIVATE(3, "private"),
    MOB_ABODE(4, "abode");

    private final int typeId;
    private final String name;

    StructureType(int typeId, String name) {
        this.typeId = typeId;
        this.name = name;
    }

    public int getTypeId() {
        return typeId;
    }
    public String getName() {
        return name;
    }

    public static StructureType valueOf(int id) {
        for(StructureType st : StructureType.values()) {
            if (st.getTypeId() == id) {
                return st;
            }
        }
        return null;
    }

    public static StructureType getByName(String name) {
        for(StructureType st : StructureType.values()) {
            if (st.getName().equals(name.toLowerCase())) {
                return st;
            }
        }
        throw new IllegalArgumentException("No constant named " + name);
    }
}
