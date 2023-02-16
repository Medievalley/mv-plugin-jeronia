package org.shrigorevich.ml.admin;

import org.bukkit.Location;
import org.shrigorevich.ml.common.Service;
import org.shrigorevich.ml.domain.structure.DraftStruct;
import org.shrigorevich.ml.domain.structure.StructureType;

import java.util.Optional;

public interface StructAdminService extends Service {

    void draftLocation(String key, Location l);
    Optional<DraftStruct> getDraftStruct(String key);
    void draftName(String key, String name);
    void draftType(String key, StructureType type);
    boolean isStructValid(DraftStruct struct);
}
