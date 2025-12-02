package com.spring.jwt.repository;

import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.InspectorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InspectorProfileRepo extends JpaRepository<InspectorProfile,Integer> {

}
