package org.shrigorevich.ml.domain.npc.impl;

import org.shrigorevich.ml.domain.npc.DraftNpc;

public class NpcQueryBuilder {

    public String saveNpc(DraftNpc m) {
        return String.format(
            """
            with rows as (
                INSERT INTO location (x, y, z)
                VALUES (%d, %d, %d), (%d, %d, %d) returning id
            )
            INSERT INTO struct_npc (name, role_id, struct_id, spawn, work)
            SELECT '%s', %d, %d, ids[1], ids[2]
            from (select array_agg(id) ids from rows) as locids returning id""",
            m.getSpawnLoc().x(), m.getSpawnLoc().y(), m.getSpawnLoc().z(),
            m.getWorkLoc().x(), m.getWorkLoc().y(), m.getWorkLoc().z(),
            m.getName(), m.getRole().getRoleId(), m.getStructId());
    }

    public String getNpcList() {
        return """
            select n.id, n.name, struct_id as structId, n.alive, n.role_id as roleId, s.world,
            sl.x as spawnX, sl.y as spawnY, sl.z as spawnZ, wl.x as workX, wl.y as workY, wl.z as workZ
            from struct_npc n
            join struct s on s.id = n.struct_id
            join location sl on sl.id = n.spawn
            join location wl on wl.id = n.work""";
    }

    public String getByStructId(int structId) {
        return String.format(
            """
            select n.id, n.name, struct_id as structId, n.alive, n.role_id as roleId, s.world,
            sl.x as spawnX, sl.y as spawnY, sl.z as spawnZ, wl.x as workX, wl.y as workY, wl.z as workZ
            from struct_npc n
            join struct s on s.id = n.struct_id
            join location sl on sl.id = n.spawn
            join location wl on wl.id = n.work
            where n.struct_id = %d""", structId);
    }

    public String getById(int id) {
        return String.format(
            """
            select n.id, n.name, struct_id as structId, n.alive, n.role_id as roleId, s.world,
            sl.x as spawnX, sl.y as spawnY, sl.z as spawnZ, wl.x as workX, wl.y as workY, wl.z as workZ
            from struct_npc n
            join struct s on s.id = n.struct_id
            join location sl on sl.id = n.spawn
            join location wl on wl.id = n.work
            where n.id = %d""", id);
    }
}

//with rows as (
//    INSERT INTO location (x, y, z)
//VALUES (1, 2, 3), (1, 2, 3)
//    RETURNING id
//    )
//    insert into struct (loc1, loc2, type_id, world)
//    select ids[1], ids[2], 1, 'world' from (select array_agg(id) ids from rows) as locids;
