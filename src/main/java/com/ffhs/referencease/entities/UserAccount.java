package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.jpa.UUIDConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
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
  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID userId;
  @Column(unique = true)
  @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Bitte geben Sie eine gültige Email-Adresse ein. (@Pattern Validation)")
  private String email;
  private String password;
  @ManyToMany
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
  private Set<Role> roles;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "employeeId")
  private Employee employee;

  public UserAccount(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
