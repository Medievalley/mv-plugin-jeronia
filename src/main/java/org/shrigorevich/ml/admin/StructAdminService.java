package org.shrigorevich.ml.admin;

import org.bukkit.Location;
import org.shrigorevich.ml.common.Service;
import org.shrigorevich.ml.domain.structure.DraftStruct;
import org.shrigorevich.ml.domain.structure.Structure;
import org.shrigorevich.ml.domain.structure.StructureType;

import java.util.Optional;

public interface StructAdminService extends Service {

    void draftLocation(String key, Location l);
    Optional<DraftStruct> getDraftStruct(String key);
    void draftName(String key, String name);
    void draftType(String key, StructureType type);
    void setSelectedStruct(String key, Structure struct);
    Optional<Structure> getSelectedStruct(String key);
    boolean isStructValid(DraftStruct struct);
}
