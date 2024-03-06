package io.github.mkutz.howtotest.annotations.friend;

import java.time.LocalDate;
import java.util.UUID;

public record Friend(
    UUID id,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    LocalDate birthday) {

  public Friend(FriendEntity entity) {
    this(
        entity.getId(),
        entity.getFirstName(),
        entity.getLastName(),
        entity.getEmail(),
        entity.getPhoneNumber(),
        entity.getBirthday());
  }

  public FriendEntity toEntity() {
    final var entity = new FriendEntity();
    entity.setId(id);
    entity.setFirstName(firstName);
    entity.setLastName(lastName);
    entity.setEmail(email);
    entity.setPhoneNumber(phoneNumber);
    entity.setBirthday(birthday);
    return entity;
  }
}
