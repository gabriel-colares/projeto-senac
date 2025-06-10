package com.colares.projeto.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 45)
  private String username;

  @Column(nullable = false, length = 100)
  private String password;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  public enum Role {
    ADMIN,
    RECEPCIONISTA,
    PROFISSIONAL
  }

  public User() {
  }

  public User(int id, String name, String username, String password, Role role) {
    this.id = id;
    this.name = name;
    this.username = username;
    this.password = password;
    this.role = role;
  }

  // Getters e Setters

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public String toString() {
    return "User: " + name + " (" + username + "), Role: " + role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof User user))
      return false;
    return id == user.id &&
        Objects.equals(name, user.name) &&
        Objects.equals(username, user.username) &&
        Objects.equals(password, user.password) &&
        role == user.role;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, username, password, role);
  }
}
