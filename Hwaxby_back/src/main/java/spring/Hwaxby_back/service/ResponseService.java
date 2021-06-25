package spring.Hwaxby_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.Hwaxby_back.domain.Coordinates;
import spring.Hwaxby_back.domain.Response;
import spring.Hwaxby_back.repository.CoordRepository;
import spring.Hwaxby_back.repository.ResponseRepository;

import java.util.Optional;

@Service
public class ResponseService {

    private final ResponseRepository responseRepository;

    @Autowired
    public ResponseService(ResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    public Long save(Response response) {
        responseRepository.save(response);
        return response.getId();
    }

    public Optional<Response> findOne(Long respId) {
        return responseRepository.findById(respId);
    }

}
