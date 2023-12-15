package com.ffhs.referencease.entities;

import com.ffhs.referencease.annotations.Unique;
import com.ffhs.referencease.annotations.UniqueEmail;
import com.ffhs.referencease.converters.jpa.UUIDConverter;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "UserAccount")
@NoArgsConstructor
public class UserAccount implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  public UserAccount(String email, String password) {
    this.email = email;
    this.password = password;
  }

  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID userId;

  @Column(unique = true)
//  @Email(message = "Invalid email format.")
  private String email;

  @Column(length = 60)
  private String password;

  @Transient // Diese Annotation sorgt dafür, dass das Feld nicht persistent gespeichert wird
  private String confirmPassword;

  @ManyToMany
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "userId"),
      inverseJoinColumns = @JoinColumn(name = "roleId")
  )
  private Set<Role> roles;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "employeeId")
  private Employee employee;
}
