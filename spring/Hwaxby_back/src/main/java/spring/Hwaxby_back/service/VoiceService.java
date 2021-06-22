package spring.Hwaxby_back.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Properties;

import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import spring.Hwaxby_back.domain.Voice;
import spring.Hwaxby_back.util.PropertyFileReader;

@Service
public class VoiceService {

    public Voice voiceToText( String voiceFilePath ) throws Exception {
        System.out.println("start");
        Voice voice = new Voice();

        Properties prop = PropertyFileReader.readPropertyFile("api-key.properties");
        String openApiURL = prop.getProperty("voice.open.api.url");
        String accessKey = prop.getProperty("voice.open.api.accesskey");    // 발급받은 API Key
        String languageCode = "korean";     // 언어 코드
        String audioFilePath = new ClassPathResource(voiceFilePath).getFile().getAbsolutePath(); // 녹음된 음성 파일 경로
        String audioContents = null;

        Gson gson = new Gson();

        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();

        try {
            Path path = Paths.get(audioFilePath);
            byte[] audioBytes = Files.readAllBytes(path);
            audioContents = Base64.getEncoder().encodeToString(audioBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        argument.put("language_code", languageCode);
        argument.put("audio", audioContents);

        request.put("access_key", accessKey);
        request.put("argument", argument);

        URL url;
        Integer responseCode = null;
        String responBody = null;
        try {
            url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(request).getBytes("UTF-8"));
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
            InputStream is = con.getInputStream();
            byte[] buffer = new byte[is.available()];
            int byteRead = is.read(buffer);
            responBody = new String(buffer);


            voice.setFilePath(responBody);

            System.out.println("[responseCode] " + responseCode);
            System.out.println("[responBody]");
            System.out.println(responBody);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return voice;
    }
}
