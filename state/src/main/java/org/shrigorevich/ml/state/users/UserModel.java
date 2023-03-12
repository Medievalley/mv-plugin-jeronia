package org.shrigorevich.ml.state.users;

public interface UserModel {
    String getId();
    String getName();
    String getIp();
    boolean isVerified();
    int getRoleId();
    int getLives();
}
