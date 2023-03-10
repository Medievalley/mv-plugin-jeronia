package org.shrigorevich.ml.config;

public enum ConfPath {
    MOB_SPAWN_MAX_MOB_QTY("mob_spawn.max_mob_qty"),
    MOB_SPAWN_PRESSURE_INTERVAL("mob_spawn.pressure_interval"),
    MOB_SPAWN_PRESSURE_PF("mob_spawn.pressure_players_factor"),
    USER_MAX_JOBS_QTY("user.max_jobs_qty");
    private final String path;

    ConfPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static ConfPath getByPath(String path) throws IllegalArgumentException {
        for(ConfPath p : ConfPath.values()) {
            if (p.getPath().equals(path)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Enum does not contain such a path");
    }
}
