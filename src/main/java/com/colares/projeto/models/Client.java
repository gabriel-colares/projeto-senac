package com.colares.projeto.models;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.List;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 20)
    private String telephone;

    @Column(length = 100)
    private String email;

    public Client() {
    }

    public Client(int id, String name, String telephone, String email) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.email = email;
    }

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Verifica se há um cliente com o mesmo nome na lista.
     */
    public static boolean isDuplicateName(List<Client> clients, String name) {
        return clients.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name.trim()));
    }

    /**
     * Verifica se há um cliente com o mesmo email na lista.
     */
    public static boolean isDuplicateEmail(List<Client> clients, String email) {
        return clients.stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email.trim()));
    }

    @Override
    public String toString() {
        return "Client: " + name + ", Tel: " + telephone + ", Email: " + email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Client))
            return false;
        Client client = (Client) o;
        return id == client.id &&
                Objects.equals(name, client.name) &&
                Objects.equals(telephone, client.telephone) &&
                Objects.equals(email, client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, telephone, email);
    }
}
