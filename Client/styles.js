/* eslint-disable prettier/prettier */
import {StyleSheet} from 'react-native';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'space-between',
  },
  top: {
    height: '10%',
    backgroundColor: 'steelblue',
    alignItems: 'center',
    justifyContent: 'space-around',
  },
  center: {
    height: '30%',
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
});

export default styles;