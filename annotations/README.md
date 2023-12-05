# Annotations

Annotations are not strictly code.
However, usually they configure the framework to change its behavior quite a lot.
Hence, omitting annotations can cause sever bugs and so their effects should be covered by unit tests.

One obvious way to test this is to start application and check its behavior directly.
This is valid and necessary as the framework's behavior is changed by a lot of things beside the annotations (e.g. the classpath, the application.yml/application.properties or other configuration files, configuration classes that may implicitly depend upon each other, …).

E.g.: [FriendApiTest]

Unfortunately, starting the application usually takes quite some time and testing a lot of different cases on this level is probably not a good idea.
Therefore, the above tests only test very basic cases: one valid and one invalid.

As things like validation –e.g. via bean validation annotations–, and JSON parsing –e.g. via Jackson– both should be tested thoroughly and with a lot of example cases, this project demonstrates some ways to work around the need to start the application.


## Testing JSON/Jackson Annotations

To check if the annotations and configurations for JSON parsing work, we can simply create a `new ObjectMapper()`.
Spring does a lot more than this (see [JacksonAutoConfiguration]).
For example, it "auto-registration for all Module beans with all ObjectMapper beans
(including the defaulted ones)".
This means that our `new ObjectMapper()` isn't configured like the one Spring provides and hence our tests won't produce exactly the same results as we will see in the framework.
However, we only want to demonstrate, that the annotations we use do what the should.
So we can configure our own `ObjectMapper` the way our classes need it and trust in our few integration tests to detect any deviations due to configuration differences.

In [FriendDtoObjectMapperTest] we simply can test two cases for the annotations of [FriendDto]

1. `writeValueAsString`: writing the JSON representation of an object, and
2. `readValue`: reading JSON into an object.

Both tests only run for a couple of millis and hence a lot of variants could be tested.


## Testing Bean Validation Annotations

The bean validation annotations of [FriendDto] are evaluated as the [FriendController] annotates the parameter with `@Valid @RequestBody`.
This causes Spring to validate the given object with a `Validator` before the controller method is even called.
To verify these annotations, we rely on the basic cases in [FriendApiTest].

The `Validator` that is used by Spring is configured via [ValidationAutoConfiguration].
That class mostly takes care of how the validation messages are resolved, while the general validation logic is mostly left to defaults.
So, as long as we don't make detailed tests on the generated validation message, we can simplify use `Validation.buildDefaultValidatorFactory().getValidator()` to create a `Validator` for our test.

In [FriendDtoValidatorTest] there are two parametrized tests that easily could be extended with even more cases as the only run for a few millis.


[FriendApiTest]: <src/integrationTest/java/io/github/mkutz/howtotest/beanvalidation/FriendApiTest.java>
[FriendDto]: <src/main/java/io/github/mkutz/howtotest/beanvalidation/friend/FriendDto.java>
[FriendController]: <src/main/java/io/github/mkutz/howtotest/beanvalidation/friend/FriendController.java>
[FriendDtoObjectMapperTest]: <src/test/java/io/github/mkutz/howtotest/beanvalidation/friend/FriendDtoObjectMapperTest.java>
[FriendDtoValidatorTest]: <src/test/java/io/github/mkutz/howtotest/beanvalidation/friend/FriendDtoValidatorTest.java>
[JacksonAutoConfiguration]: <https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/jackson/JacksonAutoConfiguration.java>
[ValidationAutoConfiguration]: <https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/validation/ValidationAutoConfiguration.java>
