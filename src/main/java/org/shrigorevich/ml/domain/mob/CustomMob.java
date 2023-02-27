package org.shrigorevich.ml.domain.mob;

import org.shrigorevich.ml.domain.ai.TaskHolder;
import org.shrigorevich.ml.domain.mob.custom.MobType;

import java.util.UUID;

public interface CustomMob extends TaskHolder {
    UUID getId();
    double getPower();
    MobType getType();
}
