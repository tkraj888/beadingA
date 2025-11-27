package com.spring.jwt.entity;

import com.spring.jwt.dto.saveCar.SaveCarDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@Table(name = "save_car", uniqueConstraints = {@UniqueConstraint(columnNames = {"carId", "userId"})})
public class SaveCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saveCarId", nullable = false)
    private Integer saveCarId;

    private  Integer carId;

    private int userId;

    public SaveCar(SaveCarDto saveCarDto) {
        this.saveCarId = saveCarDto.getSaveCarId();
        this.carId = saveCarDto.getCarId();
        this.userId = saveCarDto.getUserId();
    }

    public SaveCar() {
    }

    public void setUser(int user) {
    }

    public void setCar(Car car) {
    }
}
