package org.shrigorevich.ml.domain.callbacks;

public interface IFindOneCallback<T> {
    void onQueryDone(T model);
}
