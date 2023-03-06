package ru.did.kafkaadapter.domain.service;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KafkaTemplateFactory {
    private final ConcurrentHashMap<String, KafkaTemplate<String, String>> templates = new ConcurrentHashMap<>();

    public KafkaTemplate<String, String> get(String kafkaUrl) {
        if (templates.containsKey(kafkaUrl))
            return templates.get(kafkaUrl);

        KafkaTemplate<String, String> kafkaTemplate = createNew(kafkaUrl);
        templates.put(kafkaUrl, kafkaTemplate);
        return kafkaTemplate;
    }

    private KafkaTemplate<String, String> createNew(String kafkaUrl) {
        Map<String, Object> producerConfigs = getConfigs(kafkaUrl);
        ProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(producerConfigs);
        return new KafkaTemplate<>(factory);
    }

    private Map<String, Object> getConfigs(String kafkaUrl) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }
}
