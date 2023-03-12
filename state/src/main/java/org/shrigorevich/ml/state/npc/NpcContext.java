package org.shrigorevich.ml.state.npc;

import org.shrigorevich.ml.common.Coords;
import org.shrigorevich.ml.state.npc.models.StructNpcModel;

import java.util.List;
import java.util.Optional;

public interface NpcContext {
    int save(String name, int roleId, int structId, Coords spawn, Coords work) throws Exception;
    List<StructNpcModel> get() throws Exception;
    List<StructNpcModel> getByStructId(int structId) throws Exception;
    Optional<StructNpcModel> get(int id) throws Exception;
}
