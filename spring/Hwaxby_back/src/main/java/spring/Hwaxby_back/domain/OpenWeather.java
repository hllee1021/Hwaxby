package spring.Hwaxby_back.domain;

import com.google.gson.JsonArray;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class OpenWeather {

    private List<Weather> weather;

    /** 내부 매개 변수 */
    private String base;

    private Main main;

    private Wind wind;

    private Clouds clouds;

    private Rain rain;

    private Snow snow;

    /** 가시성 */
    private int visibility;

    /** 데이터 계산 시간, 유닉스, UTC */
    private long dt;

    /** UTC에서 초 단위로 이동 */
    private int timezone;

    /** 도시 ID */
    private long id;

    /** 도시 이름 */
    private String name;

    /** 내부 매개 변수 */
    private int cod;

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
    public static class Main {

        /** 온도. 단위 기본값 : 켈빈, 미터법 : 섭씨, 임페리얼 : 화씨 */
        private float temp;

        /** 온도. 단위 기본값 : 켈빈, 미터법 : 섭씨, 임페리얼 : 화씨 */
        private float feels_like;

        /** 현재 최저 온도.(대규모 대도시 및 도시 지역 내) */
        private float temp_min;

        /** 현재 최대 온도.(대규모 대도시 및 도시 지역 내)*/
        private float temp_max;

        /** 대기압 (해수면, 해수면 또는 grnd_level 데이터가 없는 경우), hPa */
        private int pressure;

        /** 습도, % */
        private float humidity;

        /** 해수면의 대기압, hPa */
        private float sea_level;

        /** 지면에서의 대기압, hPa */
        private float grnd_level;
    }

    @Data
    public static class Wind {

        /** 바람의 속도. 단위 기본값 : meter/sec, 미터법 : meter/sec, 임페리얼 : miles/hour */
        private float speed;

        /** 풍향,도 (기상) */
        private int deg;

        /**  바람 돌풍. 단위 기본값 : meter/sec, 미터법 : meter/sec, 임페리얼 : miles/hour */
        private float gust;
    }

    @Data
    public static class Clouds {

        /** 흐림, % */
        private int all;
    }

    @Data
    public static class Rain {

        /** 지난 1 시간 동안의 강우량, mm */
        @JsonProperty("1h")
        private float rain1h;

        /** 지난 3 시간 동안의 강우량, mm */
        @JsonProperty("3h")
        private float rain3h;
    }

    @Data
    public static class Snow {

        /** 지난 1 시간 동안의 눈량, mm */
        @JsonProperty("1h")
        private float snow1h;

        /** 지난 3 시간 동안의 눈량, mm */
        @JsonProperty("3h")
        private float snow3h;
    }

    @Data
    public static class Sys {

        private int type;

        private int id;

        /** 국가 코드 (GB, JP 등) */
        private String country;

        /** 일출 시간, 유닉스, UTC */
        private long sunrise;

        /** 일몰 시간, 유닉스, UTC */
        private long sunset;
    }
}
