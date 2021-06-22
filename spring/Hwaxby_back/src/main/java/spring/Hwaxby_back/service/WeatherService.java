package spring.Hwaxby_back.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import spring.Hwaxby_back.domain.OpenWeather;
import spring.Hwaxby_back.util.PropertyFileReader;

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
    public OpenWeather getCurrentByCoor(float lat, float lon) throws Exception {

        String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

        Properties prop = PropertyFileReader.readPropertyFile("api-key.properties");
        String apiKey = prop.getProperty("weather.api.accesskey");

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        OpenWeather response = new OpenWeather();

        try {
            urlBuilder.append("?" + URLEncoder.encode("lat", "UTF-8") + "=" + lat);
            urlBuilder.append("&" + URLEncoder.encode("lon", "UTF-8") + "=" + lon);
            urlBuilder.append("&" + URLEncoder.encode("appid", "UTF-8") + "=" + apiKey);
            urlBuilder.append("&" + URLEncoder.encode("lang", "UTF-8") + "=kr");
            urlBuilder.append("&" + URLEncoder.encode("units", "UTF-8") + "=metric");

            System.out.println(urlBuilder);
            RestTemplate restTemplate = new RestTemplate();
            response = restTemplate.getForObject(urlBuilder.toString(), OpenWeather.class);

            System.out.println("trial");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


}

