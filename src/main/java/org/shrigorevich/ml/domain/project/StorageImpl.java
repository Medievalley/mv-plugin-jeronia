package org.shrigorevich.ml.domain.project;

import org.bukkit.Material;
import org.shrigorevich.ml.domain.project.models.ResourceModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageImpl implements Storage {
    private final Map<Material, Integer> resources;

    public StorageImpl(List<ResourceModel> resList) {
        this.resources = new HashMap<>();
        for (ResourceModel model : resList) {
            Material type = Material.valueOf(model.getType());
            resources.put(type, model.getNumber());
        }
    }

    public StorageImpl(){
        resources = new HashMap<>();
    }

    @Override
    public int getResource(Material material) {
        return resources.getOrDefault(material, 0);
    }

    @Override
    public void addResource(Material material, int number) {
        int curNumber = getResource(material) + number;
        resources.put(material, number);
    }
}
