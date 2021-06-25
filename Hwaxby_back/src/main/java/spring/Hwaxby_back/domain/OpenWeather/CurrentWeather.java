package spring.Hwaxby_back.domain.OpenWeather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CurrentWeather extends OpenWeather{

    /** 내부 매개 변수 */
    private Current current;

    @Data
    public static class Current{

        private List<Weather> weather;
        private Rain rain;
        private Snow snow;

        /** 데이터 계산 시간, 유닉스, UTC */
        private long dt;

        /** sun */
        private long sunrise;
        private long sunset;

        /** 온도. 단위 기본값 : 켈빈, 미터법 : 섭씨, 임페리얼 : 화씨 */
        private int temp;

        /** 체감 온도. 단위 기본값 : 켈빈, 미터법 : 섭씨, 임페리얼 : 화씨 */
        private int feels_like;

        /** 대기압 (해수면, 해수면 또는 grnd_level 데이터가 없는 경우), hPa */
        private int pressure;

        /** 습도, % */
        private int humidity;

        /** dew_point 단위 기본값 : 켈빈, 미터법 : 섭씨, 임페리얼 : 화씨 */
        private int dew_point;

        /** 구름 % */
        private int clouds;

        /** UV index */
        private float uvi;

        /** 가시성 */
        private int visibility;

        /** 바람의 속도. 단위 기본값 : meter/sec, 미터법 : meter/sec, 임페리얼 : miles/hour */
        private int wind_speed;

        /**  바람 돌풍. 단위 기본값 : meter/sec, 미터법 : meter/sec, 임페리얼 : miles/hour */
        private int wind_gust;

        /** 풍향,도 (기상) */
        private int wind_deg;
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

    @Data
    public static class Rain {

        /** 지난 1 시간 동안의 강우량, mm */
        @JsonProperty("1h")
        private long rain1h;
    }

    @Data
    public static class Snow {

        /** 지난 1 시간 동안의 눈량, mm */
        @JsonProperty("1h")
        private long snow1h;
    }
}
