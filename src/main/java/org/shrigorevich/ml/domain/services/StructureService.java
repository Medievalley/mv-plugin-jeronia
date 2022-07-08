package org.shrigorevich.ml.domain.services;

import org.bukkit.Location;
import org.shrigorevich.ml.db.callbacks.ICreateOneCallback;
import org.shrigorevich.ml.db.contexts.IStructureContext;
import org.shrigorevich.ml.domain.models.IStructure;

import java.util.HashMap;
import java.util.Map;

public class StructureService implements IStructureService {
    private final IStructureContext structureContext;
    private final Map<String, IStructure> structures;


    public StructureService(IStructureContext structureContext) {
        this.structureContext = structureContext;
        this.structures = new HashMap<>();
    }
}
