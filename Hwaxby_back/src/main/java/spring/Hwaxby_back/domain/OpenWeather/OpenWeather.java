package spring.Hwaxby_back.domain.OpenWeather;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public abstract class OpenWeather {

    /** 위경도 */
    private float lat;
    private float lon;

    /** UTC에서 초 단위로 이동 */
    private String timezone;
    private int timezone_offset;

}
