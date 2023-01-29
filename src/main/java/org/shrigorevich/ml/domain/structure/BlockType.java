package org.shrigorevich.ml.domain.structure;

public enum BlockType {
    DEFAULT(1), SAFE_LOC(2), ABODE_SPAWN(3);

    private final int typeId;

    BlockType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public static BlockType valueOf(int id) {
        for(BlockType st : BlockType.values()) {
            if (st.getTypeId() == id) {
                return st;
            }
        }
        return null;
    }
}
