package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.structure.contracts.AbodeStruct;
import org.shrigorevich.ml.domain.structure.models.AbodeStructModel;

public class AbodeStructImpl extends StructureImpl implements AbodeStruct {

    private final String name;

    public AbodeStructImpl(AbodeStructModel model) {
        super(model);
        this.name = model.getName();
    }

    @Override
    public String getName() {
        return name;
    }
}
