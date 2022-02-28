package spring.Hwaxby_back.repository;


import org.springframework.stereotype.Repository;
import spring.Hwaxby_back.domain.Response;
import spring.Hwaxby_back.domain.Voice;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryResponseRepository implements ResponseRepository {
    private static Map<Long, Response> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Response save(Response response) {
        response.setId(++sequence);
        store.put(response.getId(), response);
        return response;
    }

    @Override
    public Optional<Response> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public void clearStore() {
        store.clear();
    }
}
