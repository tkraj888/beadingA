package com.spring.jwt.inspectionReport.controller;

import com.spring.jwt.dto.ResponceDto;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.inspectionReport.Dto.AllInspectionReportDto;
import com.spring.jwt.inspectionReport.Dto.InspectionReportDto;
import com.spring.jwt.inspectionReport.Interface.InspectionReportService;
import com.spring.jwt.inspectionReport.excepation.InspectionReportAlreadyExistsException;
import com.spring.jwt.inspectionReport.excepation.InspectionReportFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inspectionReport")
public class InspectionReportController {

    @Autowired
    private InspectionReportService inspectionReportService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addInspectionReport(@RequestBody InspectionReportDto inspectionReportDto) {
        try {
            InspectionReportDto savedReport = inspectionReportService.addInspectionReport(inspectionReportDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("success", "Inspection report added successfully"));
        }catch (UserNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "user not found"));
        }
        catch (InspectionReportFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "beading car not found"));
        }
        catch (InspectionReportAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseDto("error", "report AlreadyExists by this ID "));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "Error adding inspection report"));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<AllInspectionReportDto> getAllInspectionReports() {
        try {
            List<InspectionReportDto> reports = inspectionReportService.GetAllInspectionReport();
            return ResponseEntity.status(HttpStatus.OK).body(new AllInspectionReportDto("success", reports, null));
        } catch (InspectionReportFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AllInspectionReportDto("unsuccess", null, e.getMessage()));
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getByInspectionReportId(@RequestParam Integer inspectionReportId) {
        try {
            InspectionReportDto report = inspectionReportService.getByInspectionReportId(inspectionReportId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponceDto("Inspection report retrieved successfully", report));
        } catch (InspectionReportFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("Inspection report not found with ID: " + inspectionReportId, null));
        }
    }

    @PatchMapping("/edit")
    public ResponseEntity<ResponseDto> updateInspectionReport(
            @RequestParam Integer inspectionReportId,
            @RequestBody InspectionReportDto inspectionReportDto) {
        try {
            String result = inspectionReportService.editInspectionReport(inspectionReportId, inspectionReportDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success", result));
        } catch (InspectionReportFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("unsuccess", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteInspectionReport(@RequestParam Integer inspectionReportId) {
        try {
            String result = inspectionReportService.deleteInspectionReport(inspectionReportId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success", result));
        } catch (InspectionReportFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("unsuccess", e.getMessage()));
        }
    }

    @GetMapping("/getByBeadingCar")
    public ResponseEntity<?> getByBeadingCarId(@RequestParam Integer beadingCarId) {
        try {
            InspectionReportDto report = inspectionReportService.getByBeadCarId(beadingCarId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponceDto("Inspection report retrieved successfully", report));
        } catch (InspectionReportFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("No inspection report found for BeadingCar ID: " + beadingCarId, null));
        }
    }

    @GetMapping("/getByUserId")
    public ResponseEntity<AllInspectionReportDto> getByUserId(@RequestParam Integer userId) {
        try {
            List<InspectionReportDto> reports = inspectionReportService.getByCarId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(new AllInspectionReportDto("success", reports, null));
        } catch (InspectionReportFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AllInspectionReportDto("unsuccess", null, e.getMessage()));
        }
    }

}


