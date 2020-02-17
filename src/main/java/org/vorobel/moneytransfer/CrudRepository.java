package org.vorobel.moneytransfer;

import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface CrudRepository<E, ID> {
    <S extends E> S save(@Valid @NotNull S entity);

    <S extends E> S update(@Valid @NotNull S entity);

    <S extends E> Iterable<S> saveAll(@Valid @NotNull Iterable<S> entities);

    Optional<E> findById(@NotNull ID id);

    boolean existsById(@NotNull ID id);

    Iterable<E> findAll();

    long count();

    void deleteById(@NotNull ID id);

    void delete(@NotNull E entity);

    void deleteAll(@NotNull Iterable<? extends E> entities);

    void deleteAll();
}