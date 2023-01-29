package org.shrigorevich.ml.domain.users.models;

public interface UserModel {
    String getId();
    String getName();
    String getIp();
    boolean isVerified();
    int getRoleId();
    int getLives();
}
