package com.spring.jwt.B2BConfirm;

import com.spring.jwt.entity.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "B2BConfirm")
public class B2BConfirm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b2BConfirmIdId", nullable = false)
    private Integer b2BConfirmId;

    @Column(name = "B2BId")
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

    @Column(name = "price")
    private String price;

}
