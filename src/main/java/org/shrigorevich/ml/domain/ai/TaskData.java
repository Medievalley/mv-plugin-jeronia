package org.shrigorevich.ml.domain.ai;

import java.util.UUID;

public interface TaskData {
    UUID getId();
    TaskType getType();
}
