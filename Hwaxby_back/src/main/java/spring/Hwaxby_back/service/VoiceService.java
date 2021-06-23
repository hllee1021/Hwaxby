package spring.Hwaxby_back.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import spring.Hwaxby_back.domain.Voice;
import spring.Hwaxby_back.domain.VoiceType;
import spring.Hwaxby_back.repository.VoiceRepository;
import spring.Hwaxby_back.util.PropertyFileReader;

@Service
public class VoiceService {

    private final VoiceRepository voiceRepository;
    Properties prop = PropertyFileReader.readPropertyFile("api-key.properties");

    @Autowired
    public VoiceService(VoiceRepository voiceRepository) throws Exception {
        this.voiceRepository = voiceRepository;
    }

    public Voice voiceToText( Voice voice ) throws Exception {

        voice.setType(VoiceType.ASK);

        String openApiURL = prop.getProperty("voice.open.api.asr.url");
        String accessKey = prop.getProperty("voice.open.api.accesskey");    // 발급받은 API Key
        String languageCode = "korean";     // 언어 코드
        String audioContents = null;

        Gson gson = new Gson();

        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();

        audioContents = voice.getData();

        argument.put("language_code", languageCode);
        argument.put("audio", audioContents);

        request.put("access_key", accessKey);
        request.put("argument", argument);

        URL url;
        Integer responseCode = null;
        String responBody = null;
        String responseBody = null;

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

            responseBody = new JsonParser().parse(responBody).getAsJsonObject().get("return_object").getAsJsonObject().get("recognized").getAsString();
            voice.setText(responseBody);

            voiceRepository.save(voice);

            System.out.println("[responseCode] " + responseCode);
            System.out.println("[responBody]");
            System.out.println(responseBody);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return voice;
    }

    static public class NameEntity {
        final String text;
        final String type;
        Integer count;
        public NameEntity (String text, String type, Integer count) {
            this.text = text;
            this.type = type;
            this.count = count;
        }
    }

    public Voice voiceParsing( Voice voice ) {
        // 언어 분석 기술 문어/구어 중 한가지만 선택해 사용
        String openApiURL = prop.getProperty("voice.open.api.nlu-spoken.url");;
        String accessKey = prop.getProperty("voice.open.api.accesskey");    // 발급받은 API Key
        String analysisCode = "ner";   // 언어 분석 코드
        String text = "";           // 분석할 텍스트 데이터
        Gson gson = new Gson();

        text += voice.getText();
        
        text += "오늘 서울 날씨 어때";
        
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();

        argument.put("analysis_code", analysisCode);
        argument.put("text", text);

        request.put("access_key", accessKey);
        request.put("argument", argument);

        URL url;
        Integer responseCode = null;
        JsonObject responBodyJson = null;
        Map<String, Object> responeBody = null;

        try {
            url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(request).getBytes("UTF-8"));
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();

            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            responBodyJson = new JsonParser().parse(sb.toString()).getAsJsonObject();

            // http 요청 오류 시 처리
            if ( responseCode != 200 ) {
                // 오류 내용 출력
                System.out.println("[error] " + responBodyJson);
                voice.setTextParsed(responBodyJson);
                return voice;
            }
            System.out.println("---");
            JsonObject responBody = gson.fromJson(responBodyJson, JsonObject.class);

            System.out.println(responBody.get("result"));
            Integer result = responBody.get("result").getAsInt();
            JsonObject returnObject;
            List<JsonObject> sentences;

            // 분석 요청 오류 시 처리
            if ( result != 0 ) {
                // 오류 내용 출력
                System.out.println("[error] " + responBody.get("result"));
                voice.setTextParsed((JsonObject) responBody.get("result"));
                return voice;
            }

            // 분석 결과 활용
            returnObject = responBody.get("return_object").getAsJsonObject();

            sentences = (List<JsonObject>) returnObject.get("sentence");
            System.out.println("Sentence---------");
            System.out.println(sentences);

            Map<String, NameEntity> nameEntitiesMap = new HashMap<String, NameEntity>();
            List<NameEntity> nameEntities = null;

            for( JsonObject sentence : sentences ) {
                // 개체명 분석 결과 수집 및 정렬
                List<Map<String, Object>> nameEntityRecognitionResult = (List<Map<String, Object>>) sentence.get("NE");
                for( Map<String, Object> nameEntityInfo : nameEntityRecognitionResult ) {
                    String name = (String) nameEntityInfo.get("text");
                    NameEntity nameEntity = nameEntitiesMap.get(name);
                    if ( nameEntity == null ) {
                        nameEntity = new NameEntity(name, (String) nameEntityInfo.get("type"), 1);
                        nameEntitiesMap.put(name, nameEntity);
                    } else {
                        nameEntity.count = nameEntity.count + 1;
                    }
                }
            }

            if ( 0 < nameEntitiesMap.size() ) {
                nameEntities = new ArrayList<NameEntity>(nameEntitiesMap.values());
                nameEntities.sort( (nameEntity1, nameEntity2) -> {
                    return nameEntity2.count - nameEntity1.count;
                });
            }

            // 인식된 개채명들 많이 노출된 순으로 출력 ( 최대 5개 )
            System.out.println("");
            nameEntities
                    .stream()
                    .limit(5)
                    .forEach(nameEntity -> {
                        System.out.println("[개체명] " + nameEntity.text + " ("+nameEntity.count+")" );
                    });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return voice;
    }
}
