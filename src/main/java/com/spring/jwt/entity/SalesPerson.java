package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "sales_person")
public class SalesPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salesPersonId", nullable = false)
    private Integer salesPersonId;

    @Column(name = "address")
    private String address;

    @Column (name = "first_name")
    private String firstName;

    @Column (name = "last_name")
    private String lastName;

    @Column(name = "document_id")
    private Long  documentId;

    @Column(name = "area")
    private String area;

    @Column (name = "city")
    private String city;

    @Column(name = "joining date")
    private Date joiningdate;

    @Column(name = "status")
    private Boolean status;

    @OneToOne
    @JoinColumn(name = "UserId")
    private User user;
}