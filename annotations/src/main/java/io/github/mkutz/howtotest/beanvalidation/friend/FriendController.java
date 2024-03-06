package io.github.mkutz.howtotest.beanvalidation.friend;

import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping(
      path = {"friends", "friends/"},
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<FriendDto>> getAll() {
    return ResponseEntity.ok(service.getAll().map(FriendDto::new).toList());
  }

  @GetMapping(path = "friends/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<FriendDto> getSingle(@PathVariable UUID id) {
    return ResponseEntity.of(service.findById(id).map(FriendDto::new));
  }

  @PostMapping(
      path = {"friends", "friends/"},
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<FriendDto> post(
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
