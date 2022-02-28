import React from 'react';
import {Button, StyleSheet} from 'react-native';
import {NavigationContainer} from '@react-navigation/native';
import {
  createStackNavigator,
  StackHeaderLeftButtonProps,
} from '@react-navigation/stack';
import {
  createDrawerNavigator,
  DrawerNavigationProp,
} from '@react-navigation/drawer';

import Icon from 'react-native-vector-icons/MaterialIcons';

import HomePage from './HomePage';
import MyLogPage from './MyLogPage';
import styles from '../styles';

const Stack = createStackNavigator();
const Drawer = createDrawerNavigator();

type TypeDrawerProp = DrawerNavigationProp<
  {
    HomeStackNavi: undefined;
    MyLogStackNavi: undefined;
  },
  'HomeStackNavi'
>;

interface DrawerProp {
  navigation: TypeDrawerProp;
}

const HomeStackNavi = ({navigation}: DrawerProp) => {
  return (
    <Stack.Navigator
      screenOptions={{
        headerLeft: (props: StackHeaderLeftButtonProps) => (
          <Icon style={styles.icon}
            name='menu'
            size={30}
            color="#c8c8c8"
            onPress={() => navigation.openDrawer()}
          />
        ),
      }}>
      <Stack.Screen name="화스비" component={HomePage} />
    </Stack.Navigator>
  );
};

const MyLogStackNavi = ({navigation}: DrawerProp) => {
  return (
    <Stack.Navigator
      screenOptions={{
        headerLeft: (props: StackHeaderLeftButtonProps) => (
          <Icon style={styles.icon}
            name='menu'
            size={30}
            color="#c8c8c8"
            onPress={() => navigation.openDrawer()}
          />
        ),
      }}>
      <Stack.Screen name="대화 기록" component={MyLogPage} />
    </Stack.Navigator>
  );
};

const DrawNavi = () => {
  return (
    <Drawer.Navigator>
      <Drawer.Screen name="화스비" component={HomeStackNavi} />
      <Drawer.Screen name="대화 기록" component={MyLogStackNavi} />
    </Drawer.Navigator>
  );
};

const MainNavi = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen
        name="DrawNavi"
        component={DrawNavi}
        options={{
          headerShown: false,
        }}
      />
    </Stack.Navigator>
  );
};

export default () => {
  return (
    <NavigationContainer>
      <MainNavi />
    </NavigationContainer>
  );
};
