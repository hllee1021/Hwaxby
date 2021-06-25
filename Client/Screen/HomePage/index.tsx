/* eslint-disable prettier/prettier */
import React, { Component, useState, useEffect, useDebugValue } from 'react';
import {SafeAreaView, TouchableOpacity, StyleSheet, Text, View, Image, TouchableHighlight, Button} from 'react-native';
import { Buffer } from 'buffer';
import AudioRecord from 'react-native-audio-record';
import Sound from 'react-native-sound';
// import { AudioPlayer } from 'react-native-audio-player-recorder';
import styles from '../..//styles';
import fs from 'react-native-fs';
import axios from 'axios';
import Geolocation from 'react-native-geolocation-service';

export default class HomePage extends Component {
  sound = new Sound('./heykakao.wav');
  state = {
    audioFile: '',
    recording: false,
    loaded: false,
    paused: true,
    isRecording: false,
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

  isRecordingHandler = () => {
    this.setState({ 
        isRecording: !(this.state.isRecording) 
    });
    console.log("isRecording", this.state.isRecording);
    if (!this.state.isRecording) {
        console.log("heher");
        this.start();
    }
    else {
        this.stop();
        this.ask();
    }
  }

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
  //     AudioPlayer.play(this.state.audioFile);
  //     this.setState({ paused: false, loaded: true });
  //   }
  // };

  // pause = () => {
  //   AudioPlayer.pause();
  //   this.setState({ paused: true });
  // };

  render() {
    const { recording, paused, recordFile, answerVoice, isRecording } = this.state;
    return (
        <View style={styles.container}>
        <SafeAreaView style={styles.mainContainer}>
          <Text style={styles.text}>This is Home Page</Text>
          <Text>{answerVoice}</Text>
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
    //   <View style={styles.bottom}>
    //     <TouchableHighlight
    //       style={styles.button}
    //       onPress={this.start}
    //       disabled={recording}>
    //       <Text>Click to talk</Text>
    //     </TouchableHighlight>
    //     <TouchableHighlight
    //       style={styles.button}
    //       onPress={this.stop}
    //       disabled={!recording}>
    //       <Text>Click to stop</Text>
    //     </TouchableHighlight>
    //     <TouchableHighlight
    //       style={styles.button}
    //       onPress={this.ask}>
    //       <Text>Click to ask</Text>
    //     </TouchableHighlight>
    //     {/* <TouchableHighlight
    //       style={styles.button}
    //       onPress={this.play}
    //       disabled={!audioFile}>
    //       <Text>Click to talk</Text>
    //     </TouchableHighlight>
    //     <TouchableHighlight
    //       style={styles.button}
    //       onPress={this.pause}
    //       disabled={!audioFile}>
    //       <Text>Click to talk</Text>
    //     </TouchableHighlight> */}
    //     {/* <View style={styles.row}>
    //       <Button onPress={this.start} title="Record" disabled={recording} />
    //       <Button onPress={this.stop} title="Stop" disabled={!recording} />
    //       {paused ? (
    //         <Button onPress={this.play} title="Play" disabled={!audioFile} />
    //       ) : (
    //         <Button onPress={this.pause} title="Pause" disabled={!audioFile} />
    //       )}
    //     </View> */}
    //   </View>
    );
  }
}
