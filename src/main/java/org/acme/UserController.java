package org.acme;

import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Path("/users")
@RolesAllowed("ADMIN")
@Slf4j
public class UserController {
  @Inject UserService userService;

  @GET
  @NonBlocking
  public Uni<List<UserEntity>> getAll() {
    return userService.getAll();
  }

  @GET
  @Path("/{id}")
  @NonBlocking
  public Uni<UserEntity> getById(@PathParam(value = "id") String id) {
    return userService.getSingle(id);
  }

  @DELETE
  @Path("/{id}")
  @NonBlocking
  public Uni<Boolean> delete(@PathParam(value = "id") String id) {
    return userService.delete(id);
  }

  @POST
  @NonBlocking
  public Uni<UserEntity> create(UserEntity user) {
    return userService.create(user);
  }
}
