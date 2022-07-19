package org.shrigorevich.ml.domain.callbacks;

import org.shrigorevich.ml.db.models.GetStructModel;

import java.util.Optional;

public interface IFindStructCallback {

    void done(Optional<GetStructModel> m);
}
