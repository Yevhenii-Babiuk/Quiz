package com.qucat.quiz.repositories.dao;

import java.util.List;

public interface GenericDao<T> {

    T get(int id);

    List<T> getAll();

    int save(T t);

    void update(T t);

    void delete(T t);

    void deleteById(int id);

}
