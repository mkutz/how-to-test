package io.github.mkutz.howtotest.annotations.friend;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<FriendEntity, UUID> {}
