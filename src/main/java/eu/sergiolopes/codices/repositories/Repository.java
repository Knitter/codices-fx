package eu.sergiolopes.codices.repositories;

import java.util.List;

public interface Repository<T> {

    T find(int id);

    List<T> findAll();

    List<T> list(int page, int offset);

    boolean save(T obj);

    boolean delete(T obj);

}
