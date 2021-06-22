/* eslint-disable prettier/prettier */
import React, { useEffect, useRef, useState } from 'react';
import { AppState, TouchableOpacity } from 'react-native';
import AudioRecorderPlayer, {
  AudioEncoderAndroidType,
  AudioSet,
  AudioSourceAndroidType,
  AVEncoderAudioQualityIOSType,
  AVEncodingOption,
} from 'react-native-audio-recorder-player';
import RNFetchBlob from 'rn-fetch-blob';
import {Component, SafeAreaView, StyleSheet, Text, View, Image, TouchableHighlight, Button} from 'react-native';
import styles from './styles';


function Recorder() {
  const audioRecorderPlayer = new AudioRecorderPlayer();
  const initialTime = `00:00`;
  const [isRecording, setIsRecording] = useState<boolean>(false);
  const [isPaused, setIsPaused] = useState<boolean>(false);
  const [isPlaying, setIsPlaying] = useState<boolean>(false);
  const [isRemoved, setIsRemoved] = useState<boolean>(false);
  const [recordMetering, setRecordMetering] = useState<number | undefined>();
  const [recordingDuration, setRecordingDuration] = useState<number>();
  const [recordTime, setRecordTime] = useState<string>(initialTime);
  const [recordSecs, setRecordSecs] = useState<number>();
  const [playSecs, setPlaySecs] = useState<number>();
  const [playTime, setPlayTime] = useState<string>(initialTime);
  const [duration, setDuration] = useState<string>(initialTime);
  const [currentPositionSec, setcurrentPositionSec] = useState<number>();
  const [currentDurationSec, setcurrentDurationSec] = useState<number>();
  // const audioRecordPlayerRef = useRef<AudioRecorderPlayer>();

  const onStartRecord = async () => {
    const result = await audioRecorderPlayer.startRecorder();
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
    const msg = await audioRecorderPlayer.startPlayer();
    console.log(msg);
    audioRecorderPlayer.addPlayBackListener((e) => {
      setcurrentPositionSec(e.currentPosition);
      setcurrentDurationSec(e.duration);
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