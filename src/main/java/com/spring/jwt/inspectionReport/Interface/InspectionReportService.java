package com.spring.jwt.inspectionReport.Interface;

import com.spring.jwt.inspectionReport.Dto.InspectionReportDto;

import java.util.List;

public interface InspectionReportService {

    InspectionReportDto addInspectionReport(InspectionReportDto inspectionReportDto);

    List<InspectionReportDto> GetAllInspectionReport();

    public InspectionReportDto getByInspectionReportId(Integer inspectionReportId);

    String editInspectionReport(Integer inspectionReportId,InspectionReportDto inspectionReportDto);

    public String deleteInspectionReport(Integer inspectionReportId);

    public InspectionReportDto getByBeadCarId(Integer beadingCarId);

    List<InspectionReportDto> getByCarId(Integer userId);

}
