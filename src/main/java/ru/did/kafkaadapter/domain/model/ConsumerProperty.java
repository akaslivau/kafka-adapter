package ru.did.kafkaadapter.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerProperty {
    private String listenTopic;
    private String kafkaUrl;

    public static ConsumerProperty create(String topic, String kafkaUrl) {
        return ConsumerProperty.builder()
                .listenTopic(topic)
                .kafkaUrl(kafkaUrl).build();
    }
}