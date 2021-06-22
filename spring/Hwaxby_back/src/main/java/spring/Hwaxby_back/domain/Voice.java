package spring.Hwaxby_back.domain;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Voice {

    private Long id;

    private byte[] data;

    private JsonObject text;

    private JsonObject textParsed;

    private VoiceType type;

    private Long weatherId;

    private String filePath;

}
