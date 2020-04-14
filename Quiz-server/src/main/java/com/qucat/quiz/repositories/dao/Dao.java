package com.qucat.quiz.repositories.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    T get(int id);

    List<T> getAll();

    int save(T t);

    void update(T t);

    void delete(T t);

}
