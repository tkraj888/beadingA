package com.spring.jwt.userForm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "userForm")
public class UserForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer userFormId;

    public String carOwnerName;

    public String brand;

    public String model;

    private String variant;

    public String regNo;

    public String address1;

    public String address2;

    public  Integer pinCode;

    public  String rc;

    public LocalDateTime inspectionDate;

    public String mobileNo;

    public LocalDateTime createdTime;

    public String status;

    public Integer userId;

    private Integer salesPersonId;

    public Integer inspectorId;




}
