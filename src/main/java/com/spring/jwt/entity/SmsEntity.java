package com.spring.jwt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class SmsEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer smsId;
    private Long mobNumber;
    private String otp;
    private String salt;
    private LocalDateTime createdAt;
    private String status;


    public SmsEntity() {
        this.createdAt = LocalDateTime.now();
    }
}
