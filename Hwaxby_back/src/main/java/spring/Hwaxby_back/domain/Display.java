package spring.Hwaxby_back.domain;

import com.google.gson.JsonArray;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.Getter;
import lombok.Setter;
import spring.Hwaxby_back.domain.OpenWeather.OpenWeather;

@Getter @Setter
public class Display {

    private Long id;

    private OpenWeather apiData;

    private String displayData;

    private Float lat;
    private Float lon;

}
