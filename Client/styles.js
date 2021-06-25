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
  talkArea: {
    height: '15%',
    backgroundColor: 'steelblue',
    alignItems: 'center',
    justifyContent: 'space-around',
    borderBottomWidth: 1,
  },
  topWeather: {
    // flex:1,
    // maxWidth: '100%',
    height: '34%',
    backgroundColor: 'red',
    // alignItems: 'center',
    justifyContent: 'space-around',
    borderBottomWidth: 1,
  },
  childTopWeather: {
    // backgroundColor: 'red',
    alignItems: 'center',
    // justifyContent: 'space-around',
  },
  centerWeather: {
    borderBottomWidth: 1,
    height: '17%',
    alignItems: 'center',
    backgroundColor: 'green',
    flexDirection: 'row'
    // justifyContent: 'space-around',
  },
  childCenterWeather: {
    width: '20%',
    height: '100%',
    alignItems: 'center',
    backgroundColor: 'pink',
    justifyContent: 'space-around',
  },
  bottomWeather: {
    height: '34%',
    alignItems: 'center',
    backgroundColor: 'yellow',
    justifyContent: 'space-around',
    borderBottomWidth: 1,
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
    // alignItems: 'center',
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
  regionText: {
    fontSize: 30,
    fontWeight: 'bold',
  },
  weatherText: {
    fontSize: 15,
  },
  tempText: {
    fontSize: 50,
  },
  describeText: {
    fontSize: 15,
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