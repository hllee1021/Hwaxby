package spring.Hwaxby_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.Hwaxby_back.domain.Coordinates;
import spring.Hwaxby_back.repository.CoordRepository;

import java.util.Optional;

@Service
public class CoordService {

    private final CoordRepository coordRepository;

    @Autowired
    public CoordService(CoordRepository coordRepository) {
        this.coordRepository = coordRepository;
    }

    public Long save(Coordinates coordinates) {
        coordRepository.save(coordinates);
        return coordinates.getId();
    }

    public Optional<Coordinates> findOne(Long coordId) {
        return coordRepository.findById(coordId);
    }

}
