package com.spring.jwt.inspectionReport.excepation;

import org.springframework.http.HttpStatus;

public class InspectionReportFoundException extends RuntimeException{

    private final String message;
    private HttpStatus httpStatus;
    public InspectionReportFoundException(String message) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
    public String getMessage() {
        return message;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

