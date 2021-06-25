/* eslint-disable prettier/prettier */
import React, { Component, useState, useEffect, useDebugValue } from 'react';
import {SafeAreaView, StyleSheet, Text, View, Image, TouchableHighlight, Button} from 'react-native';
import { Buffer } from 'buffer';
import AudioRecord from 'react-native-audio-record';
import Sound from 'react-native-sound';
// import { AudioPlayer } from 'react-native-audio-player-recorder';
import styles from './styles';
import fs from 'react-native-fs';
import axios from 'axios';
import Geolocation from '@react-native-community/geolocation';
export default class Recorder extends Component {
  sound = new Sound('./heykakao.wav')
  state = {
    recordFile: '',
    audioFile: '',
    recording: false,
    loaded: false,
    paused: true,
    recordVoice: 'not yet',
    answerVoice: 'not yet',
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
      // let myVoice = await fs.readFile('/data/user/0/com.ttest/files/test.wav', 'base64');
      fs.writeFile('/data/user/0/com.ttest/files/response.wav',this.state.audioFile, 'base64');
      // console.log(myVoice);
      this.sound = new Sound('/data/user/0/com.ttest/files/response.wav', '', error => {
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
        console.log(error);
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
        'http://172.20.10.3:8080/ask',
          {voice: {data : myVoice},
          coordinates: {lat: lat, lon: lon}},);
      this.setState({
        recordVoice: askResponse.data.voice.text,
      });
      console.log(askResponse.data.voice.text);
      resResponse = await axios.post(
        'http://172.20.10.3:8080/response',
          {voice: {id : askResponse.data.voice.id},
          coordinates : {id : askResponse.data.coordinates.id}},
      );
      console.log(resResponse.data.voice.text)
      this.setState({
        answerVoice: resResponse.data.voice.text,
        audioVoice: resResponse.data.voice.data,
      });
      console.log('done');
    });
  };
  // play = () => {
  //   if (this.state.loaded) {
  //     AudioPlayer.unpause();
  //     this.setState({ paused: false });
  //   } else {
  //     AudioPlayer.play(this.state.recordFile);
  //     this.setState({ paused: false, loaded: true });
  //   }
  // };

  // pause = () => {
  //   AudioPlayer.pause();
  //   this.setState({ paused: true });
  // };

  render() {
    const { recording, paused, recordFile } = this.state;
    return (
      <View style={styles.bottom}>
        <Text>{this.state.recordVoice}</Text>
        <TouchableHighlight
          style={styles.button}
          onPress={this.start}
          disabled={recording}>
          <Text>Click to talk</Text>
        </TouchableHighlight>
        <TouchableHighlight
          style={styles.button}
          onPress={this.stop}
          disabled={!recording}>
          <Text>Click to stop</Text>
        </TouchableHighlight>
        <Text>{this.state.answerVoice}</Text>
        <TouchableHighlight
          style={styles.button}
          onPress={this.ask}>
          <Text>Click to ask</Text>
        </TouchableHighlight>
        {/* <TouchableHighlight
          style={styles.button}
          onPress={this.play}
          disabled={!recordFile}>
          <Text>Click to talk</Text>
        </TouchableHighlight>
        <TouchableHighlight
          style={styles.button}
          onPress={this.pause}
          disabled={!recordFile}>
          <Text>Click to talk</Text>
        </TouchableHighlight> */}
        {/* <View style={styles.row}>
          <Button onPress={this.start} title="Record" disabled={recording} />
          <Button onPress={this.stop} title="Stop" disabled={!recording} />
          {paused ? (
            <Button onPress={this.play} title="Play" disabled={!recordFile} />
          ) : (
            <Button onPress={this.pause} title="Pause" disabled={!recordFile} />
          )}
        </View> */}
      </View>
    );
  }
}