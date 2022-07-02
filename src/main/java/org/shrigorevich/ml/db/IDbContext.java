package org.shrigorevich.ml.db;

import java.util.List;

public interface IDbContext<T> {

    T getById(int id);

    List<T> getAll();

    void add(T entity);

    void remove(T entity);
}
