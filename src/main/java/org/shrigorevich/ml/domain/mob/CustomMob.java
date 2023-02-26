package org.shrigorevich.ml.domain.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.shrigorevich.ml.domain.ai.TaskHolder;

import java.util.UUID;

public interface CustomMob<T extends Mob> extends TaskHolder {
    UUID getId();
    double getPower();
    T getEntity();
}
