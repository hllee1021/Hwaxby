package spring.Hwaxby_back.repository;

import org.springframework.stereotype.Repository;
import spring.Hwaxby_back.domain.Coordinates;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryCoordRepository implements CoordRepository {

    private static Map<Long, Coordinates> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Coordinates save(Coordinates coordinates) {
        coordinates.setId(++sequence);
        store.put(coordinates.getId(), coordinates);
        return coordinates;
    }

    @Override
    public Optional<Coordinates> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public void clearStore() {
        store.clear();
    }

}
