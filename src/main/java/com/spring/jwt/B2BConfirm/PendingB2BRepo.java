package com.spring.jwt.B2BConfirm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingB2BRepo extends JpaRepository<PendingB2B, Integer> {
    List<PendingB2B> findByBeadingCarId(Integer beadingCarId);
}

