package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name= "b2bCars")
public class B2BCars {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer b2bCarId;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "carId")
    private Integer carId;

    @Column(name = "dealerId")
    private Integer dealerId;

    @Column(name = "time")
    private LocalDate Time;

}
