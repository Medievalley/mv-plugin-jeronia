package org.shrigorevich.ml.domain.npc.contracts;

import org.shrigorevich.ml.domain.npc.models.StructNpcModel;

import java.util.List;
import java.util.Optional;

public interface NpcContext {

    int save(StructNpcModel npc);
    List<StructNpcModel> get();
    List<StructNpcModel> getByStructId(int structId);
    Optional<StructNpcModel> get(int id);
}
