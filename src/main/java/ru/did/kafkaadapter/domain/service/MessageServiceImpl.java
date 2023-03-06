package ru.did.kafkaadapter.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.did.kafkaadapter.adapter.KafkaConsumerRegistrar;
import ru.did.kafkaadapter.adapter.KafkaProducerService;
import ru.did.kafkaadapter.controller.dto.Message;
import ru.did.kafkaadapter.domain.model.ConsumerProperty;
import ru.did.kafkaadapter.domain.model.KafkaMessage;

import static ru.did.kafkaadapter.domain.model.KafkaMessage.MSG_UUID;

/***
 * Service for controller
 */
@Primary
@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final KafkaProducerService producerService;
    private final KafkaConsumerRegistrar kafkaConsumerRegistrar;

    @Override
    public void createMessage(Message message) {
        KafkaMessage msg = createKafkaMsg(message);
        msg.setHeader(MSG_UUID, message.getMessageUuid()); //some custom headers for correlation, as example

        boolean succeeded = producerService.send(msg);
        if (!succeeded)
            throw new RuntimeException("Message sending failed");

        ConsumerProperty consumerProperty = ConsumerProperty.create(message.getListenTopicName(), message.getKafkaUrl());
        if (!consumerProperty.getListenTopic().isEmpty()) {
            kafkaConsumerRegistrar.registerCustomKafkaListener(null, consumerProperty);
        }

    }

    private KafkaMessage createKafkaMsg(Message dto) {
        return KafkaMessage.builder()
                .payload(dto.getMessage())
                .kafkaUrl(dto.getKafkaUrl())
                .topic(dto.getSendTopicName())
                .build();
    }
}
