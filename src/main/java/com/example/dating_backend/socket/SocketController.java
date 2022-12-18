package com.example.dating_backend.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/greeting")
    public void handle(SocketDto.Message msg) {
        System.out.println("test");
        System.out.println(msg.toString());

        String reSend = "너가 보낸건 " + msg.toString();

        messagingTemplate.convertAndSend("/queue/test",reSend);
    }
}

