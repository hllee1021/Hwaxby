/* eslint-disable prettier/prettier */
import React, { useEffect, useRef, useState } from 'react';
import { AppState, TouchableOpacity } from 'react-native';
import AudioRecorderPlayer, {
    AVEncoderAudioQualityIOSType,
    AVEncodingOption,
    AudioEncoderAndroidType,
    AudioSet,
    AudioSourceAndroidType,
    PlayBackType,
    RecordBackType,
  } from 'react-native-audio-recorder-player';
import RNFetchBlob from 'rn-fetch-blob';
import {Component, SafeAreaView, StyleSheet, Text, View, Image, TouchableHighlight, Button, PermissionsAndroid} from 'react-native';
import styles from './styles';

type State = {
    // isLoggingIn: boolean;
    // recordSecs: number;
    // recordTime: string;
    // currentPositionSec: number;
    // currentDurationSec: number;
    // playTime: string;
    // duration: string;
}

const Recorder = ({
    // isLoggingIn,
    // recordSecs,
    // recordTime,
    // currentPositionSec,
    // currentDurationSec,
    // playTime,
    // duration
  }: State) =>{
    const dirs = RNFetchBlob.fs.dirs;
    const path = `${dirs.CacheDir}/hello.mp3`
    // const audioRecorderPlayer: AudioRecorderPlayer;

    const initialTime = '00:00:00';
    const [isRecording, setIsRecording] = useState<boolean>(false);
    const [isLoggingIn, setIsLoggingIn] = useState<boolean>(false);
    const [isPaused, setIsPaused] = useState<boolean>(false);
    const [isPlaying, setIsPlaying] = useState<boolean>(false);
    const [isRemoved, setIsRemoved] = useState<boolean>(false);
    const [recordMetering, setRecordMetering] = useState<number | undefined>();
    const [recordingDuration, setRecordingDuration] = useState<number>();
    const [recordTime, setRecordTime] = useState<string>(initialTime);
    const [recordSecs, setRecordSecs] = useState<number>();
    const [currentPositionSec, setCurrentPositionSec] = useState<number>();
    const [currentDurationSec, setCurrentDurationSec] = useState<number>();
    const [playTime, setPlayTime] = useState<string>(initialTime);
    const [duration, setDuration] = useState<string>(initialTime);
    
    const audioRecorderPlayer = new AudioRecorderPlayer();
    // this.audioRecorderPlayer.setSubscriptionDuration(0.1); // optional. Default is 0.5




//   const audioRecorderPlayer = new AudioRecorderPlayer();
//   const initialTime = `00:00`;
//   const [isRecording, setIsRecording] = useState<boolean>(false);
//   const [isPaused, setIsPaused] = useState<boolean>(false);
//   const [isPlaying, setIsPlaying] = useState<boolean>(false);
//   const [isRemoved, setIsRemoved] = useState<boolean>(false);
//   const [recordMetering, setRecordMetering] = useState<number | undefined>();
//   const [recordingDuration, setRecordingDuration] = useState<number>();
//   const [recordTime, setRecordTime] = useState<string>(initialTime);
//   const [recordSecs, setRecordSecs] = useState<number>();
//   const [playSecs, setPlaySecs] = useState<number>();
//   const [playTime, setPlayTime] = useState<string>(initialTime);
//   const [duration, setDuration] = useState<string>(initialTime);
//   const [currentPositionSec, setcurrentPositionSec] = useState<number>();
//   const [currentDurationSec, setcurrentDurationSec] = useState<number>();
  const audioRecordPlayerRef = useRef<AudioRecorderPlayer>();

  const onStartRecord = async () => {
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
      setIsRecording(true);
      const audioSet: AudioSet = {
        AudioEncoderAndroid: AudioEncoderAndroidType.AAC,
        AudioSourceAndroid: AudioSourceAndroidType.MIC,
        AVEncoderAudioQualityKeyIOS: AVEncoderAudioQualityIOSType.high,
        AVNumberOfChannelsKeyIOS: 2,
        AVFormatIDKeyIOS: AVEncodingOption.aac,
      };
      const meteringEnabled = true;
    const result = await audioRecorderPlayer.startRecorder(path, audioSet, meteringEnabled);
    audioRecorderPlayer.addRecordBackListener((e) => {
      setRecordSecs(e.currentPosition);
      setRecordTime(audioRecorderPlayer.mmssss(
        Math.floor(e.currentPosition),
      ));
      return;
    });
    console.log(result);
  };

  const onStopRecord = async () => {
    const result = await audioRecorderPlayer.stopRecorder();
    audioRecorderPlayer.removeRecordBackListener();
    setRecordSecs(0);
    console.log(result);
  };

  const onStartPlay = async () => {
    console.log('onStartPlay');
    const msg = await audioRecorderPlayer.startPlayer(path);
    console.log("a");
    const volume = await audioRecorderPlayer.setVolume(1.0);
    console.log("b");
    console.log(msg);
    console.log("c");
    audioRecorderPlayer.addPlayBackListener((e) => {
        if (e.currentPosition === e.duration) {
            console.log('finished');
            audioRecorderPlayer.stopPlayer();
        }
      setCurrentPositionSec(e.currentPosition);
      setCurrentDurationSec(e.duration);
      setPlayTime(audioRecorderPlayer.mmssss(Math.floor(e.currentPosition)));
      setDuration(audioRecorderPlayer.mmssss(Math.floor(e.duration)));
      return;
    });
  };

  const onPausePlay = async () => {
    await audioRecorderPlayer.pausePlayer();
  };

  const onStopPlay = async () => {
    console.log('onStopPlay');
    audioRecorderPlayer.stopPlayer();
    audioRecorderPlayer.removePlayBackListener();
  };

  return (
      <View style={styles.bottom}>
          <TouchableHighlight
          onPress={() => onStartRecord()}
          underlayColor="red"
          style={styles.button}>
              <Text style={styles.title}>Click to talk</Text>
          </TouchableHighlight>
          <TouchableHighlight
          onPress={() => onStopRecord()}
          underlayColor="red"
          style={styles.button}>
              <Text style={styles.title}>Click to stop talking</Text>
          </TouchableHighlight>
          <TouchableHighlight
          onPress={() => onStartPlay()}
          underlayColor="red"
          style={styles.button}>
              <Text style={styles.title}>Click to play</Text>
          </TouchableHighlight>
          <TouchableHighlight
          onPress={() => onStopPlay()}
          underlayColor="red"
          style={styles.button}>
              <Text style={styles.title}>Click to stop playing</Text>
          </TouchableHighlight>
      </View>
  );
};

export default Recorder;