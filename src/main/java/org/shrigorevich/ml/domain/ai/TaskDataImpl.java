package org.shrigorevich.ml.domain.ai;

import java.util.UUID;

public class TaskDataImpl implements TaskData {

    private final UUID id;
    private final TaskType type;

    public TaskDataImpl(TaskType type) {
        this.id = UUID.randomUUID();
        this.type = type;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public TaskType getType() {
        return type;
    }
}
