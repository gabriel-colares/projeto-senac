package com.colares.projeto.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "moneybox")
public class MoneyBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String type;

    @Lob
    @Column
    private String description;

    @Column
    private Double value;

    @Column(nullable = false)
    private LocalDateTime datetime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public MoneyBox() {
    }

    public MoneyBox(int id, String type, String description, Double value, LocalDateTime datetime, User user) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.value = value;
        this.datetime = datetime;
        this.user = user;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "MoneyBox: " + type + ", $" + value + ", Description: " + description + ", Date: " + datetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MoneyBox))
            return false;
        MoneyBox moneyBox = (MoneyBox) o;
        return id == moneyBox.id &&
                Double.compare(moneyBox.value, value) == 0 &&
                Objects.equals(type, moneyBox.type) &&
                Objects.equals(description, moneyBox.description) &&
                Objects.equals(datetime, moneyBox.datetime) &&
                Objects.equals(user, moneyBox.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, description, value, datetime, user);
    }
}
