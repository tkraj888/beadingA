package com.spring.jwt.B2B;

import com.spring.jwt.dto.ResponceDto;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.dto.ResponseSingleCarDto;
import com.spring.jwt.entity.Status;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/b2b")
@RequiredArgsConstructor
public class B2BController {

    private final B2BService b2BService;

    @PostMapping("/add")
    public ResponseEntity<?> addB2B(@Valid @RequestBody B2BPostDto b2BPostDto) {
        ResponseDto response = new ResponseDto();
        try {
            String message = b2BService.addB2B(b2BPostDto);
            response.setStatus("success");
            response.setMessage(message);
            return new ResponseEntity(response,HttpStatus.CREATED);
        } catch (RuntimeException e) {
            response.setStatus("error");
            response.setMessage(e.getMessage());
            return new ResponseEntity( response,HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/by-beading-car")
    public ResponseEntity<ResponseAllB2BDto> getByBeadingCarId(
            @RequestParam String requestStatus,
            @RequestParam Integer beadingCarId) {
        ResponseAllB2BDto response = new ResponseAllB2BDto();
        try {
            List<B2BDto> b2bList = b2BService.getByBeadingCarId(requestStatus, beadingCarId);
            response.setStatus("success");
            response.setMessage("B2B transactions retrieved successfully.");
            response.setList(b2bList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.setStatus("error");
            response.setMessage("No B2B transactions found.");
            response.setException(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getByB2BId(@RequestParam Integer b2BId) {
        ResponseSingleCarDto response = new ResponseSingleCarDto();
        try {
            B2BDto b2bDto = b2BService.getByB2bId(b2BId);
            response.setMessage("B2B transaction retrieved successfully.");
            response.setObject((b2bDto));
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.setMessage("B2B transaction not found.");
            response.setException(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getB2BCountByStatusAndDealer(
            @RequestParam String requestStatus,
            @RequestParam Integer sellerDealerId) {
        ResponceDto response = new ResponceDto();
        try {
            int count = b2BService.getB2BCountByStatusAndDealer(requestStatus, sellerDealerId);
            response.setMessage("Count retrieved successfully.");
            response.setObject( count);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            ResponseDto response1 = new ResponseDto();
            response1.setStatus("error");
            response1.setMessage("Error retrieving count.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseAllB2BDto> deleteB2B(@RequestParam Integer b2BId) {
        ResponseAllB2BDto response = new ResponseAllB2BDto();
        try {
            b2BService.deleteB2B(b2BId);
            response.setStatus("success");
            response.setMessage("B2B transaction deleted successfully.");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            response.setStatus("error");
            response.setMessage("B2B transaction not found for deletion.");
            response.setException(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseAllB2BDto> getAllB2BRecords() {
        ResponseAllB2BDto response = new ResponseAllB2BDto();
        try {
            List<B2BDto> b2bList = b2BService.getAllB2BRecords();
            response.setStatus("success");
            response.setMessage("All B2B records retrieved successfully.");
            response.setList(b2bList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.setStatus("error");
            response.setMessage("No B2B records found.");
            response.setException(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-status")
    public ResponseEntity<ResponseAllB2BDto> getByStatus(@RequestParam String requestStatus) {
        ResponseAllB2BDto response = new ResponseAllB2BDto();
        try {
            List<B2BDto> b2bList = b2BService.getByStatus(requestStatus);
            response.setStatus("success");
            response.setMessage("B2B records retrieved successfully.");
            response.setList(b2bList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.setStatus("error");
            response.setMessage("No B2B records found.");
            response.setException(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-buyer-dealer")
    public ResponseEntity<ResponseAllB2BDto3> getByBuyerDealerId(@RequestParam Integer buyerDealerId) {
        ResponseAllB2BDto3 response = new ResponseAllB2BDto3();
        try {
            List<B2BByerGetInfoDto> b2bList = b2BService.getByBuyerDealerId(buyerDealerId);
            response.setStatus("success");
            response.setMessage("B2B records retrieved successfully.");
            response.setList(b2bList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.setStatus("error");
            response.setMessage("No B2B records found.");
            response.setException(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count-by-beading-car")
    public ResponseEntity<?> getCountByBeadingCarId(@RequestParam Integer beadingCarId) {
        ResponceDto response = new ResponceDto();
        try {
            int count = b2BService.getCountByBeadingCarId(beadingCarId);
            response.setMessage("B2B count retrieved successfully.");
            response.setObject((count));
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            ResponseDto response1 = new ResponseDto();
            response1.setMessage("Error retrieving B2B count.");
           response1.setStatus("error");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
    }


    @PatchMapping("/update")
    public ResponseEntity<ResponseAllB2BDto> updateB2B(@RequestParam Integer b2BId, @RequestBody B2BDto b2BDto) {
        ResponseAllB2BDto response = new ResponseAllB2BDto();
        try {
            B2B updatedB2B = b2BService.updateB2B(b2BId, b2BDto);
            response.setStatus("success");
            response.setMessage("B2B transaction updated successfully.");
//            response.setList(List.of(mapToB2BDto(updatedB2B))); // Wrap single result in a list
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.setStatus("error");
            response.setMessage("Error updating B2B transaction.");
            response.setException(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/by-sales-person")
    public ResponseEntity<ResponseAllB2BDto> getBySalesPerson(@RequestParam Integer salesPersonId) {
        ResponseAllB2BDto response = new ResponseAllB2BDto();
        try {
            List<B2BDto> b2bList = b2BService.getBySealsPerson(salesPersonId);
            response.setStatus("success");
            response.setMessage("B2B records retrieved successfully.");
            response.setList(b2bList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.setStatus("error");
            response.setMessage("No B2B records found for the provided sales person ID.");
            response.setException(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    private B2BDto mapToB2BDto(B2B b2B) {
        B2BDto b2BDto = new B2BDto();
        b2BDto.setB2BId(b2B.getB2BId());
        b2BDto.setBeadingCarId(b2B.getBeadingCarId());
        b2BDto.setBuyerDealerId(b2B.getBuyerDealerId());
        b2BDto.setSellerDealerId(b2B.getSellerDealerId());
        b2BDto.setTime(b2B.getTime().toLocalDate());
        b2BDto.setRequestStatus(b2B.getRequestStatus());
        b2BDto.setSalesPersonId(b2B.getSalesPersonId());
        return b2BDto;
    }
}
