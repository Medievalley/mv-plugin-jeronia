package org.shrigorevich.ml.domain.structure;

public enum StructureType {
    MAIN(1, "main"),
    AGRONOMIC(2, "agronomic"),
    PRIVATE(3, "private"),
    ABODE(4, "abode"),
    PRESSURE(5, "pressure"),
    FARM_GUILD(6, "farm_guild"),
    CRAFT_GUILD(7, "craft_guild"),
    DEFENCE_GUILD(8, "defence_guild"),
    WIZARD_GUILD(9, "WIZARD_GUILD");

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
