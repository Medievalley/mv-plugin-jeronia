package org.shrigorevich.ml.domain.callbacks;

import org.shrigorevich.ml.db.models.GetStructModel;

import java.util.Optional;

public interface ISaveStructCallback {

    void done(Optional<GetStructModel> m, boolean res, String msg);
//    void structFound(Optional<GetStructModel> m, boolean res);
}
