/* eslint-disable prettier/prettier */
import {StyleSheet} from 'react-native';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: '100%',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
  },
  top: {
    height: '10%',
    backgroundColor: 'steelblue',
    alignItems: 'center',
    justifyContent: 'space-around',
  },
  center: {
    height: '60%',
    alignItems: 'center',
    justifyContent: 'space-around',
  },
  bottom: {
    height: '30%',
    alignItems: 'center',
  },
  title: {
    fontSize: 10,
    fontWeight: 'bold',
  },
  image: {
    width: 300,
    height: 300,
  },
  button: {
    width: 200,
    height: 50,
    backgroundColor: 'steelblue',
    alignItems: 'center',
    justifyContent: 'space-around',
  },
  icon: {
    paddingLeft: 15,
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

export default styles;