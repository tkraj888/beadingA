package com.spring.jwt.repository;

import com.spring.jwt.entity.ProfilePhoto1;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfilePhotoRepo1 extends JpaRepository<ProfilePhoto1, Integer> {
    Optional<ProfilePhoto1> findByUserId(Integer userId);
    void deleteByUserId(Integer userId);
}

