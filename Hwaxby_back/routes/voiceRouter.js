const express = require('express');
const app = express();
const router = require('express').Router();
const fs = require('fs');
const config = require('../config.json');
const rates= require('sample-rate');
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
