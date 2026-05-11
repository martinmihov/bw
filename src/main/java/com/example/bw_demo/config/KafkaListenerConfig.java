package com.example.bw_demo.config;

import com.example.bw_demo.model.UserPostDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.*;

@Configuration
public class KafkaListenerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, UserPostDto> consumerFactory() {
        JacksonJsonDeserializer<UserPostDto> deserializer = new JacksonJsonDeserializer<>(UserPostDto.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "user-posts-group");

        return new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserPostDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserPostDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
