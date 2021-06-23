package spring.Hwaxby_back.repository;

import spring.Hwaxby_back.domain.Member;
import spring.Hwaxby_back.domain.Voice;

import java.util.Optional;

public interface VoiceRepository {
    //
    Voice save(Voice voice);
    Optional<Voice> findById(Long id);
}
