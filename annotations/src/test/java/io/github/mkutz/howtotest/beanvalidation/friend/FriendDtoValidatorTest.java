package io.github.mkutz.howtotest.beanvalidation.friend;

import static io.github.mkutz.howtotest.beanvalidation.FriendTestDataBuilder.aFriend;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FriendDtoValidatorTest {

  static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  static Stream<FriendDto> validFriendDtos() {
    return Stream.of(
        aFriend()
            .id(null)
            .name("Somé", "Bødy Ⅲ")
            .email("somebody@mkutz.github.io")
            .phoneNumber("+49 (123) 555456")
            .birthday(LocalDate.of(1982, 2, 19))
            .buildDto(),
        aFriend().birthday(LocalDate.now()).buildDto());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("validFriendDtos")
  void validate_valid(FriendDto valid) {
    assertThat(validator.validate(valid)).isEmpty();
  }

  static Stream<Arguments> invalidFriendDtos() {
    return Stream.of(
        arguments(aFriend().lastName("456").buildDto(), List.of("lastName")),
        arguments(aFriend().firstName("123").buildDto(), List.of("firstName")),
        arguments(aFriend().lastName("Bødy\nⅢ").buildDto(), List.of("lastName")),
        arguments(aFriend().email("invalid").buildDto(), List.of("email")),
        arguments(aFriend().phoneNumber("abc").buildDto(), List.of("phoneNumber")),
        arguments(aFriend().birthday(LocalDate.now().plusDays(1)).buildDto(), List.of("birthday")));
  }

  @ParameterizedTest(name = "{0} invalid {1}")
  @MethodSource("invalidFriendDtos")
  void validate_invalid(FriendDto invalid, List<String> invalidPaths) {
    assertThat(validator.validate(invalid))
        .extracting(violation -> violation.getPropertyPath().toString())
        .containsExactlyInAnyOrderElementsOf(invalidPaths);
  }
}
