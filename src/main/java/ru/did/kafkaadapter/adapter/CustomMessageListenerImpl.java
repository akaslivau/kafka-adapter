package ru.did.kafkaadapter.adapter;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;
import ru.did.kafkaadapter.domain.model.ConsumerProperty;
import ru.did.kafkaadapter.domain.service.MessageConsumerService;

/**
 * Service to perform some actions, when message consumed from "foreign" kafka
 */
@Component
@RequiredArgsConstructor
public class CustomMessageListenerImpl extends CustomMessageListener {
    private final MessageConsumerService consumerService;

    @Override
    @SneakyThrows
    public KafkaListenerEndpoint createKafkaListenerEndpoint(String name, ConsumerProperty props) {
        MethodKafkaListenerEndpoint<String, String> kafkaListenerEndpoint =
                createDefaultMethodKafkaListenerEndpoint(name, props);
        kafkaListenerEndpoint.setBean(new MyMessageListener());
        kafkaListenerEndpoint.setMethod(MyMessageListener.class.getMethod(
                "onMessage", ConsumerRecord.class));
        return kafkaListenerEndpoint;
    }

    private class MyMessageListener implements MessageListener<String, String> {
        @Override
        public void onMessage(ConsumerRecord<String, String> record) {
            consumerService.execute(record);
        }
    }
}

