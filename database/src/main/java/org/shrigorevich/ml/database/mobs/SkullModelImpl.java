package org.shrigorevich.ml.database.mobs;

import org.shrigorevich.ml.state.mobs.models.SkullModel;

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
