package org.shrigorevich.ml.domain.npc;

public class NpcQueryBuilder {

    public String saveNpc() {
        return String.join("\n",
                "");
    }
}

//with rows as (
//    INSERT INTO location (x, y, z)
//VALUES (1, 2, 3), (1, 2, 3)
//    RETURNING id
//    )
//    insert into struct (loc1, loc2, type_id, world)
//    select ids[1], ids[2], 1, 'world' from (select array_agg(id) ids from rows) as locids;
