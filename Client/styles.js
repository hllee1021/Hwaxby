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
    maxWidth: '100%',
    height: '25%',
    // backgroundColor: 'steelblue',
    // alignItems: 'center',
    justifyContent: 'space-around',
  },
  inputTalkArea: {
    // backgroundColor: 'steelblue',
    alignItems: 'center',
    justifyContent: 'space-around',
    marginBottom : 20,
  },
  topWeather: {
    // flex:1,
    // maxWidth: '100%',
    height: '30%',
    // backgroundColor: 'red',
    // alignItems: 'center',
    justifyContent: 'space-around',
    // borderTopWidth: 1,
    // borderBottomWidth: 1,
  },
  childTopWeather: {
    // backgroundColor: 'red',
    alignItems: 'center',
    // justifyContent: 'space-around',
  },
  centerWeather: {
    borderTopWidth: 1,
    borderBottomWidth: 1,
    height: '10%',
    alignItems: 'center',
    // backgroundColor: 'green',
    flexDirection: 'row'
    // justifyContent: 'space-around',
  },
  childCenterWeather: {
    width: '20%',
    height: '100%',
    alignItems: 'center',
    // backgroundColor: 'pink',
    justifyContent: 'space-around',
  },
  bottomWeather: {
    height: '60%',
    alignItems: 'center',
    // backgroundColor: 'yellow',
    justifyContent: 'space-around',
    borderBottomWidth: 1,
  },
  title: {
    fontSize: 10,
    fontWeight: 'bold',
  },
  image: {
    width: 350,
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
  boldWeatherText: {
    fontSize: 15,
    fontWeight: 'bold',
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
  iconItem: {
    width: 40,
    height : 80,
    transform: [{scale: 1}]
  },
  clickedImageItem: {
    height: 120,
    width: 110,
  },
  backImage: {
    flex: 1,
    resizeMode: 'cover',
    justifyContent: 'center',
  },
  main: {
    borderTopWidth : 1,
    flex: 1,
    resizeMode: 'cover',
    justifyContent: 'center',
    height: 1200,
  },
});

export default styles;