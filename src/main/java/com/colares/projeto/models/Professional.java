package com.colares.projeto.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "professional")
public class Professional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String specialist;

    @Column(length = 11)
    private String cpf;

    public Professional() {
    }

    public Professional(int id, String name, String specialist, String cpf) {
        this.id = id;
        this.name = name;
        this.specialist = specialist;
        this.cpf = cpf;
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

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "Professional: " + name + ", Specialist: " + specialist + ", CPF: " + cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Professional))
            return false;
        Professional that = (Professional) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(specialist, that.specialist) &&
                Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specialist, cpf);
    }
}
