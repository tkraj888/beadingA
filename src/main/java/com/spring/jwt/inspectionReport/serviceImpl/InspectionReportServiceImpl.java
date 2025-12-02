package com.spring.jwt.inspectionReport.serviceImpl;

import com.spring.jwt.entity.BeadingCAR;
import com.spring.jwt.exception.DealerNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.inspectionReport.Dto.InspectionReportDto;
import com.spring.jwt.inspectionReport.Interface.InspectionReportService;
import com.spring.jwt.inspectionReport.entity.InspectionReport;
import com.spring.jwt.inspectionReport.excepation.InspectionReportAlreadyExistsException;
import com.spring.jwt.inspectionReport.excepation.InspectionReportFoundException;
import com.spring.jwt.inspectionReport.repo.InspectionReportRepository;
import com.spring.jwt.repository.BeadingCarRepo;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InspectionReportServiceImpl implements InspectionReportService {
@Autowired
  private InspectionReportRepository inspectionReportRepository;
@Autowired
  private UserRepository userRepository;
@Autowired
  private BeadingCarRepo beadingCarRepo;

    @Override
    public InspectionReportDto addInspectionReport(InspectionReportDto inspectionReportDto) {
        if (!userRepository.existsById(inspectionReportDto.getUserId())) {
            throw new UserNotFoundExceptions("User not found with ID: " + inspectionReportDto.getUserId());
        }
        if (inspectionReportDto.getBeadingCarId() == null) {
            throw new InspectionReportFoundException("BeadingCarId cannot be null");
        }
        Optional<BeadingCAR> beadingCarOptional = beadingCarRepo.findById(inspectionReportDto.getBeadingCarId());
        if (beadingCarOptional.isEmpty()) {
            throw new InspectionReportFoundException("BeadingCar not found with ID: " + inspectionReportDto.getBeadingCarId());
        }
        if (inspectionReportRepository.existsByBeadingCarId(inspectionReportDto.getBeadingCarId())) {
            throw new InspectionReportAlreadyExistsException("Inspection report already submitted for BeadingCarId: " + inspectionReportDto.getBeadingCarId());
        }
        InspectionReport inspectionReport = convertToEntity(inspectionReportDto);
        InspectionReport savedReport = inspectionReportRepository.save(inspectionReport);
        BeadingCAR beadingCar = beadingCarOptional.get();
        beadingCar.setCarStatus("ACTIVE");
        beadingCar.setInspectionReportId(savedReport.getInspectionReportId());
        beadingCarRepo.save(beadingCar);
        return convertToDto(savedReport);
    }


    @Override
    public List<InspectionReportDto> GetAllInspectionReport() {
        List<InspectionReport> reports = inspectionReportRepository.findAll();
        if (reports.isEmpty()) {
            throw new InspectionReportFoundException("No inspection reports found");
        }
        return reports.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public InspectionReportDto getByInspectionReportId(Integer inspectionReportId) {
        Optional<InspectionReport> optionalReport = inspectionReportRepository.findById(inspectionReportId);
        if (optionalReport.isEmpty()) {
            throw new InspectionReportFoundException("Inspection report not found with ID: " + inspectionReportId);
        }
        return convertToDto(optionalReport.get());
    }

    @Override
    public String editInspectionReport(Integer inspectionReportId, InspectionReportDto inspectionReportDto) {
        Optional<InspectionReport> optionalReport = inspectionReportRepository.findById(inspectionReportId);
        if (optionalReport.isEmpty()) {
            throw new InspectionReportFoundException("Inspection report not found with ID: " + inspectionReportId);
        }
        InspectionReport existingReport = optionalReport.get();
        updateEntity(existingReport, inspectionReportDto);
        inspectionReportRepository.save(existingReport);
        return "Inspection report updated successfully";
    }

    @Override
    public String deleteInspectionReport(Integer inspectionReportId) {
        Optional<InspectionReport> optionalReport = inspectionReportRepository.findById(inspectionReportId);
        if (optionalReport.isEmpty()) {
            throw new InspectionReportFoundException("Inspection report not found with ID: " + inspectionReportId);
        }
        inspectionReportRepository.delete(optionalReport.get());
        return "Inspection report deleted successfully";
    }

    @Override
    public InspectionReportDto getByBeadCarId(Integer beadingCarId) {
        Optional<InspectionReport> optionalReport = inspectionReportRepository.findByBeadingCarId(beadingCarId);
        if (optionalReport.isEmpty()) {
            throw new InspectionReportFoundException("No inspection report found for BeadingCar ID: " + beadingCarId);
        }
        return convertToDto(optionalReport.get());
    }


    @Override
    public List<InspectionReportDto> getByCarId(Integer userId) {
        List<InspectionReport> reports = inspectionReportRepository.findByUserId(userId);
        if (reports.isEmpty()) {
            throw new InspectionReportFoundException("No inspection reports found for User ID: " + userId);
        }
        return reports.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private InspectionReportDto convertToDto(InspectionReport inspectionReport) {
        InspectionReportDto dto = new InspectionReportDto();
        dto.setInspectionReportId(inspectionReport.getInspectionReportId());
        dto.setRCAvailability(inspectionReport.getRCAvailability());
        dto.setMismatchInRC(inspectionReport.getMismatchInRC());
        dto.setRTONOCIssued(inspectionReport.getRTONOCIssued());
        dto.setInsuranceType(inspectionReport.getInsuranceType());
        dto.setNoClaimBonus(inspectionReport.getNoClaimBonus());
        dto.setUnderHypothecation(inspectionReport.getUnderHypothecation());
        dto.setLoanStatus(inspectionReport.getLoanStatus());
        dto.setRoadTaxPaid(inspectionReport.getRoadTaxPaid());
        dto.setPartipeshiRequest(inspectionReport.getPartipeshiRequest());
        dto.setDuplicateKey(inspectionReport.getDuplicateKey());
        dto.setChassisNumberEmbossing(inspectionReport.getChassisNumberEmbossing());
        dto.setManufacturingDate(inspectionReport.getManufacturingDate());
        dto.setRegistrationDate(inspectionReport.getRegistrationDate());
        dto.setRTO(inspectionReport.getRTO());
        dto.setFitnessUpto(inspectionReport.getFitnessUpto());
        dto.setCNGLPGFitmentInRC(inspectionReport.getCNGLPGFitmentInRC());
        dto.setUserId(inspectionReport.getUserId());
        dto.setBeadingCarId(inspectionReport.getBeadingCarId());
        dto.setNOCStatus(inspectionReport.getNOCStatus());
        return dto;
    }

    private InspectionReport convertToEntity(InspectionReportDto dto) {
        InspectionReport entity = new InspectionReport();
        entity.setRCAvailability(dto.getRCAvailability());
        entity.setMismatchInRC(dto.getMismatchInRC());
        entity.setRTONOCIssued(dto.getRTONOCIssued());
        entity.setInsuranceType(dto.getInsuranceType());
        entity.setNoClaimBonus(dto.getNoClaimBonus());
        entity.setUnderHypothecation(dto.getUnderHypothecation());
        entity.setLoanStatus(dto.getLoanStatus());
        entity.setRoadTaxPaid(dto.getRoadTaxPaid());
        entity.setPartipeshiRequest(dto.getPartipeshiRequest());
        entity.setDuplicateKey(dto.getDuplicateKey());
        entity.setChassisNumberEmbossing(dto.getChassisNumberEmbossing());
        entity.setManufacturingDate(dto.getManufacturingDate());
        entity.setRegistrationDate(dto.getRegistrationDate());
        entity.setRTO(dto.getRTO());
        entity.setFitnessUpto(dto.getFitnessUpto());
        entity.setCNGLPGFitmentInRC(dto.getCNGLPGFitmentInRC());
        entity.setUserId(dto.getUserId());
        entity.setBeadingCarId(dto.getBeadingCarId());
        entity.setNOCStatus(dto.getNOCStatus());
        return entity;
    }

    private void updateEntity(InspectionReport entity, InspectionReportDto dto) {
        if (dto.getRCAvailability() != null) {
            entity.setRCAvailability(dto.getRCAvailability());
        }
        if (dto.getMismatchInRC() != null) {
            entity.setMismatchInRC(dto.getMismatchInRC());
        }
        if (dto.getRTONOCIssued() != null) {
            entity.setRTONOCIssued(dto.getRTONOCIssued());
        }
        if (dto.getInsuranceType() != null) {
            entity.setInsuranceType(dto.getInsuranceType());
        }
        if (dto.getNoClaimBonus() != null) {
            entity.setNoClaimBonus(dto.getNoClaimBonus());
        }
        if (dto.getUnderHypothecation() != null) {
            entity.setUnderHypothecation(dto.getUnderHypothecation());
        }
        if (dto.getLoanStatus() != null) {
            entity.setLoanStatus(dto.getLoanStatus());
        }
        if (dto.getRoadTaxPaid() != null) {
            entity.setRoadTaxPaid(dto.getRoadTaxPaid());
        }
        if (dto.getPartipeshiRequest() != null) {
            entity.setPartipeshiRequest(dto.getPartipeshiRequest());
        }
        if (dto.getDuplicateKey() != null) {
            entity.setDuplicateKey(dto.getDuplicateKey());
        }
        if (dto.getChassisNumberEmbossing() != null) {
            entity.setChassisNumberEmbossing(dto.getChassisNumberEmbossing());
        }
        if (dto.getManufacturingDate() != null) {
            entity.setManufacturingDate(dto.getManufacturingDate());
        }
        if (dto.getRegistrationDate() != null) {
            entity.setRegistrationDate(dto.getRegistrationDate());
        }
        if (dto.getRTO() != null) {
            entity.setRTO(dto.getRTO());
        }
        if (dto.getFitnessUpto() != null) {
            entity.setFitnessUpto(dto.getFitnessUpto());
        }
        if (dto.getCNGLPGFitmentInRC() != null) {
            entity.setCNGLPGFitmentInRC(dto.getCNGLPGFitmentInRC());
        }
        if (dto.getNOCStatus() != null) {
            entity.setNOCStatus(dto.getNOCStatus());
        }
    }
}

