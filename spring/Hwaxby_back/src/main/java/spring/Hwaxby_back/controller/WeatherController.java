package spring.Hwaxby_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import spring.Hwaxby_back.domain.OpenWeather;
import spring.Hwaxby_back.domain.Weather;
import spring.Hwaxby_back.service.WeatherService;

@Controller
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("response")
    public ResponseEntity<?> getWeather(@RequestBody Weather weather) throws Exception {
        System.out.println("here");
        OpenWeather result = weatherService.getCurrentByCoor(weather.getLat(), weather.getLon());
        Weather weather1 = new Weather();
        weather1.setApiData(result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
