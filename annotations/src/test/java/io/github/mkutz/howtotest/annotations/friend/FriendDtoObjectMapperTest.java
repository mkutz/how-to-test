package io.github.mkutz.howtotest.annotations.friend;

import static io.github.mkutz.howtotest.annotations.FriendTestDataBuilder.aFriend;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

class FriendDtoObjectMapperTest {

  static ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

  @Test
  void writeAsString() throws JsonProcessingException {
    var builder = aFriend();

    var json = objectMapper.writeValueAsString(builder.buildDto());

    assertThat(json).isEqualTo(builder.buildJson());
  }

  @Test
  void readValue() throws JsonProcessingException {
    var builder = aFriend();

    var dto = objectMapper.readValue(builder.buildJson(), FriendDto.class);

    assertThat(dto).isEqualTo(builder.buildDto());
  }
}
