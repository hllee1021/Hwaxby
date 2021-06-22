const express = require('express');
const app = express();
const router = require('express').Router();
const fs = require('fs');
const config = require('../config.json');
const audiorecorder= require('node-audiorecorder');
const path = require('path');
let request = require('request');

let accessKey = config.aihub_key;
let languageCode = 'korean';
// let audioFilePath = 'heykakao.wav';
let audioFilePath = 'ss.m4a';
let audioData = fs.readFileSync(audioFilePath);
let openApiURL = 'http://aiopen.etri.re.kr:8000/WiseASR/Recognition';

router.get('/', function(req,res){
  console.log(rates);
  res.send("hi")
});

router.get('/soundRecord', async function(req,res){
  const DIRECTORY = 'examples-recordings';
  const recorder = new audiorecorder({
    program: 'sox',
    encoding: 'LINEAR16',
    bits: 16,
    rate: 16000,
    silence: 2
  }, console);
  if (!fs.existsSync(DIRECTORY)) {
    fs.mkdirSync(DIRECTORY);
  }
  const fileName = path.join(DIRECTORY, Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 4).concat('.wav'));
  console.log('Writing new recording file at: ', fileName);
  const fileStream = fs.createWriteStream(fileName, { encoding: 'binary' });
  recorder.start();
  setTimeout(() => recorder.stop(), 10000);
  // audioRecorder.stream().on('close', function (code) {
  //   console.warn('Recording closed. Exit code: ', code);
  // });
  // audioRecorder.stream().on('end', function () {
  //   console.warn('Recording ended.');
  // });
  // audioRecorder.stream().on('error', function () {
  //   console.warn('Recording error.');
  // });
  // process.stdin.resume();
  // console.warn('Press ctrl+c to exit.');
})

router.get('/soundText',async function(req,res){
  let requestJson = {
    'access_key': accessKey,
    'argument': {
        'language_code': languageCode,
        'audio': audioData.toString('base64')
    }
  };
  let options = {
    url: openApiURL,
    body: JSON.stringify(requestJson),
    headers: {'Content-Type':'application/json; charset=UTF-8'} 
  }
  request.post(options, function (error, response, body) {
    console.log('responseCode = ' + response.statusCode);
    console.log('responseBody = ' + body);
  });
  res.json("done");
})

module.exports = router;
