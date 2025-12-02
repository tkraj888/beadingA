package com.spring.jwt.inspectionReport.repo;

import com.spring.jwt.inspectionReport.entity.InspectionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InspectionReportRepository extends JpaRepository<InspectionReport, Integer> {


    Optional<InspectionReport> findByBeadingCarId(Integer beadingCarId);

    List<InspectionReport> findByUserId(Integer userId);

    boolean existsByBeadingCarId(Integer beadingCarId);


}
