package org.shrigorevich.ml.domain.project.models;

public class ResourceModelImpl implements ResourceModel {

    private String type;
    private int number;

    public ResourceModelImpl(){}

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setType(String type) {
        this.type = type;
    }
}
