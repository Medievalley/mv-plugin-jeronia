package org.shrigorevich.ml.domain.scoreboard;

public enum BoardType {
    PROJECT("Project:");

    private final String name;
    BoardType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
