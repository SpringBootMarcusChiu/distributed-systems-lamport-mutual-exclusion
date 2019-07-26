package com.marcuschiu.example.spring.boot.mastercodesnippet.controller;

import com.marcuschiu.example.spring.boot.mastercodesnippet.model.ReleaseMessage;
import com.marcuschiu.example.spring.boot.mastercodesnippet.model.ReplyMessage;
import com.marcuschiu.example.spring.boot.mastercodesnippet.model.RequestMessage;
import com.marcuschiu.example.spring.boot.mastercodesnippet.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/message")
public class AppMessageController {

    @Autowired
    EventService eventService;

    @PostMapping("/request")
    public @ResponseBody String message(@RequestBody RequestMessage message) {
        eventService.process(message);
        return "send me the procrastination articles";
    }

    @PostMapping("/reply")
    public @ResponseBody String message(@RequestBody ReplyMessage message) {
        eventService.process(message);
        return "muhammad";
    }

    @PostMapping("/release")
    public @ResponseBody String message(@RequestBody ReleaseMessage message) {
        eventService.process(message);
        return "Lord Rama";
    }
}
