package io.github.mkutz.howtotest.beanvalidation.friend;

import java.time.LocalDate;
import java.util.UUID;

public record Friend(
    UUID id,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    LocalDate birthday) {}
