package spring.Hwaxby_back.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class Script {

    private Long id;
    String script;

    HashMap<String,String> weather_map = new HashMap<String,String>(){{
        put("200", "가벼운 비를 동반한 천둥구름");
        put("201", "비를 동반한 천둥구름");
        put("202", "폭우를 동반한 천둥구름");
        put("210", "약한 천둥구름");
        put("211", "천둥구름");
        put("212", "강한 천둥구름");
        put("221", "불규칙적 천둥구름");
        put("230", "약한 연무를 동반한 천둥구름");
        put("231", "연무를 동반한 천둥구름");
        put("232", "강한 안개비를 동반한 천둥구름");

        put("300", "가벼운 안개비가 내려요");
        put("301", "안개비가 내려요");
        put("302", "강한 안개비가 내려요");
        put("310", "가벼운 적은비가 내려요");
        put("311", "적은비가 내려요");
        put("312", "강한 적은비가 내려요");
        put("313", "소나기와 안개비가 내려요");
        put("314", "강한 소나기와 안개비가 내려요");
        put("321", "소나기가 내려요");

        put("500", "약한 비가 내려요");
        put("501", "중간 비가 내려요");
        put("502", "강한 비가 내려요");
        put("503", "매우 강한 비가 내려요");
        put("504", "극심한 비가 내려요");
        put("511", "우박이 떨어져요");
        put("520", "약한 소나기 비가 내려요");
        put("521", "소나기 비가 내려요");
        put("522", "강한 소나기 비가 내려요");
        put("531", "불규칙적 소나기 비가 내려요");

        put("600", "가벼운 눈이 내려요");
        put("601", "눈이 내려요");
        put("602", "강한 눈이 내려요");
        put("611", "진눈깨비가 내려요");
        put("612", "소나기 진눈깨비가 내려요");
        put("615", "약한 비와 눈이 내려요");
        put("616", "비와 눈이 내려요");
        put("620", "약한 소나기 눈이 내려요");
        put("621", "소나기 눈이 내려요");
        put("622", "강한 소나기 눈이 내려요");

        put("701", "안개가 살짝 있어요");
        put("711", "하늘이 뿌옇게 보여요");
        put("721", "대기가 혼탁해요");
        put("731", "모래 먼지가 날려요");
        put("741", "안개가 있어요");
        put("751", "모래가 날려요");
        put("761", "먼지가 있어요");
        put("762", "화산재 날려요");
        put("771", "돌풍이 있어요");
        put("781", "토네이도가 발생했어요");

        put("800", "구름 한 점 없는 맑은 하늘입니다");
        put("801", "약간의 구름이 낀 하늘입니다");
        put("802", "드문드문 구름이 낀 하늘입니다");
        put("803", "구름이 거의 없는 하늘입니다");
        put("804", "구름으로 뒤덮인 흐린 하늘입니다");
        put("900", "토네이도가 발생했어요");
        put("901", "태풍이 발생했어요");
        put("902", "허리케인이 발생했어요");
        put("903", "많이 춥습니다");
        put("904", "많이 덥습니다");
        put("905", "바람이 있어요");
        put("906", "우박이 떨어져요");
        put("951", "바람이 거의 없어요");
        put("952", "약한 바람이 있어요");
        put("953", "부드러운 바람이 있어요");
        put("954", "중간 세기 바람이 있어요");
        put("955", "신선한 바람이 있어요");
        put("956", "센 바람이 있어요");
        put("957", "돌풍에 가까운 센 바람이 있어요");
        put("958", "돌풍이 있어요");
        put("959", "심각한 돌풍이 있어요");
        put("960", "폭풍이 발생했어요");
        put("961", "강한 폭풍이 발생했어요");
        put("962", "허리케인이 발생했어요");
    }};

    //  main_weather = new ObjectMapper().readValue(jsonObj.toJSONString(), Map.class) ; 활용한 코드 최적화 필요

}
