package com.spring.jwt.inspectionReport.repo;

import com.spring.jwt.inspectionReport.entity.InspectionReport;
import com.spring.jwt.inspectionReport.entity.NormalInspectionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface NormalInspectionReportRepository extends JpaRepository<InspectionReport, Integer> {



}
