package org.shrigorevich.ml.domain.structure.contracts;

import org.bukkit.entity.Villager;

import java.util.Optional;

public interface WorkPlace {
    Optional<Villager> getLaborer();
    void setLaborer(Villager e);
}
