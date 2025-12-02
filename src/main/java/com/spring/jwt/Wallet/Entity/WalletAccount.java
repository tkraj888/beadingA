package com.spring.jwt.Wallet.Entity;

import com.spring.jwt.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "walletAccount")
public class WalletAccount {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "accountId", nullable = false)
        private Integer accountId;

        @Column(name = "openingBalance")
        private Integer openingBalance;

        @Column(name = "status")
        private String status;

        @Column(name = "LastUpdateTime")
        private LocalDateTime lastUpdateTime;

        @Column(name = "panCard")
        private String panCard;

        @ManyToOne
        @JoinColumn(name = "UserId", unique = true)
        private User user;

        @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
        private List<Transaction> transactions;


}
