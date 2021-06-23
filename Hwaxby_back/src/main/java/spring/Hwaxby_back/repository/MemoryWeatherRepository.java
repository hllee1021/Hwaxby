package spring.Hwaxby_back.repository;

import org.springframework.stereotype.Repository;
import spring.Hwaxby_back.domain.Display;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryWeatherRepository implements WeatherRepository {

    private static Map<Long, Display> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Display save(Display display) {
        display.setId(++sequence);
        store.put(display.getId(), display);
        return display;
    }

    @Override
    public Optional<Display> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
