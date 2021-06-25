/* eslint-disable prettier/prettier */
import React, { Component, useState, useEffect, useDebugValue } from 'react';
import {SafeAreaView, StyleSheet, Text, View, Image, TouchableHighlight, Button} from 'react-native';
import { Buffer } from 'buffer';
import AudioRecord from 'react-native-audio-record';
// import { AudioPlayer } from 'react-native-audio-player-recorder';
import styles from './styles';
import fs from 'react-native-fs';
import axios from 'axios';
import Geolocation from 'react-native-geolocation-service';
export default class Recorder extends Component {
  sound = null;
  state = {
    audioFile: '',
    recording: false,
    loaded: false,
    paused: true,
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
    this.setState({ audioFile: '', recording: true, loaded: false });
    AudioRecord.start();
  };

  stop = async () => {
    if (!this.state.recording) return;
    console.log('stop record');
    let audioFile = await AudioRecord.stop();
    console.log('audioFile', audioFile);
    this.setState({ audioFile, recording: false });
  };

  // read = async () => {
  //   let myVoice = await fs.readFile('/data/user/0/com.client/files/test.wav', 'base64');
  //   console.log(myVoice);
  // };

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
      console.log("heee");
      console.log(myVoice);
      console.log(askResponse.data.voice.text);
      resResponse = await axios.post(
        'http://10.0.2.2:8080/response',
          {voice: {id : askResponse.data.voice.id},
          coordinates : {id : askResponse.data.coordinates.id}},
      );
      this.setState({
        answerVoice: resResponse.data.voice.text,
      });
      console.log('done');
    });
  };
  // play = () => {
  //   if (this.state.loaded) {
  //     AudioPlayer.unpause();
  //     this.setState({ paused: false });
  //   } else {
  //     AudioPlayer.play(this.state.audioFile);
  //     this.setState({ paused: false, loaded: true });
  //   }
  // };

  // pause = () => {
  //   AudioPlayer.pause();
  //   this.setState({ paused: true });
  // };

  render() {
    const { recording, paused, audioFile } = this.state;
    return (
      <View style={styles.bottom}>
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
        <TouchableHighlight
          style={styles.button}
          onPress={this.ask}>
          <Text>Click to ask</Text>
        </TouchableHighlight>
        {/* <TouchableHighlight
          style={styles.button}
          onPress={this.play}
          disabled={!audioFile}>
          <Text>Click to talk</Text>
        </TouchableHighlight>
        <TouchableHighlight
          style={styles.button}
          onPress={this.pause}
          disabled={!audioFile}>
          <Text>Click to talk</Text>
        </TouchableHighlight> */}
        {/* <View style={styles.row}>
          <Button onPress={this.start} title="Record" disabled={recording} />
          <Button onPress={this.stop} title="Stop" disabled={!recording} />
          {paused ? (
            <Button onPress={this.play} title="Play" disabled={!audioFile} />
          ) : (
            <Button onPress={this.pause} title="Pause" disabled={!audioFile} />
          )}
        </View> */}
      </View>
    );
  }
}
