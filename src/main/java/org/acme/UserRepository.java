package org.acme;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class UserRepository implements PanacheRepositoryBase<UserEntity, String> {

  @WithTransaction
  public Uni<UserEntity> findByEmail(String email) {
    return find("email = :email", Parameters.with("email", email))
        .firstResult();
  }

  @WithTransaction
  public Uni<UserEntity> findByToken(String reset_password_token) {
    return find(
            "resetPasswordToken = :reset_password_token",
            Parameters.with("reset_password_token", reset_password_token))
        .firstResult()
        .log("findByToken");
  }
}
