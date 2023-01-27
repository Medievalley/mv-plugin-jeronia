package org.shrigorevich.ml.domain.structure.impl;

import org.shrigorevich.ml.domain.structure.AbodeStruct;
import org.shrigorevich.ml.domain.structure.models.StructModel;

public class AbodeStructImpl extends StructureImpl implements AbodeStruct {

    private final String name;

    public AbodeStructImpl(StructModel model) {
        super(model);
        this.name = model.getName();
    }

    @Override
    public String getName() {
        return name;
    }
}