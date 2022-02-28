# __TTS(Text To Speech) Model__

## __Glow-TTS & Hifi-GAN__

### __Glow-TTS__

![glow-tts](https://user-images.githubusercontent.com/59776953/155987243-231892f6-7604-4c8d-a6ad-a7cd150f8ac9.png)

- 텍스트를 Mel Spectrogram으로 변환하는 Encoder, Decoder 과정
- 플로우 기반 생성 모델과 동적 프로그래밍 속성을 활용한 정렬 모델이 필요 없는 빠른 합성
- Tacotron2와 비슷한 품질의 음성을 약 15배 더 빠르게 합성


### Hifi-GAN
![hifi-gan](https://user-images.githubusercontent.com/59776953/155987289-cb628410-ac87-4b1c-a903-23eae8e0c576.jpeg)
- Mel Spectrogram으로부터 음성을 합성하는 Vocoder 과정
- GAN 모델의 경우, 합성한 음성의 품질이 다소 떨어지지만 속도와 파라미터 개수 부분에서 부분적으로 개선
- HiFi-GAN은 높은 MOS값 구현

### Datasets
 - 2시간 30분 분량의 음성 데이터
 - 음성 데이터를 전사한 script

### Prerequisites
- [Requirements.txt](https://github.com/sce-tts/TTS/blob/sce-tts/requirements.txt) 

### Train a model
<img width="773" alt="model-architecture" src="https://user-images.githubusercontent.com/59776953/155987786-bd9f81a1-220f-4fd0-bba0-f021441efa18.png">

### Results
- [synthesize audio](https://github.com/2hwayoung/Hwaxby/blob/main/Model/output.wav)

### References
- [SCE-TTS : 내 목소리로 TTS 만들기](https://github.com/sce-tts/sce-tts.github.io)
