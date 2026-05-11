package com.example.bw_demo;

import com.example.bw_demo.model.PostDto;
import com.example.bw_demo.model.UserDto;
import com.example.bw_demo.model.UserPostDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka
@SpringBootTest
@ActiveProfiles("test")
class BwDemoApplicationTests {

	@Test
	void mergesPostsWithUsersCorrectly() {
		List<UserDto> users = List.of(
				new UserDto(1L, "Alice", "aliceTheMalice", "alice@example.com"),
				new UserDto(2L, "Bob", "bobo", "bob@example.com")
		);

		List<PostDto> posts = List.of(
				new PostDto(1L, 101L, "Short title", "body1"),
				new PostDto(2L, 102L, "Another title", "body2"),
				new PostDto(2L, 103L, "One more title", "body3")
		);

		Map<Long, UserDto> userMap = users.stream().collect(Collectors.toMap(UserDto::getId, u -> u));

		List<UserPostDto> merged = posts.stream()
				.filter(p -> p.getTitle() != null && p.getTitle().length() <= 200)
				.map(p -> {
					UserDto user = userMap.get(p.getUserId());
					return new UserPostDto(p.getId(), user.getUsername(), user.getEmail(), p.getTitle(), p.getBody());
				})
				.collect(Collectors.toList());

		assertThat(merged).hasSize(3);
		assertThat(merged.get(0).getUserName()).isEqualTo("aliceTheMalice");
		assertThat(merged.get(1).getUserEmail()).isEqualTo("bob@example.com");
		assertThat(merged.get(2).getUserName()).isEqualTo("bobo");
	}
}
