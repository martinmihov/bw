package com.example.bw_demo.kafka;

import com.example.bw_demo.entity.UserPostEntity;
import com.example.bw_demo.model.UserPostDto;
import com.example.bw_demo.repository.UserPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserPostConsumer {

    private static final Logger log = LoggerFactory.getLogger(UserPostConsumer.class);

    private final UserPostRepository userPostRepository;

    @Autowired
    public UserPostConsumer(final UserPostRepository userPostRepository) {
        this.userPostRepository = userPostRepository;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(final UserPostDto result) {
        try {
            final UserPostEntity entity = new UserPostEntity(result.getPostId(), result.getUserName(), result.getUserEmail(),
                    result.getPostTitle(), result.getPostBody());

            this.userPostRepository.save(entity);
            log.debug("Saved postId {}", result.getPostId());
        } catch (Exception e) {
            log.error("Failed to persist entity for postId {}", result.getPostId(), e);
        }
    }
}
