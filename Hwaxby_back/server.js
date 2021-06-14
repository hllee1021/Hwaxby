// DEPENDENCIES
const express = require('express');
const request = require('request');
const fs = require('fs');
const app = express();
const port = 1818;

//Import Routers 
const voiceRouter = require('./routes/voiceRouter')
app.use('/voice', voiceRouter)

//Static File
app.use(express.static('public'));

//기본 라우터
app.get('/', (req, res) => {
  res.end("Hwaxby Backend Server~");
})

//테스트용 라우터
app.get('/test', function(req,res){
  console.log("api come~")
  res.json({result : "id is " + req.query.id});
})

app.listen(port, () => console.log(`Server listening on port ${port}`));