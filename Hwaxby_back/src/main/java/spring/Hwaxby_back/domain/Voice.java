package spring.Hwaxby_back.domain;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

@Getter @Setter
public class Voice {

    private Long id;

    private String data;

    private String text;

    private JsonObject textParsed;

    private VoiceType type;

}
