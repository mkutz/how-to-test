package io.github.mkutz.howtotest.beanvalidation;

import static io.github.mkutz.howtotest.beanvalidation.FriendTestDataBuilder.aFriend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class FriendApiTest {

  @Value("http://localhost:${local.server.port}")
  String baseUrl;

  @Autowired WebTestClient webClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();

  @Test
  void postFriend() {
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
  void postFriend_invalid_data() {
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
