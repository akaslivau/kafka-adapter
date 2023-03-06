package ru.did.kafkaadapter.domain.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@Setter
public class KafkaMessage {

    public static final String MSG_UUID = "msgUuid";

    private String topic;
    private String kafkaUrl;
    private String payload;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Map<String, Object> headers = new HashMap<>();

    public Message<String> build() {
        MessageBuilder<String> builder = MessageBuilder.withPayload(payload);
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            builder.setHeader(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    public void setHeader(String headerName, @Nullable Object headerValue) {
        headers.put(headerName, headerValue);
    }
}
