package repository;

import java.util.Optional;

public interface Repository<Id, T> {
    void save(T elem) throws Exception;

    boolean delete(T elem) throws Exception;
    T findById(Id id) throws Exception;
    Optional<T> deleteAt(int index);
    Optional<T> getOne(int index);

    boolean contains(T elem);
    void put(T elem) throws Exception;
    int size();

    Iterable<T> getAll() throws Exception;
}
