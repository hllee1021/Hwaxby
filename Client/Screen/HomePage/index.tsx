/* eslint-disable prettier/prettier */
import React, { Component, useState, useEffect, useDebugValue } from 'react';
import {SafeAreaView, TouchableOpacity, StyleSheet, Text, View, Image, TouchableHighlight, Button} from 'react-native';
import { Buffer } from 'buffer';
import AudioRecord from 'react-native-audio-record';
import Sound from 'react-native-sound';
// import { AudioPlayer } from 'react-native-audio-player-recorder';
import styles from '../../styles';
import fs from 'react-native-fs';
import axios from 'axios';
import Geolocation from 'react-native-geolocation-service';

export default class HomePage extends Component {
  sound = new Sound('./heykakao.wav');
  state = {
    recordFile: '',
    audioFile: '',
    isFirst: true,
    recording: false,
    loaded: false,
    paused: true,
    recordVoice: 'not yet',
    answerVoice: 'not yet',
    isRecording: false,
    region: '',
    weather: '',
    temp: '',
    highTemp: '',
    lowTemp: '',
  };

  async componentDidMount() {
    // await this.checkPermission();
    this.initAudioRecord();
    // this.initAudioPlayer();
  }

  initAudioRecord = () => {
    const options = {
      sampleRate: 16000,
      channels: 1,
      bitsPerSample: 16,
      wavFile: 'test.wav',
    };

    AudioRecord.init(options);

    AudioRecord.on('data', data => {
      const chunk = Buffer.from(data, 'base64');
      console.log('chunk size', chunk.byteLength);
      // do something with audio chunk
    });
  };
  
  isRecordingHandler = async () => {
    this.setState({ 
        isRecording: !(this.state.isRecording) 
    });
    console.log("isRecording", this.state.isRecording);
    if (!this.state.isRecording) {
        this.start();
    }
    else {
        await this.stop();
        this.ask();
    }
  }

  start = () => {
    console.log('start record');
    this.setState({ recordFile: '', recording: true, loaded: false });
    AudioRecord.start();
  };

  stop = async () => {
    if (!this.state.recording) return;
    console.log('stop record');
    let recordFile = await AudioRecord.stop();
    console.log('recordFile', recordFile);
    this.setState({ recordFile, recording: false });
  };

  load = () => {
    return new Promise(async(resolve, reject) => {
      // let myVoice = await fs.readFile('/data/user/0/com.client/files/test.wav', 'base64');
      fs.writeFile('/data/user/0/com.client/files/response.wav',this.state.audioFile, 'base64');
      // console.log(myVoice);
      this.sound = new Sound('/data/user/0/com.client/files/response.wav', '', error => {
        if (error) {
          console.log('failed to load the file', error);
          return reject(error);
        }
        this.setState({ loaded: true });
        return resolve();
      });
    });
  };

  play = async () => {
    if (!this.state.loaded) {
      try {
        await this.load();
      } catch (error) {
        console.log("err: ", error);
      }
    }

    this.setState({ paused: false });
    Sound.setCategory('Playback');

    this.sound.play(success => {
      if (success) {
        console.log('successfully finished playing');
      } else {
        console.log('playback failed due to audio decoding errors');
      }
      this.setState({ paused: true });
      // this.sound.release();
    });
  };

  ask = async() => {
    console.log('ask start');
    let myVoice = await fs.readFile('/data/user/0/com.client/files/test.wav', 'base64');
    let lat;
    let lon;
    let askResponse;
    let resResponse;
    Geolocation.getCurrentPosition( async({ coords }) => {
      lat = coords.latitude;
      lon = coords.longitude;
      console.log(lat, lon);
      askResponse = await axios.post(
        'http://10.0.2.2:8080/ask',
          {voice: {data : myVoice},
          coordinates: {lat: lat, lon: lon}},);
      this.setState({
        recordVoice: askResponse.data.voice.text,
      });
      console.log("askResponse data: " , askResponse.data.voice.text);
      resResponse = await axios.post(
        'http://10.0.2.2:8080/response',
          {voice: {id : askResponse.data.voice.id},
          coordinates : {id : askResponse.data.coordinates.id}},
      );
      console.log("resResponse voice text: ", resResponse.data.voice.text);
      console.log(JSON.stringify(resResponse.data));
      this.setState({
        answerVoice: resResponse.data.voice.text,
        audioFile: resResponse.data.voice.data,
        region: resResponse.data.currentApiData.timezone,
        weather: resResponse.data.currentApiData.current.weather[0].description,
        temp: resResponse.data.currentApiData.current.temp,
        highTemp: resResponse.data.forecastApiData.daily[0].temp.max,
        lowTemp: resResponse.data.forecastApiData.daily[0].temp.min,
        isFirst: false,
      });
      this.play();
      console.log('done');
    });
  };

  render() {
    const { recording, paused, recordFile, 
      recordVoice, answerVoice, isRecording,
     region, weather, temp,
    highTemp, lowTemp, isFirst} = this.state;
    return (
        <View style={styles.container}>
        <SafeAreaView style={styles.mainContainer}>
          <View style={styles.talkArea}>
            <Text style={styles.text}>{recordVoice}</Text>
            <Text style={styles.text}>{answerVoice}</Text>
          </View>
          {isFirst ? (
            <View></View>
          ) : (
            <View>
            <View style={styles.topWeather}>
            <View style={styles.childTopWeather}>
            <Text style={styles.regionText}>{region}</Text>
            <Text style={styles.weatherText}>대체로 흐림</Text>
            <Text style={styles.tempText}>{temp}°C</Text>
            <Text style={styles.weatherText}>최고:{highTemp}°C 최저: {lowTemp}°C</Text>
            </View>
          </View>
          <View style={styles.centerWeather}>
            <View style={styles.childCenterWeather}>
              <Text style={styles.weatherText}>시간</Text>
              <Text style={styles.weatherText}>아이콘</Text>
              <Text style={styles.weatherText}>22°C</Text>
            </View>
            <View style={styles.childCenterWeather}>
              <Text style={styles.weatherText}>시간</Text>
              <Text style={styles.weatherText}>아이콘</Text>
              <Text style={styles.weatherText}>22°C</Text>
            </View>
            <View style={styles.childCenterWeather}>
              <Text style={styles.weatherText}>시간</Text>
              <Text style={styles.weatherText}>아이콘</Text>
              <Text style={styles.weatherText}>22°C</Text>
            </View>
            <View style={styles.childCenterWeather}>
              <Text style={styles.weatherText}>시간</Text>
              <Text style={styles.weatherText}>아이콘</Text>
              <Text style={styles.weatherText}>22°C</Text>
            </View>
            <View style={styles.childCenterWeather}>
              <Text style={styles.weatherText}>시간</Text>
              <Text style={styles.weatherText}>아이콘</Text>
              <Text style={styles.weatherText}>22°C</Text>
            </View>
          </View>
          <View style={styles.bottomWeather}>
            <Text style={styles.describeText}>일요일                                    아이콘 습도       최고온도 최저온도</Text>
            <Text style={styles.describeText}>일요일                                    아이콘 습도       최고온도 최저온도</Text>
            <Text style={styles.describeText}>일요일                                    아이콘 습도       최고온도 최저온도</Text>
            <Text style={styles.describeText}>일요일                                    아이콘 습도       최고온도 최저온도</Text>
            <Text style={styles.describeText}>일요일                                    아이콘 습도       최고온도 최저온도</Text>
            <Text style={styles.describeText}>일요일                                    아이콘 습도       최고온도 최저온도</Text>
            <Text style={styles.describeText}>일요일                                    아이콘 습도       최고온도 최저온도</Text>
          </View>
          </View>
          )}
        </SafeAreaView>
        <TouchableOpacity 
            onPress={this.isRecordingHandler} 
            style={styles.speakContainer}>
            {!isRecording && (
                <Image
                    style={styles.imageItem}
                    source={require('Client/components/images/beforeListen.png')}
                />
            )}
            {isRecording && (
                <Image
                    style={styles.clickedImageItem}
                    source={require('Client/components/images/listening.png')}
                />
            )}
        </TouchableOpacity>
      </View>
    );
  }
}
