package spring.Hwaxby_back.domain.OpenWeather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        private String dts;
        private String dow;

        /** sun & moon */
        private long sunrise;
        private String sunrises;
        private long sunset;
        private String sunsets;
        private long moonrise;
        private long moonset;
        private float moon_phase;

        /** 대기압 (해수면, 해수면 또는 grnd_level 데이터가 없는 경우), hPa */
        private int pressure;

        /** 습도, % */
        private int humidity;

        /** dew_point 단위 기본값 : 켈빈, 미터법 : 섭씨, 임페리얼 : 화씨 */
        private long dew_point;

        /** 바람의 속도. 단위 기본값 : meter/sec, 미터법 : meter/sec, 임페리얼 : miles/hour */
        private int wind_speed;

        /**  바람 돌풍. 단위 기본값 : meter/sec, 미터법 : meter/sec, 임페리얼 : miles/hour */
        private int wind_gust;

        /** 풍향,도 (기상) */
        private int wind_deg;

        /** 구름 % */
        private int clouds;

        /** UV index */
        private float uvi;

        /** Probability of precipitation */
        private float pop;

        /** 강우량 (비, 눈), mm */
        private int rain;
        private int snow;

        public void setDtsnDow() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd a");
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("h:d");
            SimpleDateFormat dateWeekFormat = new SimpleDateFormat("E");
            Date date = new Date();
            date.setTime(this.dt * 1000);
            this.dts = dateFormat.format(date);
            this.dow = dateWeekFormat.format(date);
            date = new Date();
            date.setTime(this.sunrise * 1000);
            this.sunrises =dateTimeFormat.format(date);
            date = new Date();
            date.setTime(this.sunset * 1000);
            this.sunsets =dateTimeFormat.format(date);
        }

    }

    @Data
    public static class Temp {
        /** 온도 */
        private int day;
        private int min;
        private int max;
        private int night;
        private int eve;
        private int morn;
    }

    @Data
    public static class FeelsLike {
        /** 체감온도 */
        private int day;
        private int night;
        private int eve;
        private int morn;
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
