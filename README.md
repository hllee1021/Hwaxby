# 화스비



### 21-1 YBIGTA Conference

팀원: 김연재 (18기), 이화영(17기), 김희진(17기), 이유림(17기), 정원선(17기), 이학림 (17기)

프로젝트 기간: 6/5 ~ 6/26

# 프로젝트 개요



![image](https://user-images.githubusercontent.com/35593748/156005075-4aaa0bbc-f08a-405a-8431-61f03dd1e562.png)

오늘의 날씨는! 대답은 화영이의 목소리로!

실시간으로 사용자의 위치정보를 확인해서 그에 맞는 기상정보를 제공함과 동시에 학회원의 목소리를 합성한 음성 대답도 같이 들려주는 실시간 날씨 알리미 프로젝트입니다.

React-Native, Spring, Glow-TTS, HiFi-GAN 을 활용했습니다.

# 프로젝트 기술 스택



- **Front-End**
    - react 17.0.1
    - react-native 0.64.2
    - typescript 4.3.4
    - [raect-native-sound](https://www.npmjs.com/package/react-native-sound) 0.11.0
    - [react-native-audio-record](https://www.npmjs.com/package/react-native-sound) 0.2.2
- **Back-End**
    - Spring Framework 5.1.18
    - Spring Boot 2.4.7
    - JDK 8
- **Model**
    - python 3.8
    - pytorch 1.7
    - Glow-TTS
    - HiFi-GAN
- **External API**
    - [OpenWeatherAPI](https://openweathermap.org/api)
    - [AIHub](https://aiopen.etri.re.kr/service_api.php)

# 프로젝트 아키텍처 & 플로우



![https://user-images.githubusercontent.com/69628269/155997290-bfef4c11-c3b8-4ed6-a76b-a63331bcdcab.png](https://user-images.githubusercontent.com/69628269/155997290-bfef4c11-c3b8-4ed6-a76b-a63331bcdcab.png)

### 1. Client (React-Native)

Client 단에서 사용자의 질문(음성파일)과 안드로이드 기기 위치 정보를 수집해 Server에 전송합니다.

### 2. Server (Spring Boot)

Client에서 받은 경도와 위도 정보를 토대로 어떤 지역인지 Naver Map API를 사용해 알아냅니다. 해당 지역의 날씨 정보를 OpenWeather API에 요청해서 받아옵니다.

사용자 음성파일을 AIHub API을 통해 문자열로 변환 시킨 후, 그에 맞는 대답을 생성합니다. 그 후, Model에 대답으로 사용할 문자열을 보내 목소리가 입혀진 음성 파일을 받습니다.

위의 작업을 모두 마친 후, 날씨 정보와 대답 음성 파일을 Client에 전송합니다.

### 3. Model

화자로 선정된 화영의 음성 데이터를 기반으로 Glow-TTS와 Hifi-GAN으로 학습시킨다.
Server에서 요청한 문자열을 미리 학습된 모델에 넣어 음성 데이터를 Byte 형식으로 반환한다. 반환한 음성 데이터를 server로 전송한다.

# Repository



- [Client](https://github.com/2hwayoung/Hwaxby/tree/main/Client/README.md)
- [Hwaxby_back](https://github.com/2hwayoung/Hwaxby/tree/main/Hwaxby_back/README.md)
- [Model](https://github.com/2hwayoung/Hwaxby/tree/main/Model/README.md)

# 프로젝트 결과물



21-1 YBIGTA Conference에서 발표했던 영상은 [Youtube](https://www.youtube.com/watch?v=-DDcqjeOwEs&list=PLIZ3mKAU9rah0SrBdKdzqZJYcCmEZH_-r&index=7)에 업로드되어 있습니다.
