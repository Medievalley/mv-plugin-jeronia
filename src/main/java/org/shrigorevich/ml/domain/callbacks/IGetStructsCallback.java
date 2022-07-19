package org.shrigorevich.ml.domain.callbacks;

import org.shrigorevich.ml.db.models.GetStructModel;

import java.util.List;

public interface IGetStructsCallback {
    void found(List<GetStructModel> structs);
}
