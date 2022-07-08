package org.shrigorevich.ml.db.callbacks;

public interface ICreateOneCallback<T> {

    void onQueryDone(T model, String msg);
}
