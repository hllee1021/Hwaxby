package spring.Hwaxby_back.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import spring.Hwaxby_back.domain.Member;

import java.util.logging.Logger;

@Controller
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Member> home() {
        Member member = new Member();
        member.setName("김희진");
        System.out.println("out");
        return new ResponseEntity<> (member, HttpStatus.OK);
    }

}