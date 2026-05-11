package com.example.bw_demo.service;

import com.example.bw_demo.model.UserPostDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserPostService {

    Mono<Void> mergeAndSend();

    List<UserPostDto> getUserPosts(int page, int size);
}