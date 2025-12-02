package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inspectorProfile")
public class InspectorProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspector_profile_id", nullable = false)
    private int id;

    @Column(name = "address")
    private String address;

    @Column(name = "city", nullable = false, length = 45)
    private String city;

    @Column(name = "firstName", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "status")
    private Boolean status;

    @OneToOne
    @JoinColumn(name = "UserId")
    private User user;
}
