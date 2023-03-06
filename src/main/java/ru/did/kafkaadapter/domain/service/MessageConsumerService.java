package ru.did.kafkaadapter.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.stream.StreamSupport;

import static ru.did.kafkaadapter.domain.model.KafkaMessage.MSG_UUID;

@Component
@RequiredArgsConstructor

/***
 * Service to perform action, when reply recieved from 'listenTopicName'
 */
public class MessageConsumerService {
    static Logger log = LoggerFactory.getLogger(MessageConsumerService.class);

    public void execute(ConsumerRecord<String, String> record) {
        String reply = record.value();
        String topic = record.topic();
        String uuid = getHeader(record, MSG_UUID);

        log.info("Message consumed. UUID: {}. Topic: {}. Payload: {}", uuid, topic, reply); //do whatever you want
    }

    private String getHeader(ConsumerRecord<String, String> record, String key) {
        return StreamSupport.stream(record.headers().spliterator(), false)
                .filter(x -> x.key().equalsIgnoreCase(key))
                .map(x -> convert(x.value())).findFirst().orElse("-");
    }

    private String convert(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
