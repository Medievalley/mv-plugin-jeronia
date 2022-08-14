package org.shrigorevich.ml.domain.ai;

public enum TaskPriority {
    HIGH(1), MIDDLE(2), LOW(3);

    private final int value;

    TaskPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
