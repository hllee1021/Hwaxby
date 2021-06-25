package spring.Hwaxby_back.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.Hwaxby_back.domain.Coordinates;
import spring.Hwaxby_back.domain.OpenWeatherType;
import spring.Hwaxby_back.domain.Voice;
import spring.Hwaxby_back.domain.VoiceItem.TextParsed;
import spring.Hwaxby_back.domain.VoiceItem.VoiceType;
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

    public Long save(Voice voice) {
        voiceRepository.save(voice);
        return voice.getId();
    }

    public Optional<Voice> findOne(Long voiceId) {
        return voiceRepository.findById(voiceId);
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

    static public class Morpheme {
        final String text;
        final String type;
        public Morpheme (String text, String type) {
            this.text = text;
            this.type = type;
        }
    }

    static public class NameEntity {
        final String text;
        final String type;
        public NameEntity (String text, String type) {
            this.text = text;
            this.type = type;
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

        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();

        argument.put("analysis_code", analysisCode);
        argument.put("text", text);

        request.put("access_key", accessKey);
        request.put("argument", argument);

        URL url;
        Integer responseCode = null;
        String responBodyJson = null;
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

            responBodyJson = sb.toString();

            // http 요청 오류 시 처리
            if ( responseCode != 200 ) {
                // 오류 내용 출력
                System.out.println("[error] " + responBodyJson);
                return voice;
            }

            System.out.println("---");

            responeBody = gson.fromJson(responBodyJson, Map.class);
            Integer result = ((Double) responeBody.get("result")).intValue();
            Map<String, Object> returnObject;
            List<Map> sentences;

            // 분석 요청 오류 시 처리
            if ( result != 0 ) {
                // 오류 내용 출력
                System.out.println("[error] " + responeBody.get("result"));
                return voice;
            }

            // 분석 결과 활용
            returnObject = (Map<String, Object>) responeBody.get("return_object");
            sentences = (List<Map>) returnObject.get("sentence");

            System.out.println("Sentence---------");
            System.out.println(sentences);

            Map<String, Morpheme> morphemesMap = new HashMap<String, Morpheme>();
            Map<String, NameEntity> nameEntitiesMap = new HashMap<String, NameEntity>();
            List<Morpheme> morphemes = null;
            List<NameEntity> nameEntities = null;

            for(  Map<String, Object> sentence : sentences ) {
                // 형태소 분석기 결과 수집 및 정렬
                List<Map<String, Object>> morphologicalAnalysisResult = (List<Map<String, Object>>) sentence.get("morp");
                for( Map<String, Object> morphemeInfo : morphologicalAnalysisResult ) {
                    String lemma = (String) morphemeInfo.get("lemma");
                    Morpheme morpheme = morphemesMap.get(lemma);
                    if ( morpheme == null ) {
                        morpheme = new Morpheme(lemma, (String) morphemeInfo.get("type"));
                        morphemesMap.put(lemma, morpheme);
                    }
                }

                // 개체명 분석 결과 수집 및 정렬
                List<Map<String, Object>> nameEntityRecognitionResult = (List<Map<String, Object>>) sentence.get("NE");
                for( Map<String, Object> nameEntityInfo : nameEntityRecognitionResult ) {
                    String name = (String) nameEntityInfo.get("text");
                    NameEntity nameEntity = nameEntitiesMap.get(name);
                    if ( nameEntity == null ) {
                        nameEntity = new NameEntity(name, (String) nameEntityInfo.get("type"));
                        nameEntitiesMap.put(name, nameEntity);
                    }
                }

                // 자외선 단어 추출
                List<Map<String, Object>>  rayRecognitionResult = (List<Map<String, Object>>) sentence.get("word");
                for( Map<String, Object> rayInfo : rayRecognitionResult ) {
                    String name = (String) rayInfo.get("text");
                    Morpheme rayEntity = morphemesMap.get(name);
                    if (rayEntity == null && name.equals("자외선")) {
                        System.out.println("[ray]"+ name);
                        rayEntity = new Morpheme(name, "ray");
                        morphemesMap.put(name, rayEntity);
                    }
                }
            }

            String textParsedJson = null;
            ArrayList<String> infoList = new ArrayList<String>();
            TextParsed textParsed = new TextParsed();

            if ( 0 < morphemesMap.size() ) {
                morphemes = new ArrayList<Morpheme>(morphemesMap.values());
                System.out.println("morp");
                morphemes
                        .stream()
                        .filter(morpheme -> {
                            return morpheme.text.contains("습도") ||
                                    morpheme.text.contains("바람") ||
                                    morpheme.text.contains("온도") ||
                                    morpheme.text.contains("구름") ||
                                    morpheme.text.contains("자외선") ||
                                    morpheme.text.equals("비") ||
                                    morpheme.text.equals("눈") ||
                                    morpheme.text.equals("해");
                        })
                        .forEach(morpheme -> {
                            infoList.add(morpheme.text);


                        });
            }
            textParsedJson = gson.toJson(infoList);
            System.out.println("textParsedJson is ");
            System.out.println(textParsedJson);
            textParsed.setInfo(infoList);
            textParsed.setDay(-1);
            textParsed.setOpenWeatherType(OpenWeatherType.CURRENT);
            textParsed.setCity("HERE");

            if ( 0 < nameEntitiesMap.size() ) {
                nameEntities = new ArrayList<NameEntity>(nameEntitiesMap.values());
                System.out.println("here");
                nameEntities
                        .stream()
                        .filter(nameEntity -> {
                            return nameEntity.type.equals("LC_OTHERS") ||
                                    nameEntity.type.equals("LCP_COUNTRY") ||
                                    nameEntity.type.equals("LCP_PROVINCE") ||
                                    nameEntity.type.equals("LCP_COUNTY") ||
                                    nameEntity.type.equals("LCP_CITY") ||
                                    nameEntity.type.equals("LCP_CAPITALCITY") ||
                                    nameEntity.type.equals("LCG_RIVER") ||
                                    nameEntity.type.equals("LCG_OCEAN") ||
                                    nameEntity.type.equals("LCG_BAY") ||
                                    nameEntity.type.equals("LCG_MOUNTAIN") ||
                                    nameEntity.type.equals("LCG_ISLAND") ||
                                    nameEntity.type.equals("LCG_CONTINENT") ||
                                    nameEntity.type.equals("LC_TOUR");
                        })
                        .forEach(nameEntity -> {
                            textParsed.setCity(nameEntity.text);
                        });
            }

            // 인식된 개채명들 많이 노출된 순으로 출력 ( 최대 5개 )
//            System.out.println("");
//            nameEntities
//                    .stream()
//                    .forEach(nameEntity -> {
//                        System.out.println("[개체명] " + nameEntity.text );
//                    });

            if ( 0 < nameEntitiesMap.size() ) {
                nameEntities = new ArrayList<NameEntity>(nameEntitiesMap.values());
                System.out.println("here");
                nameEntities
                        .stream()
                        .filter(nameEntity -> {
                            return nameEntity.type.equals("DT_OTHERS") ||
                                    nameEntity.type.equals("DT_DAY");
                        })
                        .forEach(nameEntity -> {
                            if (nameEntity.type.equals("DT_OTHERS") && !nameEntity.text.contains("전")) {
                                if (nameEntity.text.contains("하루") || nameEntity.text.contains("1일") || nameEntity.text.contains("일일")) { textParsed.setDay(1);}
                                else if (nameEntity.text.contains("이틀") || nameEntity.text.contains("2일") || nameEntity.text.contains("이일")) { textParsed.setDay(2); }
                                else if (nameEntity.text.contains("사흘") || nameEntity.text.contains("3일") || nameEntity.text.contains("삼일")) { textParsed.setDay(3); }
                                else if (nameEntity.text.contains("나흘") || nameEntity.text.contains("4일") || nameEntity.text.contains("사일")) { textParsed.setDay(4); }
                                else if (nameEntity.text.contains("닷새") || nameEntity.text.contains("5일") || nameEntity.text.contains("오일")) { textParsed.setDay(5); }
                                else if (nameEntity.text.contains("엿새") || nameEntity.text.contains("6일") || nameEntity.text.contains("육일")) { textParsed.setDay(6); }
                                else if (nameEntity.text.contains("이레") || nameEntity.text.contains("7일") || nameEntity.text.contains("칠일")) { textParsed.setDay(7); }
                            } else  {
                                if (nameEntity.text.contains("오늘")) { textParsed.setDay(0); }
                                else if (nameEntity.text.contains("내일")) { textParsed.setDay(1);}
                                else if (nameEntity.text.contains("모레")) { textParsed.setDay(2);}
                                else if (nameEntity.text.contains("글피")) { textParsed.setDay(3);}
                                else if (nameEntity.text.contains("그글피")) { textParsed.setDay(4);}
                            }
                            textParsed.setOpenWeatherType(OpenWeatherType.FORECAST);
                        });
            }

            voice.setTextParsed(textParsed);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return voice;
    }
}