package spring.Hwaxby_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import spring.Hwaxby_back.domain.Display;
import spring.Hwaxby_back.service.WeatherService;

@Controller
public class ResponseController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("response")
    public ResponseEntity<?> getWeather(@RequestBody Display display) throws Exception {
        System.out.println("here");
        OpenWeather result = weatherService.getCurrentByCoor(display.getLat(), display.getLon());
        Display display1 = new Display();
        display1.setApiData(result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
