package com.spring.jwt.Wallet.Dto;

import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WalletAccountDTO {
    private Integer AccountId;
    private Double OpeningBalance;
    private String Status;
    private LocalDateTime LastUpdateTime;
    private String PanCard;
    private int userId;

}