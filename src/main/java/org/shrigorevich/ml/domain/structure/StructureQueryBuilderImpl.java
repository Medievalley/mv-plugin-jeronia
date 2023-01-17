package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.structure.models.StructModel;

public class StructureQueryBuilderImpl {

    public String save(StructModel m) {
        return
            String.join("\n",
                "with rows as (",
                String.format("INSERT INTO location (x, y, z) VALUES (%d, %d, %d), (%d, %d, %d)",
                        m.getX1(), m.getY1(), m.getZ1(), m.getX2(), m.getY2(), m.getZ2()),
                "RETURNING id )",
                "insert into struct (loc1, loc2, type_id, world)",
                String.format("select ids[1], ids[2], %d, '%s' from (select array_agg(id) ids from rows) as locids;",
                        m.getTypeId(), m.getWorld())
                );
    }

    @Deprecated
    public String getById(int id) {
        return String.format(
            "select ls.struct_id as id, ls.name, ls.volume_id as volumeId, ls.priority, s.type_id as typeId, " +
            "s.world, s.x1, s.y1, s.z1, s.x2, s.y2, s.z2\n" +
            "from lore_struct ls JOIN struct s ON s.id = ls.struct_id where ls.struct_id=%d ", id);
    }

    public String getStructures() {
        return String.join("\n",
            "select id, name, volume_id as volumeId, priority, deposit, resources, type_id as typeId",
            "world, x1, y1, z1, x2, y2, z2,",
            "(select count(id)::int from struct_block where struct_id=s.id and broken=true) as brokenBlocks",
            "(select count(id)::int from struct_block where struct_id=s.id and broken=false) as blocks",
            "from struct");
    }

    public String setVolume(int structId, int volumeId) {
        return String.format("UPDATE struct SET volume_id = %d where id = %d", volumeId, structId);
    }

    public String getVolumeById(int id) {
        return String.join("\n",
            "SELECT id, size_x as sizex, size_y as sizey, size_z as sizez, name",
            String.format("FROM volume WHERE id=%d", id));
    }

    public String saveStructBlocks() {
        return "INSERT INTO struct_block (struct_id, volume_block_id, hp_trigger) VALUES (?, ?, ?)";
    }
}

//with rows as (
//    INSERT INTO location (x, y, z)
//VALUES (1, 2, 3), (1, 2, 3)
//    RETURNING id
//    )
//    insert into struct (loc1, loc2, type_id, world)
//    select ids[1], ids[2], 1, 'world' from (select array_agg(id) ids from rows) as locids;
