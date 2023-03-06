package ru.did.kafkaadapter.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("sendTopicName")
    private String sendTopicName;

    @JsonProperty("listenTopicName")
    private String listenTopicName;

    @JsonProperty("message")
    private String message;

    @JsonProperty("kafkaUrl")
    private String kafkaUrl;

    @JsonProperty("messageUuid")
    private String messageUuid;

    @JsonProperty("headerCommand")
    private String headerCommand;
}
