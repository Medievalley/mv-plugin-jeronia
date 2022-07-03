package org.shrigorevich.ml.db.callbacks;

public interface IFindOneCallback<T> {

    void onQueryDone(T model);
}
