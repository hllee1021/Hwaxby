# Hwaxby 화스비

P-TTS(개인화 음성합성 기술)을 활용한 실시간 날씨 알리미

팀장: 이유림(YBIGTA 17기), 이화영(YBIGTA 17기)

팀원: 김희진(YBIGTA 17기), 김연재(YBIGTA 18기), 정원선(YBIGTA 17기), 이학림(YBIGTA 17기)



---------------

실시간 날씨 알리미 프로젝트(Hwaxby)는 앱에서 실시간으로 사용자의 위치 정보를 확인하여 원하는 날씨 정보를 알려주는 기능에 원하는 보이스를 더하여 서비스를 제공하는 것을 목표로 합니다.

![image-20210618160021975](C:/Users/USER/AppData/Roaming/Typora/typora-user-images/image-20210618160021975.png)



먼저 open api인 weather api에서 실시간으로 날씨 데이터를 불러옵니다.

그리고 카프카를 이용해 데이터를 가져와서 전처리와 음성합성 모델링을 거친후 데이터베이스에 저장하고자 합니다.

여기서 음성합성모델은 대용량의 음성데이터에서 잡음제거나 오디오 슬라이싱같은 전처리를 한 후 음성-문자변환 스크립트를 생성하고

그 후 모델학습을 시킬 예정입니다. Mozilla나 nvidia에서 제공하는 모델을 활용할 계획인데 더 좋은 모델을 찾는다면 충분히 바뀔 수 잇겠죠~

그렇게 만들어진 결과물을 spring 프레임워크 기반의 웹에 띄우고 이 전체 플로우를 도커 컴포즈로 관리하고자 합니다.