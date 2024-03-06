package io.github.mkutz.howtotest.annotations.friend;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class FriendService {

  private final FriendRepository repository;

  public FriendService(FriendRepository repository) {
    this.repository = repository;
  }

  public Stream<Friend> getAll() {
    return repository.findAll().stream().map(Friend::new);
  }

  public void add(Friend friend) {
    repository.save(friend.toEntity());
  }

  public Optional<Friend> findById(UUID id) {
    return repository.findById(id).map(Friend::new);
  }
}
