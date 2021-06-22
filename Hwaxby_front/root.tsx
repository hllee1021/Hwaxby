/* eslint-disable prettier/prettier */
// import { palette } from '@silvia/silvia-common';
import React, { useEffect, useRef, useState } from 'react';
import { AppState, TouchableOpacity } from 'react-native';
import AudioRecorderPlayer, {
  AudioEncoderAndroidType,
  AudioSet,
  AudioSourceAndroidType,
  AVEncoderAudioQualityIOSType,
  AVEncodingOption,
} from 'react-native-audio-recorder-player';
import RNFetchBlob from 'rn-fetch-blob';

// import { spacing } from '../../../theme';
// import { Icon, Text } from '../../atoms';
// import { Stack } from '../../layouts';

type IRecordingInputProps = {
  value: string;
  onChange: (change: string) => void;
  onShowDeleteRecordModal: (onDeleteRecord: () => void) => void;
};

const initialTime = `00:00`;

export const RecordingInput = ({
  value,
  onChange,
  onShowDeleteRecordModal,
}: IRecordingInputProps) => {
  const [isRecording, setIsRecording] = useState<boolean>(false);
  const [isPaused, setIsPaused] = useState<boolean>(false);
  const [isPlaying, setIsPlaying] = useState<boolean>(false);
  const [isRemoved, setIsRemoved] = useState<boolean>(false);
  const [recordMetering, setRecordMetering] = useState<number | undefined>();
  const [recordingDuration, setRecordingDuration] = useState<number>();
  const [recordTime, setRecordTime] = useState<string>(initialTime);
  const [recordSecs, setRecordSecs] = useState<number>();
  const [playSecs, setPlaySecs] = useState<number>();
  const [playTime, setPlayTime] = useState<string>(initialTime);

  const audioRecordPlayerRef = useRef<AudioRecorderPlayer>();

  const isPlayButtonDisabled = !value;
  const isRecordButtonDisabled = !!value;
  const isRemoveRecordingButtonDisabled = !value || isPlaying;
  const isCompleteRecordingButtonDisabled = !!value || !isPaused;

  useEffect(() => {
    audioRecordPlayerRef.current = new AudioRecorderPlayer();
    const cleanAll = () => {
      audioRecordPlayerRef.current && audioRecordPlayerRef.current
        .stopRecorder()
        .catch((e) => console.log(`error while stopping recorder on unmount, ${e}`));
      audioRecordPlayerRef.current && audioRecordPlayerRef.current
        .stopPlayer()
        .catch((e) => console.log(`error while stopping player on unmount, ${e}`));
    };
    const listener = (status) => {
      if (status === 'background' || status === 'inactive') {
        cleanAll();
      }
    };
    AppState.addEventListener('change', listener);
    return () => {
      AppState.removeEventListener('change', listener);
      cleanAll();
    };
  }, [isRemoved]);

  const handleStartRecord = async () => {
    const dirs = RNFetchBlob.fs.dirs;
    const path = `${dirs.CacheDir}/hello.mp3`;
    setIsRecording(true);
    const audioSet: AudioSet = {
      AudioEncoderAndroid: AudioEncoderAndroidType.AAC,
      AudioSourceAndroid: AudioSourceAndroidType.MIC,
      AVEncoderAudioQualityKeyIOS: AVEncoderAudioQualityIOSType.high,
      AVNumberOfChannelsKeyIOS: 2,
      AVFormatIDKeyIOS: AVEncodingOption.aac,
    };
    const meteringEnabled = true;
    await audioRecordPlayerRef.current.startRecorder(path, audioSet, meteringEnabled);
    audioRecordPlayerRef.current.addRecordBackListener((e) => {
      setRecordSecs(e.currentPosition);
      setRecordTime(audioRecordPlayerRef.current.mmss(Math.floor(e.currentPosition / 1000)));
      setRecordingDuration(e.currentPosition);
      setRecordMetering(e.currentMetering);
    });
  };

  const handleStopRecord = async () => {
    const result = await audioRecordPlayerRef.current.stopRecorder();
    audioRecordPlayerRef.current.removeRecordBackListener();
    setIsRecording(false);
    setIsPaused(false);
    setRecordSecs(0);
    onChange(result);
    setRecordMetering(undefined);
  };

  const handleStartPlay = async () => {
    setIsPlaying(true);
    try {
      await audioRecordPlayerRef.current.startPlayer(value);
      audioRecordPlayerRef.current.addPlayBackListener(async (e) => {
        setPlaySecs(e.currentPosition);
        setPlayTime(audioRecordPlayerRef.current.mmss(Math.floor(e.currentPosition / 1000)));
        if (e.currentPosition === e.duration) {
          await handleStopPlay();
        }
      });
    } catch (e) {
      console.log('e', e);
      setIsPlaying(false);
    }
  };

  const handlePauseRecord = async () => {
    try {
      setIsPaused(true);
      await audioRecordPlayerRef.current.pauseRecorder();
    } catch (e) {
      console.log('e', e);
      setIsPaused(false);
    }
  };

  const handleResumeRecord = async () => {
    setIsPaused(false);
    await audioRecordPlayerRef.current.resumeRecorder();
  };

  const handleStopPlay = async () => {
    setIsPlaying(false);
    await audioRecordPlayerRef.current.stopPlayer();
    audioRecordPlayerRef.current.removePlayBackListener();
    setPlaySecs(0);
  };

  const handlePressRemoveRecording = () => {
    setRecordTime(initialTime);
    setPlayTime(initialTime);
    onChange(null);
    setIsRemoved(!isRemoved);
  };

  const handleOpenDeleteRecordModal = () => {
    onShowDeleteRecordModal(handlePressRemoveRecording);
  };

  return (
    <Stack align="center" space={spacing['spacing-xxl-6']}>
      <Stack align="center">
        <Text
          category="h7"
          color={(() => {
            if (recordTime === initialTime) {
              return palette['black-a38'];
            }
            if (recordTime !== initialTime && isRecording && !isPaused) {
              return palette['red-600'];
            }
            if (recordTime !== initialTime && (isPaused || !isRecording)) {
              return palette['geekBlue-600'];
            }
          })()}
        >
          {playTime !== initialTime ? playTime : recordTime}
        </Text>
      </Stack>
      <Stack horizontal align="center" space={spacing['spacing-xxl-6']}>
        {!value ? (
          <Stack align="center" space={spacing['spacing-m']}>
            <Stack align="center">
              <TouchableOpacity
                style={{
                  height: 64,
                  width: 64,
                  borderRadius: 120,
                  alignItems: 'center',
                  justifyContent: 'center',
                  borderWidth: 1,
                  ...(isRecording && !isPaused
                    ? {
                        borderColor: palette['geekBlue-600'],
                        backgroundColor: palette['black-a08'],
                      }
                    : {
                        borderColor: palette['gray-200'],
                        backgroundColor: palette['white-1000'],
                      }),
                }}
                onPress={() =>
                  isRecording
                    ? isPaused
                      ? handleResumeRecord()
                      : handlePauseRecord()
                    : handleStartRecord()
                }
                disabled={isRecordButtonDisabled}
              >
                <Icon
                  name="mic"
                  size={32}
                  color={
                    isRecordButtonDisabled
                      ? palette['gray-200']
                      : isRecording
                      ? isPaused
                        ? palette['geekBlue-600']
                        : palette['red-600']
                      : palette['geekBlue-600']
                  }
                />
              </TouchableOpacity>
            </Stack>
            <Stack align="center">
              {isRecording ? (
                isPaused ? (
                  <Text category="s1" color={palette['geekBlue-600']}>
                    계속녹음
                  </Text>
                ) : (
                  <Text category="s1" color={palette['red-600']}>
                    녹음중지
                  </Text>
                )
              ) : (
                <Text
                  category="s1"
                  color={!value ? palette['geekBlue-600'] : palette['geekBlue-600']}
                >
                  녹음하기
                </Text>
              )}
            </Stack>
          </Stack>
        ) : (
          // remove record
          <Stack align="center" space={spacing['spacing-m']}>
            <Stack align="center">
              <TouchableOpacity
                style={{
                  height: 64,
                  width: 64,
                  borderRadius: 120,
                  alignItems: 'center',
                  justifyContent: 'center',
                  borderColor: palette['gray-300'],
                  borderWidth: 1,
                  backgroundColor: isRemoveRecordingButtonDisabled
                    ? palette['black-a08']
                    : palette['white-1000'],
                }}
                onPress={handleOpenDeleteRecordModal}
              >
                <Icon
                  name="delete_forever_outline"
                  size={32}
                  color={isRemoveRecordingButtonDisabled ? palette['red-200'] : palette['red-500']}
                />
              </TouchableOpacity>
            </Stack>
            <Stack align="center">
              <Text
                category="s1"
                color={isRemoveRecordingButtonDisabled ? palette['black-a38'] : palette['red-600']}
              >
                삭제
              </Text>
            </Stack>
          </Stack>
        )}
        {!value ? ( // complete record
          <Stack align="center" space={spacing['spacing-m']}>
            <Stack align="center">
              <TouchableOpacity
                style={{
                  height: 64,
                  width: 64,
                  borderRadius: 120,
                  alignItems: 'center',
                  justifyContent: 'center',
                  borderColor: palette['gray-300'],
                  borderWidth: 1,
                  backgroundColor: isCompleteRecordingButtonDisabled
                    ? palette['black-a08']
                    : palette['white-1000'],
                }}
                onPress={handleStopRecord}
              >
                <Icon
                  name="check"
                  size={32}
                  color={
                    isCompleteRecordingButtonDisabled
                      ? palette['black-a38']
                      : palette['geekBlue-600']
                  }
                />
              </TouchableOpacity>
            </Stack>
            <Stack align="center">
              <Text
                category="s1"
                color={
                  isCompleteRecordingButtonDisabled ? palette['black-a38'] : palette['geekBlue-600']
                }
              >
                완료
              </Text>
            </Stack>
          </Stack>
        ) : (
          // remove record
          <Stack align="center" space={spacing['spacing-m']}>
            <Stack align="center">
              {isPlaying ? (
                <TouchableOpacity
                  style={{
                    height: 64,
                    width: 64,
                    borderRadius: 120,
                    alignItems: 'center',
                    justifyContent: 'center',
                    borderColor: palette['gray-300'],
                    borderWidth: 1,
                    backgroundColor: palette['white-1000'],
                  }}
                  onPress={handleStopPlay}
                >
                  <Icon name="stop" size={32} color={palette['geekBlue-600']} />
                </TouchableOpacity>
              ) : (
                <TouchableOpacity
                  style={{
                    height: 64,
                    width: 64,
                    borderRadius: 120,
                    alignItems: 'center',
                    justifyContent: 'center',
                    borderColor: palette['gray-300'],
                    borderWidth: 1,
                    backgroundColor: isPlayButtonDisabled
                      ? palette['black-a08']
                      : palette['white-1000'],
                  }}
                  onPress={handleStartPlay}
                  disabled={isPlayButtonDisabled}
                >
                  <Icon
                    name="play_arrow"
                    size={32}
                    color={isPlayButtonDisabled ? palette['black-a38'] : palette['geekBlue-600']}
                  />
                </TouchableOpacity>
              )}
            </Stack>
            <Stack align="center">
              <Text
                category="s1"
                color={isPlayButtonDisabled ? palette['black-a38'] : palette['geekBlue-600']}
              >
                {isPlaying ? '정지' : '재생'}
              </Text>
            </Stack>
          </Stack>
        )}
      </Stack>
    </Stack>
  );
};