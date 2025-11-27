package com.spring.jwt.dto.BeadingCAR;

import com.spring.jwt.dto.BidCarDto;
import com.spring.jwt.inspectionReport.Dto.InspectionReportDto;
import lombok.Data;

import java.util.List;
@Data
public class BeadingCarDetailsDto {
    private List<InspectionReportDto> inspectionReports;
    private List<BeadingCARDto> beadingCars;
    private List<BidCarDto> bidCarPhotos;

    // Getters and setters...
}
