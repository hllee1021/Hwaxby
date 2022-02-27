package spring.Hwaxby_back.domain;

import lombok.Getter;
import lombok.Setter;
import spring.Hwaxby_back.domain.VoiceItem.TextParsed;
import spring.Hwaxby_back.domain.VoiceItem.VoiceType;

@Getter @Setter
public class Voice {

    private Long id;

    private String data;

    private String text;

    private TextParsed textParsed;

    private VoiceType type;

}
