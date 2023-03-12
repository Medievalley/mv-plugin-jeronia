package org.shrigorevich.ml.database.users;

public class UserQueryBuilder {

    public String getByName(String name) {
        return String.join("\n",
            "SELECT u.id, u.login as name, u.ip, u.verified, pd.lives, r.id as roleId",
                "from users u JOIN player_data pd on u.id = pd.user_id JOIN role r on r.id = pd.role_id",
                String.format("WHERE u.login = '%s'", name));
    }

    public String updateKillStatistics(String userId, String entityType){
        return String.format("INSERT INTO kill_stats (user_id, entity_type) VALUES ('%s', '%s')\n" +
                "ON CONFLICT (user_id, entity_type) DO\n" +
                "UPDATE SET kills = kill_stats.kills + 1;", userId, entityType);
    }

    public String updateDeathStatistics(String userId, String deathReason) {
        return String.format("INSERT INTO death_stats (user_id, reason) VALUES ('%s', '%s')\n" +
                "ON CONFLICT (user_id, reason) DO\n" +
                "UPDATE SET deaths = death_stats.deaths + 1;", userId, deathReason);
    }

    public String decrementUserLives(String userId) {
        return String.format("UPDATE player_data SET lives = player_data.lives - 1 WHERE user_id = '%s'", userId);
    }
}
