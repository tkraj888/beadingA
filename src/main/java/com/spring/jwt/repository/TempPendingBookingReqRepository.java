package com.spring.jwt.repository;

import com.spring.jwt.entity.TempPendingBookingReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface TempPendingBookingReqRepository extends JpaRepository<TempPendingBookingReq, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM TempPendingBookingReq t WHERE t.createdDate < :cutoffDate")
    void deleteOlderThan(LocalDateTime cutoffDate);
}