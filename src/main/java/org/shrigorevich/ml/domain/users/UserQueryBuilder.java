package org.shrigorevich.ml.domain.users;

public class UserQueryBuilder {

    public String getByName(String name) {
        return String.join("\n",
            "SELECT u.id, u.login as name, u.ip, u.verified, pd.lives, r.id as roleId",
                "from users u JOIN player_data pd on u.id = pd.user_id JOIN role r on r.id = pd.role_id",
                String.format("WHERE u.username = '%s'", name));
    }

}
