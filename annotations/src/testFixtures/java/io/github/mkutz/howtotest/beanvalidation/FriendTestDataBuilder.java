package io.github.mkutz.howtotest.beanvalidation;

import static java.util.UUID.randomUUID;

import io.github.mkutz.howtotest.beanvalidation.friend.Friend;
import io.github.mkutz.howtotest.beanvalidation.friend.FriendDto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.rng.simple.RandomSource;

public class FriendTestDataBuilder {

  private static final String JSON_FORMAT =
      "{\"firstName\":\"%s\","
          + "\"lastName\":\"%s\","
          + "\"email\":\"%s\","
          + "\"phoneNumber\":\"%s\","
          + "\"birthday\":\"%s\"}";
  private UUID id = randomUUID();
  private String firstName = RandomStringUtils.randomAlphabetic(6, 12);
  private String lastName = RandomStringUtils.randomAlphabetic(6, 12);
  private String email =
      "%s@%s.org"
          .formatted(
              RandomStringUtils.randomAlphabetic(6, 12), RandomStringUtils.randomAlphabetic(6, 12));
  private String phoneNumber = "+1 (234) 555%s".formatted(RandomStringUtils.randomNumeric(3, 9));
  private LocalDate birthday = LocalDate.now().minusYears(RandomSource.JDK.create().nextInt(0, 99));

  private FriendTestDataBuilder() {}

  public static FriendTestDataBuilder aFriend() {
    return new FriendTestDataBuilder();
  }

  public FriendTestDataBuilder invalid() {
    this.email = "invalid";
    return this;
  }

  public FriendTestDataBuilder id(UUID id) {
    this.id = id;
    return this;
  }

  public FriendTestDataBuilder firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public FriendTestDataBuilder lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public FriendTestDataBuilder name(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    return this;
  }

  public FriendTestDataBuilder email(String email) {
    this.email = email;
    return this;
  }

  public FriendTestDataBuilder phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  public FriendTestDataBuilder birthday(LocalDate birthday) {
    this.birthday = birthday;
    return this;
  }

  public Friend build() {
    return new Friend(id, firstName, lastName, email, phoneNumber, birthday);
  }

  public FriendDto buildDto() {
    return new FriendDto(build());
  }

  public String buildJson() {
    return JSON_FORMAT.formatted(
        firstName,
        lastName,
        email,
        phoneNumber,
        birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
  }
}
