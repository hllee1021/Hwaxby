package spring.Hwaxby_back.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonArray;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import spring.Hwaxby_back.domain.Coordinates;
import spring.Hwaxby_back.domain.OpenWeather.CurrentWeather;
import spring.Hwaxby_back.domain.OpenWeather.ForecastWeather;
import spring.Hwaxby_back.domain.OpenWeather.OpenWeather;
import spring.Hwaxby_back.domain.OpenWeatherType;
import spring.Hwaxby_back.util.PropertyFileReader;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

@Service
public class WeatherService {

    /**
     * Coordinates 정보를 기준으로 현재 날씨를 return 한다.
     *
     * @param lat
     * @param lon
     * @return OpenWeather Entity
     * @throws Exception
     */
    public OpenWeather getCurrentByCoor(OpenWeather response, OpenWeatherType type, double lat, double lon) throws Exception {

        String BASE_URL = "https://api.openweathermap.org/data/2.5/onecall";

        Properties prop = PropertyFileReader.readPropertyFile("api-key.properties");
        String apiKey = prop.getProperty("weather.api.accesskey");

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);

        try {
            if (type.equals(OpenWeatherType.CURRENT)){
                urlBuilder.append("?" + URLEncoder.encode("lat", "UTF-8") + "=" + lat);
                urlBuilder.append("&" + URLEncoder.encode("lon", "UTF-8") + "=" + lon);
                urlBuilder.append("&" + URLEncoder.encode("appid", "UTF-8") + "=" + apiKey);
                urlBuilder.append("&" + URLEncoder.encode("lang", "UTF-8") + "=kr");
                urlBuilder.append("&" + URLEncoder.encode("units", "UTF-8") + "=metric");
                urlBuilder.append("&" + URLEncoder.encode("exclude", "UTF-8") + "=minutely,hourly,alerts,daily");

                System.out.println(urlBuilder);
                RestTemplate restTemplate = new RestTemplate();
                response = restTemplate.getForObject(urlBuilder.toString(), CurrentWeather.class);
            } else if (type.equals(OpenWeatherType.FORECAST)){
                urlBuilder.append("?" + URLEncoder.encode("lat", "UTF-8") + "=" + lat);
                urlBuilder.append("&" + URLEncoder.encode("lon", "UTF-8") + "=" + lon);
                urlBuilder.append("&" + URLEncoder.encode("appid", "UTF-8") + "=" + apiKey);
                urlBuilder.append("&" + URLEncoder.encode("lang", "UTF-8") + "=kr");
                urlBuilder.append("&" + URLEncoder.encode("units", "UTF-8") + "=metric");
                urlBuilder.append("&" + URLEncoder.encode("exclude", "UTF-8") + "=minutely,hourly,alerts,current");

                System.out.println(urlBuilder);
                RestTemplate restTemplate = new RestTemplate();
                response = restTemplate.getForObject(urlBuilder.toString(), ForecastWeather.class);
            }


//            System.out.println("trial");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Coordinates geocoder(String city) throws Exception {
//        StringBuffer sb = new StringBuffer();

        Properties prop = PropertyFileReader.readPropertyFile("api-key.properties");
        String clientId = prop.getProperty("naver.api.accessID");
        String clientSecret = prop.getProperty("naver.api.accesskey");

        try{
            String addr = URLEncoder.encode(city, "utf-8");
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr;

            URL url = new URL(apiURL);
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            http.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            int responseCode = http.getResponseCode();

            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(http.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();

            System.out.println(response);

//            System.out.println("code:"+responseCode + ":" + response.toString());
//            InputStreamReader in = new InputStreamReader(http.getInputStream(), "utf-8");
//            String line;
//
//            while ((line = br.readLine()) != null){
//                sb.append(line).append("\n");
//            }

//            JSONParser parser = new JSONParser();
//            JSONPObject jsonObject1;
//            JSONPObject jsonObject2;
//            JsonArray jsonArray;
//            String x = "";
//            String y = "";
//
//            jsonObject1 = (JSONPObject) parser.parse(sb.toString());
//            jsonArray = (JsonArray) jsonObject1.get("addresses");
//            for (int i = 0 ; j < jsonArray.size() ; )
        } catch (IOException e){
            System.out.println(e);
        }
        return null;
    }

}

