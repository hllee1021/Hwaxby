package spring.Hwaxby_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.Hwaxby_back.domain.Member;
import spring.Hwaxby_back.domain.OpenWeather.CurrentWeather;
import spring.Hwaxby_back.domain.OpenWeather.ForecastWeather;
import spring.Hwaxby_back.domain.Script;
import spring.Hwaxby_back.repository.MemberRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ScriptService {

    public String current_begin(int weather_id, Script script) {

        String str = "지금은 ";

        if (200 <= weather_id && weather_id < 300){
            str += ("하늘에 " + script.getWeather_map().get(Integer.toString(weather_id)) + "이 보여요.");
        } else if (300 <= weather_id && weather_id < 700){
            str += (script.getWeather_map().get(Integer.toString(weather_id)) + ".");
            str += " 우산 꼭 챙기세요.";
        }

        script.setScript(str);
        return str; // test 용으로 일단은 string return, void여도 된다.
    }

    public String current_temp(Script script, CurrentWeather currentWeather, ForecastWeather forecastWeather) {
        String str = script.getScript();

        str += ("현재 기온은 " + Integer.toString(currentWeather.getCurrent().getTemp()) + "도 이며,");
        str += ("오늘 최저 기온은 " + Integer.toString(forecastWeather.getDaily().get(0).getTemp().getMin()) + "도, ");
        str += ("최고 기온은 " + Integer.toString(forecastWeather.getDaily().get(0).getTemp().getMax()) + "도로 예상됩니다.");

        if (currentWeather.getCurrent().getFeels_like() >= 30){
            str += ("체감 온도는 " + Integer.toString(currentWeather.getCurrent().getFeels_like()) + "도로, 다소 더운 날씨입니다.");
        }

        script.setScript(str);
        return str;
    }

    public String forecast_begin(int weather_id, Script script, int day){

        HashMap<Integer, String> script_day = new HashMap<Integer, String>(){{
            put(0, "오늘은");
            put(1, "내일은");
            put(2, "모레는");
            put(3, "3일 뒤에는");
            put(4, "4일 뒤에는");
            put(5, "5일 뒤에는");
            put(6, "6일 뒤에는");
            put(7, "7일 뒤에는");
        }};
        String str = script_day.get(day);

        if (200 <= weather_id && weather_id < 300){
            str += (" 하늘에 " + script.getWeather_map().get(Integer.toString(weather_id)) + "이 보일 예정입니다.");
        } else if (300 <= weather_id && weather_id < 700){
            str += (" " + script.getWeather_map().get(Integer.toString(weather_id)) + ".");
            str += " 우산 꼭 챙기세요.";
        }

        script.setScript(str);
        return str; // test 용으로 일단은 string return, void여도 된다.
    }

    public String forecast_temp(Script script, CurrentWeather currentWeather, ForecastWeather forecastWeather, int day) {
        String str = script.getScript();

        str += ("최저 기온은 " + Integer.toString(forecastWeather.getDaily().get(day).getTemp().getMin()) + "도, ");
        str += ("최고 기온은 " + Integer.toString(forecastWeather.getDaily().get(day).getTemp().getMax()) + "도로 예상됩니다.");

        if (forecastWeather.getDaily().get(day).getFeels_like().getMorn() >= 30 &&
            forecastWeather.getDaily().get(day).getFeels_like().getDay() >= 30 &&
            forecastWeather.getDaily().get(day).getFeels_like().getEve() >= 30 &&
            forecastWeather.getDaily().get(day).getFeels_like().getNight() >= 30 ){
            str += ("체감 온도가 하루 종일 30도 이상으로, 다소 더운 날씨가 예상됩니다.");
        }

        script.setScript(str);
        return str;
    }

    public String specific_current(ArrayList<String> info, Script script, CurrentWeather currentWeather, ForecastWeather forecastWeather){
        String str = "현재 ";
        boolean dot = false;

        for (int i = 0 ; i < info.size() ; i++){
            if (info.get(i).equals("습도")){
                str += ("습도는 " + Integer.toString(currentWeather.getCurrent().getHumidity()) + "퍼센트");
            } else if (info.get(i).equals("바람")){
                str += ("바람은 초속" + Integer.toString(currentWeather.getCurrent().getWind_speed()) + "미터 퍼 세크");
            } else if (info.get(i).equals("온도")){
                str += ("현재 기온은 " + Integer.toString(currentWeather.getCurrent().getTemp()) + "도 이며,");
                str += ("오늘 최저 기온은 " + Integer.toString(forecastWeather.getDaily().get(0).getTemp().getMin()) + "도, ");
                str += ("최고 기온은 " + Integer.toString(forecastWeather.getDaily().get(0).getTemp().getMax()) + "도로 예상됩니다.");

                if (currentWeather.getCurrent().getFeels_like() >= 30){
                    str += ("체감 온도는 " + Integer.toString(currentWeather.getCurrent().getFeels_like()) + "도로, 다소 더운 날씨입니다.");
                }

                dot = true;
            } else if (info.get(i).equals("구름")){
                if (currentWeather.getCurrent().getClouds() >= 60){
                    str += "구름이 많이 끼어있어요.";
                } else {
                    str += "구름이 적어요.";
                }

                dot = true;
            } else if (info.get(i).equals("자외선")){
                if (currentWeather.getCurrent().getUvi() < 3.0){ // 매우 낮음
                    str += ("자외선 지수는 " + Float.toString(currentWeather.getCurrent().getUvi()) + "로, 매우 낮음");
                } else if (currentWeather.getCurrent().getUvi() < 5.0){ // 낮음
                    str += ("자외선 지수는 " + Float.toString(currentWeather.getCurrent().getUvi()) + "로, 낮음");
                } else if (currentWeather.getCurrent().getUvi() < 7.0){ // 보통
                    str += ("자외선 지수는 " + Float.toString(currentWeather.getCurrent().getUvi()) + "로, 보통");
                } else if (currentWeather.getCurrent().getUvi() < 9.0){ // 강함
                    str += ("자외선 지수는 " + Float.toString(currentWeather.getCurrent().getUvi()) + "로, 강함");
                } else {    // 매우 강함
                    str += ("자외선 지수는 " + Float.toString(currentWeather.getCurrent().getUvi()) + "로, 매우 강함");
                }
            } else if (info.get(i).equals("비")){
                if (currentWeather.getCurrent().getRain().getRain1h() == 0){
                    str += "비가 오지 않습니다.";
                } else {
                    str += ("비는 지난 한 시간 동안 " + Integer.toString(currentWeather.getCurrent().getRain().getRain1h()) + "밀리리터 내렸습니다.");
                }
                dot = true;
            } else if (info.get(i).equals("눈")){
                if (currentWeather.getCurrent().getSnow().getSnow1h() == 0){
                    str += "눈이 오지 않습니다.";
                } else {
                    str += ("눈은 지난 한 시간 동안 " + Integer.toString(currentWeather.getCurrent().getSnow().getSnow1h()) + "밀리리터 내렸습니다.");
                }
                dot = true;
            }


            if (i == info.size() - 1 && !dot){
                str += "입니다.";
            } else if (i == info.size() - 1 && dot){
                dot = false;
                // 아무 것도 더하지 않는다.
            } else if (dot){
                str += " 또한, ";
                dot = false;
            } else if (i % 2 == 1){
                str += "이고, ";
            } else {
                str += "이며, ";
            }
        }

        script.setScript(str);
        return str;
    }

    public String specific_forecast(ArrayList<String> info, Script script, CurrentWeather currentWeather, ForecastWeather forecastWeather, int day){
        HashMap<Integer, String> script_day = new HashMap<Integer, String>(){{
            put(0, "오늘, ");
            put(1, "내일, ");
            put(2, "모레, ");
            put(3, "3일 뒤, ");
            put(4, "4일 뒤, ");
            put(5, "5일 뒤, ");
            put(6, "6일 뒤, ");
            put(7, "7일 뒤, ");
        }};
        String str = script_day.get(day);

        boolean dot = false;

        for (int i = 0 ; i < info.size() ; i++){
            if (info.get(i).equals("습도")){
                str += ("습도는 " + Integer.toString(forecastWeather.getDaily().get(day).getHumidity()) + "퍼센트");
            } else if (info.get(i).equals("바람")){
                str += ("바람은 초속" + Integer.toString(forecastWeather.getDaily().get(day).getWind_speed()) + "미터 퍼 세크");
            } else if (info.get(i).equals("온도")){
                str += ("최저 기온은 " + Integer.toString(forecastWeather.getDaily().get(day).getTemp().getMin()) + "도, ");
                str += ("최고 기온은 " + Integer.toString(forecastWeather.getDaily().get(day).getTemp().getMax()) + "도로 예상됩니다.");

                if (forecastWeather.getDaily().get(day).getFeels_like().getMorn() >= 30 &&
                        forecastWeather.getDaily().get(day).getFeels_like().getDay() >= 30 &&
                        forecastWeather.getDaily().get(day).getFeels_like().getEve() >= 30 &&
                        forecastWeather.getDaily().get(day).getFeels_like().getNight() >= 30){
                    str += ("체감 온도가 하루 종일 30도 이상으로, 다소 더운 날씨가 예상됩니다.");
                }

                dot = true;
            } else if (info.get(i).equals("구름")){
                if (forecastWeather.getDaily().get(day).getClouds() >= 60){
                    str += "하늘에는 구름이 많겠습니다.";
                } else {
                    str += "하늘에는 구름이 적겠습니다.";
                }

                dot = true;
            } else if (info.get(i).equals("자외선")){
                if (forecastWeather.getDaily().get(day).getUvi() < 3.0){ // 매우 낮음
                    str += ("자외선 지수는 " + Float.toString(forecastWeather.getDaily().get(day).getUvi()) + "로, 매우 낮음");
                } else if (forecastWeather.getDaily().get(day).getUvi() < 5.0){ // 낮음
                    str += ("자외선 지수는 " + Float.toString(forecastWeather.getDaily().get(day).getUvi()) + "로, 낮음");
                } else if (forecastWeather.getDaily().get(day).getUvi() < 7.0){ // 보통
                    str += ("자외선 지수는 " + Float.toString(forecastWeather.getDaily().get(day).getUvi()) + "로, 보통");
                } else if (forecastWeather.getDaily().get(day).getUvi() < 9.0){ // 강함
                    str += ("자외선 지수는 " + Float.toString(forecastWeather.getDaily().get(day).getUvi()) + "로, 강함");
                } else {    // 매우 강함
                    str += ("자외선 지수는 " + Float.toString(forecastWeather.getDaily().get(day).getUvi()) + "로, 매우 강함");
                }
            } else if (info.get(i).equals("비")){
                if (forecastWeather.getDaily().get(day).getRain() == 0){
                    str += "예보에 비가 없습니다.";
                } else {
                    str += ("비는 " + Integer.toString(100 * (int)forecastWeather.getDaily().get(day).getPop() ) + "퍼센트의 확률로, "
                            + Integer.toString(forecastWeather.getDaily().get(day).getRain()) + "밀리리터 예보되어 있습니다.");
                }
                dot = true;
            } else if (info.get(i).equals("눈")){
                if (forecastWeather.getDaily().get(day).getSnow() == 0){
                    str += "예보에 눈이 없습니다.";
                } else {
                    str += ("눈은 " + Integer.toString(100 * (int)forecastWeather.getDaily().get(day).getPop() ) + "퍼센트의 확률로, "
                            + Integer.toString(forecastWeather.getDaily().get(day).getSnow()) + "밀리리터 예보되어 있습니다.");
                }
                dot = true;
            }


            if (i == info.size() - 1 && !dot){
                str += "일 것으로 예상됩니다.";
            } else if (i == info.size() - 1 && dot){
                dot = false;
                // 아무 것도 더하지 않는다.
            } else if (dot){
                str += " 또한, ";
                dot = false;
            } else if (i % 2 == 1){
                str += "이고, ";
            } else {
                str += "이며, ";
            }
        }

        script.setScript(str);
        return str;
    }
}
