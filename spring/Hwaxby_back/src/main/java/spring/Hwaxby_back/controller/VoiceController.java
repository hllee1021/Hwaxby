package spring.Hwaxby_back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.Hwaxby_back.service.MemberService;
import spring.Hwaxby_back.service.VoiceService;

@Controller
public class VoiceController {

    private VoiceService memberService;

    @GetMapping("ask")
    public String voiceToText(@RequestParam("voiceFilePath") String voiceFilePath) throws Exception {
        System.out.println("out");
        return memberService.voiceToText(voiceFilePath);
    }
}
