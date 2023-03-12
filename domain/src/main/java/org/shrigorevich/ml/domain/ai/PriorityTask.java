package org.shrigorevich.ml.domain.ai;

public interface PriorityTask extends Task, Comparable<PriorityTask> {
    TaskPriority getPriority();
    boolean shouldBeBlocked();
    void setBlocked(boolean blocked);
    boolean isBlocked();
}
