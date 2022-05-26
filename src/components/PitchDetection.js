import React, {useEffect, useState} from 'react';
import NativeEventEmitter from 'react-native/Libraries/EventEmitter/NativeEventEmitter';
import {Button, Text, StyleSheet, StatusBar, View} from 'react-native';

import PitchModule from './PitchModule';
import {frequencyToNote} from '../lib/FrequencyToNote';
import {check, request, PERMISSIONS, RESULTS} from 'react-native-permissions';

const PitchDetection = () => {
  const [pitch, setPitch] = useState(0);
  const [note, setNote] = useState('');

  useEffect(() => {
    const pitchEventEmitter = new NativeEventEmitter(PitchModule);
    pitchEventEmitter.addListener('Pitch', event => {
      console.log('pitch event', event);
      setPitch(event.pitch);
      const {note, side, cent_index} = frequencyToNote(event.pitch);
      console.log(`detected ${note}-${cent_index}-${side}`);
      setNote(note);
    });
  }, []);

  const onStartRecord = async () => {
    console.log('Starting record');
    try {
      const x = await PitchModule.startRecord();
      console.log(`Started record ${x}`);
    } catch (e) {
      console.error(e);
    }
  };

  const checkPermissions = () => {
    check(PERMISSIONS.ANDROID.RECORD_AUDIO)
      .then(result => {
        switch (result) {
          case RESULTS.UNAVAILABLE:
            console.log(
              'This feature is not available (on this device / in this context)',
            );
            break;
          case RESULTS.DENIED:
            console.log(
              'The permission has not been requested / is denied but requestable',
            );
            break;
          case RESULTS.LIMITED:
            console.log('The permission is limited: some actions are possible');
            break;
          case RESULTS.GRANTED:
            console.log('The permission is granted');
            break;
          case RESULTS.BLOCKED:
            console.log('The permission is denied and not requestable anymore');
            break;
        }
      })
      .catch(error => {
        console.log(error);
      });
  };

  const requestAudioPermission = () => {
    request(PERMISSIONS.ANDROID.RECORD_AUDIO)
      .then(result => {
        console.log(result);
      })
      .catch(error => {
        console.log(error);
      });
  };

  return (
    <View style={styles.container}>
      <Text style={styles.item}>Pitch: {pitch}</Text>
      <Text style={styles.item}>Note: {note}</Text>
      <Button title="Request Permissions" onPress={requestAudioPermission} />
      <Button title="Check Permissions" onPress={checkPermissions} />
      <Button title="Record" onPress={onStartRecord} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    paddingTop: StatusBar.currentHeight,
    backgroundColor: '#ecf0f1',
    padding: 8,
  },
  item: {
    margin: 24,
    fontSize: 18,
    fontWeight: 'bold',
    textAlign: 'center',
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});
