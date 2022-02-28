package spring.Hwaxby_back.repository;

import spring.Hwaxby_back.domain.Coordinates;

import java.util.Optional;

public interface CoordRepository {
    Coordinates save(Coordinates coordinates);
    Optional<Coordinates> findById(Long id);
}
