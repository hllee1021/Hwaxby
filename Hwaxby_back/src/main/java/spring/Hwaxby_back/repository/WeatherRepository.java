package spring.Hwaxby_back.repository;

import spring.Hwaxby_back.domain.Member;
import spring.Hwaxby_back.domain.Weather;

import java.util.List;
import java.util.Optional;

public interface WeatherRepository {
    Weather save(Weather weather);
    Optional<Weather> findById(Long id);
//    Optional<Weather> findByName(String name);
//    List<Weather> findAll();
}