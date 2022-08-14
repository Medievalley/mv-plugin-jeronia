package org.shrigorevich.ml.domain.ai;

public enum TaskPriority {
    HIGH(3), MIDDLE(2), LOW(1);

    private final int value;

    TaskPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
