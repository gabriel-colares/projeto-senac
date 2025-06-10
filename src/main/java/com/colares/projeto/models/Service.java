package com.colares.projeto.models;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @Lob
    @Column
    private String description;

    @Column
    private Double price;

    @Column
    private Integer duration; // em minutos

    public Service() {
    }

    public Service(int id, String name, String description, Double price, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Service: " + name +
                ", Price: $" + price +
                ", Duration: " + duration + " mins";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Service))
            return false;
        Service service = (Service) o;
        return id == service.id &&
                Double.compare(service.price, price) == 0 &&
                Objects.equals(name, service.name) &&
                Objects.equals(description, service.description) &&
                Objects.equals(duration, service.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration);
    }

    // Utilitário para verificar duplicidade de nome
    public static boolean isDuplicateName(List<Service> services, String name) {
        return services.stream().anyMatch(s -> s.getName().equalsIgnoreCase(name));
    }
}
