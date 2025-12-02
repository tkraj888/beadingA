package com.spring.jwt.Color.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@Table(name = "color")
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ColorId", nullable = false)
    private Integer ColorId;

    @Column(name = "name")
    private String name;

    public Color() {
    }
}
