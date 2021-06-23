package spring.Hwaxby_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import spring.Hwaxby_back.domain.*;
import spring.Hwaxby_back.domain.OpenWeather.CurrentWeather;
import spring.Hwaxby_back.domain.OpenWeather.ForecastWeather;
import spring.Hwaxby_back.domain.OpenWeather.OpenWeather;
import spring.Hwaxby_back.service.WeatherService;

@Controller
public class ResponseController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("response")
    public ResponseEntity<?> getResponse(@RequestBody Ask askData) throws Exception {
        // [TEMP] tester
        System.out.println("here");
        Coordinates test_cor = new Coordinates();
        test_cor.setLat(36.504658); test_cor.setLon(129.44539);
        askData.setCoordinates(test_cor);
        OpenWeatherType type;
        type = OpenWeatherType.CURRENT;

        /** 0. Response 객체 생성 */
        Response response = new Response();

        /** 1. Ask-Voice-Text Tokenizing */

        /** 2. 1번 결과로부터 Keyword 추출 */

        response.setType(type);


        /** 3. 2번 결과로부터 Keyword에 대한 (Current, Forecast) 분기  -> OpenWeather API */
        OpenWeather api_result = new OpenWeather() {};

        if (type.equals(OpenWeatherType.CURRENT)){
            api_result = new CurrentWeather();
        } else if (type.equals(OpenWeatherType.FORECAST)){
            api_result = new ForecastWeather();
        }

        response.setApiData(weatherService.getCurrentByCoor(api_result, type, askData.getCoordinates().getLat(), askData.getCoordinates().getLon()));

        /** 4. Model Input 문장 생성 및 Model 요청 -> Voice(Type:Response) 객체 생성*/

        /** 5. Display 객체 생성 */
        Display display1 = new Display();

        /** 6. 최종적인 Response 객체 setting  */

        /** 7. return */
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("test")
    public String test() throws Exception {
        System.out.println("testing");
        weatherService.geocoder("영덕");
        return null;
    }
}
