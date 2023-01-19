package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.structure.models.StructModel;

public class StructureQueryBuilderImpl {

    public String save(StructModel m) {
        if (StructureType.valueOf(m.getTypeId()) == StructureType.INFRA) {
            return String.join("\n",
                "insert into struct (name, type_id, volume_id, world, priority, x1, y1, z1, x2, y2, z2)",
                String.format("VALUES ('%s', %d, %d, '%s', %d, %d, %d, %d, %d, %d, %d);",
                    m.getName(), m.getTypeId(), m.getVolumeId(), m.getWorld(), m.getPriority(),
                    m.getX1(), m.getY1(), m.getZ1(), m.getX2(), m.getY2(), m.getZ2()));
        } else {
            throw new IllegalArgumentException(
                    String.format("Save script for StructType: %d not implemented", m.getTypeId()));
        }
    }

    @Deprecated
    public String getById(int id) {
        return String.format(
            "select ls.struct_id as id, ls.name, ls.volume_id as volumeId, ls.priority, s.type_id as typeId, " +
            "s.world, s.x1, s.y1, s.z1, s.x2, s.y2, s.z2\n" +
            "from lore_struct ls JOIN struct s ON s.id = ls.struct_id where ls.struct_id=%d ", id);
    }

    //TODO: refactor broken blocks logic
    @Deprecated
    public String getStructures() {
        return String.join("\n",
            "select id, name, volume_id as volumeId, priority, deposit, resources, type_id as typeId",
            "world, x1, y1, z1, x2, y2, z2,",
            "(select count(id)::int from struct_block where struct_id=s.id and broken=true) as brokenBlocks,",
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

    public String getStructBlocks(int structId) {
        return String.join("\n",
            selectStructBlock(),
            String.format("where struct_id=%d", structId));
    }

    public String getStructBlock(int id) {
        return String.join("\n",
            selectStructBlock(),
            String.format("where b.id=%d", id));
    }

    public String updateBlocksStatus() {
        return "UPDATE struct_block SET broken=? WHERE id=?";
    }

    public String restoreBlock(int id) {
        return String.format("UPDATE struct_block SET broken=false WHERE id=%d", id);
    }

    public String restoreStruct(int id) {
        return String.format("UPDATE struct_block SET broken=false WHERE struct_id=%d and broken=true", id);
    }

    public String unAttachVolume(int structId) {
        return String.format("UPDATE struct SET volume_id=null WHERE id=%d", structId);
    }

    public String clearStructBlocks(int structId) {
        return String.format("DELETE FROM struct_block WHERE struct_id=%d", structId);
    }

    public String updateResources(int structId, int stockSize) {
        return String.format("UPDATE struct SET resources=%d WHERE id=%d", stockSize, structId);
    }


    private String selectStructBlock() {
        return String.join("\n",
                "select b.id, s.id as structId, b.type, v.block_data as blockData,",
                "b.broken, b.hp_trigger as triggerDestruction,",
                "v.x+s.x1 as x, v.y+s.y1 as y, v.z+s.z1 as z",
                "from struct_block b",
                "join volume_block v ON b.volume_block_id=v.id",
                "join struct s ON b.struct_id = s.id");
    }
}

//with rows as (
//    INSERT INTO location (x, y, z)
//VALUES (1, 2, 3), (1, 2, 3)
//    RETURNING id
//    )
//    insert into struct (loc1, loc2, type_id, world)
//    select ids[1], ids[2], 1, 'world' from (select array_agg(id) ids from rows) as locids;
