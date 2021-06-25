package spring.Hwaxby_back.repository;

import org.springframework.stereotype.Repository;
import spring.Hwaxby_back.domain.Response;
import spring.Hwaxby_back.domain.Voice;

import java.util.Optional;

public interface ResponseRepository {
    Response save(Response response);
    Optional<Response> findById(Long id);
}
