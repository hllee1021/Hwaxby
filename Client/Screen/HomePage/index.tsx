/* eslint-disable prettier/prettier */
import React, { Component, useState, useEffect, useDebugValue } from 'react';
import {SafeAreaView, TouchableOpacity, ScrollView, ImageBackground, StyleSheet, Text, View, Image, TouchableHighlight, Button} from 'react-native';
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
    recordVoice: '오늘 날씨가 어때?',
    answerVoice: '',
    isRecording: false,
    region: '',
    weather: '',
    temp: '',
    highTemp: '',
    lowTemp: '',
    rain: '',
    humidity: '',
    wind: '',
    feeling: '',
    sunRise: '',
    sunFall: '',
    dts: '',
    foreHumidity: [],
    foreHighTemp: [],
    foreLowTemp: [],
    icon : [],
    dow: [],
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

  getIcon = async(icon : string) => {
    const uri = 'http://openweathermap.org/img/wn/' + icon + '@2x.png';
    const pic = await axios.get(uri);
    // console.log(pic);
    return pic;
  }

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
        'http://192.168.43.52:8080/ask',
          {voice: {data : myVoice},
          coordinates: {lat: lat, lon: lon}},);
      this.setState({
        recordVoice: askResponse.data.voice.text,
      });
      console.log("askResponse data: " , askResponse.data.voice.text);
      resResponse = await axios.post(
        'http://192.168.43.52:8080/response',
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
        rain: resResponse.data.currentApiData.current.rain.rain1h,
        humidity: resResponse.data.currentApiData.current.humidity,
        wind: resResponse.data.currentApiData.current.wind_speed,
        feeling: resResponse.data.currentApiData.current.feels_like,
        sunRise: resResponse.data.forecastApiData.daily[0].sunrises,
        sunFall: resResponse.data.forecastApiData.daily[0].sunsets,
        dts: resResponse.data.currentApiData.current.dts,
        foreHumidity: [resResponse.data.forecastApiData.daily[1].humidity,resResponse.data.forecastApiData.daily[2].humidity,
        resResponse.data.forecastApiData.daily[3].humidity,resResponse.data.forecastApiData.daily[4].humidity,
        resResponse.data.forecastApiData.daily[5].humidity,resResponse.data.forecastApiData.daily[6].humidity,
        resResponse.data.forecastApiData.daily[7].humidity],
        foreHighTemp: [resResponse.data.forecastApiData.daily[1].temp.max,resResponse.data.forecastApiData.daily[2].temp.max,
        resResponse.data.forecastApiData.daily[3].temp.max,resResponse.data.forecastApiData.daily[4].temp.max,
        resResponse.data.forecastApiData.daily[5].temp.max,resResponse.data.forecastApiData.daily[6].temp.max,
        resResponse.data.forecastApiData.daily[7].temp.max],
        foreLowTemp: [resResponse.data.forecastApiData.daily[1].temp.min,resResponse.data.forecastApiData.daily[2].temp.min,
        resResponse.data.forecastApiData.daily[3].temp.min,resResponse.data.forecastApiData.daily[4].temp.min,
        resResponse.data.forecastApiData.daily[5].temp.min,resResponse.data.forecastApiData.daily[6].temp.min,
        resResponse.data.forecastApiData.daily[7].temp.min],
        icon: ['https://openweathermap.org/img/wn/'+resResponse.data.forecastApiData.daily[1].weather[0].icon+'@2x.png',
        'https://openweathermap.org/img/wn/'+resResponse.data.forecastApiData.daily[2].weather[0].icon+'@2x.png',
        'https://openweathermap.org/img/wn/'+resResponse.data.forecastApiData.daily[3].weather[0].icon+'@2x.png',
        'https://openweathermap.org/img/wn/'+resResponse.data.forecastApiData.daily[4].weather[0].icon+'@2x.png',
        'https://openweathermap.org/img/wn/'+resResponse.data.forecastApiData.daily[5].weather[0].icon+'@2x.png',
        'https://openweathermap.org/img/wn/'+resResponse.data.forecastApiData.daily[6].weather[0].icon+'@2x.png',
        'https://openweathermap.org/img/wn/'+resResponse.data.forecastApiData.daily[7].weather[0].icon+'@2x.png',],
        dow: [resResponse.data.forecastApiData.daily[1].dow,resResponse.data.forecastApiData.daily[2].dow,
        resResponse.data.forecastApiData.daily[3].dow,resResponse.data.forecastApiData.daily[4].dow,
        resResponse.data.forecastApiData.daily[5].dow,resResponse.data.forecastApiData.daily[6].dow,
        resResponse.data.forecastApiData.daily[7].dow,],
      });
      this.play();
      console.log('done');
    });
  };

  render() {
    const { recording, paused, recordFile, 
      recordVoice, answerVoice, isRecording,
     region, weather, temp,
    highTemp, lowTemp, isFirst,
  rain, humidity, wind,
feeling, sunRise, sunFall,
foreHumidity, foreHighTemp, foreLowTemp,
icon, dow, dts} = this.state;
    return (
        <View style={styles.container}>
        <SafeAreaView style={styles.mainContainer}>
          <View style={styles.talkArea}>
            <View>
              <Text style={styles.text}>{recordVoice}</Text>
            </View>
            <View>
            <Text style={styles.text}>{answerVoice}</Text>
            </View>
          </View>
          <ScrollView>
          {isFirst ? (
            <View></View>
          ) : (
            <View style={styles.main}>
              <ImageBackground style={styles.backImage} source={{ uri: 'https://images.pexels.com/photos/255379/pexels-photo-255379.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500'}}>
            <View style={styles.topWeather}>
            <View style={styles.childTopWeather}>
            <Text style={styles.text}>{dts}</Text>
            <Text style={styles.regionText}>{region}</Text>
            <Text style={styles.weatherText}>{weather}</Text>
            <Text style={styles.tempText}>{temp}°C</Text>
            <Text style={styles.weatherText}>최고:{highTemp}°C 최저: {lowTemp}°C</Text>
            </View>
          </View>
          </ImageBackground>
          <View style={styles.centerWeather}>
            <View style={styles.childCenterWeather}>
              <Text style={styles.weatherText}>강수량</Text>
              <Text style={styles.weatherText}>{rain}</Text>
            </View>
            <View style={styles.childCenterWeather}>
              <Text style={styles.weatherText}>습도</Text>
              <Text style={styles.weatherText}>{humidity}%</Text>
            </View>
            <View style={styles.childCenterWeather}>
              <Text style={styles.weatherText}>풍속</Text>
              <Text style={styles.weatherText}>{wind}m/s</Text>
            </View>
            <View style={styles.childCenterWeather}>
              <Text style={styles.weatherText}>체감온도</Text>
              <Text style={styles.weatherText}>{feeling}°C</Text>
            </View>
            <View style={styles.childCenterWeather}>
              <Text style={styles.weatherText}>일출/일몰</Text>
              <Text style={styles.weatherText}>{sunRise}/{sunFall}</Text>
            </View>
          </View>
          <View style={styles.bottomWeather}>
            <Text style={styles.describeText}>{dow[0]}요일                  <Image style={styles.iconItem} resizeMode= 'contain' source={{uri : icon[0]}}/> {foreHumidity[0]}%       {foreHighTemp[0]} / {foreLowTemp[0]}</Text>
            <Text style={styles.describeText}>{dow[1]}요일                  <Image style={styles.iconItem} resizeMode= 'contain' source={{uri : icon[1]}}/> {foreHumidity[1]}%       {foreHighTemp[1]} / {foreLowTemp[1]}</Text>
            <Text style={styles.describeText}>{dow[2]}요일                  <Image style={styles.iconItem} resizeMode= 'contain' source={{uri : icon[2]}}/> {foreHumidity[2]}%       {foreHighTemp[2]} / {foreLowTemp[2]}</Text>
            <Text style={styles.describeText}>{dow[3]}요일                  <Image style={styles.iconItem} resizeMode= 'contain' source={{uri : icon[3]}}/> {foreHumidity[3]}%       {foreHighTemp[3]} / {foreLowTemp[3]}</Text>
            <Text style={styles.describeText}>{dow[4]}요일                  <Image style={styles.iconItem} resizeMode= 'contain' source={{uri : icon[4]}}/> {foreHumidity[4]}%       {foreHighTemp[4]} / {foreLowTemp[4]}</Text>
            <Text style={styles.describeText}>{dow[5]}요일                  <Image style={styles.iconItem} resizeMode= 'contain' source={{uri : icon[5]}}/> {foreHumidity[5]}%       {foreHighTemp[5]} / {foreLowTemp[5]}</Text>
            <Text style={styles.describeText}>{dow[6]}요일                  <Image style={styles.iconItem} resizeMode= 'contain' source={{uri : icon[6]}}/> {foreHumidity[6]}%       {foreHighTemp[6]} / {foreLowTemp[6]}</Text>
          </View>
          </View>
          )}
          </ScrollView>
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
