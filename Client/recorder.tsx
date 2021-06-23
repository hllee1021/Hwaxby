/* eslint-disable prettier/prettier */
import AudioRecorderPlayer, {
  AVEncoderAudioQualityIOSType,
  AVEncodingOption,
  AudioEncoderAndroidType,
  AudioSet,
  AudioSourceAndroidType,
  PlayBackType,
  RecordBackType,
} from 'react-native-audio-recorder-player';
import {
  Dimensions,
  PermissionsAndroid,
  Platform,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  TouchableHighlight,
} from 'react-native';
import React, {Component, useState} from 'react';
import styles from './styles';
import RNFetchBlob from 'rn-fetch-blob';
import axios from 'axios';
import fs from 'react-native-fs';
import Geolocation from '@react-native-community/geolocation';

interface State {
  isLoggingIn: boolean;
  recordSecs: number;
  recordTime: string;
  currentPositionSec: number;
  currentDurationSec: number;
  playTime: string;
  duration: string;
  //입력된 음성
  recordVoice: string;
  //대답 음성
  answerVoice: string;
}

const screenWidth = Dimensions.get('screen').width;

class Recorder extends Component<any, State> {
  private dirs = RNFetchBlob.fs.dirs;
  private path = Platform.select({
    ios: 'hello.m4a',
    android: `${this.dirs.CacheDir}/hello.mp3`,
    // android: `${this.dirs.CacheDir}/hello.mp3`,
  });
  private audioRecorderPlayer: AudioRecorderPlayer;

  constructor(props: any) {
    super(props);
    this.state = {
      isLoggingIn: false,
      recordSecs: 0,
      recordTime: '00:00:00',
      currentPositionSec: 0,
      currentDurationSec: 0,
      playTime: '00:00:00',
      duration: '00:00:00',
      recordVoice: 'not yet',
      answerVoice: 'not yet',
    };

    this.audioRecorderPlayer = new AudioRecorderPlayer();
    this.audioRecorderPlayer.setSubscriptionDuration(0.1); // optional. Default is 0.5
  }

  public render() {
    let playWidth =
      (this.state.currentPositionSec / this.state.currentDurationSec) *
      (screenWidth - 56);

    if (!playWidth) {
      playWidth = 0;
    }
  
  // api에 음성파일 제출
  // const [askResponse, setAskResponse] = useState('not yet');
  const ask = async() => {
    console.log('ask start');
    let voicePath : string = this.path!;
    console.log(voicePath);
    let myVoice = await fs.readFile(voicePath, 'base64');
    let lat;
    let lon;
    let askResponse;
    let resResponse;
    Geolocation.getCurrentPosition( async({ coords }) => {
      lat = coords.latitude;
      lon = coords.longitude;
      console.log(lat);
      console.log(lon);
      // console.log(myVoice);
      askResponse = await axios.get(
        'http://14.45.41.233:8080/ask',
        {params:
          {voice: {data : myVoice},
          coordinates: {lat: lat, lon: lon}},
      });
      resResponse = await axios.get(
        'http://14.45.41.233:8080/response',
        {params:
          {voice: {id : askResponse.data.voice.id},
          coordinates : {id : askResponse.data.coordinates.id}},
      });
      console.log('done');
    });
    // let askResponse = await axios.get('http://10.0.2.2:8080/ask', {params: {data: myVoice, lat: lat, lon: lon }});
  };

    return (
      <View style = {styles.bottom}>
        <Text>{this.state.recordVoice}</Text>
        <TouchableHighlight
          style={styles.button}
          onPress={this.onStartRecord}>
          <Text>Click to talk</Text>
        </TouchableHighlight>
        <TouchableHighlight
          style={[styles.button]}
          onPress={this.onStopRecord}>
          <Text>Click to stop</Text>
        </TouchableHighlight>
        <Text>{this.state.answerVoice}</Text>
        <TouchableHighlight
          style={styles.button}
          onPress={this.onStartPlay}>
          <Text>Click to play</Text>
        </TouchableHighlight>
        <TouchableHighlight
          style={styles.button}
          onPress={() => ask()}>
          <Text>Click to ask</Text>
        </TouchableHighlight>
        </View>
    );
  }

  private onStatusPress = (e: any) => {
    const touchX = e.nativeEvent.locationX;
    console.log(`touchX: ${touchX}`);
    const playWidth =
      (this.state.currentPositionSec / this.state.currentDurationSec) *
      (screenWidth - 56);
    console.log(`currentPlayWidth: ${playWidth}`);

    const currentPosition = Math.round(this.state.currentPositionSec);
    console.log(`currentPosition: ${currentPosition}`);

    if (playWidth && playWidth < touchX) {
      const addSecs = Math.round(currentPosition + 1000);
      this.audioRecorderPlayer.seekToPlayer(addSecs);
      console.log(`addSecs: ${addSecs}`);
    } else {
      const subSecs = Math.round(currentPosition - 1000);
      this.audioRecorderPlayer.seekToPlayer(subSecs);
      console.log(`subSecs: ${subSecs}`);
    }
  };

  private onStartRecord = async () => {
    if (Platform.OS === 'android') {
      try {
        const grants = await PermissionsAndroid.requestMultiple([
          PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
          PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE,
          PermissionsAndroid.PERMISSIONS.RECORD_AUDIO,
        ]);

        console.log('write external stroage', grants);

        if (
          grants['android.permission.WRITE_EXTERNAL_STORAGE'] ===
            PermissionsAndroid.RESULTS.GRANTED &&
          grants['android.permission.READ_EXTERNAL_STORAGE'] ===
            PermissionsAndroid.RESULTS.GRANTED &&
          grants['android.permission.RECORD_AUDIO'] ===
            PermissionsAndroid.RESULTS.GRANTED
        ) {
          console.log('permissions granted');
        } else {
          console.log('All required permissions not granted');
          return;
        }
      } catch (err) {
        console.warn(err);
        return;
      }
    }

    const audioSet: AudioSet = {
      AudioEncoderAndroid: AudioEncoderAndroidType.AAC,
      AudioSourceAndroid: AudioSourceAndroidType.MIC,
      AVEncoderAudioQualityKeyIOS: AVEncoderAudioQualityIOSType.high,
      AVNumberOfChannelsKeyIOS: 2,
      AVFormatIDKeyIOS: AVEncodingOption.aac,
    };

    console.log('audioSet', audioSet);
    // ? Custom path
    const uri = await this.audioRecorderPlayer.startRecorder(
      this.path,
      // '/data/user/0/com.client/cache/hello.mp3',
      audioSet,
    );

    //? Default path
    // const uri = await this.audioRecorderPlayer.startRecorder(
    //   undefined,
    //   audioSet,
    // );

    this.audioRecorderPlayer.addRecordBackListener((e: RecordBackType) => {
      console.log('record-back', e);
      this.setState({
        recordSecs: e.currentPosition,
        recordTime: this.audioRecorderPlayer.mmssss(
          Math.floor(e.currentPosition),
        ),
      });
    });
    console.log(`uri: ${uri}`);
  };

  private onPauseRecord = async () => {
    try {
      await this.audioRecorderPlayer.pauseRecorder();
    } catch (err) {
      console.log('pauseRecord', err);
    }
  };

  private onResumeRecord = async () => {
    await this.audioRecorderPlayer.resumeRecorder();
  };

  private onStopRecord = async () => {
    const result = await this.audioRecorderPlayer.stopRecorder();
    this.audioRecorderPlayer.removeRecordBackListener();
    this.setState({
      recordSecs: 0,
    });
    console.log(result);
  };

  private onStartPlay = async () => {
    console.log('onStartPlay');
    //? Custom path
    const msg = await this.audioRecorderPlayer.startPlayer(this.path);

    //? Default path
    // const msg = await this.audioRecorderPlayer.startPlayer();

    const volume = await this.audioRecorderPlayer.setVolume(1.0);
    console.log(`file: ${msg}`, `volume: ${volume}`);

    this.audioRecorderPlayer.addPlayBackListener((e: PlayBackType) => {
      if (e.currentPosition === e.duration) {
        console.log('finished');
        this.audioRecorderPlayer.stopPlayer()
        .catch(err => console.log(err.message));
      }
      this.setState({
        currentPositionSec: e.currentPosition,
        currentDurationSec: e.duration,
        playTime: this.audioRecorderPlayer.mmssss(
          Math.floor(e.currentPosition),
        ),
        duration: this.audioRecorderPlayer.mmssss(Math.floor(e.duration)),
      });
    });
  };

  private onPausePlay = async () => {
    await this.audioRecorderPlayer.pausePlayer();
  };

  private onResumePlay = async () => {
    await this.audioRecorderPlayer.resumePlayer();
  };

  private onStopPlay = async () => {
    console.log('onStopPlay');
    this.audioRecorderPlayer.stopPlayer();
    this.audioRecorderPlayer.removePlayBackListener();
  };
}

export default Recorder;