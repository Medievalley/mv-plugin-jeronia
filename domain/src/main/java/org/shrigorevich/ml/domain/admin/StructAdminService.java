package org.shrigorevich.ml.domain.admin;

import org.bukkit.Location;
import org.shrigorevich.ml.domain.Service;
import org.shrigorevich.ml.domain.structures.DraftStruct;
import org.shrigorevich.ml.domain.structures.StructureType;

import java.util.Optional;

public interface StructAdminService {

    void draftLocation(String key, Location l);
    Optional<DraftStruct> getDraftStruct(String key);
    void draftName(String key, String name);
    void draftType(String key, StructureType type);
    boolean isStructValid(DraftStruct struct);
}
