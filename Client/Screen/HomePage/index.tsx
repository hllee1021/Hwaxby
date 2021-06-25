import React, {useState} from 'react';
import {
  TouchableOpacity,
  SafeAreaView,
  StyleSheet,
  Text,
  View,
  Image,
} from 'react-native';

const HomePage = () => {
  const [isPushed, setIsPushed] = useState(false);

  const changeHandler = () => {
    setIsPushed(prev => !prev);
  };

  return (
    <View style={styles.container}>
      <SafeAreaView style={styles.mainContainer}>
        <Text style={styles.text}>This is Home Page</Text>
      </SafeAreaView>
      <TouchableOpacity onPress={changeHandler} style={styles.speakContainer}>
        {!isPushed && (
          <Image
            style={styles.imageItem}
            source={require('Client/components/images/beforeListen.png')}
          />
        )}
        {isPushed && (
          <Image
            style={styles.clickedImageItem}
            source={require('Client/components/images/listening.png')}
          />
        )}
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: '100%',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
  },
  mainContainer: {
    flex: 1,
    color: '#000',
  },
  speakContainer: {
    minHeight: 100,
    paddingBottom: 30,
    color: '#000',
  },
  text: {
    fontSize: 20,
    fontWeight: 'bold',
  },
  imageItem: {
    height: 90,
    width: 80,
  },
  clickedImageItem: {
    height: 120,
    width: 110,
  },
});

export default HomePage;
