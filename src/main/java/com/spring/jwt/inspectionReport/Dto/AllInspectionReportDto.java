package com.spring.jwt.inspectionReport.Dto;

import lombok.Data;

import java.util.List;

@Data
public class AllInspectionReportDto {
    private String message;
    private List<?> list;
    private String exception;

    public AllInspectionReportDto(String message) {
        this.message = message;
    }

    public AllInspectionReportDto(String message, List<?> list, String exception) {
        this.message = message;
        this.list = list;
        this.exception = exception;
    }
}
