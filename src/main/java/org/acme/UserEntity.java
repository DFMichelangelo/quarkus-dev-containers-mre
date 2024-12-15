package org.acme;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Slf4j
@ToString(exclude = "password")
@Table(name = "Users")
public class UserEntity extends PanacheEntityBase {
  @Id
  @UuidGenerator
  @Column(name = "id", updatable = false, nullable = false, length = 36)
  private String id;

  private String name;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "roles", nullable = false)
  @Convert(converter = SetConverter.class)
  private Set<String> roles = Set.of("ADMIN");

  @Column private String resetPasswordToken;

  public void copyFrom(UserEntity from) {
    setId(from.getId());
    setName(from.getName());
    setEmail(from.getEmail());
    setPassword(from.getPassword());
    setRoles(from.getRoles());
    setResetPasswordToken(from.getResetPasswordToken());
  }
}
