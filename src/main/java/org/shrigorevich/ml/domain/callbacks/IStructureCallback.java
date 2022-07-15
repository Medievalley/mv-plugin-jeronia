package org.shrigorevich.ml.domain.callbacks;

import org.shrigorevich.ml.db.models.GetStructModel;

import java.util.Optional;

public interface IStructureCallback {

    void structSaved(Optional<GetStructModel> m, boolean res, String msg);
}
