package io.github.mkutz.howtotest.annotations.friend;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record FriendDto(
    @NotBlank @Pattern(regexp = "^\\p{L}[\\p{L}\\p{Zs}\\p{N}]+") String firstName,
    @NotBlank @Pattern(regexp = "^\\p{L}[\\p{L}\\p{Zs}\\p{N}]+") String lastName,
    @Email String email,
    @Pattern(regexp = "^[0-9.+()/ ]+") String phoneNumber,
    @NotNull @PastOrPresent @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthday) {

  public FriendDto(Friend friend) {
    this(
        friend.firstName(),
        friend.lastName(),
        friend.email(),
        friend.phoneNumber(),
        friend.birthday());
  }
}
