package spring.Hwaxby_back.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Response {

    private Long id;

    // 가공한 데이터
    private Voice voice;
    private Weather apiData;

    // 제공받은 데이터
    private Float lat;
    private Float lon;
    private String city;

}
