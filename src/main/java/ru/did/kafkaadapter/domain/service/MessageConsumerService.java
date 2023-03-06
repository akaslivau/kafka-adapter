package ru.did.kafkaadapter.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.diasoft.micro.domain.MsgEntity;
import ru.diasoft.micro.domain.repository.MessageRepository;
import ru.diasoft.micro.model.KConsumedMessage;
import ru.diasoft.micro.qsftcmmessagemsgconsumedevent.publish.QsftcmmessageMsgConsumedEventPublishGateway;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.StreamSupport;

import static ru.diasoft.micro.domain.KafkaMessage.DQ_COMMAND_NAME;
import static ru.diasoft.micro.domain.KafkaMessage.DQ_MESSAGE_GUID;

@Component
@RequiredArgsConstructor
public class ConsumeReplyUseCase {
    private final QsftcmmessageMsgConsumedEventPublishGateway gateway;
    private final MessageRepository repo;

    private final Set<Long> proccessedItems = new HashSet<>();

    public void execute(ConsumerRecord<String, String> record) {
        String reply = record.value();
        String topic = record.topic();
        String uuid = getHeader(record, DQ_MESSAGE_GUID);
        String command = getHeader(record, DQ_COMMAND_NAME);

        repo.getByUuidAndTopic(uuid, topic).ifPresent(msg -> {
            if(isProccessed(msg)){
                return;
            }
            KConsumedMessage payload = getPayload(msg, reply);
            Message<KConsumedMessage> message = MessageBuilder
                    .withPayload(payload)
                    .setHeader(DQ_MESSAGE_GUID, uuid)
                    .setHeader(DQ_COMMAND_NAME, command)
                    .build();
            gateway.qsftcmmessageMsgConsumedEvent(message);

            msg.setProcessed(true);
            repo.save(msg);
            proccessedItems.add(msg.getMessageId());
        });
    }

    private boolean isProccessed(MsgEntity msg) {
        return Boolean.TRUE.equals(msg.isProcessed()) ||
                proccessedItems.contains(msg.getMessageId());
    }

    private String getHeader(ConsumerRecord<String, String> record, String key) {
        return StreamSupport.stream(record.headers().spliterator(), false)
                .filter(x -> x.key().equalsIgnoreCase(key))
                .map(x -> convert(x.value())).findFirst().orElse("-");
    }

    private String convert(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private KConsumedMessage getPayload(MsgEntity msg, String reply) {
        return KConsumedMessage.builder()
                .message(reply)
                .comandName(msg.getComandName())
                .messageUuid(msg.getMessageUuid())
                .listenTopicName(msg.getListenTopicName())
                .kafkaUrl(msg.getKafkaUrl())
                .build();
    }
}
