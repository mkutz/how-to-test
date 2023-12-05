package io.github.mkutz.howtotest.beanvalidation.friend;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FriendService {

  private final List<Friend> friends = new ArrayList<>();

  public List<Friend> getAll() {
    return friends;
  }

  public void add(Friend friend) {
    friends.add(friend);
  }
}
