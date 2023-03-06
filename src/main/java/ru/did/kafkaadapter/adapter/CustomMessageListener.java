package ru.did.kafkaadapter.domain.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import ru.did.kafkaadapter.domain.model.ConsumerProperty;

import java.util.Optional;
import java.util.Properties;

public abstract class CustomMessageListener {
    private static int NUMBER_OF_LISTENERS = 0;

    @Value("app.kafka.send.group.id")
    private String groupId;

    public abstract KafkaListenerEndpoint createKafkaListenerEndpoint(String name, ConsumerProperty props);

    protected MethodKafkaListenerEndpoint<String, String> createDefaultMethodKafkaListenerEndpoint(
            String name, ConsumerProperty props) {
        MethodKafkaListenerEndpoint<String, String> endpoint =
                new MethodKafkaListenerEndpoint<>();
        endpoint.setId(getConsumerId(name));
        endpoint.setGroupId(Optional.ofNullable(groupId).orElse("unknown"));
        endpoint.setAutoStartup(true);
        endpoint.setTopics(props.getListenTopic());
        endpoint.setConsumerProperties(mapFrom(props));
        endpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());
        return endpoint;
    }

    private Properties mapFrom(ConsumerProperty props) {
        Properties res = new Properties();
        res.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getKafkaUrl());
        res.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        res.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return res;
    }

    private static String getConsumerId(String name) {
        if (isBlank(name)) {
            return CustomMessageListener.class.getCanonicalName() + "#" + NUMBER_OF_LISTENERS++;
        } else {
            return name;
        }
    }

    private static boolean isBlank(String string) {
        return Optional.ofNullable(string)
                .map(StringUtils::isBlank)
                .orElse(true);
    }
}