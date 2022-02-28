# 백엔드 README.md

# 환경설정

### Version

- Spring Framework 5.1.18
- Spring Boot 2.4.7
- JDK 8
- 개발환경: JetBrain IntelliJ IDEA

# 웹 애플리케이션 계층 구조

<pre>
<code>
├── Controller
│   ├── ask-controller
│   └── response-controller
├── Domain
│   ├── voice
│   ├── VoiceItem
│   ├── ├── text-parsed
│   ├── ├── voice-type
│   ├── coordinates
│   ├── open-weather-type
│   ├── OpenWeather
│   ├── ├── current-weather
│   ├── ├── forecast-weather
│   ├── ├── open-weather
│   ├──  ask
│   ├── response
│   ├── script
│   └── display
├── Repository
│   ├── voice-respository
│   ├── coord-respository
│   ├── weather-respository
│   └── response-respository
└── Service
    ├── voice-service
    ├── coord-service
    ├── weather-service
    ├── script-service
    └── response-service
</code>
</pre>

# 아키텍처

<img width="706" alt="스크린샷 2022-02-28 오후 10 37 05" src="https://user-images.githubusercontent.com/69628269/155997290-bfef4c11-c3b8-4ed6-a76b-a63331bcdcab.png">

- AI API의 언어 분석 REST API을 통해 자연어 문장의 의미 이해
- Naver Open API에서 특정 도시의 위도, 경도 정보 요청
- OpenWeather API를 통해 위치에 따른 날씨 정보 요청
- Client와 API 통신하는 Backend 서버를 구축

# API

## [Naver Open API - GeoCoding API](https://www.ncloud.com/product/applicationService/maps)

- request: city name
- response: latitude, longitude

## [OpenWeather API](https://openweathermap.org/api)

- current weather - `current`
  - request : latitude, longitude
  - response : 습도, 바람, 온도, 구름, 자외선, 비, 눈
- 5 days/3hour forcast API - `daily`
  - request : latitude, longitude, day
  - response : 습도, 바람, 온도, 구름, 자외선, 비, 눈

## [Voice API](https://aiopen.etri.re.kr/)

- request: language_code(’korean’), audio
- response: text

## Client API

|   이름      |      Method    |  input  | output |
| :-----: | :----: | :----- | :--- |
| /ask| GET | {  voice: {data: String}, coordinates: { lat: double, lon: double}}| { voice: {  id: Long,  data: byte[],  text: String,  type: String}, coordinates: {  id: Long,  lon: double,  lat: double}}   |
| /response| GET | {voice: {id: Long},coordinates: {id: Long}}| {voice: {   id: Long,  data: byte[],  text: String,  type: String,  filePath: String}, type : String)} ,apiData: {   lat : double,  lon : double,  timezone : string,  timezone_offset : int, current or forecast: Object, display: Object} |
