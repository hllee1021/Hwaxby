package spring.Hwaxby_back.repository;


import org.springframework.stereotype.Repository;
import spring.Hwaxby_back.domain.Member;
import spring.Hwaxby_back.domain.Voice;

import java.util.*;

@Repository
public class MemoryVoiceRepository implements VoiceRepository {

    private static Map<Long, Voice> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Voice save(Voice voice) {
        voice.setId(++sequence);
        store.put(voice.getId(), voice);
        return voice;
    }

    @Override
    public Optional<Voice> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public void clearStore() {
        store.clear();
    }
}
