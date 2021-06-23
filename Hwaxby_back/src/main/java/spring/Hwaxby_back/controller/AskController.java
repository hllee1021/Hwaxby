package spring.Hwaxby_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import spring.Hwaxby_back.domain.Ask;
import spring.Hwaxby_back.domain.Coordinates;
import spring.Hwaxby_back.domain.Voice;
import spring.Hwaxby_back.repository.CoordRepository;
import spring.Hwaxby_back.repository.VoiceRepository;
import spring.Hwaxby_back.service.CoordService;
import spring.Hwaxby_back.service.MemberService;
import spring.Hwaxby_back.service.VoiceService;

@Controller
public class AskController {

    private final VoiceService voiceService;
    private final CoordService coordService;

    @Autowired
    public AskController(VoiceService voiceService, CoordService coordService) {
        this.voiceService = voiceService;
        this.coordService = coordService;
    }

    @PostMapping("ask")
    public ResponseEntity<?> voiceToText(@RequestBody Ask askData) throws Exception {
        System.out.println("a");
        System.out.println(askData.getVoice().getData());
        Voice voice = new Voice();
        voice.setData(askData.getVoice().getData());

        Coordinates coordinates = new Coordinates();
        coordinates.setLat(askData.getCoordinates().getLat());
        coordinates.setLon(askData.getCoordinates().getLon());
        Long coordId = coordService.save(coordinates);

        Ask result = new Ask();
        result.setVoice(voiceService.voiceToText(voice));
        result.setCoordinates(coordService.findOne(coordId).orElse(coordinates));

        return new ResponseEntity<> (result, HttpStatus.OK);
    }

<<<<<<< HEAD
    @PostMapping("parse")
    public ResponseEntity<?> parse(@RequestBody Voice voice) {
        Voice result = voiceService.voiceParsing(voice);
        return new ResponseEntity<> (result, HttpStatus.OK);
    }
=======
    @GetMapping("parse")
    public ResponseEntity<?> parse(@RequestBody Ask askData) throws Exception {
        System.out.println("a");
        System.out.println(askData.getVoice().getData());
        Voice voice = new Voice();
        voice.setData(askData.getVoice().getData());

        Coordinates coordinates = new Coordinates();
        coordinates.setLat(askData.getCoordinates().getLat());
        coordinates.setLon(askData.getCoordinates().getLon());
        Long coordId = coordService.save(coordinates);

        Ask result = new Ask();
        result.setVoice(voiceService.voiceToText(voice));
        result.setCoordinates(coordService.findOne(coordId).orElse(coordinates));

        return new ResponseEntity<> (result, HttpStatus.OK);
    }

//    @GetMapping("parse")
//    public ResponseEntity<?> parse(@RequestBody Voice voice) {
//        Voice result = voiceService.voiceParsing(voice);
//        return new ResponseEntity<> (result, HttpStatus.OK);
//    }
>>>>>>> 9463eda5... [revise] HTTP Method from Get to Post

}
