package spring.Hwaxby_back.domain.VoiceItem;


import lombok.Getter;
import lombok.Setter;
import spring.Hwaxby_back.domain.OpenWeatherType;

import java.util.ArrayList;

@Getter @Setter
public class TextParsed {
    private String city;

    private OpenWeatherType openWeatherType;

    private int day;

    private ArrayList<String> Info; //json
}
