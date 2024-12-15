package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class AuthenticationUtils {
  @Inject UserService userService;

  public Uni<UserEntity> getAdmin() {
    return userService.getSingleByEmail("admin@test.com")
        .invoke(e -> log.info("admin retrieved: {}", e));
  }

  public Uni<UserEntity> getUser() {
    return userService.getSingleByEmail("user@test.com")
            .invoke(e -> log.info("user retrieved: {}", e));
  }
}
