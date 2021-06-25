package spring.Hwaxby_back.domain;

import lombok.Getter;
import lombok.Setter;
import spring.Hwaxby_back.domain.OpenWeather.OpenWeather;

@Getter @Setter
public class Response {

    private Long id;

    // 가공한 데이터
    private Voice voice; // Type : Response
    private Display display;
    private OpenWeather apiData;
    private OpenWeather currentApiData;
    private OpenWeather forecastApiData;
    private OpenWeatherType type;
    private int day;


//    // 제공받은 데이터
//    private Float lat;
//    private Float lon;
//    private String city;

}
