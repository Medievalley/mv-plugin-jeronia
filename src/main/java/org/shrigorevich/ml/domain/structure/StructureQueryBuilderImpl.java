package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.structure.models.StructModel;

public class StructureQueryBuilderImpl {

    public String save(StructModel m) {
        String sql = "with rows as (\n" +
                "    INSERT INTO location (x, y, z)\n" +
                "    VALUES (%d, %d, 3), (1, 2, 3)\n" +
                "    RETURNING id\n" +
                ")\n" +
                "insert into struct (loc1, loc2, type_id, world)\n" +
                "select ids[1], ids[2], 1, 'world' from (select array_agg(id) ids from rows) as locids;\n";
        return String.format(
                "WITH rows as (\n" +
                "    INSERT INTO struct (type_id, destructible, world, x1, y1, z1, x2, y2, z2)\n" +
                "    VALUES (%d, %b, '%s', %d, %d, %d, %d, %d, %d)\n" +
                "    RETURNING id\n" +
                ")\n" +
                "INSERT INTO lore_struct (struct_id, name) SELECT id, '%s' from rows\n" +
                "RETURNING struct_id",
                m.getTypeId(), m.getWorld(),
                m.getX1(), m.getY1(), m.getZ1(), m.getX2(), m.getY2(), m.getZ2(), m.getName()
        );
    }

    public String getById(int id) {
        return String.format(
            "select ls.struct_id as id, ls.name, ls.volume_id as volumeId, ls.priority, s.type_id as typeId, " +
            "s.world, s.x1, s.y1, s.z1, s.x2, s.y2, s.z2\n" +
            "from lore_struct ls JOIN struct s ON s.id = ls.struct_id where ls.struct_id=%d ", id);
    }

    public String getLoreStructures() {
        return String.format(
                "select ls.struct_id as id, ls.name, ls.volume_id as volumeId, ls.priority, ls.stock, s.type_id as typeId, \n" +
                "s.world, s.x1, s.y1, s.z1, s.x2, s.y2, s.z2,\n" +
                "(select count(id)::int from struct_block where struct_id=s.id and broken=true) as brokenBlocks,\n" +
                "(select count(id)::int from struct_block where struct_id=s.id and broken=false) as blocks\n" +
                "from lore_struct ls JOIN struct s ON s.id = ls.struct_id"
        );
    }

    private String insertStructLocs(StructModel m) {
        return String.format(
                "with rows as (\n" +
                "    INSERT INTO location (x, y, z)\n" +
                "    VALUES (1, 2, 3), (1, 2, 3)\n" +
                "    RETURNING id\n" +
                ")");
    }
}

//with rows as (
//    INSERT INTO location (x, y, z)
//VALUES (1, 2, 3), (1, 2, 3)
//    RETURNING id
//    )
//    insert into struct (loc1, loc2, type_id, world)
//    select ids[1], ids[2], 1, 'world' from (select array_agg(id) ids from rows) as locids;
