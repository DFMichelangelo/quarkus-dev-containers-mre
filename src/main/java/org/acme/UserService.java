package org.acme;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@ApplicationScoped
@Slf4j
public class UserService{
  @Inject UserRepository userRepository;

  @WithTransaction
  public Uni<List<UserEntity>> getAll() {
    return userRepository.listAll();
  }

  @WithTransaction
  public Uni<UserEntity> getSingle(String id) {
    return userRepository.findById(id);
  }

  @WithTransaction
  public Uni<UserEntity> getSingleByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @WithTransaction
  public Uni<Boolean> delete(String id) {
    return userRepository.deleteById(id);
  }

  @WithTransaction
  public Uni<UserEntity> create(UserEntity user) {
    return Uni.createFrom()
        .item(user)
        .flatMap(userRepository::persistAndFlush);
  }

  @WithTransaction
  public Uni<UserEntity> update(UserEntity user) {
    return getSingle(user.getId())
            .onItem()
            .ifNotNull()
            .transformToUni(
                    retrievedUser -> {
                      retrievedUser.copyFrom(user);
                      return retrievedUser.persist();
                    });
  }

}
