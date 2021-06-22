package spring.Hwaxby_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import spring.Hwaxby_back.domain.Voice;
import spring.Hwaxby_back.service.MemberService;
import spring.Hwaxby_back.service.VoiceService;

@Controller
public class VoiceController {

    private final VoiceService voiceService;

    @Autowired
    public VoiceController(VoiceService voiceService) {
        this.voiceService = voiceService;
    }

    @GetMapping("ask")
    public ResponseEntity<?> voiceToText(@RequestBody Voice voice) throws Exception {
        System.out.println(voice.getFilePath());
        Voice responseVoice = voiceService.voiceToText(voice.getFilePath());
        voiceService.voiceParsing(responseVoice);
        return new ResponseEntity<> (responseVoice, HttpStatus.OK);
    }
}
