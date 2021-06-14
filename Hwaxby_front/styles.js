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
    height: '70%',
    alignItems: 'center',
    justifyContent: 'space-around',
  },
  bottom: {
    height: '20%',
    alignItems: 'center',
  },
  title: {
    fontSize: 30,
    fontWeight: 'bold',
  },
  image: {
    width: 300,
    height: 300,
  },
  button: {
    width: 200,
    height: 75,
    backgroundColor: 'steelblue',
    alignItems: 'center',
    justifyContent: 'space-around',
  },
});

export default styles;
