package spring.Hwaxby_back.domain.OpenWeather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ForecastWeather extends OpenWeather{

    /** 내부 매개 변수 */
    private List<Daily> daily;

    @Data
    public static class Daily{

        private List<Weather> weather;
        private Temp temp;
        private FeelsLike feels_like;

        /** 데이터 계산 시간, 유닉스, UTC */
        private long dt;

        /** sun & moon */
        private long sunrise;
        private long sunset;
        private long moonrise;
        private long moonset;
        private float moon_phase;

        /** 대기압 (해수면, 해수면 또는 grnd_level 데이터가 없는 경우), hPa */
        private int pressure;

        /** 습도, % */
        private float humidity;

        /** dew_point 단위 기본값 : 켈빈, 미터법 : 섭씨, 임페리얼 : 화씨 */
        private float dew_point;

        /** 바람의 속도. 단위 기본값 : meter/sec, 미터법 : meter/sec, 임페리얼 : miles/hour */
        private float wind_speed;

        /**  바람 돌풍. 단위 기본값 : meter/sec, 미터법 : meter/sec, 임페리얼 : miles/hour */
        private float wind_gust;

        /** 풍향,도 (기상) */
        private int wind_deg;

        /** 구름 % */
        private int clouds;

        /** UV index */
        private float uvi;

        /** Probability of precipitation */
        private float pop;

        /** 강우량 (비, 눈), mm */
        private long rain;
        private long snow;

    }

    @Data
    public static class Temp {
        /** 온도 */
        private float day;
        private float min;
        private float max;
        private float night;
        private float eve;
        private float morn;
    }

    @Data
    public static class FeelsLike {
        /** 체감온도 */
        private float day;
        private float night;
        private float eve;
        private float morn;
    }

    @Data
    public static class Weather {

        /** 기상 조건 ID */
        private int id;

        /** 날씨 매개 변수 그룹 (비, 눈, 극한 등) */
        private String main;

        /** 그룹 내 날씨 조건 */
        private String description;

        /** 날씨 아이콘 ID */
        private String icon;
    }
}
