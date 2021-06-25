package spring.Hwaxby_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.Hwaxby_back.domain.Member;
import spring.Hwaxby_back.domain.Script;
import spring.Hwaxby_back.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ScriptService {

    public String begin(int weather_id, Script script) {

        String str = "오늘은 ";

        if (200 <= weather_id && weather_id < 300){
            str += ("하늘에 " + script.getWeather_map().get(Integer.toString(weather_id)) + "이 보여요.");
        } else if (300 <= weather_id && weather_id < 700){
            str += script.getWeather_map().get(Integer.toString(weather_id));
            str += " 우산 꼭 챙기세요.";
        }

        script.setScript(str);
        return str; // test 용으로 일단은 string return, void여도 된다.
    }

}
