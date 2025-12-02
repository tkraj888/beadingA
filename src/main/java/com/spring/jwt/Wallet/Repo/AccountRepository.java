package com.spring.jwt.Wallet.Repo;


import com.spring.jwt.Wallet.Entity.WalletAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<WalletAccount, Integer> {
    Optional<WalletAccount> findByUserId(Integer userId);
//    Optional<WalletAccount> findByEmail(String email);
//    Optional<WalletAccount> findByMobileNo(String mobileNo);
//    List<WalletAccount> findByStatus(String status);
//    List<WalletAccount> findByOpeningBalanceBetween(Double minBalance, Double maxBalance);
//    List<WalletAccount> findByLastUpdateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}