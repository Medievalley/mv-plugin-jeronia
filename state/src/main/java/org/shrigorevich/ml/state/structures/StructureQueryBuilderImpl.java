package org.shrigorevich.ml.state.structures;

import org.shrigorevich.ml.common.Coords;

public class StructureQueryBuilderImpl {

    public String save(String name, int typeId, String world, Coords l1, Coords l2) {
        return String.join("\n",
            "insert into struct (name, type_id, world, x1, y1, z1, x2, y2, z2)",
            String.format("VALUES ('%s', %d, '%s', %d, %d, %d, %d, %d, %d);",
                name, typeId, world, l1.x(), l1.y(), l1.z(), l2.x(), l2.y(), l2.z()));
    }

    @Deprecated
    public String getById(int id) {
        return String.format(
            "select id, name, volume_id as volumeId, priority, deposit, resources, type_id as typeId, " +
            "world, x1, y1, z1, x2, y2, z2\n" +
            "from struct where id=%d ", id);
    }

    public String getStructures() {
        return String.join("\n",
            "select id, name, volume_id as volumeId, priority, deposit, resources, type_id as typeId,",
            "world, x1, y1, z1, x2, y2, z2",
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

    public String getStructBlocks() {
        return selectStructBlock();
    }

    public String getBrokenBlockCount(int structId) {
        return String.format("SELECT COUNT(*) FROM struct_block WHERE broken=true and struct_id=%d", structId);
    }

    public String getStructBlockCount(int structId) {
        return String.format("SELECT COUNT(*) FROM struct_block WHERE struct_id=%d", structId);
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
                "select b.id, s.id as structId, b.type_id as typeId, v.block_data as blockData,",
                "b.broken, b.hp_trigger as triggerDestruction,",
                "v.x+s.x1 as x, v.y+s.y1 as y, v.z+s.z1 as z",
                "from struct_block b",
                "join volume_block v ON b.volume_block_id = v.id",
                "join struct s ON b.struct_id = s.id");
    }

    public String changeBlockType(int blockId, int typeId) {
        return String.format("UPDATE struct_block SET type_id=%d WHERE id=%d", typeId, blockId);
    }
}

//with rows as (
//    INSERT INTO location (x, y, z)
//VALUES (1, 2, 3), (1, 2, 3)
//    RETURNING id
//    )
//    insert into struct (loc1, loc2, type_id, world)
//    select ids[1], ids[2], 1, 'world' from (select array_agg(id) ids from rows) as locids;

//"(select count(id)::int from struct_block where struct_id=s.id and broken=true) as brokenBlocks,",
//"(select count(id)::int from struct_block where struct_id=s.id and broken=false) as blocks",