package org.shrigorevich.ml.domain.npc.contracts;

import org.shrigorevich.ml.domain.npc.models.StructNpcModel;

import java.util.List;
import java.util.Optional;

public interface NpcContext {

    /**
     *
     * @param npc
     * @return ID of created npc
     * @throws Exception
     */
    int save(StructNpcModel npc) throws Exception;
    List<StructNpcModel> get();
    List<StructNpcModel> getByStructId(int structId);
    Optional<StructNpcModel> get(int id);
}
