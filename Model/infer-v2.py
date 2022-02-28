# from google.colab import drive
# drive.mount('/content/drive')

import os
import sys
from pathlib import Path
sys.path.append("/mnt/c/Users/USER/git/Hwaxby/Model/g2pk/")
sys.path.append("/mnt/c/Users/USER/git/Hwaxby/Model/TTS/")
# %cd /content/drive/My Drive/Conference
# !git clone --depth 1 https://github.com/sce-tts/TTS.git -b sce-tts
# !git clone --depth 1 https://github.com/sce-tts/g2pK.git
# %cd /content/drive/My Drive/Conference/TTS
# !pip install -q --no-cache-dir -e .
# %cd /content/drive/My Drive/Conference/g2pK
# !pip install -q --no-cache-dir "konlpy" "jamo" "nltk" "python-mecab-ko"
# !pip install -q --no-cache-dir -e .

# %cd /content/drive/My Drive/Conference/g2pK
import g2pk
g2p = g2pk.G2p()

import re
from unicodedata import normalize
# import IPython

from TTS.utils.synthesizer import Synthesizer

def normalize_text(text):
    text = text.strip()

    for c in ",;:":
        text = text.replace(c, ".")
    text = remove_duplicated_punctuations(text)

    text = jamo_text(text)

    text = g2p.idioms(text)
    text = g2pk.english.convert_eng(text, g2p.cmu)
    text = g2pk.utils.annotate(text, g2p.mecab)
    text = g2pk.numerals.convert_num(text)
    text = re.sub("/[PJEB]", "", text)

    text = alphabet_text(text)

    # remove unreadable characters
    text = normalize("NFD", text)
    text = "".join(c for c in text if c in symbols)
    text = normalize("NFC", text)

    text = text.strip()
    if len(text) == 0:
        return ""

    # only single punctuation
    if text in '.!?':
        return punctuation_text(text)

    # append punctuation if there is no punctuation at the end of the text
    if text[-1] not in '.!?':
        text += '.'

    return text


def remove_duplicated_punctuations(text):
    text = re.sub(r"[.?!]+\?", "?", text)
    text = re.sub(r"[.?!]+!", "!", text)
    text = re.sub(r"[.?!]+\.", ".", text)
    return text


def split_text(text):
    text = remove_duplicated_punctuations(text)

    texts = []
    for subtext in re.findall(r'[^.!?\n]*[.!?\n]', text):
        texts.append(subtext.strip())

    return texts


def alphabet_text(text):
    text = re.sub(r"(a|A)", "에이", text)
    text = re.sub(r"(b|B)", "비", text)
    text = re.sub(r"(c|C)", "씨", text)
    text = re.sub(r"(d|D)", "디", text)
    text = re.sub(r"(e|E)", "이", text)
    text = re.sub(r"(f|F)", "에프", text)
    text = re.sub(r"(g|G)", "쥐", text)
    text = re.sub(r"(h|H)", "에이치", text)
    text = re.sub(r"(i|I)", "아이", text)
    text = re.sub(r"(j|J)", "제이", text)
    text = re.sub(r"(k|K)", "케이", text)
    text = re.sub(r"(l|L)", "엘", text)
    text = re.sub(r"(m|M)", "엠", text)
    text = re.sub(r"(n|N)", "엔", text)
    text = re.sub(r"(o|O)", "오", text)
    text = re.sub(r"(p|P)", "피", text)
    text = re.sub(r"(q|Q)", "큐", text)
    text = re.sub(r"(r|R)", "알", text)
    text = re.sub(r"(s|S)", "에스", text)
    text = re.sub(r"(t|T)", "티", text)
    text = re.sub(r"(u|U)", "유", text)
    text = re.sub(r"(v|V)", "브이", text)
    text = re.sub(r"(w|W)", "더블유", text)
    text = re.sub(r"(x|X)", "엑스", text)
    text = re.sub(r"(y|Y)", "와이", text)
    text = re.sub(r"(z|Z)", "지", text)
    text = re.sub(r"0", "영", text)
    text = re.sub(r"1", "일", text)
    text = re.sub(r"2", "이", text)
    text = re.sub(r"3", "삼", text)
    text = re.sub(r"4", "사", text)
    text = re.sub(r"5", "오", text)
    text = re.sub(r"6", "육", text)
    text = re.sub(r"7", "칠", text)
    text = re.sub(r"8", "팔", text)
    text = re.sub(r"9", "구", text)

    return text


def punctuation_text(text):
    # 문장부호
    text = re.sub(r"!", "느낌표", text)
    text = re.sub(r"\?", "물음표", text)
    text = re.sub(r"\.", "마침표", text)

    return text


def jamo_text(text):
    # 기본 자모음
    text = re.sub(r"ㄱ", "기역", text)
    text = re.sub(r"ㄴ", "니은", text)
    text = re.sub(r"ㄷ", "디귿", text)
    text = re.sub(r"ㄹ", "리을", text)
    text = re.sub(r"ㅁ", "미음", text)
    text = re.sub(r"ㅂ", "비읍", text)
    text = re.sub(r"ㅅ", "시옷", text)
    text = re.sub(r"ㅇ", "이응", text)
    text = re.sub(r"ㅈ", "지읒", text)
    text = re.sub(r"ㅊ", "치읓", text)
    text = re.sub(r"ㅋ", "키읔", text)
    text = re.sub(r"ㅌ", "티읕", text)
    text = re.sub(r"ㅍ", "피읖", text)
    text = re.sub(r"ㅎ", "히읗", text)
    text = re.sub(r"ㄲ", "쌍기역", text)
    text = re.sub(r"ㄸ", "쌍디귿", text)
    text = re.sub(r"ㅃ", "쌍비읍", text)
    text = re.sub(r"ㅆ", "쌍시옷", text)
    text = re.sub(r"ㅉ", "쌍지읒", text)
    text = re.sub(r"ㄳ", "기역시옷", text)
    text = re.sub(r"ㄵ", "니은지읒", text)
    text = re.sub(r"ㄶ", "니은히읗", text)
    text = re.sub(r"ㄺ", "리을기역", text)
    text = re.sub(r"ㄻ", "리을미음", text)
    text = re.sub(r"ㄼ", "리을비읍", text)
    text = re.sub(r"ㄽ", "리을시옷", text)
    text = re.sub(r"ㄾ", "리을티읕", text)
    text = re.sub(r"ㄿ", "리을피읍", text)
    text = re.sub(r"ㅀ", "리을히읗", text)
    text = re.sub(r"ㅄ", "비읍시옷", text)
    text = re.sub(r"ㅏ", "아", text)
    text = re.sub(r"ㅑ", "야", text)
    text = re.sub(r"ㅓ", "어", text)
    text = re.sub(r"ㅕ", "여", text)
    text = re.sub(r"ㅗ", "오", text)
    text = re.sub(r"ㅛ", "요", text)
    text = re.sub(r"ㅜ", "우", text)
    text = re.sub(r"ㅠ", "유", text)
    text = re.sub(r"ㅡ", "으", text)
    text = re.sub(r"ㅣ", "이", text)
    text = re.sub(r"ㅐ", "애", text)
    text = re.sub(r"ㅒ", "얘", text)
    text = re.sub(r"ㅔ", "에", text)
    text = re.sub(r"ㅖ", "예", text)
    text = re.sub(r"ㅘ", "와", text)
    text = re.sub(r"ㅙ", "왜", text)
    text = re.sub(r"ㅚ", "외", text)
    text = re.sub(r"ㅝ", "워", text)
    text = re.sub(r"ㅞ", "웨", text)
    text = re.sub(r"ㅟ", "위", text)
    text = re.sub(r"ㅢ", "의", text)

    return text


def normalize_multiline_text(long_text):
    texts = split_text(long_text)
    normalized_texts = [normalize_text(text).strip() for text in texts]
    return [text for text in normalized_texts if len(text) > 0]

def synthesize(text):
    wavs = synthesizer.tts(text, None, None)
    return wavs

synthesizer = Synthesizer(
    "/mnt/c/Users/USER/git/Hwaxby/Model/glowtts-v2/checkpoint_37000.pth.tar",
    "/mnt/c/Users/USER/git/Hwaxby/Model/glowtts-v2/config.json",
    None,
    "/mnt/c/Users/USER/git/Hwaxby/Model/hifigan-v2/checkpoint_295000.pth.tar",
    "/mnt/c/Users/USER/git/Hwaxby/Model/hifigan-v2/config.json",
    None,
    None,
    False,
)
symbols = synthesizer.tts_config.characters.characters

from scipy.io import wavfile
import numpy as np
from base64 import b64encode

texts = str(sys.argv[1])
# texts = """안 촉촉촉한 초코칩 
# 연재 바보래요
# """

print(texts)

samplerate = 22050; fs = 100
t = np.linspace(0., 1., samplerate) 
amplitude = np.iinfo(np.int16).max
data = amplitude * np.sin(2. * np.pi * fs * t)
print("Data")
print(data)
print(type(data))
import json
import librosa
import soundfile as sf
result = ""

# for text in normalize_multiline_text(texts):
# print(text)
wav = synthesizer.tts(texts, None, None)
# wavjson = json.dumps(wav)
# basewav = b64encode(wavjson)
# print(basewav)
print(type(wav))
# print(wav)
sr = 22050
# wav, sr = librosa.load(librosa.util.example_audio_file(),duration=5.0)
# librosa.output.write_wav('/mnt/c/Users/USER/git/Hwaxby/Model/file_trim_5s.wav', wav, sr)
sf.write('/mnt/c/Users/USER/git/Hwaxby/Model/output.wav', wav, sr, format='WAV', endian='LITTLE', subtype='PCM_16')
# wavfile.write("/mnt/c/Users/USER/git/Hwaxby/Model/output", samplerate, data.astype(np.int16))
# wavfile.write("/mnt/c/Users/USER/git/Hwaxby/Model/output", samplerate, wav.astype(np.int16))
# wav.export("/mnt/c/Users/USER/git/Hwaxby/Model/output", format="wav")
# IPython.display.display(IPython.display.Audio(wav, rate=22050)) 
# with open("/content/drive/My Drive/Conference/final_output", "wb") as f:
#   f.write(bytes(wav))

with open("/mnt/c/Users/USER/git/Hwaxby/Model/output.wav",mode="rb") as f:
    # print(f)
    enc=b64encode(f.read())
#   print(enc)

with open("/mnt/c/Users/USER/git/Hwaxby/Model/output", mode="wb") as bf:
    bf.write(enc)
