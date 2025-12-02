package com.spring.jwt.Bidding.Repository;

import com.spring.jwt.entity.SmsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SmsRepo extends JpaRepository<SmsEntity,Integer> {

    SmsEntity findByMobNumberAndOtp(Long mobNumber, String otp);

    List<SmsEntity> findByMobNumber (Long MobNumber);

}
