package org.shrigorevich.ml.domain.npc;

import org.shrigorevich.ml.domain.npc.models.StructNpcModel;

public class NpcQueryBuilder {

    public String saveNpc(StructNpcModel m) {
        return String.join("\n",
            "with rows as ( INSERT INTO location (x, y, z)",
            String.format("VALUES (%d, %d, %d), (%d, %d, %d)", m.getSpawnX(), m.getSpawnY(), m.getSpawnZ(),
                m.getWorkX(), m.getWorkY(), m.getWorkZ()),
            "INSERT INTO struct_npc (name, role_id, struct_id, spawn, work)",
            "SELECT '%s', %d, %d, ids[1], ids[2]",
            "from (select array_agg(id) ids from rows) as locids returning id;");
    }
}

//with rows as (
//    INSERT INTO location (x, y, z)
//VALUES (1, 2, 3), (1, 2, 3)
//    RETURNING id
//    )
//    insert into struct (loc1, loc2, type_id, world)
//    select ids[1], ids[2], 1, 'world' from (select array_agg(id) ids from rows) as locids;