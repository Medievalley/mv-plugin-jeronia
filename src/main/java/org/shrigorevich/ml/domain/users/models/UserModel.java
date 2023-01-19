package org.shrigorevich.ml.domain.users.models;

public interface UserModel {
    int getId();
    String getName();
    String getIp();
    boolean isVerified();
    int getRoleId();
    int getLives();
}
