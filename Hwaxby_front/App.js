/* eslint-disable prettier/prettier */
import React, { useState } from 'react';
import {SafeAreaView, StyleSheet, Text, View, Image, TouchableHighlight} from 'react-native';
import axios from 'axios';
import styles from './styles.js';

let micImage = {uri : 'https://cdn.icon-icons.com/icons2/2070/PNG/512/microphone_icon_125603.png'};

function App() {

  const [name, setName] = useState('not yet');
  const onName = async () => {
    console.log("a");
    let response = await axios.get('http://10.0.2.2:8080/');
    console.log(response);
    setName(response.data.name);
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
    </SafeAreaView>
  );
}
export default App;
