package ru.did.kafkaadapter.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;
import ru.did.kafkaadapter.domain.model.ConsumerProperty;

@Component
@RequiredArgsConstructor
@SuppressWarnings({"java:S3740"})
public class KafkaConsumerRegistrar {
    private final BeanFactory beanFactory;

    private final KafkaListenerEndpointRegistry endpointRegistry;
    private final KafkaListenerContainerFactory containerFactory;

    @SneakyThrows
    public void registerCustomKafkaListener(String name, ConsumerProperty props) {
        CustomMessageListener listener =
                (CustomMessageListenerImpl) beanFactory.getBean(Class.forName(CustomMessageListenerImpl.class.getName()));
        endpointRegistry.registerListenerContainer(
                listener.createKafkaListenerEndpoint(name, props),
                containerFactory, true);
    }
}