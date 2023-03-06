package ru.did.kafkaadapter.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.did.kafkaadapter.domain.model.KafkaMessage;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    @Value("app.kafka.send.timeout.seconds")
    private Long sendTimeout;

    private final KafkaTemplateFactory factory;

    public boolean send(KafkaMessage msg) {
        KafkaTemplate<String, String> kt = factory.get(msg.getKafkaUrl());
        kt.setDefaultTopic(msg.getTopic());
        try {
            kt.send(msg.build()).get(Optional.ofNullable(sendTimeout).orElse(10L), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (ExecutionException | TimeoutException e) {
            return false;
        }
        return true;
    }
}
