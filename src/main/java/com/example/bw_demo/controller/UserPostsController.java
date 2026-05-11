package com.example.bw_demo.controller;

import com.example.bw_demo.model.UserPostDto;
import com.example.bw_demo.service.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/user-posts")
public class UserPostsController {

    private final UserPostService userPostService;

    @Autowired
    public UserPostsController(final UserPostService userPostService) {
        this.userPostService = userPostService;
    }

    @PostMapping("/gather")
    public Mono<ResponseEntity<String>> gather() {

        return this.userPostService.mergeAndSend().then(Mono.just(ResponseEntity.accepted().body("Gathering started")));
    }

    @GetMapping
    public ResponseEntity<List<UserPostDto>> getUserPosts(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {

        final List<UserPostDto> payloads = this.userPostService.getUserPosts(page, size);

        return ResponseEntity.ok(payloads);
    }
}
