package spring.Hwaxby_back.repository;

import org.springframework.stereotype.Repository;
import spring.Hwaxby_back.domain.Member;
import spring.Hwaxby_back.domain.Weather;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryWeatherRepository implements WeatherRepository {

    private static Map<Long, Weather> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Weather save(Weather weather) {
        weather.setId(++sequence);
        store.put(weather.getId(), weather);
        return weather;
    }

    @Override
    public Optional<Weather> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
