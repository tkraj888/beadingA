package com.spring.jwt.inspectionReport.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@AllArgsConstructor
@Table(name = "inspectionReport")
public class InspectionReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspectionReportId", nullable = false)
    private Integer inspectionReportId;

    @Column(name = "rCAvailability")
    private String RCAvailability;

    @Column(name = "mismatchInRC")
    private String MismatchInRC;

    @Column(name = "RTONOCIssued")
    private String RTONOCIssued ;

    @Column(name = "insuranceType")
    private String InsuranceType ;

    @Column(name = "noClaimBonus")
    private String NoClaimBonus ;

    @Column(name = "underHypothecation")
    private String UnderHypothecation ;

    @Column(name = "loanStatus")
    private String LoanStatus  ;

    @Column(name = "roadTaxPaid")
    private String RoadTaxPaid  ;

    @Column(name = "partipeshiRequest")
    private String PartipeshiRequest  ;

    @Column(name = "duplicateKey")
    private String DuplicateKey  ;

    @Column(name = "chassisNumberEmbossing")
    private String ChassisNumberEmbossing ;

    @Column(name = "manufacturingDate")
    private Date ManufacturingDate  ;

    @Column(name = "RegistrationDate")
    private Date RegistrationDate   ;

    @Column(name = "RTO")
    private String RTO  ;

    @Column(name = "fitnessUpto")
    private Date FitnessUpto  ;

    @Column(name = "CNGLPGFitmentInRC")
    private String CNGLPGFitmentInRC   ;

    @Column(name = "UserId")
    private int userId;

    @Column(name = "beadingCarId")
    private Integer beadingCarId;

    @Column(name = "NOCStatus")
    private String NOCStatus;

    public InspectionReport() {

    }
}
