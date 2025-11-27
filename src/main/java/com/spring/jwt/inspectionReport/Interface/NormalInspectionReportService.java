package com.spring.jwt.inspectionReport.Interface;
import com.spring.jwt.inspectionReport.Dto.InspectionReportDto;
import com.spring.jwt.inspectionReport.Dto.NormalInspectionReportDto;

import java.util.List;

public interface NormalInspectionReportService {


    NormalInspectionReportDto addInspectionReport(NormalInspectionReportDto normalInspectionReportDto);

    List<NormalInspectionReportDto> GetAllInspectionReport();

    public NormalInspectionReportDto getByInspectionReportId(Integer inspectionReportId);

    String editInspectionReport(Integer NormalInspectionReportId,NormalInspectionReportDto normalInspectionReportDto);

    public String deleteInspectionReport(Integer NormalInspectionReportId);

    public NormalInspectionReportDto getByBeadCarId(Integer CarId);

    List<NormalInspectionReportDto> getByCarId(Integer userId);

}
