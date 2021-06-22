/* eslint-disable prettier/prettier */
import React, { useState, useEffect, useDebugValue } from 'react';
import {Component, SafeAreaView, StyleSheet, Text, View, Image, TouchableHighlight, Button} from 'react-native';
import axios from 'axios';
import styles from './styles';
import Recorder from './recorder';

let micImage = {uri : 'https://cdn.icon-icons.com/icons2/2070/PNG/512/microphone_icon_125603.png'};

function App() {

  const [name, setName] = useState('not yet');
  const onName = async () => {
    let response = await axios.get('http://10.0.2.2:1818/test',{params: {id: 123}});
    setName(response.data.result);
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.top}>
        <Text style={styles.title}>Hwaxby</Text>
      </View>
      <View style={styles.center}>
        <Image source={micImage} style={styles.image}/>
      </View>
      <View style={styles.bottom}>
        <TouchableHighlight
          onPress={onName}
          underlayColor="red"
          style={styles.button}>
          <Text style={styles.title}>Click to talk {name}</Text>
        </TouchableHighlight>
      </View>
      <Recorder/>
    </SafeAreaView>
  );
}
export default App;