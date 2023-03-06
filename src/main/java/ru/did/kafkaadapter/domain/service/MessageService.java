package ru.did.kafkaadapter.domain.service;

import ru.did.kafkaadapter.controller.dto.Message;

public interface MessageService {

    void createMessage(Message message);
}
