package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.structure.models.LoreStructModel;

public class StructureQueryBuilderImpl {

    public String save(LoreStructModel m) {
        return String.format(
                "WITH rows as (\n" +
                "    INSERT INTO struct (type_id, destructible, world, x1, y1, z1, x2, y2, z2)\n" +
                "    VALUES (%d, %b, '%s', %d, %d, %d, %d, %d, %d)\n" +
                "    RETURNING id\n" +
                ")\n" +
                "INSERT INTO lore_struct (struct_id, name) SELECT id, '%s' from rows\n" +
                "RETURNING struct_id",
                m.getTypeId(), m.isDestructible(), m.getWorld(),
                m.getX1(), m.getY1(), m.getZ1(), m.getX2(), m.getY2(), m.getZ2(), m.getName()
        );
    }

    public String getById(int id) {
        return String.format(
            "select ls.struct_id as id, ls.name, ls.volume_id as volumeId, ls.priority, s.type_id as typeId, " +
            "s.world, s.x1, s.y1, s.z1, s.x2, s.y2, s.z2\n" +
            "from lore_struct ls JOIN struct s ON s.id = ls.struct_id where ls.struct_id=%d ", id);
    }
}
