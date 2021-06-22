package spring.Hwaxby_back.domain;

import com.google.gson.JsonArray;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Weather {

    private Long id;

    private OpenWeather apiData;

    private String displayData;

    private Float lat;
    private Float lon;

}
