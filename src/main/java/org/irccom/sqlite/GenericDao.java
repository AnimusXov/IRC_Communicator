package org.irccom.sqlite;

import java.util.Collection;
import java.util.Optional;

public interface GenericDao<T, I> {

    Optional<I> getId();

    Optional<T> get(int id);

    Collection<T> getAll();

    Optional<I> save(T t);

    void update(T t);

    void delete(T t);
}
