package com.spring.jwt.Wallet.Entity;

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
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId", nullable = false)
    private Integer transactionId;

    @Column(name = "type", nullable = false, length = 250)
    private String type;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "closingBalance")
    private Double closingBalance;

    @Column(name = "status")
    private String status;

    @Column(name = "LastUpdateTime")
    private LocalDateTime lastUpdateTime;

    @Column(name = "otherAccountNo")
    private Integer otherAccountNo;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false) // Update this line
    private WalletAccount account;

    public void setUser() {
    }
}
