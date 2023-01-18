package org.shrigorevich.ml.domain.npc.contracts;

import org.shrigorevich.ml.domain.npc.models.StructNpcDB;

import java.util.List;
import java.util.Optional;

public interface NpcContext {

    int save(StructNpcDB npc);
    List<StructNpcDB> get();
    List<StructNpcDB> getByStructId(int structId);
    Optional<StructNpcDB> get(int id);
}