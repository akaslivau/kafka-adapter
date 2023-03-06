package ru.did.kafkaadapter.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.did.kafkaadapter.controller.dto.Message;
import ru.did.kafkaadapter.domain.service.MessageService;

@RestController("ru.did.kafkaadapter.controller.MessageController")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService service;

    @PostMapping("/message")
    public ResponseEntity<Void> CreateMessage(@NonNull Message message) {
        service.createMessage(message);
        return ResponseEntity.notFound().build();
    }


}
