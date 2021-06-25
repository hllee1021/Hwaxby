package spring.Hwaxby_back.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import spring.Hwaxby_back.domain.*;
import spring.Hwaxby_back.domain.OpenWeather.CurrentWeather;
import spring.Hwaxby_back.domain.OpenWeather.ForecastWeather;
import spring.Hwaxby_back.domain.OpenWeather.OpenWeather;
import spring.Hwaxby_back.domain.VoiceItem.TextParsed;
import spring.Hwaxby_back.service.CoordService;
import spring.Hwaxby_back.service.VoiceService;
import spring.Hwaxby_back.service.WeatherService;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class ResponseController {

    private final VoiceService voiceService;
    private final WeatherService weatherService;
    private final CoordService coordService;

    @Autowired
    public ResponseController(VoiceService voiceService, WeatherService weatherService, CoordService coordService) {
        this.voiceService = voiceService;
        this.weatherService = weatherService;
        this.coordService = coordService;
    }

    @PostMapping("response")
    public ResponseEntity<?> getResponse(@RequestBody Ask askData) throws Exception {

        System.out.println("/response API called");
        // [TEMP] tester
//        Coordinates test_cor = new Coordinates();
//        test_cor.setLat(36.504658); test_cor.setLon(129.44539);
//        askData.setCoordinates(test_cor);
//
//        Voice test_voice = new Voice();
//        test_voice.setId(0L);
//        test_voice.setText("화스비 서울에 비나 눈 와?");
//        askData.setVoice(test_voice);


        Optional<Coordinates> opcoor = coordService.findOne(askData.getCoordinates().getId()); // get Coordinates
        Coordinates coordinates = null;

        if (opcoor.isPresent()) {
            coordinates = opcoor.get();
        } else {
            System.out.println("There's no Coordinates entity match to id: "+ askData.getCoordinates().getId());
        }

        /** 0. Response 객체 생성 */
        Response response = new Response();

        /** 1. Ask-Voice-Text Tokenizing */
        Optional<Voice> opvoice = voiceService.findOne(askData.getVoice().getId()); // get Voice
        Voice voice = null;

        if (opvoice.isPresent()) {
            voice = opvoice.get();
            voice = voiceService.voiceParsing(voice);
        } else {
            System.out.println("There's no voice entity match to id: "+ askData.getVoice().getId());
        }


        /** 2. 1번 결과로부터 Keyword 추출 */
        // OpenWeatherType (CURRENT, FORECAST) 추출
        OpenWeatherType type;
        type = voice.getTextParsed().getOpenWeatherType();
        response.setType(type);
        response.setDay(voice.getTextParsed().getDay());

        // city(지역)이 주어진 경우 -> geocoder 함수 호출 -> Coordinates 추출
        if (!voice.getTextParsed().getCity().equals("HERE")){ // city가 HERE이 아니다 = 별도의 city 요청
            Coordinates city_coor = weatherService.geocoder(voice.getTextParsed().getCity());
            coordinates.setLat(city_coor.getLat());
            coordinates.setLon(city_coor.getLon());
        }


        /** 3. 2번 결과로부터 Keyword에 대한 (Current, Forecast) 분기  -> OpenWeather API */
        OpenWeather api_result = new OpenWeather() {};

        if (type.equals(OpenWeatherType.CURRENT)){
            api_result = new CurrentWeather();
        } else if (type.equals(OpenWeatherType.FORECAST)){
            api_result = new ForecastWeather();
        }

        response.setApiData(weatherService.getCurrentByCoor(api_result, type, coordinates.getLat(), coordinates.getLon()));

        /** 4. Model Input 문장 생성 및 Model 요청 -> Voice(Type:Response) 객체 생성*/
        Voice resvoice = new Voice();
        resvoice.setId(voice.getId());
        resvoice.setText(voice.getText());
        System.out.println(voice.getText());
        resvoice.setData(voice.getData());
        response.setVoice(resvoice);


        /** 5. Display 객체 생성 및 setting */
        Display display = new Display();
//        JSONObject data = new JSONObject();
        Display.DisplayINFO data = new Display.DisplayINFO();

        System.out.println("testtesttest");

        System.out.println(askData.getVoice().getId());
        System.out.println(voice.getId());
        System.out.println(response.getVoice().getId());
        ArrayList<String> needed_Info = voice.getTextParsed().getInfo();
        display.setInfo(needed_Info);

//        System.out.println(needed_Info.size());

        if (type.equals(OpenWeatherType.CURRENT)){
            CurrentWeather apiData = (CurrentWeather) response.getApiData();

            for (int i = 0 ; i < needed_Info.size() ; i++){
                if (needed_Info.get(i).equals("습도")){
                    data.setHumidity(apiData.getCurrent().getHumidity());
//                    data.put(needed_Info.get(i), apiData.getCurrent().getHumidity());
                } else if (needed_Info.get(i).equals("바람")){  // speed, gust, deg
                    System.out.println("바람");
                    data.setWind(new Display.DisplayINFO.Wind(
                            apiData.getCurrent().getWind_speed(),
                            apiData.getCurrent().getWind_gust(),
                            apiData.getCurrent().getWind_deg()
                    ));
                } else if (needed_Info.get(i).equals("온도")){
                    System.out.println("온도");
                    data.setTemp(new Display.DisplayINFO.Temp(
                            apiData.getCurrent().getTemp(),
                            apiData.getCurrent().getFeels_like()
                    ));
                } else if (needed_Info.get(i).equals("구름")){
                    System.out.println("구름");
                    data.setClouds(apiData.getCurrent().getClouds());
                } else if (needed_Info.get(i).equals("자외선")){
                    System.out.println("자외선");
                    data.setUvi(apiData.getCurrent().getUvi());
                } else if (needed_Info.get(i).equals("비")){
                    System.out.println("비");
                    data.setRain(new Display.DisplayINFO.Rain(
                        apiData.getCurrent().getRain().getRain1h()
                    ));
                } else if (needed_Info.get(i).equals("눈")){
                    System.out.println("눈");
                    data.setSnow(new Display.DisplayINFO.Snow(
                            apiData.getCurrent().getSnow().getSnow1h()
                    ));
                }
            }
            display.setDisplayData(data);

        } else if (type.equals(OpenWeatherType.FORECAST)){
            ForecastWeather apiData = (ForecastWeather) response.getApiData();

            System.out.println(voice.getTextParsed().getDay());

            for (int i = 0 ; i < needed_Info.size() ; i++){
                if (needed_Info.get(i).equals("습도")){
                    System.out.println("습도");
                    data.setHumidity(apiData.getDaily().get(voice.getTextParsed().getDay()).getHumidity());
                } else if (needed_Info.get(i).equals("바람")){  // speed, gust, deg
                    System.out.println("바람");
                    data.setWind(new Display.DisplayINFO.Wind(
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getWind_speed(),
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getWind_gust(),
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getWind_deg()
                    ));
                } else if (needed_Info.get(i).equals("온도")){
                    System.out.println("온도");
                    data.setTemp(new Display.DisplayINFO.Temp(
                            // 온도 관련 (Min, Max, Morn, Day, Eve, Night)
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getTemp().getMin(),
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getTemp().getMax(),
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getTemp().getMorn(),
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getTemp().getDay(),
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getTemp().getEve(),
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getTemp().getNight(),

                            // 체감 온도 관련 (Morn, Day, Eve, Night)
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getFeels_like().getMorn(),
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getFeels_like().getDay(),
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getFeels_like().getEve(),
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getFeels_like().getNight()
                    ));
                } else if (needed_Info.get(i).equals("구름")){
                    System.out.println("구름");
                    data.setClouds(apiData.getDaily().get(voice.getTextParsed().getDay()).getClouds());
                } else if (needed_Info.get(i).equals("자외선")){
                    System.out.println("자외선");
                    data.setUvi(apiData.getDaily().get(voice.getTextParsed().getDay()).getUvi());
                } else if (needed_Info.get(i).equals("비")){
                    System.out.println("비");
                    System.out.println(apiData.getDaily().get(voice.getTextParsed().getDay()).getRain());
                    data.setRain(new Display.DisplayINFO.Rain(
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getRain()
                    ));
                } else if (needed_Info.get(i).equals("눈")){
                    System.out.println("눈");
                    System.out.println(apiData.getDaily().get(voice.getTextParsed().getDay()).getSnow());
                    data.setSnow(new Display.DisplayINFO.Snow(
                            apiData.getDaily().get(voice.getTextParsed().getDay()).getSnow()
                    ));
                }
            }
            display.setDisplayData(data);
        }

        response.setDisplay(display);

        /** 6. return */
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("test")
    public ResponseEntity<?> tester(@RequestBody TextParsed askData) throws Exception {
        System.out.println("testing");
        Coordinates response = weatherService.geocoder(askData.getCity());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
