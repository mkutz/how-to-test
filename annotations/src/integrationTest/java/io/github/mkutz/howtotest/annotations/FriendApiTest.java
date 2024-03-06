package io.github.mkutz.howtotest.annotations;

import static io.github.mkutz.howtotest.annotations.FriendTestDataBuilder.aFriend;

import io.github.mkutz.howtotest.annotations.friend.FriendRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = LocalTestApplication.class)
class FriendApiTest {

  @Value("http://localhost:${local.server.port}")
  String baseUrl;

  @Autowired WebTestClient webClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
  @Autowired FriendRepository repository;

  @BeforeEach
  void clearDatabase() {
    repository.deleteAll();
  }

  @Test
  void getAll() {
    var friendBuilder = aFriend();
    repository.save(friendBuilder.buildEntity());

    var actualResponse = webClient.get().uri("/friends/").exchange();

    actualResponse.expectAll(
        response -> response.expectStatus().isEqualTo(200),
        response -> response.expectBody().json("[%s]".formatted(friendBuilder.buildJson())));
  }

  @Test
  void getAll_empty() {
    var actualResponse = webClient.get().uri("/friends/").exchange();

    actualResponse.expectAll(
        response -> response.expectStatus().isEqualTo(200),
        response -> response.expectBody().json("[]"));
  }

  @Test
  void getSingle() {
    var friendBuilder = aFriend();
    var friendId = repository.save(friendBuilder.buildEntity()).getId();

    var actualResponse = webClient.get().uri("/friends/" + friendId).exchange();

    actualResponse.expectAll(
        response -> response.expectStatus().isEqualTo(200),
        response -> response.expectBody().json(friendBuilder.buildJson()));
  }

  @Test
  void getSingle_not_found() {
    var actualResponse = webClient.get().uri("/friends/" + UUID.randomUUID()).exchange();

    actualResponse.expectAll(
        response -> response.expectStatus().isEqualTo(404),
        response -> response.expectBody().isEmpty());
  }

  @Test
  void post() {
    var friendJson = aFriend().buildJson();

    var actualResponse =
        webClient
            .post()
            .uri("/friends/")
            .header("Content-Type", "application/json")
            .bodyValue(friendJson)
            .exchange();

    actualResponse.expectAll(
        response -> response.expectStatus().isEqualTo(HttpStatus.CREATED),
        response -> response.expectHeader().valueMatches("Location", baseUrl + "/friends/.+"),
        response -> response.expectBody().json(friendJson));
  }

  @Test
  void post_invalid_data() {
    var invalidFriendJson = aFriend().invalid().buildJson();

    var actualResponse =
        webClient
            .post()
            .uri("/friends/")
            .header("Content-Type", "application/json")
            .bodyValue(invalidFriendJson)
            .exchange();

    actualResponse.expectAll(
        response -> response.expectStatus().isEqualTo(HttpStatus.BAD_REQUEST),
        response -> response.expectHeader().doesNotExist("Location"),
        response -> response.expectBody().jsonPath("title", "Bad Request"),
        response -> response.expectBody().jsonPath("detail", "Invalid request content."),
        response -> response.expectBody().jsonPath("status", 400));
  }
}
