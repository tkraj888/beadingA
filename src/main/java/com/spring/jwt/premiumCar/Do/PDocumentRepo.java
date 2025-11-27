package com.spring.jwt.premiumCar.Do;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDocumentRepo extends JpaRepository<PDocument,Integer> {
    @Query("SELECT jfq FROM PDocument jfq WHERE jfq.userId = :userId AND jfq.documentType = :documentType")
    public List<PDocument> findByDocumentTypeAndUserID(Integer userId, String documentType);
    @Query("SELECT jfq FROM PDocument jfq WHERE jfq.userId = :userId")
    public List<PDocument> findByUserId(Integer userId);

    @Query("SELECT jfq FROM PDocument jfq WHERE jfq.premiumCarId = :premiumCarId")

    List<PDocument> findByPremiumCarId(Integer premiumCarId);
}