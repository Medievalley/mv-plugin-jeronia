package org.shrigorevich.ml.admin;

import org.bukkit.Location;
import org.shrigorevich.ml.common.Service;
import org.shrigorevich.ml.domain.structure.contracts.Structure;

import java.util.ArrayList;
import java.util.Optional;

public interface StructAdminService extends Service {

    void setCorner(String key, Location l);
    ArrayList<Location> getStructCorners(String key);
    void setSelectedStruct(String key, Structure struct);
    Optional<Structure> getSelectedStruct(String key);
}
