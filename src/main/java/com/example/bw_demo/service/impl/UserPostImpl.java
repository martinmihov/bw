package com.example.bw_demo.service.impl;

import com.example.bw_demo.entity.UserPostEntity;
import com.example.bw_demo.model.PostDto;
import com.example.bw_demo.model.UserDto;
import com.example.bw_demo.model.UserPostDto;
import com.example.bw_demo.repository.UserPostRepository;
import com.example.bw_demo.service.UserPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.bw_demo.util.Constants.MAX_POST_TITLE_LENGTH;

@Service
public class UserPostImpl implements UserPostService {

    private static final Logger log = LoggerFactory.getLogger(UserPostImpl.class);

    private final WebClient webClient;
    private final KafkaTemplate<String, UserPostDto> kafkaTemplate;
    private final String topic;
    private final UserPostRepository userPostRepository;

    @Autowired
    public UserPostImpl(final WebClient webClient,
                        final KafkaTemplate<String, UserPostDto> kafkaTemplate,
                        final Environment env,
                        final UserPostRepository userPostRepository) {
        this.webClient = webClient;
        this.kafkaTemplate = kafkaTemplate;
        this.topic = env.getProperty("${kafka.topic}", "user-posts-topic");
        this.userPostRepository = userPostRepository;
    }

    @Override
    public List<UserPostDto> getUserPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postId").ascending());
        Page<UserPostEntity> pageResult = this.userPostRepository.findAll(pageable);

        List<UserPostDto> result = pageResult.stream()
                .map(e -> new UserPostDto(
                        e.getPostId(),
                        e.getUserName(),
                        e.getUserEmail(),
                        e.getPostTitle(),
                        e.getPostBody()))
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public Mono<Void> mergeAndSend() {
        Mono<List<UserDto>> usersMono = getMonoUserList();
        Mono<List<PostDto>> postsMono = getMonoPostLists();

        return Mono.zip(usersMono, postsMono)
                .flatMap(tuple -> {
                    List<UserDto> users = tuple.getT1();
                    List<PostDto> posts = tuple.getT2();

                    Map<Long, UserDto> userMap = users.stream()
                            .collect(Collectors.toMap(UserDto::getId, u -> u));

                    List<UserPostDto> payloads = posts.stream()
                            .filter(p -> p.getTitle() != null && p.getTitle().length() <= MAX_POST_TITLE_LENGTH)
                            .map(p -> {
                                UserDto user = userMap.get(p.getUserId());
                                return new UserPostDto(p.getId(), user.getUsername(), user.getEmail(), p.getTitle(), p.getBody());
                            }).toList();
                    payloads.forEach(result -> {
                        this.kafkaTemplate.send(topic, String.valueOf(result.getPostId()), result);
                    });

                    return Mono.empty();
                });
    }

    private Mono<List<PostDto>> getMonoPostLists() {
        return this.webClient.get()
                .uri("/posts")
                .retrieve()
                .bodyToFlux(PostDto.class)
                .collectList()
                .timeout(Duration.ofSeconds(20))
                .doOnError(e -> log.error("Failed to fetch posts", e))
                .onErrorReturn(List.of());
    }

    private Mono<List<UserDto>> getMonoUserList() {
        return this.webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(UserDto.class)
                .collectList()
                .timeout(Duration.ofSeconds(20))
                .doOnError(e -> log.error("Failed to fetch users", e))
                .onErrorReturn(List.of());
    }
}
