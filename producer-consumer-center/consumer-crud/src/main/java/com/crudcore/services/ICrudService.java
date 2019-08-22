package com.crudcore.services;

import java.util.List;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-24-11:10
 */
public interface ICrudService<T> {
    List<T> findAll();

    T findById(int tid);

    T save(T t);

    void deleteById(int tid);

    T modify(T t);
}
