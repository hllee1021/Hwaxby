package spring.Hwaxby_back.repository;

import spring.Hwaxby_back.domain.Display;

import java.util.Optional;

public interface WeatherRepository {
    Display save(Display display);
    Optional<Display> findById(Long id);
//    Optional<Weather> findByName(String name);
//    List<Weather> findAll();
}