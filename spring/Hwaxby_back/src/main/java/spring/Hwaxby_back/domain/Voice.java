package spring.Hwaxby_back.domain;

import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Getter @Setter
public class Voice {

    private Long id;

    private byte[] data;

    private VoiceType type;

    private Long weatherId;

}
