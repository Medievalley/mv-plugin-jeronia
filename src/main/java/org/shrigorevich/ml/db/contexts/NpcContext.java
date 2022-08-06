package org.shrigorevich.ml.db.contexts;

import org.shrigorevich.ml.domain.npc.models.StructNpcDB;

import java.util.List;
import java.util.Optional;

public interface NpcContext {

    int save(StructNpcDB npc);

    List<StructNpcDB> get();

    Optional<StructNpcDB> get(int id);
}
