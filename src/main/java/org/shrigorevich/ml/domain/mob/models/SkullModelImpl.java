package org.shrigorevich.ml.domain.mob.models;

public class SkullModelImpl implements SkullModel {

    private String name, skin, type;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSkin() {
        return skin;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public void setType(String type) {
        this.type = type;
    }
}
