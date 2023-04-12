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

    public String addUserJob(String userId, int jobId) {
        return String.format("INSERT INTO user_job (user_id, job_id) VALUES ('%s', %d);", userId, jobId);
    }

    public String removeUserJob(String userId, int jobId) {
        return String.format("DELETE FROM user_job WHERE user_id = '%s' AND job_id = %d;", userId, jobId);
    }

    public String getUserJobsByUserId(String userId) {
        return String.format("SELECT job_id as jobId, level FROM user_job WHERE user_id = '%s'", userId);
    }

    public String getRestrictedItems() {
        return "SELECT\n" +
                "ri2.type,\n" +
                "CAST(ri2.jobAllowances AS text),\n" +
                "CAST(ri2.roleAllowances AS text)\n" +
                "FROM\n" +
                "(SELECT\n" +
                "ri.type,\n" +
                "COALESCE(json_agg(json_build_object('jobId', jia.job_id, 'level', jia.level)) FILTER (WHERE jia.job_id IS NOT NULL), '[]') AS jobAllowances,\n" +
                "COALESCE(json_agg(json_build_object('roleId', ria.role_id)) FILTER (WHERE ria.role_id IS NOT NULL), '[]') AS roleAllowances\n" +
                "FROM restricted_item as ri\n" +
                "LEFT JOIN job_item_allowance as jia on ri.id = jia.item_id\n" +
                "LEFT JOIN role_item_allowance as ria on ri.id = ria.item_id\n" +
                "GROUP  BY ri.type) ri2;";
    }
}
