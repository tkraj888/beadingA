package com.spring.jwt.inspectionReport.serviceImpl;

import com.spring.jwt.exception.UserNotFoundExceptions;

import com.spring.jwt.inspectionReport.Dto.NormalInspectionReportDto;
import com.spring.jwt.inspectionReport.Interface.NormalInspectionReportService;

import com.spring.jwt.inspectionReport.excepation.InspectionReportAlreadyExistsException;
import com.spring.jwt.inspectionReport.excepation.InspectionReportFoundException;
import com.spring.jwt.inspectionReport.repo.InspectionReportRepository;
import com.spring.jwt.repository.BeadingCarRepo;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class NormalInspectionReportServiceImpl implements NormalInspectionReportService {


    @Autowired
    private InspectionReportRepository inspectionReportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarRepo beadingCarRepo;

    @Override
    public NormalInspectionReportDto addInspectionReport(NormalInspectionReportDto normalInspectionReportDto) {
        return null;
    }

    @Override
    public List<NormalInspectionReportDto> GetAllInspectionReport() {
        return null;
    }

    @Override
    public NormalInspectionReportDto getByInspectionReportId(Integer inspectionReportId) {
        return null;
    }

    @Override
    public String editInspectionReport(Integer NormalInspectionReportId, NormalInspectionReportDto normalInspectionReportDto) {
        return null;
    }

    @Override
    public String deleteInspectionReport(Integer NormalInspectionReportId) {
        return null;
    }

    @Override
    public NormalInspectionReportDto getByBeadCarId(Integer CarId) {
        return null;
    }

    @Override
    public List<NormalInspectionReportDto> getByCarId(Integer userId) {
        return null;
    }
}
