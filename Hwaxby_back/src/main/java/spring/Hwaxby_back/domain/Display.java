package spring.Hwaxby_back.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonArray;
import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.Getter;
import lombok.Setter;
import spring.Hwaxby_back.domain.OpenWeather.OpenWeather;

import java.util.ArrayList;

@Getter @Setter
public class Display {

    private Long id;

    private DisplayINFO displayData;

    private ArrayList<String> Info;

    @Data
    public static class DisplayINFO{

        @JsonProperty("습도")
        private float humidity;

        @JsonProperty("바람")
        private Wind wind;

        @JsonProperty("온도")
        private Temp temp;

        @JsonProperty("구름")
        private int clouds;

        @JsonProperty("자외선")
        private float uvi;

        @JsonProperty("비")
        private Rain rain;

        @JsonProperty("눈")
        private Snow snow;

        @Data
        public static class Wind{
            /** 바람의 속도. 단위 기본값 : meter/sec, 미터법 : meter/sec, 임페리얼 : miles/hour */
            private float wind_speed;

            /**  바람 돌풍. 단위 기본값 : meter/sec, 미터법 : meter/sec, 임페리얼 : miles/hour */
            private float wind_gust;

            /** 풍향,도 (기상) */
            private int wind_deg;

            public Wind(float wind_speed, float wind_gust, int wind_deg) {
                this.wind_deg = wind_deg;
                this.wind_speed = wind_speed;
                this.wind_gust = wind_speed;
            }
        }

        @Data
        public static class Temp{
            /** 온도. 단위 기본값 : 켈빈, 미터법 : 섭씨, 임페리얼 : 화씨 */
            private float current_temp; // for CURRENT
            private float day;          // for FORECAST (6개 모두)
            private float min;
            private float max;
            private float night;
            private float eve;
            private float morn;

            /** 체감 온도. 단위 기본값 : 켈빈, 미터법 : 섭씨, 임페리얼 : 화씨 */
            private float feels_like_current; // for CURRENT
            private float feels_like_morn;    // for FORECAST (4개)
            private float feels_like_day;
            private float feels_like_eve;
            private float feels_like_night;

            public Temp(float temp, float feels_like) {
                this.current_temp = temp;
                this.feels_like_current = feels_like;
            }

            public Temp(float min, float max, float morn, float day, float eve, float night, float morn1, float day1, float eve1, float night1) {
                this.min = min;
                this.max = max;
                this.morn = morn;
                this.day = day;
                this.eve = eve;
                this.night = night;

                this.feels_like_morn = morn1;
                this.feels_like_day = day1;
                this.feels_like_eve = eve1;
                this.feels_like_night = night1;
            }
        }

        @Data
        public static class Rain{
            /** Probability of precipitation */
            private float pop;

            /** 강우량 (비), mm */
            private long rain;

            public Rain(long rain1h) {
                this.rain = rain1h;
            }
        }

        @Data
        public static class Snow{
            /** Probability of precipitation */
            private float pop;

            /** 강우량 (눈), mm */
            private long snow;

            public Snow(long snow1h) {
                this.snow = snow1h;
            }
        }
    }
}
