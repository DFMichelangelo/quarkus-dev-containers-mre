package org.acme;

import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpHeaders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static io.restassured.RestAssured.given;

@QuarkusTest
class UserControllerTest {
  @Inject UserService userService;
  @Inject AuthenticationUtils authenticationUtils;

  @Test
  @RunOnVertxContext
  void getAll(TransactionalUniAsserter asserter) {
    asserter.assertThat(
        () ->
            authenticationUtils
                .getAdmin()
                .map(
                    userEntity ->
                        given()
                            .when()
                            .contentType(ContentType.JSON)
                            .accept(ContentType.JSON)
                            .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + userEntity.getId()+ "A bear!")
                            .get("/users")
                            .then()),
        validatableResponse ->
            validatableResponse
                .statusCode(Response.Status.OK.getStatusCode())
                .body("$", Matchers.hasSize(Matchers.greaterThan(0))));
  }

  @Test
  @RunOnVertxContext
  void getSingle(TransactionalUniAsserter asserter) {
    asserter.assertThat(
        () ->
            authenticationUtils
                .getAdmin()
                .map(
                        userEntity ->
                        given()
                            .when()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + userEntity.getId()+ "A bear!")
                            .get("/users/91625c14-cf09-43d6-9559-1a78636f3ab3")
                            .then()),
        validatableResponse ->
            validatableResponse
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", Matchers.equalTo("91625c14-cf09-43d6-9559-1a78636f3ab3")));
  }

  @Test
  @RunOnVertxContext
  void create_and_delete_as_admin(TransactionalUniAsserter asserter) {
    UserEntity user = new UserEntity();
    user.setPassword("password");
    user.setEmail("john.remove@example.com");
    user.setRoles(Set.of("USER"));
    asserter
        .assertThat(
            () ->
                authenticationUtils
                    .getAdmin()
                    .map(
                            userEntity ->
                            given()
                                .body(user)
                                .when()
                                .header("Content-Type", "application/json")
                                .header("Accept", "application/json")
                                .header(
                                    HttpHeaders.AUTHORIZATION,
                                    "Bearer " + userEntity.getId()+ "A bear!")
                                .post("/users")
                                .then()),
            validatableResponse ->
                validatableResponse.statusCode(Response.Status.OK.getStatusCode()))
        .assertThat(
            () ->
                authenticationUtils
                    .getAdmin()
                    .flatMap(
                            userEntity ->
                            userService
                                .getSingleByEmail("john.remove@example.com")
                                .map(
                                    u ->
                                        given()
                                            .when()
                                            .header("Content-Type", "application/json")
                                            .header("Accept", "application/json")
                                            .header(
                                                HttpHeaders.AUTHORIZATION,
                                                "Bearer " + userEntity.getId()+ "A bear!")
                                            .delete("/users/" + u.getId())
                                            .then())),
            validatableResponse ->
                validatableResponse.statusCode(Response.Status.OK.getStatusCode()))
        .assertNull(() -> userService.getSingle(user.getId()));
  }

  @Test
  @RunOnVertxContext
  void getAll_as_normal_user(TransactionalUniAsserter asserter) {
    asserter.assertThat(
        () ->
            authenticationUtils
                .getUser()
                .map(
                        userEntity ->
                        given()
                            .when()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + userEntity.getId()+ "A bear!")
                            .get("/users")
                            .then()),
        validatableResponse ->
            validatableResponse.statusCode(Response.Status.OK.getStatusCode()));
  }

  @Test
  @RunOnVertxContext
  void not_get_as_normal_user(TransactionalUniAsserter asserter) {
    asserter.assertThat(
        () ->
            authenticationUtils
                .getUser()
                .map(
                        userEntity ->
                        given()
                            .when()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + userEntity.getId()+ "A bear!")
                            .get("/users/91625c14-cf09-43d6-9559-1a78636f3ab3")
                            .then()),
        validatableResponse ->
            validatableResponse.statusCode(Response.Status.OK.getStatusCode()));
  }

  @Test
  @RunOnVertxContext
  void not_delete_as_normal_user(TransactionalUniAsserter asserter) {
    asserter.assertThat(
        () ->
            authenticationUtils
                .getUser()
                .map(
                        userEntity ->
                        given()
                            .when()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer "+ userEntity.getId()+ "A bear!")
                            .delete("/users/d3f8b7a1-42c9-4e65-8a2b-7c4f1e9d2b10")
                            .then()),
        validatableResponse ->
            validatableResponse.statusCode(Response.Status.OK.getStatusCode()));
  }

  @Test
  @RunOnVertxContext
  void not_create_as_normal_user(TransactionalUniAsserter asserter) {
    asserter.assertThat(
        () ->
            authenticationUtils
                .getUser()
                .map(
                        userEntity ->
                        given()
                            .when()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + userEntity.getId()+ "A bear!")
                            .post("/users")
                            .then()),
        validatableResponse ->
            validatableResponse.statusCode(Response.Status.OK.getStatusCode()));
  }
}
