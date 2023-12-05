package io.github.mkutz.howtotest.beanvalidation.friend;

import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController("friends")
public class FriendController {

  private final FriendService service;

  public FriendController(FriendService service) {
    this.service = requireNonNull(service);
  }

  @PostMapping(
      path = {"friends", "friends/"},
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<FriendDto> postFriend(
      @Valid @RequestBody FriendDto dto, UriComponentsBuilder uriComponentsBuilder) {
    var friendId = randomUUID();
    var friend =
        new Friend(
            friendId,
            dto.firstName(),
            dto.lastName(),
            dto.email(),
            dto.phoneNumber(),
            dto.birthday());
    service.add(friend);

    return ResponseEntity.created(
            uriComponentsBuilder.path("friends/").path(friendId.toString()).build().toUri())
        .body(new FriendDto(friend));
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<ProblemDetail> handleValidationExceptions(
      WebExchangeBindException validationException) {
    return ResponseEntity.badRequest().body(validationException.getBody());
  }
}
