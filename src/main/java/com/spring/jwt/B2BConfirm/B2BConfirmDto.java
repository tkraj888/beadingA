package com.spring.jwt.B2BConfirm;

import com.spring.jwt.entity.Status;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class B2BConfirmDto {

    private Integer b2BConfirmId;

    private Integer b2BId;

    private Integer beadingCarId;

    private Integer buyerDealerId;

    private Integer sellerDealerId;

    private LocalDateTime time;

    private String message;

    private String requestStatus;

    private Integer salesPersonId;

    private String price;
}
