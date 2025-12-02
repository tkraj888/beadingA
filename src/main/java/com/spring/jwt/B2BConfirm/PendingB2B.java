package com.spring.jwt.B2BConfirm;

import com.spring.jwt.entity.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PendingB2B")
public class PendingB2B {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "B2BId", nullable = false)
    private Integer b2BId;

    @Column(name = "beadingCarId")
    private Integer beadingCarId;

    @Column(name = "buyerDealerId")
    private Integer buyerDealerId;

    @Column(name = "sellerDealerId")
    private Integer sellerDealerId;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "message")
    private String message;

    @Column(name = "requestStatus")
    private String requestStatus;

    @Column(name = "salesPersonId")
    private Integer salesPersonId;

    @Column(name = "CreatedTime")
    private Date CreatedTime;

}
