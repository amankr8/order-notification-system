package com.flykraft.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<A, B> {
    Optional<B> findById(A id);

    B save(B entity);

    void deleteById(A id);
}
